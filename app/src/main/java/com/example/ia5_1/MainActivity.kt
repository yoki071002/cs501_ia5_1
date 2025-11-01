package com.example.ia5_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ia5_1.ui.theme.Ia5_1Theme


sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Detail : Routes("detail")
    object Add : Routes("add")
    object Settings : Routes("settings")
}

data class Recipe(val id: Int, val title: String, val ingredients: String, val steps: String)

class RecipeViewModel : ViewModel() {
    private val _recipes = mutableStateListOf(
        Recipe(0, "noodles", "Waster, Noodle", "Boiled water, add noodles"),
        Recipe(1, "cake", "Flour, Egg, Milk", "Mix and fry")
    )
    val recipes: List<Recipe> = _recipes
    fun addRecipe(title: String, ingredients: String, steps: String) {
        val newId = (_recipes.maxOfOrNull { it.id } ?: 0) + 1
        _recipes.add(Recipe(newId, title, ingredients, steps))
    }
    fun getRecipeById(id: Int): Recipe? = _recipes.find { it.id == id }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppEntry()
        }
    }
}

@Composable
fun DinnerApp() {
    val navController = rememberNavController()
    val vm: RecipeViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        navController.navigate(Routes.Home.route) {
                            popUpTo(Routes.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Add") },
                    selected = false,
                    onClick = {
                        navController.navigate(Routes.Add.route) {
                            popUpTo(Routes.Home.route)
                            launchSingleTop = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        navController.navigate(Routes.Settings.route) {
                            popUpTo(Routes.Home.route)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(navController, vm)
            }
            composable(
                route = Routes.Detail.route + "/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DetailScreen(recipe = vm.getRecipeById(id))
            }
            composable(Routes.Add.route) {
                AddRecipeScreen(navController, vm)
            }
            composable(Routes.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, vm: RecipeViewModel) {
    LazyColumn {
        items(vm.recipes) { recipe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(Routes.Detail.route + "/${recipe.id}") {
                            launchSingleTop = true
                        }
                    }
            ) {
                Text(
                    text = recipe.title,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun DetailScreen(recipe: Recipe?) {
    if (recipe == null) {
        Text("Recipe not found", modifier = Modifier.padding(16.dp))
        return
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(recipe.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Ingredients:\n${recipe.ingredients}")
        Spacer(Modifier.height(8.dp))
        Text("Steps:\n${recipe.steps}")
    }
}

@Composable
fun AddRecipeScreen(navController: NavController, vm: RecipeViewModel) {
    var title by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Ingredients") })
        OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("Steps") })
    }
    Spacer(Modifier.padding(16.dp))
    Button(onClick = {
        if (title.isNotBlank()) {
            vm.addRecipe(title, ingredients, steps)
            navController.navigate(Routes.Home.route) {
                popUpTo(Routes.Home.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }) {
        Text("Save Recipe")
    }
}

@Composable
fun SettingsScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
        Text("Wait for further updates")
    }
}

@Composable
fun AppEntry() {
    MaterialTheme {
        DinnerApp()
    }
}