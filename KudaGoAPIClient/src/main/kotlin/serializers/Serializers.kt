package serializers

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import model.Place
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


object LocalDateSerializer : KSerializer<LocalDate> {
    private val zoneId = ZoneId.systemDefault()

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val instant = value.atStartOfDay(zoneId).toInstant()
        val timestamp = instant.epochSecond
        encoder.encodeLong(timestamp)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val timestamp = decoder.decodeLong()
        val instant = Instant.ofEpochSecond(timestamp)
        return instant.atZone(zoneId).toLocalDate()
    }
}

object PlaceSerializer : KSerializer<Place> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Place") {
        element<Long>("id", isOptional = true)
        element<String>("name", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: Place) {

    }

    override fun deserialize(decoder: Decoder): Place {
        val input = decoder as? JsonDecoder ?: error("Expected JsonDecoder")
        val jsonElement = input.decodeJsonElement()

        return when (jsonElement) {
            is JsonObject -> {
                val id = jsonElement["id"]?.jsonPrimitive?.long
                val name = jsonElement["name"]?.jsonPrimitive?.content
                Place(id, name)
            }
            is JsonPrimitive -> {
                Place(name = jsonElement.content)
            }
            else -> throw SerializationException("Unexpected JSON element for Place")
        }
    }
}