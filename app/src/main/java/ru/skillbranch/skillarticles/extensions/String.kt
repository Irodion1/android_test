package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    val res = mutableListOf<Int>()
    if (this == null || this.isBlank() || substr.isBlank()) return res
    val regex = if (ignoreCase) Regex(substr, RegexOption.IGNORE_CASE) else Regex(substr)
    res.addAll(regex.findAll(this, 0).map { it.range.first })
    return res
}