package com.cookandroid.kotlinapp

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class LockScreenService : Service() {
    //화면이 꺼질때 브로드캐스트 메세지를 수신하는 리서버
    var receiver: ScreenOffReceiver? =null

    private val ANDROID_CHANNEL_ID="com.cookandroid.kotlinapp"
    private val NOTIFICATION_ID=999

    override fun onCreate() {
        super.onCreate()
        if (receiver==null){
            receiver=ScreenOffReceiver()
            val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
            registerReceiver(receiver,filter)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent!=null){
            if (intent.action==null){
                //서비스가 최초실행이 아닌경우 oncreate가 불리지 않을 수 있음.
                //이 경우 receiver가 널이면 새로 생성하고 등록한다.
                if (receiver==null){
                    receiver= ScreenOffReceiver()
                    val filter=IntentFilter(Intent.ACTION_SCREEN_OFF)
                    registerReceiver(receiver,filter)
                }
            }
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val builder=Notification.Builder(this,ANDROID_CHANNEL_ID).setContentTitle(getString(R.string.app_name)).setContentText("스마트 트레커 러닝~")
            val notification=builder.build()

            startForeground(NOTIFICATION_ID,notification)
        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiver!=null)
            unregisterReceiver(receiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
