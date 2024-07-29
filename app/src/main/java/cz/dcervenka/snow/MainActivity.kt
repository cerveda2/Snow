package cz.dcervenka.snow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.dcervenka.snow.ui.detail.DetailScreenRoot
import cz.dcervenka.snow.ui.overview.OverviewScreenRoot
import cz.dcervenka.snow.ui.theme.SnowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "overview",
                    ) {
                        composable(route = "overview") {
                            OverviewScreenRoot(
                                onDetailClick = { navController.navigate("detail") },
                            )
                        }
                        composable(route = "detail") {
                            DetailScreenRoot(
                                onMoreInfoClick = { /*TODO*/ },
                                onBackClick = { navController.navigateUp() },
                            )
                        }
                    }
                }
            }
        }
    }
}