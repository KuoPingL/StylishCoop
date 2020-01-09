package app.appworks.school.stylish.home

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.MainActivity
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.Currency
import app.appworks.school.stylish.data.CurrencyDropItem
import app.appworks.school.stylish.databinding.FragmentHomeBinding
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.login.UserManager


/**
 * Created by Wayne Chen in Jul. 2019.
 */
class HomeFragment : Fragment() {

    /**
     * Lazily initialize our [HomeViewModel].
     */
    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.recyclerHome.adapter = HomeAdapter(HomeAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        binding.layoutSwipeRefreshHome.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshStatus.observe(this, Observer {
            it?.let {
                binding.layoutSwipeRefreshHome.isRefreshing = it
            }
        })

        viewModel.navigateToDetail.observe(this, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToDetailFragment(it))
                viewModel.onDetailNavigated()
            }
        })

        val items = arrayOf(CurrencyDropItem(Currency.TWD), CurrencyDropItem(Currency.USD), CurrencyDropItem(Currency.JPY))
        context?.let {
            binding.spinnerHomeCurrency.adapter = CustomDropDownAdapter(items = items)

            binding.spinnerHomeCurrency.setSelection(Currency.values().indexOf(Currency.valueOf(UserManager.userCurrency)))
        }


        binding.spinnerHomeCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                UserManager.userCurrency = getString(Currency.values()[position].abbRes)
            }

        }

        /**
         * CHECK if user is first login or has login
         * */


        return binding.root
    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.HOME
//            }
//        }
//    }
}