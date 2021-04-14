package com.raghul.asset_tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.config.SharedPreferenceUtils
import com.raghul.asset_tracker.fragment.AssetFragment
import com.raghul.asset_tracker.fragment.DashboardFragment
import com.raghul.asset_tracker.fragment.NotificationFragment
import com.raghul.asset_tracker.model.Asset
import com.raghul.asset_tracker.service.LocationService
import com.raghul.asset_tracker.utils.CommonConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var  drawer:DrawerLayout
    private lateinit var navigationView:NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        navigationView = findViewById(R.id.navigation_view)
        drawer = findViewById(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(this,drawer,
               toolbar, R.string.navigation_open_drawer,R.string.navigation_close_drawer)
        drawer.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        drawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener (navItemListener)
        navigationView.setCheckedItem(R.id.nav_dashboard)
        var navigationHeaderView = navigationView.getHeaderView(0)
        var nameTextView: TextView = navigationHeaderView.findViewById(R.id.user_text)
        nameTextView.setText(SharedPreferenceUtils.getString(CommonConstants.USERNAME))
        loadFragment(DashboardFragment())

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val navItemListener : NavigationView.OnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.nav_dashboard -> {
                loadFragment(DashboardFragment())
            }
            R.id.nav_asset ->{
                loadFragment(AssetFragment())
            }
            R.id.nav_notification->{
                loadFragment(NotificationFragment())
            }
            R.id.settings->{
                startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
            }

            R.id.logout->{
                SharedPreferenceUtils.clearAllPreference()
                finish()
            }
            else -> Toast.makeText(this,"Other clicked",Toast.LENGTH_LONG).show()
        }
        true
    }

    private fun loadFragment(fragment: Fragment): Unit{
       val supportManager  = supportFragmentManager
        val fTransaction  = supportManager.beginTransaction()
        fTransaction.replace(R.id.fragment_container,fragment)
        fTransaction.commit()

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }


}