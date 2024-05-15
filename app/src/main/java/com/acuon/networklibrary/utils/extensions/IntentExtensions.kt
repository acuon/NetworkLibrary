package com.acuon.networklibrary.utils.extensions

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? {
    return try {
        when {
            Build.VERSION.SDK_INT >= 33 -> BundleCompat.getParcelable(
                this.extras!!,
                key,
                T::class.java
            )

//            Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
        }
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? {
    return try {
        when {
            Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelable(key) as? T
        }
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? {
    return try {
        when {
            Build.VERSION.SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
        }
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? {
    return try {
        when {
            Build.VERSION.SDK_INT >= 33 -> BundleCompat.getParcelableArrayList(
                this.extras!!,
                key,
                T::class.java
            )

//            Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
        }
    } catch (e: Exception) {
        null
    }
}
