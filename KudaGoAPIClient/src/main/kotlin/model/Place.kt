package model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: Long? = null,
    val name: String? = null
)