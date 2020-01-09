package app.appworks.school.stylish.userlogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.DialogCommercialAdBinding
import app.appworks.school.stylish.databinding.DialogUserLoginAlertBinding
import app.appworks.school.stylish.ext.getVmFactory


class UserLoginDialog(val callback: (willSignUpOrSignIn: Boolean) -> Unit) : AppCompatDialogFragment() {

    /**
     * Lazily initialize our [ProfileViewModel].
     */
    private val viewModel by viewModels<UserLoginDialogViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.LoginDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DialogUserLoginAlertBinding.inflate(inflater, container, false)



//        isCancelable = false

        //if buttonCloseJustWatchAd onclick, dismiss the userSignUp alert dialog
        binding.buttonCloseJustWatchAd.setOnClickListener{
            callback(false)
            dismiss()
        }

        binding.buttonSignUpOrIn.setOnClickListener{
            callback(true)
            dismiss()



//          1.  binding.buttonCloseJustWatchAd.setOnClickListener{
//                callback(false)
//            }
//          2.  callback(true)
//          3.  userLogin?.show(supportFragmentManager, "tag")
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        return binding.root

    }


}