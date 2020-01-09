package app.appworks.school.stylish.data

import android.os.Parcelable
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.json.JSONException

@Parcelize
data class CouponMultitypeResult(val coupons: List<Coupon>? = null,
                                 val message: String? = null,
                                 val error: String? = null): Parcelable

class MultitypeJSONAdapter: JsonAdapter<CouponMultitypeResult>() {
    @FromJson override fun fromJson(reader: JsonReader): CouponMultitypeResult {
        val jsonValue = reader.readJsonValue() as Map<String, Any?>
        if (jsonValue.containsKey("data")) {
            val field = jsonValue["data"]
            if (field is String) {return CouponMultitypeResult(message = field)}
            else {
                (field as? List<Coupon>)?.let {
                    return CouponMultitypeResult(coupons = it)
                }
            }
            throw JsonDataException("EXPECTING LIST OF COUPON or STRING")

        } else {
            val error = jsonValue["error"] as String
            return CouponMultitypeResult(error = error)
        }
    }

    override fun toJson(writer: JsonWriter, value: CouponMultitypeResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}