package com.example.pusl2023_t

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_splash.*
//import kotlinx
//



class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //remove status bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                // Launch the Main Activity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            },700
        )

        val typeface: Typeface = Typeface.createFromAsset(assets,"Montserrat-Bold.ttf")
        //val tv_app_name: TextView = findViewById<TextView>(R.id.tv_app_name)
        tv_app_name.typeface= typeface




    }
}