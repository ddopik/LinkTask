package com.ddopik.linktask.base.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.google.android.material.tabs.TabLayout


/**
 * Created by ddopik on 9/17/2017.
 */

abstract class ViewPagerFragment : Fragment() {

    private var toolbar: Toolbar? = null

    abstract val mainView :Int

    abstract val mainLayout: Int

    abstract val fragments: List<Fragment>

    abstract val fragmentsTitles: ArrayList<String>

    abstract val tabLayout: Int
    abstract val viewPager: Int

    abstract val viewUpButton :Boolean ?

    abstract val viewPagerAdapter : ViewPagerAdapter

    abstract fun getToolbar(): Toolbar?



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         val mainPagerView = layoutInflater.inflate(mainLayout, container, false)
        getToolbar()?.let {
            toolbar = it
            (activity as AppCompatActivity).setSupportActionBar(it)

        }
        //        getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUp())

        val viewPagerLayout =mainPagerView.findViewById<ViewPager>(viewPager)
        val viewTabLayout = mainPagerView.findViewById<TabLayout>(tabLayout)

        viewTabLayout.setupWithViewPager( mainPagerView.findViewById<ViewPager>(viewPager))
        viewPagerLayout.adapter = setViewPagerAdapterFragment(viewPagerAdapter, fragments, fragmentsTitles)
         return mainPagerView
    }
    private fun <T> setViewPagerAdapterFragment(adapter: ViewPagerAdapter, fragments: List<T>, fragmentTitles: ArrayList<String>): FragmentPagerAdapter {
        var i = 0
        for (fragment in fragments) {
            adapter.addFragment(fragment, fragmentTitles[i])
            i++
        }
        return adapter
    }


}

