package com.example.pusl2023_t.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.jar.Attributes

class BKSTextView(context : Context, attributeSet: AttributeSet): AppCompatTextView(context, attributeSet) {

    init {
        applyFont()
    }
    private fun applyFont(){

        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)

    }


}