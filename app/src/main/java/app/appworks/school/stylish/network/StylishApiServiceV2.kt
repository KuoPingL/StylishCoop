package app.appworks.school.stylish.network

import app.appworks.school.stylish.BuildConfig
import app.appworks.school.stylish.data.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by Wayne Chen in Jul. 2019.
 */
private const val HOST_NAME = "stuarrrt.com"
private const val API_VERSION = "1.0"
private const val BASE_URL = "https://$HOST_NAME/api/$API_VERSION/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(ChatbotReplyJSONAdpater())
    .add(MultitypeJSONAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = when (BuildConfig.LOGGER_VISIABLE) {
            true -> HttpLoggingInterceptor.Level.BODY
            false -> HttpLoggingInterceptor.Level.NONE
        }
    })
    .build()

/**
 * API
 */

private class MyOkHTTPClientBuilder {
    companion object {
        fun unSafeOkHttpClient() :OkHttpClient.Builder {

            val okHttpClient = OkHttpClient.Builder()
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                })

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                    okHttpClient.sslSocketFactory(
                        sslSocketFactory,
                        trustAllCerts.first() as X509TrustManager
                    )
                    okHttpClient.hostnameVerifier(HostnameVerifier { hostname, session ->
                        hostname == HOST_NAME
                    })
                }

                return okHttpClient
            } catch (e: Exception) {
                return okHttpClient
            }
        }
    }
}

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(client)
    .build()


enum class Sort(val value: String) {
    PRICE("price"),
    POPULARITY("popularity")
}

enum class Order(val value: String) {
    DESCEND("desc"),
    ASCEND("asc")
}

/**
 * A public interface that exposes the [getMarketingHots], [getProductList], [getUserProfile],
 * [userSignIn], [checkoutOrder] methods
 */
interface StylishApiServiceV2 {
    /**
     * Returns a Coroutine [Deferred] [MarketingHotsResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "marketing/hots" endpoint will be requested with the GET HTTP method
     */
    @GET("marketing/hots")
    fun getMarketingHots():
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<MarketingHotsResult>

    @GET("products/all")
    fun getAllProducts(@Header("token") token: String?, @Header("currency") currency: String): Deferred<AllProductResult>

    /**
     * Returns a Coroutine [Deferred] [ProductListResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "products/{catalogType}" endpoint will be requested with the GET
     * HTTP method (catalogType: men, women, accessories)
     * The @Query annotation indicates that it will be added "?paging={pagingKey}" after endpoint
     */
    @GET("products/{catalogType}")
    fun getProductList(@Header("token") token: String, @Header("currency") currency: String,
                       @Path("catalogType") type: String, @Query("paging") paging: String? = null,
                       @Query("sort") sort: String? = null, @Query("order") order: String? = null):
            Deferred<ProductListResult>
    /**
     * Returns a Coroutine [Deferred] [UserProfileResult] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "user/profile" endpoint will be requested with the GET HTTP method
     * The @Header annotation indicates that it will be added "Authorization" header
     */
    @GET("user/profile")
    fun getUserProfile(@Header("token") token: String):
            Deferred<UserProfileResult>
    /**
     * Returns a Coroutine [Deferred] [UserSignInResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "user/signin" endpoint will be requested with the POST HTTP method
     * The @Field annotation indicates that it will be added "provider", "access_token" key-pairs to the body of
     * the POST HTTP method, and it have to use @FormUrlEncoded to support @Field
     */
    @FormUrlEncoded
    @POST("user/signin")
    fun userSignIn(
        @Field("provider") provider: String = "native",
        @Field("email") email: String,
        @Field("password") password: String):
            Deferred<UserSignInResult>

    /**
     * Returns a Coroutine [Deferred] [UserSignUpResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "user/signin" endpoint will be requested with the POST HTTP method
     * The @Field annotation indicates that it will be added "provider", "access_token" key-pairs to the body of
     * the POST HTTP method, and it have to use @FormUrlEncoded to support @Field
     */
    @FormUrlEncoded
    @POST("user/signup")
    fun userSignUp(
        @Field("name") name: String = "",
        @Field("email") email: String,
        @Field("password") password: String):
            Deferred<Response<UserSignUpResult>>

    /**
     * Returns a Coroutine [Deferred] [UserRefreshTokenResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "user/signin" endpoint will be requested with the POST HTTP method
     * The @Field annotation indicates that it will be added "provider", "email", "password" key-pairs to the
     * body of the POST HTTP method, and it have to use @FormUrlEncoded to support @Field
     */
    @FormUrlEncoded
    @POST("user/refresh")
    fun userRefreshToken(
        @Field("token") token: String
    ):
            Deferred<UserRefreshTokenResult>

    /**
     * Returns a Coroutine [Deferred] [CheckoutOrderResult] which can be fetched with await() if in a Coroutine scope.
     * The @POST annotation indicates that the "order/checkout" endpoint will be requested with the POST HTTP method
     * The @Header annotation indicates that it will be added "Authorization" header
     * The @Body annotation indicates that it will be added [OrderDetail] to the body of the POST HTTP method
     */
    @POST("order/checkout")
    fun checkoutOrder(@Header("Authorization") token: String, @Body orderDetail: OrderDetail):
            Deferred<CheckoutOrderResult>

    /**
     * Returns a Coroutine [Deferred] [ProductDetailResult] which can be fetched with await() if in a Coroutine scops.
     * The @GET annoation indicates that the "products/details" endpoint will be requested with the GET HTTP method
     * The @Header annotation indicates that it will be added "Cookie" header with values token={user token}
     * The @Body annotation indicates that an id [String] to the body of the GET HTTP METHOD
     */
    @GET("products/details")
    fun getProductDetail(@Header("token") token: String, @Header("currency") currency: String, @Query("id") id: String):
            Deferred<ProductDetailResult>

    @GET("userrecord/products")
    fun getUserRecord(@Header("token") token: String): Deferred<UserRecordsResult>

    @GET("coupon")
    fun getAvailableCoupons(@Header("token") token: String): Deferred<CouponMultitypeResult>

    @PUT("admin/coupon")
    fun addCoupon(@Header("token") token: String, @Header("coupon_id") couponID: Int): Deferred<CouponMultitypeResult>


    /***
     * AD
     */
    @GET("ad")
    fun getAdvertisement(): Deferred<AdResult>

    /**
     * CHATBOT
     */
    @Headers("Content-Type: application/json")
    @POST("chatbot")
    fun getChatbotReplyFor(@Body questionBody: ChatbotBody): Deferred<ChatbotReplyMultiTypeResult>


    /**
     * GROUPON
     */
    @GET("group")
    fun fetchMyGroupBuy(@Header("token") token: String): Deferred<GetGroupBuyResult>

    @Headers("Content-Type: application/json")
    @POST("group")
    fun createGroupBuy(@Body groupBuyBody: AddGroupBuyBody): Deferred<AddGroupBuyResult>

    @PUT("group")
    fun updateGroupBuy(@Header("token") token: String, @Header("productid") productID: Long): Deferred<JoinGroupBuyResult>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object StylishApiV2 {
    val retrofitService by lazy { retrofit.create(StylishApiServiceV2::class.java) }
}