package com.cookandroid.kotlinapp
import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.View

@SuppressLint("Registered")
open class Common: AppCompatActivity() {
    fun back(@Suppress("UNUSED_PARAMETER")v: View) {
        finish()
    }
    fun goSetting(@Suppress("UNUSED_PARAMETER")v: View) {

        if(intent.hasExtra("email")){
            intent.putExtra("email", intent.getStringArrayExtra("email"))
        }
        startActivity(Intent(this,Setting::class.java))

    }
}