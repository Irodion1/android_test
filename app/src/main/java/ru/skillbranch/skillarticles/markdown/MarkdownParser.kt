package ru.skillbranch.skillarticles.markdown

import java.util.regex.Pattern

object MarkdownParser {
    private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"

    // group regex
    private const val UNORDERED_LIST_ITEM_GROUP = "(^[*+-] .+$)"
    private const val HEADER_GROUP = "(^#{1,6} .+$)"
    private const val QUOTE_GROUP = "(^> .+$)"
    private const val ITALIC_GROUP = "((?<!\\*)\\*[^*].*?[^*]?\\*(?!\\*)|(?<!_)_[^_].*?[^_]?_(?!_))"
    private const val BOLD_GROUP =
        "((?<!\\*)\\*{2}[^*].*?[^*]?\\*{2}(?!\\*)|(?<!_)_{2}[^_].*?[^_]?_{2}(?!_))"
    private const val STRIKE_GROUP = "((?<!~)~{2}[^*].*?[^*]?~{2}(?!~))"
    private const val RULE_GROUP = "(^[-_*]{3}$)"
    private const val INLINE_GROUP = "((?<!`)`[^`\\s].*?[^`\\s]?`(?!`))"
    private const val LINK_GROUP = "(\\[[^\\[\\]]*?]\\(.+?\\)|^\\[*?]\\(.*?\\))"
    private const val BLOCK_CODE_GROUP = "(^```[\\s\\S]+?```$)"
    private const val ORDER_LIST_GROUP = "(^\\d{1,2}\\.\\s.+?$)"

    //result regex
    private const val MARKDOWN_GROUPS =
        "$UNORDERED_LIST_ITEM_GROUP|$HEADER_GROUP|$QUOTE_GROUP|$ITALIC_GROUP|$BOLD_GROUP|$STRIKE_GROUP|$RULE_GROUP|$INLINE_GROUP|$LINK_GROUP|$BLOCK_CODE_GROUP|$ORDER_LIST_GROUP"

    private val elementPattern by lazy { Pattern.compile(MARKDOWN_GROUPS, Pattern.MULTILINE) }

    /**
     * Parses md text to elements
     */
    fun parse(text: String): MarkDownText {
        val elements = mutableListOf<Element>().also { it.addAll(findElements(text)) }
        return MarkDownText(elements)
    }


    /**
     * Clears md text to string without md
     */
    fun clear(text: String?): String? {
        text ?: return null
        val elements = mutableListOf<Element>().also { it.addAll(findElements(text)) }
        val result = mutableListOf<String?>()
        elements.forEach {
            if (it.elements.isEmpty()) {
                result.add(it.text.toString())
            } else {
                result.add(clear(it.text.toString()))
            }
        }
        return result.joinToString(separator = "")
    }

    /**
     * Finds md element inside text
     */
    private fun findElements(text: CharSequence): List<Element> {
        val parents = mutableListOf<Element>()
        val matcher = elementPattern.matcher(text)
        var lastStartIndex = 0

        loop@ while (matcher.find(lastStartIndex)) {
            val startIndex = matcher.start()
            val endIndex = matcher.end()

            // TEXT was found between lastStartIndex and StartIndex
            if (lastStartIndex < startIndex) {
                parents.add(Element.Text(text.subSequence(lastStartIndex, startIndex)))
            }

            // found raw text
            val foundText: CharSequence

            // groups range for iterate by groups
            val groups = 1..11
            var group = -1
            for (gr in groups) {
                if (matcher.group(gr) != null) {
                    group = gr
                    break
                }
            }

            when (group) {
                // NOT FOUND -> BREAK
                -1 -> break@loop

                // UNORDERED LIST
                1 -> {
                    // text without "*."
                    foundText = text.subSequence(startIndex.plus(2), endIndex)

                    // find inner
                    val subs = findElements(foundText)
                    val element = Element.UnorderedListItem(foundText, subs)
                    parents.add(element)

                    // next find start from position endIndex (last regex char)
                    lastStartIndex = endIndex
                }

                // HEADER
                2 -> {
                    val reg = "^#{1,6}".toRegex().find(text.subSequence(startIndex, endIndex))
                    val level = reg!!.value.length

                    // text without "#"
                    foundText = text.subSequence(startIndex.plus(level.inc()), endIndex)

                    val element = Element.Header(level, foundText)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                // QUOTE
                3 -> {
                    //text without "> "
                    foundText = text.subSequence(startIndex.plus(2), endIndex)
                    val subs = findElements(foundText)
                    val element = Element.Quote(foundText, subs)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                // ITALIC
                4 -> {
                    //text without "*{}*"
                    foundText = text.subSequence(startIndex.inc(), endIndex.dec())
                    val subs = findElements(foundText)
                    val element = Element.Italic(foundText, subs)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                // BOLD
                5 -> {
                    //text without "**{}**"
                    foundText = text.subSequence(startIndex.plus(2), endIndex.minus(2))
                    val subs = findElements(foundText)
                    val element = Element.Bold(foundText, subs)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                // STRIKE
                6 -> {
                    //text without "~~{}~~"
                    foundText = text.subSequence(startIndex.plus(2), endIndex.minus(2))
                    val subs = findElements(foundText)
                    val element = Element.Strike(foundText, subs)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                // RULE
                7 -> {
                    //text without "***" insert empty character
                    val element = Element.Rule()
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                // INLINE CODE
                8 -> {
                    //text without "`{}`"
                    foundText = text.subSequence(startIndex.inc(), endIndex.dec())
                    val element = Element.InlineCode(foundText)
                    parents.add(element)
                    lastStartIndex = endIndex
                }

                // LINK
                9 -> {
                    //full text for regex
                    foundText = text.subSequence(startIndex, endIndex)
                    val (title: String, link: String) = "\\[(.*)\\]\\((.*)\\)".toRegex()
                        .find(foundText)!!.destructured
                    val element = Element.Link(link, title)
                    parents.add(element)
                    lastStartIndex = endIndex
                }
                //10 -> BLOCK CODE - optionally
                10 -> {
                    foundText = text.subSequence(startIndex.plus(3), endIndex.minus(3)).toString()

                    if (foundText.contains(LINE_SEPARATOR)) {
                        for ((index, line) in foundText.lines().withIndex()) {
                            when (index) {
                                foundText.lines().lastIndex -> parents.add(
                                    Element.BlockCode(
                                        Element.BlockCode.Type.END,
                                        line
                                    )
                                )
                                0 -> parents.add(
                                    Element.BlockCode(
                                        Element.BlockCode.Type.START,
                                        line + LINE_SEPARATOR
                                    )
                                )
                                else -> parents.add(
                                    Element.BlockCode(
                                        Element.BlockCode.Type.MIDDLE,
                                        line + LINE_SEPARATOR
                                    )
                                )
                            }
                        }
                    } else {
                        parents.add(
                            Element.BlockCode(
                                Element.BlockCode.Type.SINGLE,
                                foundText
                            )
                        )
                    }

                    lastStartIndex = endIndex
                }

                //11 -> NUMERIC LIST
                11 -> {
                    val reg = "(^\\d{1,2}.)".toRegex().find(text.substring(startIndex, endIndex))
                    val order = reg!!.value
                    foundText =
                        text.subSequence(startIndex.plus(order.length.inc()), endIndex).toString()
                    val subs = findElements(foundText)
                    val element = Element.OrderedListItem(order, foundText.toString(), subs)
                    parents.add(element)
                    lastStartIndex = endIndex
                }
            }
        }

        if (lastStartIndex < text.length) {
            val foundText = text.subSequence(lastStartIndex, text.length)
            parents.add(Element.Text(foundText))
        }

        return parents
    }
}

data class MarkDownText(val elements: List<Element>)

sealed class Element() {
    abstract val text: CharSequence
    abstract val elements: List<Element>

    data class Text(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class UnorderedListItem(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Header(
        val level: Int = 1,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Quote(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Italic(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Bold(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Strike(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()


    data class Rule(
        override val text: CharSequence = " ", //for insert span
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class InlineCode(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Link(
        val link: String,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class OrderedListItem(
        val order: String,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class BlockCode(
        val type: Type = Type.MIDDLE,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element() {
        enum class Type { START, END, MIDDLE, SINGLE }
    }

}