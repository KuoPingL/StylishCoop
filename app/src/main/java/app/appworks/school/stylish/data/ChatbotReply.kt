package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatbotReply(
    val height: Double,
    val weight: Double,
    val bmi: Double,
    @Json(name = "suggest") val message: String
): Parcelable