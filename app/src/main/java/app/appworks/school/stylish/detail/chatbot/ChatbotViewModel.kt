package app.appworks.school.stylish.detail.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.data.*
import app.appworks.school.stylish.data.source.StylishRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

enum class ChatbotQuery(val api: String, val question: Int) {
    SIZE("querySize", 1), EVENTS("queryEvents", 2)
}

class ChatbotViewModel (private val stylishRepository: StylishRepository,
                        private val product: Product): ViewModel() {

    private val tag = "ChatbotViewModel"

    var weight: Double? = null
    var height: Double? = null

    private val chats = stylishRepository.getAllChats()

    val chatItems = Transformations.map(chats) {list ->
        list.map {chat ->
            if (chat.isBot) {
                ChatbotItem.ChatbotDialogItem(
                    ChatbotDialog(
                        message = chat.message,
                        chatButtons = chat.selections,
                        sentTime = chat.sentDate))
            } else {
                ChatbotItem.UserDialogItem(
                    UserDialog(
                        message = chat.message,
                        productId = chat.productId,
                        productTitle = chat.productTitle,
                        productImage = chat.productImage,
                        sentDate = chat.sentDate))
            }
        }
    }

    private val _chatbotDialogItem = MutableLiveData<List<ChatbotItem>>()

    val chatbotDialogItem: LiveData<List<ChatbotItem>>
        get() = _chatbotDialogItem

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    fun fetchDialogFor(message: String) {
        // fetch data
        if (message.trim().isEmpty()) {
            // Display the first
            displayGreedingMessage()
        }
    }

    fun displayGreedingMessage() {
        // Fetch Database
        val chat = Chat(message = "您好，我是您的服務工具人。請問您需要什麼資訊呢？",
            selections =
            listOf(
                ChatbotButton("查詢產品合適大小", ChatbotQuery.SIZE.api, 1),
                ChatbotButton("查詢產品相關活動", ChatbotQuery.EVENTS.api, 2)
            ), sentDate = Date(), isBot = true)
        CoroutineScope(Dispatchers.IO).launch {
            stylishRepository.insertChat(chat)
        }
    }

    fun displayMessage(message: String) {
        val errorMessage = Chat(message, sentDate = Date(), isBot = true)
        CoroutineScope(Dispatchers.IO).launch {
            stylishRepository.insertChat(errorMessage)
            displayGreedingMessage()
        }
    }

    val askForHeightAndWeight = MutableLiveData<Boolean>()

    fun doneSendingHeightAndWeight() {
        askForHeightAndWeight.value = null
    }

    fun sentHeightAndWeight(height: Double, weight: Double) {
        userSelectedAPI(ChatbotQuery.SIZE.api, ChatbotQuery.SIZE.question, height, weight)
    }

    fun userSelectedAPI(api: String, question: Int, height: Double? = 0.0, weight: Double? = 0.0) {

        when(question) {
            1 -> {

                if (askForHeightAndWeight.value == null
                    || askForHeightAndWeight.value == false || height == null || weight == null) {
                    askForHeightAndWeight.value = true
                    return
                }

                doneSendingHeightAndWeight()

                if (height <= 50.0 || weight <= 2.0) {
                    displayMessage("請輸入正確身高和體重")
                    return
                }

                coroutineScope.launch {

                    stylishRepository.insertChat(Chat(message = "請問是否有合適的衣服呢？ \n身高為${height}cm\n體重為${weight}kg", isBot = false, sentDate = Date()))

                    val result = stylishRepository
                        .getReplyFromChatbot(
                            ChatbotBody(
                                question = question,
                                productId = product.id,
                                weight = weight,
                                height = height))

                    when(result) {
                        is Result.Success -> {
                            if (result.data.error != null) {
                                displayMessage(result.data.error)
                            } else {
                                result.data.reply?.let { body ->
                                    val message = body.message
                                    displayMessage("根據您的資料 : \n$message")
                                }
                            }
                        }

                        is Result.Fail -> {
                            displayMessage(result.error)
                        }

                        else -> {
                            displayMessage("請稍候再試試")
                        }
                    }
                }
            }
            2 -> {
                coroutineScope.launch {

                    stylishRepository.insertChat(Chat("我想知道此商品搭配的活動", isBot = false, sentDate = Date()))

                    val result = stylishRepository
                        .getReplyFromChatbot(
                            ChatbotBody(
                                question = question,
                                productId = product.id))
                    when(result) {
                        is Result.Success -> {
                            if (result.data.error != null) {
                                displayMessage(result.data.error)
                            } else {
                                result.data.activities?.let {activities ->
                                    displayMessage("此商品有搭配${activities.joinToString(", ")}喔~")
                                }
                            }
                        }

                        is Result.Fail -> {
                            displayMessage(result.error)
                        }

                        else -> {
                            displayMessage("請稍候再試試")
                        }
                    }
                }
            }
        }

        val list = _chatbotDialogItem.value?.toMutableList()
        list?.add(ChatbotItem.UserDialogItem(UserDialog(message = "Calling API for $api", sentDate = Date())))
        _chatbotDialogItem.value = list
    }

    fun clearData() {
        coroutineScope.launch {
            stylishRepository.clearChats()
        }
    }

}