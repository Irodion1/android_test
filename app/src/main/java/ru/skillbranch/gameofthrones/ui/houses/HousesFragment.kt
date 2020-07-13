package ru.skillbranch.gameofthrones.ui.houses

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_houses.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.RootActivity

class HousesFragment : Fragment() {

    private lateinit var housesPagerAdapter: HousesPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_houses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as RootActivity).setSupportActionBar(toolbar)
        view_pager.adapter = housesPagerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        housesPagerAdapter = HousesPagerAdapter(childFragmentManager)
//        with(tabs) {
//            setupWithViewPager(view_pager)
//            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onTabSelected(tab: TabLayout.Tab) {
//                    val position = tab.position
//
//                }
//            })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        with(menu.findItem(R.id.app_bar_search)?.actionView as SearchView) {
            queryHint = "Search character"
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance(houseTitle: String): HousesFragment {
            return HousesFragment()
        }

    }
}