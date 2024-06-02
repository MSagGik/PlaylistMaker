package com.msaggik.playlistmaker.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import java.text.SimpleDateFormat
import java.util.Locale

internal object Utils {
    fun doToPx(dp: Float, context: Context): Int {
        return TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
            .toInt()
    }

    fun visibilityView(views: Array<View>, v: View? = null) {
        for(view in views) view.visibility = View.GONE
        v?.visibility = View.VISIBLE
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormatStandardToYear(dateFormatStandard: String) : String? {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateFormatStandard)
            ?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) }
    }

    fun dateFormatMillisToMinSecShort(dateFormatMillis: Long) : String? {
        return SimpleDateFormat("m:ss", Locale.getDefault()).format(dateFormatMillis)
    }

    fun dateFormatMillisToMinSecFull(dateFormatMillis: Long) : String? {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(dateFormatMillis)
    }
}