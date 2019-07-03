package com.cookandroid.kotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.member_modify.*

class MemberModify : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_modify)
        txtHeaderTitle.text="기본정보 수정";
        btnHeaderSetting.visibility = View.GONE

        btnSubmit.setOnClickListener {
            startActivity(Intent(this,Setting::class.java))
        }
    }
}
