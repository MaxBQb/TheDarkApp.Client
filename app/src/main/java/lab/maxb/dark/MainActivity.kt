package lab.maxb.dark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import lab.maxb.dark.Domain.Model.User
import lab.maxb.dark.Presentation.Repository.Repository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        setContentView(R.layout.main_activity)
        Repository.init(application)

        // Hardcode current user
        lifecycleScope.launch {
            Repository.users.addUser(
                User("CURRENT_USER", 0, "UUID")
            )
        }
    }
}