@file:Suppress("DEPRECATION")

package com.cookandroid.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.id_find.*
import kotlinx.android.synthetic.main.setting.*
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.view.View
import kotlinx.android.synthetic.main.header.*


class Setting : Common() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        fragmentManager.beginTransaction().replace(android.R.id.content, MyPrefFragment()).commit()


    }

    class MyPrefFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.setting)

            val logout = findPreference("keyLogout") as Preference
            logout.onPreferenceClickListener = OnPreferenceClickListener {
                //open browser or intent here
                true
            }


        }
    }
}
