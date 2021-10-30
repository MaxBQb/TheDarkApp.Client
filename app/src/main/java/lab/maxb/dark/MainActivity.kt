package lab.maxb.dark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.Role
import lab.maxb.dark.Domain.Model.Server.Profile
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Interfaces.ProfileRepository
import lab.maxb.dark.Presentation.Repository.Interfaces.UsersRepository
import lab.maxb.dark.databinding.MainActivityBinding
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    private val usersRepository by inject<UsersRepository>()
    private val profileRepository by inject<ProfileRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        setSupportActionBar(binding.toolbar)
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Hardcode current user
        lifecycleScope.launch {
            usersRepository.addUser(
                User("CURRENT_USER", 0, "UUID")
            )
            profileRepository.addProfile(Profile(
                "Admin",
                0,
                Role.ADMINISTRATOR,
                "adminUUID",
                password = "admin"
            ))
            profileRepository.addProfile(Profile(
                "Moderator",
                0,
                Role.MODERATOR,
                "moderatorUUID",
                password = "moderator"
            ))
            profileRepository.addProfile(Profile(
                "User",
                0,
                Role.MODERATOR,
                "userUUID",
                password = "user"
            ))
        }
    }
}