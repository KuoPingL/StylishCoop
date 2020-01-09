package app.appworks.school.stylish.ext

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.TouchDelegate
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.data.OrderProduct
import app.appworks.school.stylish.data.Product

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Provides [List] [Product] to convert to [List] [OrderProduct] format
 */
fun List<Product>?.toOrderProductList(): List<OrderProduct> {
    val orderProducts = mutableListOf<OrderProduct>()
    this?.apply {
        for (product in this) {
            orderProducts.add(
                OrderProduct(
                    product.id,
                    product.title,
                    product.price,
                    product.colors.filter { it.code == product.selectedVariant.colorCode }.first(),
                    product.selectedVariant.size,
                    product.amount ?: 0
                )
            )
        }
    }
    return orderProducts
}

/**
 * Increase touch area of the view/button .
 */
fun View.setTouchDelegate() {
    val parent = this.parent as View  // button: the view you want to enlarge hit area
    parent.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.top -= 100    // increase top hit area
        rect.left -= 100   // increase left hit area
        rect.bottom += 100 // increase bottom hit area
        rect.right += 100  // increase right hit area
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}

fun Number.toDp(): Float {
    return this.toFloat() / (StylishApplication.instance.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Number.toPixel(): Float {
    return this.toFloat() * (StylishApplication.instance.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun ConstraintLayout.chatbotExpand() {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((this.parent as View).width,
        View.MeasureSpec.EXACTLY)

    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

    this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)

    val metrics = context.resources.displayMetrics
    val ratio = metrics.heightPixels.toFloat()/metrics.widthPixels.toFloat()
    val targetHeight = ((this.parent as View).height * 0.8).toInt()
    val targetWidth = (targetHeight / ratio).toInt()

    // older version of android ( < API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE

    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            this@chatbotExpand.layoutParams.height = if((targetHeight * interpolatedTime) < 50.toPixel()) 50.toPixel().toInt() else (targetHeight * interpolatedTime).toInt()
            this@chatbotExpand.layoutParams.width = if ((targetHeight * interpolatedTime) < 50.toPixel()) 50.toPixel().toInt() else (targetWidth * interpolatedTime).toInt()
            this@chatbotExpand.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (targetHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)

}

fun ConstraintLayout.chatbotCollapse() {

    val initialHeight = this.measuredHeight
    val initialWidth = this.measuredWidth

    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {

            var targetHeight = initialHeight - (initialHeight * interpolatedTime).toInt()
            if (targetHeight < 50.toPixel().toInt()) targetHeight = 50.toPixel().toInt()

            var targetWidth = initialWidth - (initialWidth * interpolatedTime).toInt()
            if (targetWidth < 50.toPixel().toInt()) { targetWidth = 50.toPixel().toInt()}

            this@chatbotCollapse.layoutParams.height = targetHeight
            this@chatbotCollapse.layoutParams.width = targetWidth
            this@chatbotCollapse.requestLayout()
            this@chatbotCollapse.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (initialHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)

//    val initialHeight = this.measuredHeight
//
//    val a = object : Animation() {
//        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//            if (interpolatedTime == 1f) {
//                this@collapse.visibility = View.GONE }
//            else {
//                this@collapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
//                this@collapse.requestLayout()
//                }
//            this@collapse.requestLayout()
//        }
//
//        override fun willChangeBounds(): Boolean {
//            return true
//        }
//    }
//
//    a.duration = (initialHeight / this.context.resources.displayMetrics.density).toLong()
//    this.startAnimation(a)
}


class ChatBotLayout: ConstraintLayout(StylishApplication.instance.applicationContext) {}


fun ChatBotLayout.chatbotExpand() {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((this.parent as View).width,
        View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

    this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)

    /**
     * DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    float ratio = ((float)metrics.heightPixels / (float)metrics.widthPixels
    );
     */


    val metrics = context.resources.displayMetrics
    val ratio = metrics.heightPixels.toFloat()/metrics.widthPixels.toFloat()
    val targetHeight = this.height
    val targetWidth = (this.height * ratio).toInt()

    // older version of android ( < API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE

    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            this@chatbotExpand.layoutParams.height = targetHeight
            this@chatbotExpand.layoutParams.width = targetWidth
            this@chatbotExpand.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    a.duration = (targetHeight / this.context.resources.displayMetrics.density).toLong()
    this.startAnimation(a)
}

fun ChatBotLayout.chatbotCollapse() {

}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}