package cz.dcervenka.snow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.dcervenka.snow.ui.detail.DetailScreenRoot
import cz.dcervenka.snow.ui.overview.OverviewScreenRoot
import cz.dcervenka.snow.ui.BaseViewModel
import cz.dcervenka.snow.ui.theme.SnowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnowTheme(darkTheme = isSystemInDarkTheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: BaseViewModel = hiltViewModel()
                    NavHost(
                        navController = navController,
                        startDestination = "overview",
                    ) {
                        composable(route = "overview") {
                            OverviewScreenRoot(
                                onDetailClick = { navController.navigate("detail") },
                                viewModel = viewModel,
                            )
                        }
                        composable(route = "detail") {
                            DetailScreenRoot(
                                onMoreInfoClick = { url ->
                                    if (url != null) {
                                        val browserIntent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        startActivity(browserIntent)
                                    }
                                },
                                onBackClick = { navController.navigateUp() },
                                viewModel = viewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}