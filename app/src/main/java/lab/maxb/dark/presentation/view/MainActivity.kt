package lab.maxb.dark.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.databinding.MainActivityBinding
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(R.layout.main_activity) {
    val binding: MainActivityBinding by viewBinding()
    private val authViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        setSupportActionBar(binding.toolbar)
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        authViewModel.profile observe {
            it.ifLoaded { profile ->
                binding.toolbar.isVisible = profile != null
                if (profile == null
                    && navController.currentDestination?.id !in listOf(
                        R.id.login_fragment,
                        R.id.signup_fragment,
                    ))
                    navController.navigate(
                        NavGraphDirections.actionGlobalLoginFragment()
                    )
            }
        }
    }

    fun withToolbar(block: Toolbar.() -> Unit) = launch {
        binding.toolbar.apply(block)
    }
}