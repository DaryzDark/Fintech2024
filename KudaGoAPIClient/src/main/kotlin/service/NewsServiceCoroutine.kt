package service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import model.News
import repository.NewsKudaGoRepository
import java.io.File
import java.io.PrintWriter
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger {}

@OptIn(ObsoleteCoroutinesApi::class)
private fun CoroutineScope.processorActor(file: File, channel: Channel<List<News>>) = actor<List<News>>(Dispatchers.IO) {
    PrintWriter(file).use { writer ->
        logger.info{"Actor: Started writing to file"}
        writer.println("id,title,place,description,siteUrl,favoritesCount,commentsCount,publicationDate,rating")
        for (newsList in channel) {
            logger.info{"Actor: Received a batch of news to write"}
            newsList.forEach { item ->
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
            writer.flush()
        }
    logger.info{"Actor: Finished writing to file"}
    }

class NewsServiceCoroutine(
    private val workerCount: Int,
    private val threadCount: Int,
    private val repository: NewsKudaGoRepository
) {
    private val channel = Channel<List<News>>(Channel.UNLIMITED)

    @OptIn(DelicateCoroutinesApi::class)
    fun startProcessing(pages: Int) = runBlocking {
        val dispatcher = newFixedThreadPoolContext(threadCount, "workerPool")

        val file = File("news_output.csv")
        val processor = processorActor(file, channel)

        logger.info{"Starting workers"}

        val workers = List(workerCount) { workerId ->
            launch(dispatcher) {
                for (page in (workerId + 1)..pages step workerCount) {
                    logger.info{"Worker $workerId: Fetching news for page $page"}
                    val news = repository.getNews(page)
                    logger.info{"Worker $workerId: Sending news for page $page to channel"}
                    channel.send(news)
                }
            }
        }

        workers.forEach { it.join() }
        logger.info{"All workers completed"}
        channel.close()
        logger.info{"Channel closed"}

        processor.invokeOnClose { logger.info{"Processor has finished writing"}}
    }
}


fun main() = runBlocking {
    val client = HttpClient(CIO)
    val repository = NewsKudaGoRepository(client)
    val workerCount = 4
    val threadCount = 2

    val newsService = NewsServiceCoroutine(workerCount, threadCount, repository)

    val time = measureTimeMillis {
        newsService.startProcessing(pages = 10)
    }

    logger.info{"Execution time: $time ms"}

    client.close()
}