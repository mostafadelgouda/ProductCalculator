package com.example.productcalculator.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.productcalculator.R

enum class ProductCalculatorScreen(val title: String) {
    MainScreen(title = "Main Screen"),
    EditScreen(title = "Edit Screen"),
    HistoryScreen(title = "History Screen"),

}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCalculatorAppBar(
    currentScreenTitle: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer ),
        title = {

            Text(
                text = if (currentScreenTitle == "Main Screen") "Product Calculator" else currentScreenTitle,
                modifier = Modifier.fillMaxWidth()
            )

                },
        modifier = modifier,

        actions = {

                // Add your buttons here
            IconButton(onClick = {
                navController.navigate(ProductCalculatorScreen.EditScreen.name)
            }) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    //.weight(1f),
                    painter = painterResource(
                        id = R.drawable.edit
                    ),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                navController.navigate(ProductCalculatorScreen.HistoryScreen.name)
            }) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    //.weight(1f),
                    painter = painterResource(
                        id = R.drawable.history
                    ),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                navController.navigate(ProductCalculatorScreen.MainScreen.name)
            }) {
                Icon(
                    modifier = Modifier
                        .size(40.dp),
                        //.weight(1f),
                    painter = painterResource(
                        id = R.drawable.calculator
                    ),
                    contentDescription = null
                )
//                Icon(
//                    imageVector = Icons.Rounded.,
//                    contentDescription = null
//                )
            }
                // Add more buttons as needed

        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductCalculatorApp() {
    //Create NavController
    val navController = rememberNavController()
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = ProductCalculatorScreen.valueOf(
        backStackEntry?.destination?.route ?: ProductCalculatorScreen.MainScreen.name
    )
    // Create ViewModel
    val viewModel: ProductCalculatorViewModel = viewModel(factory = ProductCalculatorViewModel.factory)

    Scaffold(
        topBar = {
            ProductCalculatorAppBar(
                currentScreenTitle = currentScreen.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navController = navController
            )
        },
    ) { innerPadding ->
        //val uiState by viewModel._state.collectAsState()
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = ProductCalculatorScreen.MainScreen.name,
        ) {
            composable(route = ProductCalculatorScreen.MainScreen.name) {
                MainScreen(viewModel = viewModel)
            }
            composable(route = ProductCalculatorScreen.EditScreen.name) {
                EditScreen(viewModel = viewModel)
            }
            composable(route = ProductCalculatorScreen.HistoryScreen.name) {
                HistoryScreen(viewModel = viewModel)
            }



        }
    }
}