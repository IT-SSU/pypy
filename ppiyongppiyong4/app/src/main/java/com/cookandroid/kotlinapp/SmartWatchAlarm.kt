package com.cookandroid.kotlinapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceFragment

@Suppress("DEPRECATION")
class SmartWatchAlarm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_watch_alarm)
        fragmentManager.beginTransaction().replace(android.R.id.content, SmartWatchAlarm.MyPrefFragment()).commit()
    }

    class MyPrefFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // Preference 정보가 있는 XML 파일 지정
            addPreferencesFromResource(R.xml.smartwatch_setting)
        }
    }
}
