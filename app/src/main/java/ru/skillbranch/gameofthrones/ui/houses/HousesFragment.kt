package ru.skillbranch.gameofthrones.ui.houses

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
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
        with(tabs) {
            setupWithViewPager(view_pager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab) {
                    val position = tab.position
                    //val rect = Rect()
//                    val tabView = tab.view as View

                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        housesPagerAdapter = HousesPagerAdapter(childFragmentManager)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        with(menu.findItem(R.id.app_bar_search)?.actionView as SearchView) {
            queryHint = "Search character"
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

}