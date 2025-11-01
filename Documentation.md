### Documentation
I use AI to help understand the concepts and how the navigation works, specifically how argument passing in Jetpack Compose Navigation truly does. At first, we both confused with the requirement in passing objects through the navigation route, doing things like ```“detail/$recipe”```, which does not work because the Compose did not accept passing through a whole object, but instead primitive types such as strings or integers. So, the alternative approach to solve this problem is to pass only the recipe’s unique ID as its identifier in the route, and then retrieve the object from the backStackEntry. And for now, this is how it is used in the project,
```
composable(
    route = Routes.Detail.route + "/{id}",
    arguments = listOf(navArgument("id") { type = NavType.IntType })
) { backStackEntry ->
    val id = backStackEntry.arguments?.getInt("id") ?: 0
    DetailScreen(recipe = vm.getRecipeById(id))
}
```
I also thought about asking questions to understand the concepts of backStackEntry using AI, a useful approach is - since I learned web app development for a whole semester, I ask it to find similar concepts in web-dev to act as a metaphor in relation to that in Kotlin, and I found this is quite useful for me in understanding.
