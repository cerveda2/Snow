package cz.dcervenka.snow.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.dcervenka.snow.R
import cz.dcervenka.snow.model.Resort
import cz.dcervenka.snow.model.SnowType
import cz.dcervenka.snow.ui.OverviewViewModel
import cz.dcervenka.snow.ui.theme.ColombiaBlue
import cz.dcervenka.snow.ui.theme.Orange
import cz.dcervenka.snow.ui.theme.SnowTheme
import cz.dcervenka.snow.ui.util.formatLiftsTotal
import cz.dcervenka.snow.ui.util.formatSnow
import cz.dcervenka.snow.ui.util.formatTemperature
import cz.dcervenka.snow.ui.util.formatTracksTotal

@Composable
fun DetailScreenRoot(
    onMoreInfoClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val resort by viewModel.detailResort.collectAsStateWithLifecycle()

    val nonNullResort = resort ?: run {
        onBackClick()
        return
    }

    DetailScreen(
        resort = nonNullResort,
        onAction = { action ->
            when (action) {
                DetailAction.OnVisitPlace -> onMoreInfoClick()
                DetailAction.OnBackClick -> onBackClick()
                is DetailAction.OnFavoriteSet -> {
                    viewModel.setFavorite(
                        resortId = action.resortId,
                        fromDetail = true
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    resort: Resort,
    onAction: (DetailAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = resort.name) },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(DetailAction.OnBackClick) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(DetailAction.OnFavoriteSet(resort.resortId)) }
                    ) {
                        Icon(
                            imageVector = if (resort.favorite) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoItem(
                    modifier = Modifier.weight(2 / 3f),
                    title = "Sníh",
                    value = formatSnow(resort.snowMinCm, resort.snowMaxCm, resort.snowNewCm),
                    additionalContent = {
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(
                                    id = resort.snowType?.iconResId ?: R.drawable.question_mark
                                ),
                                contentDescription = "Snow type icon"
                            )
                            Text(
                                text = resort.snowType?.title ?: "Neznámý",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    backgroundColor = ColombiaBlue
                )
                InfoItem(
                    modifier = Modifier.weight(1 / 3f),
                    title = "Teplota",
                    value = resort.temperature.formatTemperature(),
                    additionalContent = null,
                    backgroundColor = Orange
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InfoItem(
                    modifier = Modifier.weight(1f),
                    title = "Otevřeno tratí",
                    value = resort.tracksOpenKm.toString(),
                    additionalContent = {
                        Text(
                            text = resort.tracksTotalKm.formatTracksTotal(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    backgroundColor = ColombiaBlue
                )
                InfoItem(
                    modifier = Modifier.weight(1f),
                    title = "V provozu",
                    value = resort.liftOpen.toString(),
                    additionalContent = {
                        Text(
                            text = resort.liftTotal.formatLiftsTotal(),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    backgroundColor = ColombiaBlue
                )
            }
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .size(100.dp)
            .padding(horizontal = 14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (additionalContent != null) {
                additionalContent()
            }
        }
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    SnowTheme {
        DetailScreen(
            resort = Resort(
                resortId = "",
                areaId = "",
                name = "Resort",
                snowMinCm = 20,
                snowMaxCm = 40,
                snowNewCm = 5,
                snowType = SnowType.WET,
                temperature = 1,
                liftTotal = 15,
                liftOpen = 11,
                tracksTotalKm = 5.4f,
                tracksOpenKm = 3.1f,
                favorite = true
            ),
            onAction = {}
        )
    }
}