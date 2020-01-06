package app.appworks.school.stylish.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.FragmentCatalogBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class CatalogFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        init()
        FragmentCatalogBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@CatalogFragment
            viewpagerCatalog.let {
                tabsCatalog.setupWithViewPager(it)
                it.adapter = CatalogAdapter(childFragmentManager)
                it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsCatalog))

            }

//            filterButton.setOnClickListener{
//
//                findNavController().navigate(R.id.action_global_catalogSortFilterDialog)
//
//            }

            return@onCreateView root
        }




    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.CATALOG
//            }
//        }
//    }
}