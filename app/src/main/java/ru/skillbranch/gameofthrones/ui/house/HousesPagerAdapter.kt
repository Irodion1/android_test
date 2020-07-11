package ru.skillbranch.gameofthrones.ui.house

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.skillbranch.gameofthrones.data.local.entities.HouseType

class HousesPagerAdapter(var fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return HousesFragment.newInstance(HouseType.values()[position].title)
    }

    override fun getCount(): Int {
        return HouseType.values().size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return HouseType.values()[position].title
    }
}