package app.appworks.school.stylish.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatbotBody(
    val question: Int,
    val productId: Long,
    val height: Double? = null,
    val weight: Double? = null
): Parcelable