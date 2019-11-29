package com.ddopik.linktask

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.ddopik.attendonb.base.BaseActivity
import com.ddopik.linktask.ui.explore.view.ExploreFragment
import com.ddopik.linktask.ui.main.OnSearchAction
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var navigationView: NavigationView? = null
    private var exploreFragmentListener: OnSearchAction? = null
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView?.setNavigationItemSelectedListener(this)
        /**
         * default tap
         * */
        onNavigationItemSelected(navigationView!!.menu.getItem(0))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.ic_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return false

            }
            /**
             * Search Tab listener
             * */
            override fun onQueryTextSubmit(query: String?): Boolean {
                val listF=  supportFragmentManager.fragments
                val frag =supportFragmentManager.findFragmentByTag(ExploreFragment.TAG)
                if(frag?.tag == ExploreFragment.TAG) {
                    exploreFragmentListener = frag as ExploreFragment
                    exploreFragmentListener?.onQueryComplete(query)
                 }
                menu.findItem(R.id.ic_search).collapseActionView()
                return true

            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ic_search -> true
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
         ////////////////////
        when (item.itemId) {
            R.id.nav_explore -> {
                val exploreFragment = ExploreFragment.getInstance()
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.home_swap_container,
                        exploreFragment,
                        ExploreFragment.TAG
                    )
                    .commit()

            }
            R.id.nav_life_chat -> {
                showToast("Life Chat")
            }
            R.id.nav_gallery -> {
                showToast("Gallery")
            }
            R.id.nav_wish_list -> {
                showToast("Wish List")
            }
            R.id.nav_e_magazine -> {
                showToast("E-Magazine")
            }
        }

        ////////////////////
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun initObservers() {

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            /**
             * Handling Back navigation
             * */
            val default = supportFragmentManager.fragments.last() is ExploreFragment
            if (default) {
                super.onBackPressed()
            } else {
                /**
                 * user have navigated to Main screen and first Activity in the stack
                 * */
                supportFragmentManager.findFragmentByTag(ExploreFragment.TAG)
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.home_swap_container,
                        ExploreFragment.getInstance(),
                        ExploreFragment.TAG
                    )
                    .commit()
                nav_view.menu.getItem(0).isChecked = true

            }
        }
    }

    }


