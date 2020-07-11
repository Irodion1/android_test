package ru.skillbranch.gameofthrones.ui.house

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.skillbranch.gameofthrones.R

class HousesFragment : Fragment() {

    private lateinit var housesPagerAdapter: HousesPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_houses, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        housesPagerAdapter = HousesPagerAdapter(childFragmentManager)
    }

    companion object {
        fun newInstance(houseTitle: String): HousesFragment {
            return HousesFragment()
        }

    }
}