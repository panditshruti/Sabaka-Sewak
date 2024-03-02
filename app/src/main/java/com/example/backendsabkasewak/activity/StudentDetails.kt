package com.example.backendsabkasewak.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.backendsabkasewak.R

import com.example.backendsabkasewak.databinding.ActivityStudentDetailsBinding

import com.example.backendsabkasewak.FormFragment.LibraryFragment
import com.example.backendsabkasewak.FormFragment.SchoolFragment
import com.example.backendsabkasewak.FormFragment.TestsFragment

import com.google.android.material.tabs.TabLayout


class StudentDetails : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
    setContentView(binding.root)

        // Setup TabLayout with ViewPager
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // Add fragments for each tab
        adapter.addFragment(TestsFragment(), "Test Series")
        adapter.addFragment(LibraryFragment(), "Library")
        adapter.addFragment(SchoolFragment(), "School")
        // Add more fragments as needed

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }


    internal class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments: MutableList<Fragment> = ArrayList()
        private val titles: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}
