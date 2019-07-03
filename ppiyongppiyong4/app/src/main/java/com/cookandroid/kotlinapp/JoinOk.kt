package com.cookandroid.kotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.join_ok.*

class JoinOk : Common() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_ok)
        txtHeaderTitle.text="회원가입 완료";
        btnHome.setOnClickListener {
            startActivity(Intent(this,Main::class.java))
        }
        btnHeaderBack.setOnClickListener {
            startActivity(Intent(this,Main::class.java))
        }
    }
}
