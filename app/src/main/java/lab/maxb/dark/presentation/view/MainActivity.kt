package lab.maxb.dark.presentation.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.databinding.MainActivityBinding
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(R.layout.main_activity),
    NavigationView.OnNavigationItemSelectedListener {
    private val binding: MainActivityBinding by viewBinding()
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private val navController get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            as NavHostFragment).navController

    companion object {
        private val navbarDestinations = mapOf(
            R.id.menu_nav_main to R.id.nav_main_fragment,
            R.id.menu_nav_tasksList to R.id.nav_taskList_fragment,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Dark theme
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        // Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Sidebar (Drawer) Animation
        actionBarToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_drawerDescription_open,
            R.string.nav_drawerDescription_close
        )
        binding.drawerLayout.addDrawerListener(actionBarToggle)

        // Navigation + Sidebar
        val appBarConfiguration = AppBarConfiguration(
            navbarDestinations.values.toSet(),
            binding.drawerLayout,
            ::onSupportNavigateUp,
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(this)

        // Logic
        authViewModel.isAuthorized observe {
            binding.drawerLayout.setDrawerLockMode(
                if (it) DrawerLayout.LOCK_MODE_UNLOCKED
                else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            )
            binding.toolbar.isVisible = it
        }

        authViewModel.profile observe {
            it.ifLoaded { profile ->
                if (profile == null && navController.currentDestination?.id != R.id.nav_auth_fragment)
                    navController.navigate(NavGraphDirections.navToAuthFragment())
            }
        }
    }

    override fun onBackPressed(): Unit = with(binding.drawerLayout) {
        if (isDrawerOpen(GravityCompat.START))
            closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarToggle.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val destination = navbarDestinations[item.itemId] ?: TODO()
        if (navController.currentDestination?.id == destination)
            return false
        navController.navigate(destination)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun withToolbar(block: Toolbar.() -> Unit) = launch {
        binding.toolbar.apply(block)
    }
}