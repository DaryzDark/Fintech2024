package customDSL

@NewsDsl
class HeaderBuilder(private val level: Int) {
    private val content = StringBuilder()

    operator fun String.unaryPlus() {
        content.appendLine("${"#".repeat(level)} $this")
    }

    override fun toString(): String = content.toString()
}