package com.cookandroid.kotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.pw_find.*

class PW_find : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pw_find)
        txtHeaderTitle.text="비밀번호 찾기";
        btnHeaderSetting.visibility = View.GONE
        btnSubmit.setOnClickListener {
            finish()
        }
    }
}
