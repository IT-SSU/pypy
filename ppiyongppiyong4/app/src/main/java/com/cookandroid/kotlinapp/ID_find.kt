package com.cookandroid.kotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.id_find.*

class ID_find : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.id_find)

        txtHeaderTitle.text="아이디/비밀번호 찾기";
        btnHeaderBack.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }

        btnPwFind.setOnClickListener {
            startActivity(Intent(this,PW_find::class.java))
        }

    }
}

