package app.appworks.school.stylish.catalog.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.catalog.CatalogTypeFilter
import app.appworks.school.stylish.component.GridSpacingItemDecoration
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.UserRecord
import app.appworks.school.stylish.data.source.StylishRepository
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.network.Order
import app.appworks.school.stylish.network.Sort
import app.appworks.school.stylish.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * The [ViewModel] that is attached to the [CatalogItemFragment].
 */
class CatalogItemViewModel(
    private val stylishRepository: StylishRepository,
    catalogType: CatalogTypeFilter, // Handle the type for each catalog item
    sort: Sort, // add sort property
    order: Order // add order property
) : ViewModel() {

    // factory have three properties catalogType, sort and order.
    private val sourceFactory = PagingDataSourceFactory(catalogType, sort, order)

    //
    fun refreshWithSortAndOrder(sort: Sort, order: Order) {
        sourceFactory.sort = sort
        sourceFactory.order = order
        refresh()
    }

    val pagingDataProducts: LiveData<PagedList<Product>> = sourceFactory.toLiveData(6, null)

    // Handle load api status
    val status: LiveData<LoadApiStatus> = Transformations.switchMap(sourceFactory.sourceLiveData) {
        it.statusInitialLoad
    }

    // Handle load api error
    val error: LiveData<String> = Transformations.switchMap(sourceFactory.sourceLiveData) {
        it.errorInitialLoad
    }

    // Handle navigation to detail
    private val _navigateToDetail = MutableLiveData<Product>()

    val navigateToDetail: LiveData<Product>
        get() = _navigateToDetail

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val decoration = GridSpacingItemDecoration(
        2,
        StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_catalog_grid), true
    )

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

    fun refresh() {
        if (status.value != LoadApiStatus.LOADING) {
            sourceFactory.sourceLiveData.value?.invalidate()
        }
    }

    fun navigateToDetail(product: Product) {

        if (UserManager.isLoggedIn) {
            coroutineScope.launch {
                stylishRepository.getProductDetail(UserManager.userToken ?: "", UserManager.userCurrency, product.id.toString())
            }
        } else {
            coroutineScope.launch {
                stylishRepository.insert(UserRecord(product.id, product.title, product.price, product.mainImage))
            }
        }

        _navigateToDetail.value = product
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}