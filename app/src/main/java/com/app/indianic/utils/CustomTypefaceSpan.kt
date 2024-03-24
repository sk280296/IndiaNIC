package com.app.indianic.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan
import androidx.core.content.res.ResourcesCompat
import com.app.indianic.R
import com.app.indianic.enums.TypefaceWeight

class CustomTypefaceSpan(context: Context, type: TypefaceWeight) : TypefaceSpan("") {
    private var newType: Typeface? = null
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    companion object {
        var EXTRA_BOLD = TypefaceWeight.EXTRA_BOLD
        var BOLD = TypefaceWeight.BOLD
        var SEMI_BOLD = TypefaceWeight.SEMI_BOLD
        var MEDIUM = TypefaceWeight.MEDIUM
        var REGULAR = TypefaceWeight.REGULAR
        private fun applyCustomTypeFace(paint: Paint, tf: Typeface?) {
            val oldStyle: Int
            val old = paint.typeface
            oldStyle = old?.style ?: 0
            val fake = oldStyle and tf!!.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }
            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }
            paint.typeface = tf
        }
    }

    init {
        newType =
            if (EXTRA_BOLD == type)
                ResourcesCompat.getFont(context, R.font.general_sans_semibold)
            else if (BOLD == type) {
                ResourcesCompat.getFont(context, R.font.general_sans_semibold)
            } else if (SEMI_BOLD == type) {
                ResourcesCompat.getFont(context, R.font.general_sans_semibold)
            } else if (MEDIUM == type) {
                ResourcesCompat.getFont(context, R.font.general_sans_medium)
            } else {
                ResourcesCompat.getFont(context, R.font.general_sans_regular)
            }
    }
}