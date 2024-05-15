package com.acuon.networklibrary.bindingadapter

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.acuon.networklibrary.utils.extensions.dp
import com.acuon.networklibrary.utils.extensions.setImageRoundCornerCenterCrop
import com.acuon.networklibrary.utils.extensions.setupClearButtonWithAction

object BindingAdapters {
    /**
     *binding adapter for ImageView coupled with Glide
     * @param view - ImageView
     * @param imageUrl - this can be a URL, or resource ID, or bitmap
     */
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: Any?) {
        view.setImageRoundCornerCenterCrop(imageUrl)
    }

    /**
     *binding adapter for ImageView coupled with Glide
     * @param view - ImageView
     * @param imageUrl - this can be a URL, or resource ID, or bitmap
     * @param radius - for rounded corners in image
     */
    @JvmStatic
    @BindingAdapter("imageURL", "cornerRadius", requireAll = false)
    fun loadImageWithRoundedCorner(view: ImageView, imageUrl: Any?, radius: Int = 0) {
        view.setImageRoundCornerCenterCrop(imageUrl, radius.dp)
    }

    /**
     *binding adapter for TextView to set other data type to the textview
     * @param view - TextView
     * @param text - this can be of any data type(string, int, long, double)
     */
    @JvmStatic
    @BindingAdapter("text")
    fun setText(view: TextView, text: Any?) {
        view.text = text.toString()
    }

    /**
     *binding adapter for EditText for clearing the edittext of texts
     * @param view - EditText
     * @param boolean - depending on this value, I will show the clear button option in edittext
     */
    @JvmStatic
    @BindingAdapter("clearButton")
    fun showClearButton(view: EditText, boolean: Boolean?) {
        if (boolean == true) {
            view.setupClearButtonWithAction()
        }
    }

}