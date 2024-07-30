package com.gyasilarbi.jobdash

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2 
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> OfferFragment<Any>()
            1 -> RequestFragment()
            else -> OfferFragment<Any>()
        }
    }
}