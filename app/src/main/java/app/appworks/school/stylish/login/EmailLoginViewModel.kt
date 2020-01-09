package app.appworks.school.stylish.login

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.Result
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.util.Logger
import app.appworks.school.stylish.util.Util
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EmailLoginViewModel(private val stylishRepository: StylishRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    val username = MutableLiveData<String>()
    val nameErrorMessage = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()
    val emailErrorMessage = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordErrorMessage = MutableLiveData<String>()

    val isSignUp = MutableLiveData<Boolean>()

    val confirmationMessage = MutableLiveData<String>()

    init {
        confirmationMessage.value = "登入"
        isSignUp.value = false
    }

    fun setSignIn() {
        isSignUp.value = false
        resetAllData()
        confirmationMessage.value = "登入"
    }

    fun setSignUp() {
        isSignUp.value = true
        resetAllData()
        confirmationMessage.value = "申請"
    }

    fun resetAllData() {
        passwordErrorMessage.value = null
        emailErrorMessage.value = null
        nameErrorMessage.value = null
    }

    fun isValidUsername(): Boolean {

        val username = username.value

        return if (username == null || username.isEmpty()) {
            nameErrorMessage.value = "請輸入帳號名稱"
            false
        } else {
            val reg = Regex("([A-Za-z0-9.])\\w+")
            if (!username.matches(reg)) {
                nameErrorMessage.value = "名稱只包含英文字母(a-z)、數字(0-9) 和半形句號(.)"
                false
            }
            true
        }

    }

    fun signInOrSignUp() {

        emailErrorMessage.value = null
        passwordErrorMessage.value = null

        /** DEFAULT is SIGNIN*/
        if (isSignUp.value == true) {
            // check user name
            if (!isValidUsername()) {
                return
            }
        }

        if (userEmail.value == null) {
            emailErrorMessage.value = "請輸入電子信箱"
            return
        }

        userEmail.value?.let {email ->
            if (email.isEmpty()) {
                emailErrorMessage.value = "請輸入電子信箱"
                return
            }

            if (!validateEmail(email)) {
                emailErrorMessage.value = "電子信箱格式有誤"
                return
            }
        }

        if (password.value == null) {
            passwordErrorMessage.value = "請輸入密碼"
            return
        }

        signInOrUp(userEmail.value!!, password.value!!, username.value)

    }

    private fun validateEmail(email: String): Boolean {
        val reg = Regex("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")//"(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        return  email.matches(reg)
    }


    // Handle navigation to login success
    private val _navigateToLoginSuccess = MutableLiveData<User>()

    val navigateToLoginSuccess: LiveData<User>
        get() = _navigateToLoginSuccess

    // Handle leave login
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")
    }

    /**
     * track [StylishRepository.userSignIn]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishRemoteDataSource] : [StylishDataSource]
     * @param
     */
    private fun signInOrUp(email: String, password: String, name: String?) {
        isSignUp.value?.let {signUp ->
            if (signUp) {
                coroutineScope.launch {
                    _status.value = LoadApiStatus.LOADING
                    when(val result = stylishRepository.userSignUp(name!!, email, password)) {
                        is Result.Success -> {
                            _error.value = null

                            if (result.data.error != null) {
                                passwordErrorMessage.value = result.data.error
                            } else {
                                UserManager.userToken = result.data.userSignUp?.accessToken
                                _user.value = result.data.userSignUp?.user
                                _navigateToLoginSuccess.value = user.value
                            }
                            _status.value = LoadApiStatus.DONE

                        }

                        is Result.Fail -> {
                            _error.value = result.error
                            passwordErrorMessage.value = result.error
                            _status.value = LoadApiStatus.ERROR
                        }

                        is Result.Error -> {
                            _error.value = result.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                        }
                        else -> {
                            _error.value = Util.getString(R.string.you_know_nothing)
                            _status.value = LoadApiStatus.ERROR
                        }
                    }
                }
            } else {
                coroutineScope.launch {
                    _status.value = LoadApiStatus.LOADING
                    when(val result = stylishRepository.userSignIn(email, password)) {
                        is Result.Success -> {
                            _error.value = null
                            passwordErrorMessage.value = result.data.error
                            _status.value = LoadApiStatus.DONE
                            UserManager.userToken = result.data.userSignIn?.accessToken
                            _user.value = result.data.userSignIn?.user
                            _navigateToLoginSuccess.value = user.value
                        }

                        is Result.Fail -> {
                            _error.value = result.error
                            passwordErrorMessage.value = result.error
                            _status.value = LoadApiStatus.ERROR
                        }

                        is Result.Error -> {
                            _error.value = result.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                        }
                        else -> {
                            _error.value = Util.getString(R.string.you_know_nothing)
                            _status.value = LoadApiStatus.ERROR
                        }
                    }
                }
            }
        }
    }
}