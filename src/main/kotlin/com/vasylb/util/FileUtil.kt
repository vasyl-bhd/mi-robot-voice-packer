package com.vasylb.util

fun getFileName(path: String): String = path.substring(path.lastIndexOf("/") + 1, path.length - 4)