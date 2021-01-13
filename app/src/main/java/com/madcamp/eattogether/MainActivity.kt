package com.madcamp.eattogether

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val adapter = FragmentAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            tab.text = (viewPager.adapter as FragmentAdapter).getTitle(position)
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_group -> Toast.makeText(this, "Add Group",LENGTH_SHORT).show()
            R.id.logout -> {
                LoginManager.getInstance().logOut()
                GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/permissions/",
                    null,
                    HttpMethod.DELETE,
                    GraphRequest.Callback { LoginManager.getInstance().logOut() }).executeAsync()
                this.finish()
            }
            R.id.test -> {
                val testIntent = Intent(this@MainActivity, AddAppointment::class.java)
                startActivity(testIntent)
            }
            R.id.test_image -> {
                val testIntent = Intent(this@MainActivity, ImageTestActivity::class.java)
                startActivity(testIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    private val fragmentList = listOf(
        Pair("연락처", PeopleFragment()), Pair("예정된 약속", GroupFragment()), Pair("이전 약속", ReviewFragment())
    )
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].second
    }

    fun getTitle(position:Int):String {
        return fragmentList[position].first
    }
}