### What’s for Dinner?
a 3-screen navigation-based app for browsing and viewing user-defined recipes using route arguments and controlled backstack behavior.

Requirements:
• Home screen: list of recipe names (use LazyColumn)
• Detail screen: displays full recipe (title, ingredients, steps) using data passed via arguments
• Add Recipe screen: form for entering new recipe (basic state management)
• Use NavHostController, NavHost, and define a sealed Routes class
• Use navigate(route + "/{id}") to pass an argument and read it via backStackEntry
• Use popUpTo() to control stack behavior when adding a new recipe
• Prevent multiple copies of the same screen using launchSingleTop
• Style and layout using Scaffold and consistent navigation
• Implement a BottomNavigation bar for “Home”, “Add”, and “Settings”; Persist recipes using in-memory state in ViewModel
