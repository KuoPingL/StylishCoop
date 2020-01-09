package app.appworks.school.stylish.detail

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Result
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.data.UserRecord
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [DetailFragment].
 */
class DetailViewModel(
    private val stylishRepository: StylishRepository,
    private val arguments: Product
) : ViewModel() {

    enum class ChatbotStatus {
        SHOWING, HIDING, DONESHOWING, DONEHIDING
    }

    // Detail has product data from arguments
    private val _product = MutableLiveData<Product>().apply {
        value = arguments
    }

    val product: LiveData<Product>
        get() = _product

    val productSizesText: LiveData<String> = Transformations.map(product) {
        when (it.sizes.size) {
            0 -> ""
            1 -> it.sizes.first()
            else -> StylishApplication.instance.getString(R.string._dash_, it.sizes.first(), it.sizes.last())
        }
    }

    // it for gallery circles design
    private val _snapPosition = MutableLiveData<Int>()

    val snapPosition: LiveData<Int>
        get() = _snapPosition

    // Handle navigation to Add2cart
    private val _navigateToAdd2cart = MutableLiveData<Product>()

    val navigateToAdd2cart: LiveData<Product>
        get() = _navigateToAdd2cart

    // Handle leave detail
    private val _leaveDetail = MutableLiveData<Boolean>()

    val leaveDetail: LiveData<Boolean>
        get() = _leaveDetail



    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val decoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = 0
            } else {
                outRect.left = StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_detail_circle)
            }
        }
    }

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
        fetchUserRecord()
    }

    /**
     * When the gallery scroll, at the same time circles design will switch.
     */
    fun onGalleryScrollChange(layoutManager: RecyclerView.LayoutManager?, linearSnapHelper: LinearSnapHelper) {
        val snapView = linearSnapHelper.findSnapView(layoutManager)
        snapView?.let {
            layoutManager?.getPosition(snapView)?.let {
                if (it != snapPosition.value) {
                    _snapPosition.value = it
                }
            }
        }
    }

    fun navigateToAdd2cart(product: Product) {
        _navigateToAdd2cart.value = product
    }

    fun onAdd2cartNavigated() {
        _navigateToAdd2cart.value = null
    }

    fun leaveDetail() {
        _leaveDetail.value = true
    }


    /**
     * CHATBOT
     */

    private val _chatbotStatus = MutableLiveData<ChatbotStatus>()

    val chatbotStatus: LiveData<ChatbotStatus>
        get() = _chatbotStatus

    val isChatbotShown = MutableLiveData<Boolean>()

    fun showChatbot(isShown: Boolean?) {

        if (isShown == null || isShown == false) {
            _chatbotStatus.value = ChatbotStatus.SHOWING
        } else {
            _chatbotStatus.value = ChatbotStatus.HIDING
        }
    }

    fun resetChatbotStatus() {
        if (_chatbotStatus.value == ChatbotStatus.SHOWING) {
            _chatbotStatus.value = ChatbotStatus.DONESHOWING
            isChatbotShown.value = true
        } else {
            _chatbotStatus.value = ChatbotStatus.DONEHIDING
            isChatbotShown.value = false
        }
    }

    /**
     * User Records
     */

    val remoteRecord = stylishRepository.getUserViewRecords()


    private val _record = MutableLiveData<List<UserRecord>>()
    val record: LiveData<List<UserRecord>>
        get() = _record

    fun saveRecord() {
        if (!UserManager.isLoggedIn) {
            // save record at local

            coroutineScope.launch {
                stylishRepository.insert(UserRecord(arguments.id, arguments.title, arguments.price, arguments.mainImage))
            }
        }
    }

    fun fetchUserRecord() {
        if (UserManager.isLoggedIn) {
            // fetch Online

            coroutineScope.launch {
                UserManager.userToken?.let {
                    val result = stylishRepository.getUserViewingRecord(it)

                    when(result) {
                        is Result.Success -> {
                            _record.value = result.data.records
                        }

                        is Result.Error -> {
                            Log.i("fetchUserRecord", "error: ${result.exception.message}")
                        }

                        is Result.Fail -> {
                            Log.i("fetchUserRecord", "error: ${result.error}")
                        }

                        else -> {

                        }
                    }
                }
            }

        } else {
            // fetch database
            coroutineScope.launch {

            }
        }
    }

}