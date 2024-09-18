import customDSL.ProgrammingLanguage
import customDSL.readme


fun main() {
    val markdownText = readme {
        header(level = 1) { +"Kotlin Lecture" }
        header(level = 2) { +"DSL" }

        text {
            +("Today we will try to recreate ${bold("DSL")} from this article: ${link(link = "https://kotlinlang.org/docs/type-safe-builders.html", text = "Kotlin Docs")}!!!")
            +"It is so ${underlined("fascinating and interesting")}!"
            +code(language = ProgrammingLanguage.KOTLIN) {
                +"""
                    fun main() {
                        println("Hello world!")
                    }
                """.trimIndent()
            }
        }
    }

    println(markdownText)
}