package ru.skillbranch.gameofthrones.ui.houses.house

import androidx.fragment.app.Fragment

class HouseFragment : Fragment() {
    //private lateinit var viewModel: HousesViewModel
    companion object {
        fun newInstance(houseTitle: String): HouseFragment {
            return HouseFragment()
        }

    }
}