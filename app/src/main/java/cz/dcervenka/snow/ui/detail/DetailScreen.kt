package cz.dcervenka.snow.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import cz.dcervenka.snow.ui.theme.SnowTheme

@Composable
fun DetailScreenRoot(
    onMoreInfoClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    DetailScreen(
        onAction = { action ->
            when (action) {
                DetailAction.OnVisitPlace -> onMoreInfoClick()
                else -> Unit
            }
        }
    )
}

@Composable
fun DetailScreen(
    onAction: (DetailAction) -> Unit,
) {

}

@Preview
@Composable
private fun DetailScreenPreview() {
    SnowTheme {
        DetailScreen(
            onAction = {}
        )
    }
}