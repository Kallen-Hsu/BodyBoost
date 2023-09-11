package com.example.bodyboost
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bodyboost.Setting.AboutUs
import com.example.bodyboost.Setting.EditFragment
import com.example.bodyboost.Setting.NotificationFragment
import com.example.bodyboost.Setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
//    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
//    private var showOneTapUI = true
    val currentUser = UserSingleton.user
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = SettingFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, fragment)
//            .commit()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.sport -> {
                    replaceFragment(SportFragment())
                    true
                }
                R.id.report -> {
                    replaceFragment(ReportFragment())
                    true
                }
                R.id.record -> {
                    replaceFragment(RecordFragment())
                    true
                }
                R.id.achievement -> {
                    replaceFragment(AchievementFragment())
                    true
                }
                R.id.notification -> {
                    replaceFragment(NotificationFragment())
                    true
                }
                R.id.upgrade -> {
                    replaceFragment(UpgradeFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())

        drawerLayout = findViewById(R.id.drawer_layout)
        //工具列
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //側邊功能列
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)
        }
//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
//            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
//                .setSupported(true)
//                .build())
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    //.setServerClientId(getString())
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            // Automatically sign in when exactly one credential is retrieved.
//            .setAutoSelectEnabled(true)
//            .build()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment())
            R.id.nav_edit -> replaceFragment(EditFragment())
            R.id.nav_sport_record -> replaceFragment(RecordFragment())
            R.id.nav_meal_record -> replaceFragment(RecordFragment())
            R.id.nav_question -> replaceFragment(HomeFragment())
            R.id.nav_setting -> replaceFragment(SettingFragment())
            R.id.nav_about -> replaceFragment(AboutUs())
            R.id.nav_logout -> {
                try {
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                    val intent = Intent()
                    AlertDialog.Builder(this)
                        //AlertDialog.Builder (context: Context)
                        //參數放要傳入的 MainActivity Context
                        .setTitle("確認要登出APP")
                        .setMessage(" ")  //訊息內容
                        .setPositiveButton("確認") {_,_ ->
                            intent.setClass(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                        }
                        .setNegativeButton("取消",null)
                        .create()
                        .show()

                }catch (exception: Exception){
                    return false
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    //Outside onCreate
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    private fun showNotificationDialog() {
        val account = currentUser?.account
        if (account != null) {
//            showAlertDialog(
//                title = "通知提醒",
//                message = "是否要打開手機提醒?",
//                positiveButtonText = "開啟",
//                positiveButtonAction = {
//
//                },
//                negativeButtonText = "取消"
//            )
        } else {
            // 處理 currentUser 為空的情況
            showToast("無法獲取帳號資訊")
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    //GOOGLE 登入
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            REQ_ONE_TAP -> {
//                try {
//                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                    val idToken = credential.googleIdToken
//                    val username = credential.id
//                    val password = credential.password
//                    when {
//                        idToken != null -> {
//                            // Got an ID token from Google. Use it to authenticate
//                            // with your backend.
//                            Log.d(TAG, "Got ID token.")
//                        }
//                        password != null -> {
//                            // Got a saved username and password. Use them to authenticate
//                            // with your backend.
//                            Log.d(TAG, "Got password.")
//                        }
//                        else -> {
//                            // Shouldn't happen.
//                            Log.d(TAG, "No ID token or password!")
//                        }
//                    }
//                } catch (e: ApiException) {
//                    // ...
//                }
//            }
//        }
//    }
}