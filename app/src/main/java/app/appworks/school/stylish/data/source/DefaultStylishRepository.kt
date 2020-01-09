package app.appworks.school.stylish.data.source

import androidx.lifecycle.LiveData
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.network.Order
import app.appworks.school.stylish.network.Sort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation to load Stylish sources.
 */
class DefaultStylishRepository(private val stylishRemoteDataSource: StylishDataSource,
                               private val stylishLocalDataSource: StylishDataSource,
                               private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StylishRepository {

    override suspend fun getGroupBuys(token: String): Result<GetGroupBuyResult> {
        return stylishRemoteDataSource.getGroupBuys(token)
    }

    override suspend fun createGroupBuy(addGroupBuyBody: AddGroupBuyBody): Result<AddGroupBuyResult> {
        return stylishRemoteDataSource.createGroupBuy(addGroupBuyBody)
    }

    override suspend fun updateGroupBuy(token: String,
                                        productID: Long): Result<JoinGroupBuyResult> {
        return stylishRemoteDataSource.updateGroupBuy(token, productID)
    }

    override suspend fun getReplyFromChatbot(question: ChatbotBody): Result<ChatbotReplyMultiTypeResult> {
        return stylishRemoteDataSource.getReplyFromChatbot(question)
    }

    override fun getUserViewRecords(): LiveData<List<UserRecord>> {
        return stylishLocalDataSource.getUserViewRecords()
    }

    override suspend fun addNewCoupons(token: String, couponID: Int):
            Result<CouponMultitypeResult> {
        return stylishRemoteDataSource.addNewCoupons(token, couponID)
    }

    override suspend fun getAvaliableCoupons(token: String): Result<CouponMultitypeResult> {
        return stylishRemoteDataSource.getAvaliableCoupons(token)
    }

    override suspend fun getUserViewingRecord(token: String): Result<UserRecordsResult> {
        return stylishRemoteDataSource.getUserViewingRecord(token)
    }

    override suspend fun getProductDetail( token: String, currency: String, productId: String):
            Result<ProductDetailResult> {
        return stylishRemoteDataSource.getProductDetail(token, currency, productId)
    }

    override suspend fun userSignUp(name: String, email: String, password: String
    ): Result<UserSignUpResult> {
        return stylishRemoteDataSource.userSignUp(name, email, password)
    }

    override suspend fun userSignIn(email: String, password: String): Result<UserSignInResult> {
        return stylishRemoteDataSource.userSignIn(email, password)
    }

    override suspend fun userRefreshToken(token: String): Result<UserSignInResult> {
        return stylishRemoteDataSource.userSignIn(token)
    }

    override suspend fun getProductAll(token: String?, currency: String): Result<List<HomeItem>> {
        return stylishRemoteDataSource.getProductAll(token, currency)
    }

    override suspend fun deleteAllViewRecords() {
        return stylishLocalDataSource.deleteAllViewRecords()
    }

    override suspend fun getMarketingHots(): Result<List<HomeItem>> {
        return stylishRemoteDataSource.getMarketingHots()
    }

    override suspend fun getProductList(token: String, currency: String,
                                        type: String, paging: String?,
                                        sort: Sort?, order: Order?): Result<ProductListResult> {
        return stylishRemoteDataSource.getProductList(token, currency, type, paging, sort, order)
    }

    override suspend fun getUserProfile(token: String): Result<UserProfileResult> {
        return stylishRemoteDataSource.getUserProfile(token)
    }

    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {
        return stylishRemoteDataSource.userSignIn(fbToken)
    }

    override suspend fun checkoutOrder(
        token: String, orderDetail: OrderDetail): Result<CheckoutOrderResult> {
        return stylishRemoteDataSource.checkoutOrder(token, orderDetail)
    }

    override fun getProductsInCart(): LiveData<List<Product>> {
        return stylishLocalDataSource.getProductsInCart()
    }

    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
        return stylishLocalDataSource.isProductInCart(id, colorCode, size)
    }

    override suspend fun insertProductInCart(product: Product) {
        stylishLocalDataSource.insertProductInCart(product)
    }

    override suspend fun updateProductInCart(product: Product) {
        stylishLocalDataSource.updateProductInCart(product)
    }

    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
        stylishLocalDataSource.removeProductInCart(id, colorCode, size)
    }

    override suspend fun clearProductInCart() {
        stylishLocalDataSource.clearProductInCart()
    }

    /**
     * CHATBOT
     */
    override fun getAllChats(): LiveData<List<Chat>> {
        return stylishLocalDataSource.getAllChats()
    }

    override suspend fun clearChats() {
        stylishLocalDataSource.clearChats()
    }

    override suspend fun insertChat(chat: Chat) {
        stylishLocalDataSource.insertChat(chat)
    }


    override suspend fun getAd(): Result<AdResult> {
        return stylishRemoteDataSource.getAd()
    }

    override suspend fun insert(userRecord: UserRecord) {
        stylishLocalDataSource.insert(userRecord)
    }


    override suspend fun getUserInformation(key: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
