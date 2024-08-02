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

enum class SnowType(val stringResId: Int, val iconResId: Int) {
    WATERY(R.string.snow_type_watery, R.drawable.watery),
    POWDERY(R.string.snow_type_powdery, R.drawable.powdery),
    WET(R.string.snow_type_wet, R.drawable.wet),
    FROZEN(R.string.snow_type_frozen, R.drawable.frozen),
    FIRN(R.string.snow_type_firn, R.drawable.firn),
    ARTIFICIAL(R.string.snow_type_artificial, R.drawable.artificial)
}