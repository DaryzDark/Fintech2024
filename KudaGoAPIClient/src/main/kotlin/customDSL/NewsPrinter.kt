package customDSL

@DslMarker
annotation class NewsDsl

@NewsDsl
class NewsPrinter {
    private val content = StringBuilder()

    fun header(level: Int, block: HeaderBuilder.() -> Unit) {
        val builder = HeaderBuilder(level)
        builder.block()
        content.appendLine(builder.toString())
    }

    fun text(block: TextBuilder.() -> Unit) {
        val builder = TextBuilder()
        builder.block()
        content.appendLine(builder.toString())
    }

    override fun toString(): String = content.toString()
}