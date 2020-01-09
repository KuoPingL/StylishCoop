package app.appworks.school.stylish.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import app.appworks.school.stylish.data.UserRecord


@Dao
interface StylishViewingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: UserRecord)

    @Query("SELECT * FROM product_viewed_by_user")
    fun fetchAllRecords(): LiveData<List<UserRecord>>

    @Query("DELETE FROM product_viewed_by_user ")
    fun delete()

    @Query("SELECT * FROM product_viewed_by_user WHERE product_id = :id ")
    fun fetchRecord(id: Long):List<UserRecord>
}