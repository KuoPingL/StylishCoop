package app.appworks.school.stylish.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.appworks.school.stylish.MainViewModel
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.DialogEmailloginBinding
import app.appworks.school.stylish.ext.getVmFactory

class EmailLoginDialog : AppCompatDialogFragment() {

    private lateinit var binding: DialogEmailloginBinding

    private val viewModel by viewModels<EmailLoginViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.LoginDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEmailloginBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.dialog = this

        viewModel.isSignUp.observe(this, Observer {
            it?.let {isSignUp ->
                binding.textviewEmailloginSignup.isSelected = isSignUp
                binding.textviewEmailloginSignin.isSelected = !isSignUp
            }
        })

        val mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.user.observe(this, Observer {
            it?.let {
                mainViewModel.setupUser(it)
            }
        })

        viewModel.navigateToLoginSuccess.observe(this, Observer {
            it?.let {
                mainViewModel.navigateToLoginSuccess(it)
                dismiss()
            }
        })

        return binding.root
    }
}