package com.app.indianic.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.location.Address
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.Patterns
import android.util.Size
import android.view.inputmethod.InputMethodManager
import com.app.indianic.BuildConfig
import com.app.indianic.R
import com.app.indianic.enums.TypefaceWeight
import java.io.*
import java.lang.ref.WeakReference
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


class AppUtil {

    companion object {

        const val dateFormat1 = "MM"
        const val dateFormat2 = "MMM"
        const val dateFormat3 = "dd MMM yyyy"
        const val dateFormat4 = "yyyy-MM-dd"

        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

        fun getDateFormatted(
            dateString: String,
            givenDateFormat: String,
            requiredDateFormat: String,
        ): String {
            var spf = SimpleDateFormat(givenDateFormat)
            val newDate = spf.parse(dateString)
            spf = SimpleDateFormat(requiredDateFormat)
            return spf.format(newDate)
        }



        fun logError(title: String, description: String) {
            if (BuildConfig.DEBUG) {
                Log.e(title, description)
            }
        }

        fun progressBarLoader(context: Context, isCancel: Boolean = false): Dialog {
            val weakref = WeakReference(context)
            val dialog = Dialog(weakref.get()!!, R.style.MaterialDialog)
            dialog.setContentView(R.layout.layout_progressbar)
            dialog.setCancelable(isCancel)
            dialog.setCanceledOnTouchOutside(isCancel)
            return dialog
        }



    }


}