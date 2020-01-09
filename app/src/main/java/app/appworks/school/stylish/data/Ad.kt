package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ad(
    val displayTime: Int?,
    @Json(name = "ad_image") val images: List<String>?
): Parcelable