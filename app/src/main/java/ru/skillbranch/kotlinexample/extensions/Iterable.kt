package ru.skillbranch.kotlinexample.extensions

fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T> {
    if (!isEmpty()) {
        return this.dropLastWhile(not(predicate)).dropLast(1)
    }
    return emptyList()
}

inline fun <T> not(crossinline predicate: (T) -> Boolean) = { e: T -> !predicate(e) }