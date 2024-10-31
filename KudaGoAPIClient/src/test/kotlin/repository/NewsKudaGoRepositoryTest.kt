package repository

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NewsKudaGoRepositoryTest {

    private lateinit var mockClient: HttpClient
    private lateinit var newsRepository: NewsKudaGoRepository

    @BeforeTest
    fun setUp() {
        val mockEngine = MockEngine { request ->
            when (request.url.toString()) {
                "https://kudago.com/public-api/v1.4/news/?fields=id,title,publication_date,place,description,site_url,favorites_count,comments_count&page_size=5&order_by=-publication_date&location=nsk" -> {
                    respond(
                        content = """
                            {
                                "results": [
                                    {
                                        "id": 1,
                                        "title": "News 1",
                                        "place": null,
                                        "description": "Description 1",
                                        "site_url": "https://site1.com",
                                        "favorites_count": 10,
                                        "comments_count": 5,
                                        "publication_date": 1726677557
                                    },
                                    {
                                        "id": 2,
                                        "title": "News 2",
                                        "place": null,
                                        "description": "Description 2",
                                        "site_url": "https://site2.com",
                                        "favorites_count": 20,
                                        "comments_count": 10,
                                        "publication_date": 1726677557
                                    }
                                ]
                            }
                        """,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> {
                    respondError(HttpStatusCode.NotFound)
                }
            }
        }

        mockClient = HttpClient(mockEngine) {
        }

        newsRepository = NewsKudaGoRepository(mockClient)
    }

    @Test
    fun `test getAllNews success`() = runTest {
        // Act
        val result = newsRepository.getAllNews(5)

        // Assert
        assertEquals(2, result.size)
        assertEquals("News 1", result[0].title)
        assertEquals("https://site1.com", result[0].siteUrl)
        assertEquals(10, result[0].favoritesCount)
    }

    @Test
    fun `test getAllNews with error`() = runTest {
        val badRepository = NewsKudaGoRepository(HttpClient(MockEngine { request ->
            respondError(HttpStatusCode.InternalServerError)
        }))

        assertFailsWith<Exception> {
            badRepository.getAllNews(5)
        }
    }

    @Test
    fun `test getAllNews parsing error`() = runTest {
        val invalidJsonClient = HttpClient(MockEngine {
            respond(
                content = """ { "invalid_field": "value" } """,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        })

        val invalidRepo = NewsKudaGoRepository(invalidJsonClient)

        assertFailsWith<Exception> {
            invalidRepo.getAllNews(5)
        }
    }
}