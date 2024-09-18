import customDSL.ProgrammingLanguage
import customDSL.readme
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking
import model.News
import repository.NewsKudaGoRepository

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

fun main1() = runBlocking {
    val client = HttpClient(CIO) {
    }

    val newsRepository = NewsKudaGoRepository(client)

    try {
        val newsList: List<News> = newsRepository.getAllNews(count = 10)

        println("Retrieved ${newsList.size} news articles:")
        newsList.forEach { news ->
            println("${news.id}: ${news.title} (${news.publicationDate})")
        }
    } catch (e: Exception) {
        println("Failed to retrieve news: ${e.message}")
    } finally {
        client.close()
    }
}