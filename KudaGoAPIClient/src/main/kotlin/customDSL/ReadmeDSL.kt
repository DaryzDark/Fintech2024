package customDSL

fun readme(block: NewsPrinter.() -> Unit): String {
    val printer = NewsPrinter()
    printer.block()
    return printer.toString()
}