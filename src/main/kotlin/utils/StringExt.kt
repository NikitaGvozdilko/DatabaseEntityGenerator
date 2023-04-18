package utils

fun String.asIdentificator(): String {
    return this.replaceFirst(
        this[0],
        this[0].lowercaseChar()
    )
}

fun String.addImports(vararg import: String): String {
    val imports = StringBuilder().apply {
        import.forEach {
            append("import $it\n")
        }
        append("import")
    }
    return this.replaceFirst("import", "$imports")
}