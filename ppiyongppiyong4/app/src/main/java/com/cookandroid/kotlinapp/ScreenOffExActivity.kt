package com.cookandroid.kotlinapp

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ScreenOffExActivity : AppCompatActivity() {
    //ScreenOffReceiver 객체
    var screenOffReceiver: ScreenOffReceiver? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lock_screen)

        if (screenOffReceiver==null){
            screenOffReceiver=ScreenOffReceiver()
            val intentFilter=IntentFilter(Intent.ACTION_SCREEN_OFF)
            registerReceiver(screenOffReceiver,intentFilter)
        }
    }
}