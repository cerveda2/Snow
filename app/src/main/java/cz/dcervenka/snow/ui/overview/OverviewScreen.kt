package cz.dcervenka.snow.ui.overview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import cz.dcervenka.snow.ui.theme.SnowTheme

@Composable
fun OverviewScreenRoot(
    onDetailClick: () -> Unit,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    OverviewScreen(
        onAction = { action ->
            when (action) {
                OverviewAction.OnDetailClick -> onDetailClick()
                else -> Unit
            }
        }
    )
}

@Composable
fun OverviewScreen(
    onAction: (OverviewAction) -> Unit,
) {

}

@Preview
@Composable
private fun OverviewScreenPreview() {
    SnowTheme {
        OverviewScreen(
            onAction = {}
        )
    }
}