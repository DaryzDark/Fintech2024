package service

import io.mockk.*
import kotlinx.coroutines.runBlocking
import model.News
import repository.NewsKudaGoRepository
import java.io.File
import java.time.LocalDate
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NewsServiceTest {

    private lateinit var newsRepository: NewsKudaGoRepository
    private lateinit var newsService: NewsService

    @BeforeTest
    fun setUp() {
        newsRepository = mockk()
        newsService = NewsService(newsRepository)
    }

    @Test
    fun `should fetch most rated news successfully`() = runBlocking {
        val period = LocalDate.now().minusDays(30)..LocalDate.now()
        val mockNews = listOf(
            News(1, "Title1", null, "Description1", "https://site1.com", 10, 5, LocalDate.now()),
            News(2, "Title2", null, "Description2", "https://site2.com", 20, 10, LocalDate.now())
        )
        coEvery { newsRepository.getAllNews(100) } returns mockNews

        val result = newsService.getMostRatedNews(2, period)

        assertEquals(mockNews, result)
        coVerify { newsRepository.getAllNews(100) }
    }


    @Test
    fun `should throw exception when file already exists`() {
        val path = "test_news.csv"
        File(path).createNewFile()

        assertFailsWith<IllegalArgumentException> {
            newsService.saveNews(path, listOf())
        }
        File(path).delete()
    }

    @Test
    fun `should throw exception when error occurs during file creation`() {
        val file = mockk<File>()
        every { file.exists() } returns false
        every { file.createNewFile() } returns false

        val path = "test_news.csv"
        val news = listOf(
            News(1, "Title1", null, "Description1", "https://site1.com", 10, 5, LocalDate.now())
        )

        val spyNewsService = spyk(newsService)
        every { spyNewsService.saveNews(path, news) } throws RuntimeException("Failed to create file at the specified path.")

        assertFailsWith<RuntimeException> {
            spyNewsService.saveNews(path, news)
        }
    }
}