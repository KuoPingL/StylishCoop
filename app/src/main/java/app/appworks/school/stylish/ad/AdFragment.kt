package app.appworks.school.stylish.ad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import app.appworks.school.stylish.databinding.DialogCommercialAdBinding
import app.appworks.school.stylish.ext.getVmFactory

class AdFragment : AppCompatDialogFragment() {

    /**
     * Lazily initialize our [ProfileViewModel].
     */
    private val viewModel by viewModels<AdViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        init()
        val binding = DialogCommercialAdBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel



//        if (viewModel.status.value == null) {
//            // user info will be null if user already logged in, and it will get user info from server,
//            // here will show you how to set user info to MainViewModel
//            val adViewModel = ViewModelProviders.of(activity!!).get(AdViewModel::class.java)
//            viewModel.status.observe(this, Observer {
//                if (null != it) {
//                    adViewModel.setupUser(it)
//                }
//            })
//        }

        return binding.root

    }


}


//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.PROFILE
//            }
//        }
//    }