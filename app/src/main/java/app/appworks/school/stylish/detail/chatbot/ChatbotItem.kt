package app.appworks.school.stylish.detail.chatbot

import app.appworks.school.stylish.data.ChatbotDialog
import app.appworks.school.stylish.data.UserDialog


sealed class ChatbotItem {
    data class UserDialogItem(val userDialog: UserDialog): ChatbotItem() {
        override val id: String = "user"
    }

    data class ChatbotDialogItem(val chatbotDialogItem: ChatbotDialog): ChatbotItem() {
        override val id: String
            get() = "chatbot"
    }

    abstract val id: String
}