@file:Suppress("DEPRECATION")

package com.cookandroid.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.Preference


class Setting : Common() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        fragmentManager.beginTransaction().replace(android.R.id.content, Setting.MyPrefFragment()).commit()
    }
    class MyPrefFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.setting)

            val logout = findPreference("keyLogout") as Preference
            logout.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intent = Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.cookandroid.kotlinapp","com.cookandroid.kotlinapp.Login");
                activity?.finish()
                false
            }
        }
    }
}
