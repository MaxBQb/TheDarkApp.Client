package lab.maxb.dark.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import lab.maxb.dark.ui.navigation_drawer.R
import lab.maxb.dark.ui.core.R as coreR
import lab.maxb.dark.ui.navigation.api.R as navR

enum class DrawerDestination(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
) {
    Welcome(R.drawable.ic_home, navR.string.nav_home_title),
    Tasks(R.drawable.ic_list, navR.string.nav_taskList_title),
    FavoriteTasks(coreR.drawable.ic_favorite, navR.string.nav_favoriteTaskList_title),
    Articles(R.drawable.ic_articles, navR.string.nav_articles_title),
}
