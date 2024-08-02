package cz.dcervenka.snow.ui.util

fun Int?.formatTemperature(): String {
    val temperature = this ?: "?"

    return "$temperature °C"
}

fun formatTracksAvailable(tracksOpen: Float?, tracksTotal: Float?): String {
    val track1 = tracksOpen ?: "0.0"
    val track2 = tracksTotal ?: "0.0"

    return "$track1 z $track2 km tratí v provozu"
}

fun formatSnow(snowMin: Int?, snowMax: Int?, snowNew: Int?): String {
    return if (snowMin != null && snowMax != null && snowNew != null) {
        "$snowMin - $snowMax + $snowNew cm"
    } else "0 m sněhu"
}

fun Int?.formatLiftsTotal(): String {
    return this?.let {
        "z $it vleků"
    } ?: ""
}

fun Float?.formatTracksTotal(): String {
    return this?.let {
        "z $it km"
    } ?: ""
}
