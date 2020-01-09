package app.appworks.school.stylish.data.source.local

import androidx.room.TypeConverter
import app.appworks.school.stylish.data.ChatbotButton
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.text.SimpleDateFormat
import java.util.*

class ChatbotConverters {
    /**
     * convert [List] [Button] to JSON
     */
    @TypeConverter
    fun convertChatbotButtonsToJson(list: List<ChatbotButton>?): String? {
        list?.let {
            return Moshi.Builder().build().adapter<List<ChatbotButton>>(List::class.java).toJson(list)
        }
        return null
    }

    @TypeConverter
    fun convertJsonToChatbotButtons(json: String?): List<ChatbotButton>? {
        json?.let {
            val type = Types.newParameterizedType(List::class.java, ChatbotButton::class.java)
            val adapter: JsonAdapter<List<ChatbotButton>> = Moshi.Builder().build().adapter(type)
            return adapter.fromJson(it)
        }
        return null
    }

    @TypeConverter
    fun convertDateFromMillisecond(value: Long?): Date? {
        if (value == null) {
            return null
        }

        val cal = Calendar.getInstance()
        cal.timeInMillis = value

        return cal.time
    }

    @TypeConverter
    fun convertDateToMillisecond(date: Date?): Long? {

        if (date == null) {return Date().time}

        return date.time
    }
}