package app.appworks.school.stylish.data

import java.util.*


data class ChatbotDialog(
    val message: String,
    val chatButtons: List<ChatbotButton>?,
    val sentTime: Date
    )