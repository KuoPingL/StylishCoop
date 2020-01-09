package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdResult(
    val error: String?,
    @Json(name = "data") val ad: Ad?
): Parcelable