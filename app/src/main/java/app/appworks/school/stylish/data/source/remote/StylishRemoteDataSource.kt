package app.appworks.school.stylish.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import app.appworks.school.stylish.R
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.data.source.StylishDataSource
import app.appworks.school.stylish.network.Order
import app.appworks.school.stylish.network.Sort
import app.appworks.school.stylish.network.StylishApi
import app.appworks.school.stylish.network.StylishApiV2
import app.appworks.school.stylish.util.Logger
import app.appworks.school.stylish.util.Util.getString
import app.appworks.school.stylish.util.Util.isInternetConnected
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.http.HTTP

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Implementation of the Stylish source that from network.
 */
object StylishRemoteDataSource : StylishDataSource {

    override suspend fun updateGroupBuy(token: String, productID: Long
    ): Result<JoinGroupBuyResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.updateGroupBuy(token, productID)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun createGroupBuy(addGroupBuyBody: AddGroupBuyBody): Result<AddGroupBuyResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.createGroupBuy(addGroupBuyBody)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getGroupBuys(token: String): Result<GetGroupBuyResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.fetchMyGroupBuy(token)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getReplyFromChatbot(question: ChatbotBody): Result<ChatbotReplyMultiTypeResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.getChatbotReplyFor(question)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getAd(): Result<AdResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.getAdvertisement()

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun addNewCoupons(token: String, couponID: Int):
            Result<CouponMultitypeResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.addCoupon(token, couponID)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getAvaliableCoupons(token: String): Result<CouponMultitypeResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.getAvailableCoupons(token)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getUserViewingRecord(token: String): Result<UserRecordsResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService
            .getUserRecord(token)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)

        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getProductDetail(token: String, currency: String, productId: String): Result<ProductDetailResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService
            .getProductDetail(token, currency, productId)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun userSignUp(name: String,  email: String, password: String
    ): Result<UserSignUpResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        //TODO : SIGNIN API : can later change to StylishApiV2 to connect to Stuarrt api
        // Get the Deferred object for Retrofit_V2 request
        val getResultDeferred = StylishApiV2.retrofitService.userSignUp(name, email, password)

        val result = getResultDeferred.await()

        when(result.isSuccessful) {
            true -> {
                result.body()?.let {body ->
                    if (body.error != null) {
                        return Result.Fail(body.error)
                    } else {
                       return Result.Success(body)
                    }
                }
                throw JSONException("Error in JSON Parsing")

            }
            false -> {
                result.body()?.let {
                    return Result.Fail(it.error ?: "ERROR")
                }

                result.errorBody()?.let {

                    val text = it.source().readUtf8Line()!!


                    val json = JSONObject(text)

                    if(json.has("error")) {
                        return Result.Fail(json["error"] as String)
                    } else { }
                }

                return Result.Fail(result.message())
            }
        }
    }

    override suspend fun userSignIn(email: String, password: String): Result<UserSignInResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        //TODO : SIGNIN API : can later change to StylishApiV2 to connect to Stuarrt api
        // Get the Deferred object for Retrofit_V2 request
        val getResultDeferred = StylishApiV2.retrofitService
            .userSignIn(email = email, password = password)

        return try {
            val result = getResultDeferred.await()
            result.error?.let {
                return  Result.Fail(it)
            }
            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun userRefreshToken(token: String): Result<UserRefreshTokenResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.userRefreshToken(token)

        return try {
            val result = getResultDeferred.await()

            result.error?.let {
                return Result.Fail(it)
            }
            Result.Success(result)

        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getProductAll(token: String?, currency: String): Result<List<HomeItem>> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        val getResultDeferred = StylishApiV2.retrofitService.getAllProducts(token, currency)

        return try {
            val listResult = getResultDeferred.await()

            listResult.error?.let {
                return Result.Fail(it)
            }

            Result.Success(listResult.toHomeItems())
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getMarketingHots(): Result<List<HomeItem>> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }
        // Get the Deferred object for our Retrofit request
        val getResultDeferred = StylishApi.retrofitService.getMarketingHots()
        return try {
            // this will run on a thread managed by Retrofit
            val listResult = getResultDeferred.await()

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult.toHomeItems())

        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    /***
     * PRODUCT
     */
    override suspend fun getProductList(token: String, currency: String, type: String, paging: String?, sort: Sort?, order: Order?
    ): Result<ProductListResult> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }
        // Get the Deferred object for our Retrofit request
        val getResultDeferred = StylishApiV2.retrofitService
            .getProductList(token = token, currency = currency,
                type = type, paging = paging,
                sort = sort?.value, order = order?.value)

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = getResultDeferred.await()

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)

        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getUserProfile(token: String): Result<UserProfileResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }
        // Get the Deferred object for our Retrofit request
        val getResultDeferred = StylishApiV2.retrofitService.getUserProfile(token)

        return try {
            // this will run on a thread managed by Retrofit
            val listResult = getResultDeferred.await()

            listResult.error?.let {
                return Result.Fail(it)
            }

            Result.Success(listResult)

        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }
        // Get the Deferred object for our Retrofit request
        val getResultDeferred = StylishApi.retrofitService.userSignIn(fbToken = fbToken)
        return try {
            // this will run on a thread managed by Retrofit
            val listResult = getResultDeferred.await()

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)

        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun checkoutOrder(
        token: String, orderDetail: OrderDetail): Result<CheckoutOrderResult> {

        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }
        // Get the Deferred object for our Retrofit request
        val getPropertiesDeferred = StylishApi.retrofitService.checkoutOrder(token, orderDetail)
        return try {
            // this will run on a thread managed by Retrofit
            val listResult = getPropertiesDeferred.await()

            listResult.error?.let {
                return Result.Fail(it)
            }
            Result.Success(listResult)

        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override fun getUserViewRecords(): LiveData<List<UserRecord>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProductsInCart(): LiveData<List<Product>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insertProductInCart(product: Product) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateProductInCart(product: Product) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun clearProductInCart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserInformation(key: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllChats(): LiveData<List<Chat>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insertChat(chat: Chat) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun clearChats() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllViewRecords() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insert(userRecord: UserRecord) {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
