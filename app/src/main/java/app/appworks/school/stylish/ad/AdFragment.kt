package app.appworks.school.stylish.ad

import android.os.Bundle
import android.util.Log
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
import app.appworks.school.stylish.databinding.DialogUserLoginAlertBinding
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

//        val bindingUserSignInAlert = DialogUserLoginAlertBinding.inflate(inflater,container,false)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        isCancelable = false

        //observe if value of leave changed, if yes, change doneLeaving() 's value, and  execute dismiss() after finish running doneLeaving().
        viewModel.leave.observe(this, Observer {
            it?.let {
                if (it) {
                    viewModel.doneLeaving()
                    dismiss()
                }
            }
        })

        // observe  countdown and have progressBarAd ascend
        viewModel.advertiseCountDown.observe(this, Observer {
            Log.d("10seconds","viewModel.advertiseCountDown.observe,it = $it")
            it?.let {
                binding.progressBarAd.progress = it * 10
            }
        })


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


//        if(bindingUserSignInAlert.buttonCloseJustWatchAd.click == true) {
            viewModel.fetchAD()
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