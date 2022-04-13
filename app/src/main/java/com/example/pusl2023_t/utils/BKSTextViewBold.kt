package com.example.pusl2023_t.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * This class will be used for Custom bold text using the TextView which inherits the AppCompactTextView class.
 */
class BKSTextViewBold(context : Context, attributeSet: AttributeSet): AppCompatTextView(context, attributeSet) {

    init {
        applyFont()
    }
    private fun applyFont(){

        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)

    }


}