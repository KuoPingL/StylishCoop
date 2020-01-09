package app.appworks.school.stylish.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.util.Util.getString
import java.util.*

/**
 * Created by Wayne Chen in Jul. 2019.
 */


object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"
    private const val USER_CURRENCY = "user_currency"
    private const val USER_LAST_VISIT_TIME = "user_last_visit_time"

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

//    var lastLoginDate: Long? = null
//        get() = StylishApplication.instance
//            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
//            .getLong(USER_LAST_VISIT_TIME)
//        set(value) {
//            field = when (value) {
//                null -> {
//
//                }
//
//                else -> {
//
//                }
//            }
//        }


    var userToken: String? = null
        get() = StylishApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    StylishApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    StylishApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }

    var userCurrency: String = "TWD"
        get() = StylishApplication.instance.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_CURRENCY, "TWD") ?: "TWD"
        set(value) {
            field = when (value) {
                null -> {
                    StylishApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
                        .edit()
                        .remove(USER_CURRENCY)
                        .apply()
                    "TWD"
                }
                else -> {
                    StylishApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
                        .edit()
                        .putString(USER_CURRENCY, value)
                        .apply()
                    value
                }
            }
        }

    /**
     * It can be use to check login status directly
     */
    val isLoggedIn: Boolean
        get() = userToken != null

    /**
     * Clear the [userToken] and the [user]/[_user] data
     */
    fun clear() {
        userToken = null
        _user.value = null
    }

    private var lastChallengeTime: Long = 0
    private var challengeCount: Int = 0
    private const val CHALLENGE_LIMIT = 3

    /**
     * Winter is coming
     */
    fun challenge() {
        if (System.currentTimeMillis() - lastChallengeTime > 5000) {
            lastChallengeTime = System.currentTimeMillis()
            challengeCount = 0
        } else {
            if (challengeCount == CHALLENGE_LIMIT) {
                userToken = null
                Toast.makeText(StylishApplication.instance,
                    getString(R.string.profile_mystic_information),
                    Toast.LENGTH_SHORT).show()
            } else {
                challengeCount++
            }
        }
    }
}

