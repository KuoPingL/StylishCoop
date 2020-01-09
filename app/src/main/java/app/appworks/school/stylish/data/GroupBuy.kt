package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupBuy(
    @Json(name = "group_id") val groupID: Int,
    @Json(name = "product_id") val productID: Long,
    @Json(name = "users_id") val users: List<Long>?,
    val status: Int
): Parcelable


@Parcelize
data class GetGroupBuyResult(
    val data: List<GroupBuy>?,
    val error: String?
): Parcelable

@Parcelize
data class AddGroupBuyBody(
    val token: String,
    @Json(name = "productid")val productID: Long,
    val friend1: String?,
    val friend2:String?
): Parcelable

@Parcelize
data class AddGroupBuyResult(
    @Json(name = "data") val groupID: Int?,
    val error: String?
): Parcelable

@Parcelize
data class JoinGroupBuyResult(
    val success: String?,
    val error: String?
): Parcelable