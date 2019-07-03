package com.cookandroid.kotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.pw_change.*

class PW_change : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pw_change)
        btnHeaderSetting.visibility = View.GONE
        txtHeaderTitle.text="비밀번호 변경";
    }
}
