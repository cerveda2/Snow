package cz.dcervenka.snow.model

import com.squareup.moshi.JsonClass
import cz.dcervenka.snow.R

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

enum class SnowType(val title: String, val iconResId: Int) {
    WATERY("Mokrý", R.drawable.watery),
    POWDERY("Prachový", R.drawable.powdery),
    WET("Vlhký", R.drawable.wet),
    FROZEN("Zmrzlý", R.drawable.frozen),
    FIRN("Firn", R.drawable.firn),
    ARTIFICIAL("Technický", R.drawable.artificial)
}