package app.appworks.school.stylish.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.appworks.school.stylish.data.Chat

@Dao
interface ChatbotDatabaseDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(chat: Chat)

    @Query("SELECT * FROM chat_dialog_table")
    fun fetchAllChats(): LiveData<List<Chat>>

    @Query("DELETE FROM chat_dialog_table")
    fun clearTable()
}