package app.appworks.school.stylish.data

import java.util.*


data class UserDialog(
    val message: String,
    val productImage: String? = null,
    val productTitle: String? = null,
    val productId: String? = null,
    val sentDate: Date
)