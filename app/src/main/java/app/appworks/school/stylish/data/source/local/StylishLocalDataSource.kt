package app.appworks.school.stylish.data.source.local

import android.content.Context
import androidx.lifecycle.LiveData
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.data.source.StylishDataSource
import app.appworks.school.stylish.network.Order
import app.appworks.school.stylish.network.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Concrete implementation of a Stylish source as a db.
 */
class StylishLocalDataSource(val context: Context) : StylishDataSource {

    override suspend fun createGroupBuy(addGroupBuyBody: AddGroupBuyBody): Result<AddGroupBuyResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getGroupBuys(token: String): Result<GetGroupBuyResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateGroupBuy(
        token: String,
        productID: Long
    ): Result<JoinGroupBuyResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getReplyFromChatbot(question: ChatbotBody): Result<ChatbotReplyMultiTypeResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAd(): Result<AdResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProductAll(token: String?, currency: String): Result<List<HomeItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun addNewCoupons(token: String, couponID: Int):
            Result<CouponMultitypeResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAvaliableCoupons(token: String): Result<CouponMultitypeResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProductDetail(token: String, currency: String, productId: String
    ): Result<ProductDetailResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getProductList(token: String, currency: String,
                                        type: String, paging: String?,
                                        sort: Sort?, order: Order?): Result<ProductListResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserViewingRecord(token: String): Result<UserRecordsResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        StylishViewingRecordDatabase.getInstance(context).viewRecordDatabaseDao.fetchAllRecords()
    }

    override suspend fun userSignUp(name: String, email: String, password: String): Result<UserSignUpResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun userRefreshToken(token: String): Result<UserRefreshTokenResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun userSignIn(email: String, password: String): Result<UserSignInResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getMarketingHots(): Result<List<HomeItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getUserProfile(token: String): Result<UserProfileResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun userSignIn(fbToken: String): Result<UserSignInResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun checkoutOrder(
        token: String, orderDetail: OrderDetail): Result<CheckoutOrderResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProductsInCart(): LiveData<List<Product>> {
        return StylishDatabase.getInstance(context).stylishDatabaseDao.getAllProducts()
    }

    override suspend fun isProductInCart(id: Long, colorCode: String, size: String): Boolean {
        return withContext(Dispatchers.IO) {
            StylishDatabase.getInstance(context).stylishDatabaseDao.get(id, colorCode, size) != null
        }
    }

    override suspend fun insertProductInCart(product: Product) {
        withContext(Dispatchers.IO) {
            StylishDatabase.getInstance(context).stylishDatabaseDao.insert(product)
        }
    }

    override suspend fun updateProductInCart(product: Product) {
        withContext(Dispatchers.IO) {
            StylishDatabase.getInstance(context).stylishDatabaseDao.update(product)
        }
    }

    override suspend fun removeProductInCart(id: Long, colorCode: String, size: String) {
        withContext(Dispatchers.IO) {
            StylishDatabase.getInstance(context).stylishDatabaseDao.delete(id, colorCode, size)
        }
    }

    override suspend fun clearProductInCart() {
        withContext(Dispatchers.IO) {
            StylishDatabase.getInstance(context).stylishDatabaseDao.clear()
        }
    }


    /**
     * CHATBOT
     */
    override fun getAllChats(): LiveData<List<Chat>> {
        return ChatbotDatabase.getInstance(context).chatbotDatabaseDao.fetchAllChats()
    }

    override suspend fun insertChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            ChatbotDatabase.getInstance(context).chatbotDatabaseDao.insert(chat)
        }
    }

    override suspend fun clearChats() {
        withContext(Dispatchers.IO) {
            ChatbotDatabase.getInstance(context).chatbotDatabaseDao.clearTable()
        }
    }

    override fun getUserViewRecords(): LiveData<List<UserRecord>> {
        return StylishViewingRecordDatabase.getInstance(context).viewRecordDatabaseDao.fetchAllRecords()
    }

    override suspend fun deleteAllViewRecords() {
        withContext(Dispatchers.IO) {
            StylishViewingRecordDatabase.getInstance(context).viewRecordDatabaseDao.delete()
        }
    }

    override suspend fun insert(userRecord: UserRecord) {
        withContext(Dispatchers.IO) {
            StylishViewingRecordDatabase.getInstance(context).viewRecordDatabaseDao.insert(userRecord)
        }
    }

    override suspend fun getUserInformation(key: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
