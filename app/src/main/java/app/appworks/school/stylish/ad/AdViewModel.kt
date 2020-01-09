package app.appworks.school.stylish.ad

import android.os.CountDownTimer
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.HomeItem
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Result
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.util.Logger
import kotlinx.android.synthetic.main.dialog_commercial_ad.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.logging.Handler
import kotlin.random.Random
import android.R.string.cancel
import androidx.databinding.adapters.SeekBarBindingAdapter.setProgress
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import app.appworks.school.stylish.StylishApplication
import java.util.*


class AdViewModel(private val stylishRepository: StylishRepository) : ViewModel() {

    private val _adItems = MutableLiveData<List<HomeItem>>()

    val homeItems: LiveData<List<HomeItem>>
        get() = _adItems

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Product>()

    val navigateToDetail: LiveData<Product>
        get() = _navigateToDetail


    // Handle leave advertisement
    private val _leave = MutableLiveData<Boolean>()

    val leave: LiveData<Boolean>
        get() = _leave


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

    /**
     * Call getMarketingHotsResult() on init so we can display status immediately.
     */
    init {


        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")

//        getMarketingHotsResult(true)
    }


    /**
     * track [StylishRepository.getMarketingHots]: -> [DefaultStylishRepository] : [StylishRepository] -> [StylishRemoteDataSource] : [StylishDataSource]
     */
//    private fun getMarketingHotsResult(isInitial: Boolean = false) {
//
//        coroutineScope.launch {
//
//            if (isInitial) _status.value = LoadApiStatus.LOADING
//            // It will return Result object after Deferred flow
//            val result = stylishRepository.getProductAll(UserManager.userToken, UserManager.userCurrency)
//
//            _homeItems.value = when (result) {
//                is Result.Success -> {
//                    _error.value = null
//                    if (isInitial) _status.value = LoadApiStatus.DONE
//                    result.data
//                }
//                is Result.Fail -> {
//                    _error.value = result.error
//                    if (isInitial) _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                is Result.Error -> {
//                    _error.value = result.exception.toString()
//                    if (isInitial) _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                else -> {
//                    _error.value = getString(R.string.you_know_nothing)
//                    if (isInitial) _status.value = LoadApiStatus.ERROR
//                    null
//                }
//            }
//            _refreshStatus.value = false
//        }
//    }

//    fun refresh() {
//        if (status.value != LoadApiStatus.LOADING) {
//            getMarketingHotsResult()
//        }
//    }

//    fun navigateToDetail(product: Product) {
//        if (_status.value != LoadApiStatus.DONE) {return}
//        _status.value = LoadApiStatus.LOADING
//
//        coroutineScope.launch {
//            val result = stylishRepository
//                .getProductDetail(UserManager.userToken ?: "TWD",
//                    UserManager.userCurrency, product.id.toString())
//
//            when(result) {
//                is Result.Success -> {
//                    if (result.data.error == null) {
//                        _navigateToDetail.value = result.data.product
//                        _status.value = LoadApiStatus.DONE
//                    } else {
//                        _navigateToDetail.value = null
//                        _status.value = LoadApiStatus.ERROR
//                    }
//                }
//
//                else -> {
//                    _navigateToDetail.value = null
//                    _status.value = LoadApiStatus.ERROR
//                }
//            }
//
//        }
//    }

//    fun onDetailNavigated() {
//        _navigateToDetail.value = null
//    }

    //declare a global variable
    var timer: CountDownTimer? = null


    //progressBar_ad



    private val _advertisePicture = MutableLiveData<String>()
    val advertisePicture: LiveData<String>
        get() = _advertisePicture

    //what is coroutineScope.launch?
    fun fetchAD() {
        coroutineScope.launch {

            val tag = "GET AD"
            val result = stylishRepository.getAd()


            when (result) {
                is Result.Success -> {
                    if (result.data.error == null) {
                        result.data.ad?.let { ads ->

                            //display pictures randomly ( from 6 pictures)
                            val rand = Random(ads.images?.size ?: 0)

                            val index = rand.nextInt(ads.images?.size ?: 0)

                            _advertisePicture.value = ads.images!![index]

                            //handle countdown
                            _advertiseCountDown.value = ads.displayTime ?: 0

                            if (timer == null){
                                Log.d("10seconds","timer == null")
                                timer = object: CountDownTimer((_advertiseCountDown.value!! * 1000).toLong(), 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        Log.d("10seconds","onTick,  millisUntilFinished=$millisUntilFinished")
                                        countDown()


                                    }

                                    override fun onFinish() {
                                        Log.d("10seconds","onFinish")
                                        _advertiseCountDown.value = 0
                                    }
                                }
                            }
                            timer?.start()

                        }

                    } else {

                        Log.i(tag, "RESULT : ${result.data.ad}")


                    }
                }

                is Result.Error -> {
                    Log.i(tag, "ERROR : ${result.exception.message}")
                }

                is Result.Fail ->{
                    Log.i(tag, "FAIL : ${result.error}")
                }
            }

        }
    }





    private val _advertiseCountDown = MutableLiveData<Int>()
    val advertiseCountDown: LiveData<Int>
        get() = _advertiseCountDown



    fun countDown() {
        Log.d("10seconds","countDown")
        Log.d("10seconds","before ${_advertiseCountDown.value}")
        _advertiseCountDown.value = _advertiseCountDown.value?.minus(1)
        Log.d("10seconds","after ${_advertiseCountDown.value}")

    }

    //handle ad close button

    fun leave() {
        _leave.value = true
    }
    //handle ad close button

    fun doneLeaving() {
        _leave.value = null
    }


    //timer not related to API liveData



//    val timer2 = object: CountDownTimer(10000, 1000) {
//        override fun onTick(millisUntilFinished: Long) {
//            countDown()
//        }
//
//        override fun onFinish() {
//
//        }
//
//    }




}