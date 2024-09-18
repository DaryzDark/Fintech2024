package service

import io.github.oshai.kotlinlogging.KotlinLogging
import model.News
import repository.NewsKudaGoRepository
import java.io.File
import java.io.PrintWriter
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

class NewsService(private val newsRepository: NewsKudaGoRepository) {

    suspend fun getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
        logger.info { "Fetching top $count most rated news for the period: $period" }

        val allNews = newsRepository.getAllNews(count)

        val filteredNews = allNews
            .filter { it.publicationDate in period }
            .sortedByDescending { it.rating }
            .take(count)

        logger.info { "Retrieved ${filteredNews.size} news items after filtering by rating." }

        return filteredNews
    }

    fun saveNews(path: String, news: Collection<News>) {
        val file = File(path)

        if (file.exists()) {
            logger.error { "File already exists at path: $path" }
            throw IllegalArgumentException("File already exists at the specified path.")
        }

        try {
            logger.info { "Creating new file at path: $path" }
            if (file.createNewFile()) {
                PrintWriter(file).use { writer ->
                    logger.info { "Writing ${news.size} news items to file." }
                    writer.println("id,title,place,description,siteUrl,favoritesCount,commentsCount,publicationDate,rating")

                    news.forEach { item ->
                        val formattedPlace = item.place ?: "Not stated"
                        writer.println(
                            "${item.id}," +
                                    "\"${item.title}\"," +
                                    "\"$formattedPlace\"," +
                                    "\"${item.description}\"," +
                                    "${item.siteUrl}," +
                                    "${item.favoritesCount}," +
                                    "${item.commentsCount}," +
                                    "${item.publicationDate}," +
                                    "${item.rating}"
                        )
                    }
                }
                logger.info { "Successfully saved news to file: $path" }
            } else {
                logger.error { "Failed to create file at path: $path" }
                throw RuntimeException("Failed to create file at the specified path.")
            }
        } catch (e: Exception) {
            logger.error(e) { "Error occurred while saving news to file: $path" }
            throw RuntimeException("Error occurred while saving news to file.", e)
        }
    }
}