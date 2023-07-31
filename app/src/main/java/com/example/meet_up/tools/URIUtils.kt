package com.example.meet_up.tools

fun String.toFilename(fileFormat: String): String {
    return split("/").last().removeSuffix(fileFormat)
}

fun String.toUsername(): String {
    return split("@").first()
}