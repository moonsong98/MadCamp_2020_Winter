package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter



class TabViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm)
{
    private var fragmentList = listOf(
        Pair("Restaurant List", Contact()), Pair("Delivery", Gallery()), Pair("Visit", VisitReview())
    )

    override fun getItem(position: Int): Fragment {
        return fragmentList[position].second
    }

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].first
    }
}