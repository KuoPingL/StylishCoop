package app.appworks.school.stylish.data

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "product_viewed_by_user", primaryKeys = ["product_id"])
@Parcelize
data class UserRecord(
    @ColumnInfo(name = "product_id")
    val id: Long,
    @ColumnInfo(name = "product_name")
    val title: String? = null,
    @ColumnInfo(name = "product_price")
    val price: Float = 0f,
    @ColumnInfo(name = "product_image")
    @Json(name = "main_image") val image: String? = null
): Parcelable