package cz.dcervenka.snow.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseData(
    val areas: List<Area>? = null,
    val resorts: List<Resort>? = null,
    val lastUpdateMs: Long? = null,
)

@JsonClass(generateAdapter = true)
data class Area(
    val areaId: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class Resort(
    val resortId: String,
    val areaId: String,
    val name: String,
    val snowMinCm: Int?,
    val snowMaxCm: Int?,
    val snowNewCm: Int?,
    val snowType: SnowType?,
    val temperature: Int?,
    val liftTotal: Int?,
    val liftOpen: Int?,
    val tracksTotalKm: Float?,
    val tracksOpenKm: Float?,
    val favorite: Boolean = false
)

enum class SnowType {
    // "mokrý", "prachový", "vlhký", "zmrzlý", "firn", "technický"
    WATERY,
    POWDERY,
    WET,
    FROZEN,
    FIRN,
    ARTIFICIAL
}