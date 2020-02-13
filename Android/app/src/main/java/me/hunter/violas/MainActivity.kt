package me.hunter.violas

import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import io.violas.sdk.Client

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

            try {
                run_violas_sdk()

                Snackbar.make(view, "Create client successfully ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            catch (e : Exception)
            {
                Snackbar.make(view, "failed with " + e.message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }



        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun run_violas_sdk()
    {
        //var sdcard = getExternalStorageDirectory()
        var fileDir = getExternalFilesDir(null)

        var client = Client(
            "125.39.5.57",
            40001.toUShort(),
            fileDir.toString() + "/mint.key",
            false,
            "",
            fileDir.toString() + "/mnenonic"
        )

        client.test_validator_connection()

        var account0 = client.createNextAccount()

        var account1 = client.createNextAccount()

        for ( account in client.getAllAccounts() )
        {
            Log.v("Violas", "index=" + account.index)
        }

    }
}
