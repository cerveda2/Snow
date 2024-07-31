package cz.dcervenka.snow.ui.util

import cz.dcervenka.snow.model.SnowType

fun formatTemperature(temp: Int?): String {
    val temperature = temp ?: "?"

    return "$temperature °C"
}

fun formatTracksAvailable(tracksOpen: Float?, tracksTotal: Float?): String {
    val track1 = tracksOpen ?: "0.0"
    val track2 = tracksTotal ?: "0.0"

    return "$track1 z $track2 km tratí v provozu"
}

fun formatSnowType(snowType: SnowType?): String {
    return when (snowType) {
        SnowType.WATERY -> "Mokrý"
        SnowType.POWDERY -> "Prachový"
        SnowType.WET -> "Vlhký"
        SnowType.FROZEN -> "Zmrzlý"
        SnowType.FIRN -> "Firn"
        SnowType.ARTIFICIAL -> "Technický"
        else -> "Neznámý"
    }
}

