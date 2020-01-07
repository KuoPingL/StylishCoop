package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coupon (
    @Json(name = "couponid") val couponID: Int? = null,
    val ticket: String? = null,
    @Json(name = "user_id") val userID: Int? = null,
    val discount: Int? = null,
    @Json(name = "use_limit") val minimumTotalPurchase: Int? = null,
    val status: String? = null,
    val success: String? = null
): Parcelable