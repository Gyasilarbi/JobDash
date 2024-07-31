package com.gyasilarbi.jobdash.adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gyasilarbi.jobdash.CompletedRequestFragment
import com.gyasilarbi.jobdash.InProgressRequestFragment
import com.gyasilarbi.jobdash.NewRequestFragment

class Tab2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NewRequestFragment<Any>()
            1 -> InProgressRequestFragment<Any>()
            2 -> CompletedRequestFragment<Any>()
            else -> NewRequestFragment<Any>()
        }
    }
}