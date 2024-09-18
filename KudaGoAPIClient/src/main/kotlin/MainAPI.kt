import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.runBlocking
import model.News
import repository.NewsKudaGoRepository
import service.NewsService
import java.time.LocalDate

fun main() = runBlocking {
    val client = HttpClient(CIO) {
    }

    val newsRepository = NewsKudaGoRepository(client)

    val newsService = NewsService(newsRepository)

    val startDate = LocalDate.of(2024, 1, 1)
    val endDate = LocalDate.of(2024, 12, 31)
    val period = startDate..endDate

    val mostRatedNews: List<News> = newsService.getMostRatedNews(100, period)

    val filePath = "most_rated_news.csv"
    newsService.saveNews(filePath, mostRatedNews)

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