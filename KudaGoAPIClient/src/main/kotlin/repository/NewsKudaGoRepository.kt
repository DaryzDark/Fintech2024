package repository

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import model.News
import model.NewsResponse

private val logger = KotlinLogging.logger {}

class NewsKudaGoRepository(
    private val client: HttpClient,
) {

    suspend fun getAllNews(count: Int = 100): List<News> {
        val location = "nsk"
        val fields = "id,title,publication_date,place,description,site_url,favorites_count,comments_count"

        val json = Json { ignoreUnknownKeys = true }

        logger.info { "Requesting news for location: $location with count: $count" }

        val response: String = try {
            client.get("https://kudago.com/public-api/v1.4/news/") {
                parameter("fields", fields)
                parameter("page_size", count)
                parameter("order_by", "-publication_date")
                parameter("location", location)
            }.bodyAsText()
        } catch (e: Exception) {
            logger.error(e) { "Error while fetching news" }
            throw e
        }
        val newsResponse = try {
            json.decodeFromString<NewsResponse>(response)
        } catch (e: Exception) {
            logger.error(e) { "Error while parsing response" }
            throw e
        }

        logger.info { "Successfully retrieved ${newsResponse.results.size} news articles" }

        return newsResponse.results
    }
}