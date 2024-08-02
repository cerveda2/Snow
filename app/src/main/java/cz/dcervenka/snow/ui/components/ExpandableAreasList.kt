package cz.dcervenka.snow.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.dcervenka.snow.model.Area
import cz.dcervenka.snow.model.Resort
import cz.dcervenka.snow.model.ResponseData
import cz.dcervenka.snow.ui.theme.ColombiaBlue
import cz.dcervenka.snow.ui.theme.ErrorRed
import cz.dcervenka.snow.ui.theme.SuccessGreen
import cz.dcervenka.snow.ui.util.formatTemperature
import cz.dcervenka.snow.ui.util.formatTracksAvailable

@Composable
fun ExpandableAreaList(
    responseData: ResponseData,
    showOnlyFavorites: Boolean,
    searchInitiated: Boolean,
    onDetailClick: (String) -> Unit,
    onSetFavorite: (String) -> Unit
) {

    val areas = responseData.areas ?: emptyList()
    val resorts = responseData.resorts ?: emptyList()

    var expandedAreaIds by rememberSaveable { mutableStateOf<Set<String>>(emptySet()) }

    LazyColumn {
        items(areas) { area ->
            val visibleResorts = resorts.filter {
                if (showOnlyFavorites) {
                    it.areaId == area.areaId && it.favorite
                } else it.areaId == area.areaId
            }

            // Only display the area if there are visible resorts
            AnimatedVisibility(
                visible = visibleResorts.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                ExpandableAreaItem(
                    area = area,
                    resorts = visibleResorts,
                    expanded = expandedAreaIds.contains(area.areaId),
                    showOnlyFavorites = showOnlyFavorites,
                    searchInitiated = searchInitiated,
                    onClick = {
                        expandedAreaIds = if (expandedAreaIds.contains(area.areaId)) {
                            expandedAreaIds - area.areaId
                        } else {
                            expandedAreaIds + area.areaId
                        }
                    },
                    onDetailClick = onDetailClick,
                    onFavoriteClick = onSetFavorite,
                )
            }
        }
    }
}

@Composable
fun ExpandableAreaItem(
    area: Area,
    resorts: List<Resort>,
    expanded: Boolean,
    showOnlyFavorites: Boolean,
    searchInitiated: Boolean,
    onClick: () -> Unit,
    onDetailClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
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
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = area.name,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier.rotate(rotation),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        if (expanded || showOnlyFavorites || searchInitiated) {
            resorts.forEachIndexed { index, resort ->
                ResortItem(
                    resort = resort,
                    isFirst = index == 0,
                    isLast = index == resorts.lastIndex,
                    onDetailClick = { onDetailClick(resort.resortId) },
                    onFavoriteClick = { onFavoriteClick(resort.resortId) },
                )
            }
        }
    }
}

@Composable
fun ResortItem(
    resort: Resort,
    isFirst: Boolean,
    isLast: Boolean,
    onDetailClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isFirst) Spacer(modifier = Modifier.height(4.dp))
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(3.dp))
            .clickable { onDetailClick() }
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            .padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = if (resort.liftOpen == null || resort.liftOpen == 0)
                ErrorRed else SuccessGreen
            val icon = if (resort.liftOpen == null || resort.liftOpen == 0)
                Icons.Default.Clear else Icons.Default.Check

            Icon(
                modifier = Modifier
                    .background(color, RoundedCornerShape(8.dp))
                    .padding(2.dp),
                imageVector = icon,
                contentDescription = null
            )
            Text(
                text = resort.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = resort.temperature.formatTemperature(),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(ColombiaBlue, RoundedCornerShape(8.dp))
                    .padding(2.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatTracksAvailable(resort.tracksOpenKm, resort.tracksTotalKm),
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(4.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = resort.snowType?.title ?: "Neznámý",
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .background(ColombiaBlue, RoundedCornerShape(8.dp))
                    .padding(2.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.clickable {
                    onFavoriteClick()
                },
                imageVector = if (resort.favorite) Icons.Filled.Favorite
                else Icons.Filled.FavoriteBorder,
                contentDescription = null
            )
        }
        if (!isLast) HorizontalDivider()
    }
}