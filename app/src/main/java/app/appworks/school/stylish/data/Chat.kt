package app.appworks.school.stylish.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import app.appworks.school.stylish.data.source.local.ChatbotConverters
import java.util.*

@Entity(tableName = "chat_dialog_table", primaryKeys = ["sent_date", "is_bot"])
@TypeConverters(ChatbotConverters::class)
data class Chat(
    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "selections")
    val selections: List<ChatbotButton>? = null,

    @ColumnInfo(name = "is_bot")
    val isBot: Boolean,

    @ColumnInfo(name = "product_image")
    val productImage: String? = null,

    @ColumnInfo(name = "product_title")
    val productTitle: String? = null,

    @ColumnInfo(name = "product_id")
    val productId: String? = null,

    @ColumnInfo(name = "sent_date")
    val sentDate: Date
)