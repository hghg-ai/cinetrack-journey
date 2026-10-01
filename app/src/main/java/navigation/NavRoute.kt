package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoute ( val route: String, val title: String?= null,
        val icon: ImageVector? = null){
    object Home : NavRoute("home","Trang chủ", Icons.Default.Home)
    object Search: NavRoute("search", "Tìm kiếm", Icons.Default.Search)
    object Favorites: NavRoute("favorite","Yêu thích", Icons.Default.Favorite)
    object Detail: NavRoute("detail/{movieID}"){
        fun createRoute(movieID: Int) = "detail/$movieID"

    }
}