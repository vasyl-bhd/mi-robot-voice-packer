package com.vasylb.util

fun String.withoutExt(): String {
    return this.substring(0, this.length - 4)
}