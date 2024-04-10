sealed class Screen(val route: String) {
    object MainScreen : Screen("mainScreen")
    object DetailScreen1 : Screen("detailScreen1")
    object DetailScreen2 : Screen("detailScreen2")
    // Add other screens here
}
