package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import serializers.LocalDateSerializer
import serializers.PlaceSerializer
import java.time.LocalDate
import kotlin.math.exp

@Serializable
data class News(
    val id: Long,
    val title: String,
    @Serializable(with = PlaceSerializer::class) val place: Place? = null,
    val description: String,
    @SerialName("site_url") val siteUrl: String,
    @SerialName("favorites_count") val favoritesCount: Int,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("publication_date")
    @Serializable(with = LocalDateSerializer::class)
    val publicationDate: LocalDate
) {
    val rating: Double by lazy {
        1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }
}