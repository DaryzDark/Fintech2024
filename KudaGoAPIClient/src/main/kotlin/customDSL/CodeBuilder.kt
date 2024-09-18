package customDSL

import java.util.Locale

@NewsDsl
class CodeBuilder(private val language: ProgrammingLanguage) {
    private val content = StringBuilder()

    operator fun String.unaryPlus() {
        content.appendLine("```${language.name.lowercase(Locale.getDefault())}\n$this\n```")
    }

    override fun toString(): String = content.toString()
}