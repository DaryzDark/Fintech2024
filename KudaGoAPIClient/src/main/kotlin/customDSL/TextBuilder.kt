package customDSL

@NewsDsl
class TextBuilder {
    private val content = StringBuilder()

    fun bold(text: String): String {
        return "**$text**"
    }

    fun underlined(text: String): String {
        return "_$text"+"_"
    }

    fun link(link: String, text: String): String {
        return "[$text]($link)"
    }

    fun code(language: ProgrammingLanguage, block: CodeBuilder.() -> Unit): String {
        val builder = CodeBuilder(language)
        builder.block()
        return builder.toString()
    }

    operator fun String.unaryPlus() {
        content.appendLine(this)
    }

    override fun toString(): String = content.toString()
}