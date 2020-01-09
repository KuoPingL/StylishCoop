package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatbotReplyMultiTypeResult(
    val error: String? = null,
    val reply: ChatbotReply? = null,
    val activities: List<String>? = null
): Parcelable


@SuppressWarnings("UNCHECKEDCAST")
class ChatbotReplyJSONAdpater: JsonAdapter<ChatbotReplyMultiTypeResult>() {
    @FromJson
    override fun fromJson(reader: JsonReader): ChatbotReplyMultiTypeResult? {
        val jsonVal = reader.readJsonValue() as Map<String, Any>

        return if (jsonVal.containsKey("data")) {
            // Success
            val value = jsonVal.getValue("data")
            val isArray = value as? List<String>

            if (isArray == null) {
                // It must be a Dictionary
                (value as? Map<String, Any>)?.let {reply ->
                    ChatbotReplyMultiTypeResult(reply =
                    ChatbotReply(
                        height = reply.getValue("height") as Double,
                        weight = reply.getValue("weight") as Double,
                        bmi = reply.getValue("bmi") as Double,
                        message = reply.getValue("suggest") as String)
                    )
                }

            } else {
                ChatbotReplyMultiTypeResult(activities = isArray)
            }

        } else {
            // Error
            ChatbotReplyMultiTypeResult(error = jsonVal.getValue("error") as String)
        }
    }

    override fun toJson(writer: JsonWriter, value: ChatbotReplyMultiTypeResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}