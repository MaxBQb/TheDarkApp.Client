package lab.maxb.dark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Repository
import lab.maxb.dark.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private var mBinding: MainActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        setSupportActionBar(mBinding!!.toolbar)
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        Repository.init(application)

        // Hardcode current user
        lifecycleScope.launch {
            Repository.users.addUser(
                User("CURRENT_USER", 0, "UUID")
            )
        }
    }
}