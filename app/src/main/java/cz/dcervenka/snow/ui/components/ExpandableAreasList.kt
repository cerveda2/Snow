package cz.dcervenka.snow.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import cz.dcervenka.snow.model.Area
import cz.dcervenka.snow.model.Resort
import cz.dcervenka.snow.model.ResponseData

@Composable
fun ExpandableAreaList(
    responseData: ResponseData
) {

    val areas = responseData.areas ?: emptyList()
    val resorts = responseData.resorts ?: emptyList()

    var expandedAreaIds by remember { mutableStateOf<Set<String>>(emptySet()) }

    LazyColumn {
        items(areas) { area ->
            ExpandableAreaItem(
                area = area,
                resorts = resorts.filter { it.areaId == area.areaId },
                expanded = expandedAreaIds.contains(area.areaId),
                onClick = {
                    expandedAreaIds = if (expandedAreaIds.contains(area.areaId)) {
                        expandedAreaIds - area.areaId
                    } else {
                        expandedAreaIds + area.areaId
                    }
                }
            )
        }
    }
}

@Composable
fun ExpandableAreaItem(
    area: Area,
    resorts: List<Resort>,
    expanded: Boolean,
    onClick: () -> Unit
) {
    val targetRotation = if (expanded) 180f else 0f
    val rotation: Float by animateFloatAsState(
        targetValue = targetRotation,
        label = "rotationAnimation"
    )

    Column(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .animateContentSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = area.name
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.rotate(rotation),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        if (expanded) {
            resorts.forEach { resort ->
                ResortItem(
                    resort = resort
                )
            }
        }
    }
}

// TODO make nicer
@Composable
fun ResortItem(
    resort: Resort,
    //modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
            Text(
                text = resort.name,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            resort.temperature?.let {
                Text(
                    text = it.toString(),
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = resort.tracksTotalKm.toString(),
                modifier = Modifier.padding(4.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = resort.snowType.toString(),
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null
            )
        }
    }
}