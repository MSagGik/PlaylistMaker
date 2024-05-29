package com.msaggik.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import android.view.View

object Utils {
    fun doToPx(dp: Float, context: Context): Int {
        return TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
            .toInt()
    }

    fun visibilityView(views: Array<View>, v: View? = null) {
        for(view in views) view.visibility = View.GONE
        v?.visibility = View.VISIBLE
    }
}