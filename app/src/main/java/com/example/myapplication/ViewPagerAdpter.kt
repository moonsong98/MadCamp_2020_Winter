package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter



class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Contact()
            1 -> Gallery()
            2 -> Test()
            else -> Contact()
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Contact"
            1 -> "Gallery"
            2 -> "Test"
            else -> null
        }
    }
    /*
    private val fragmentList: ArrayList<Fragment> = ArrayList<Fragment>();
    private val fragmentTitleList: ArrayList<String> = ArrayList<String>();

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

*/

}