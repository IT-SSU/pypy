package com.cookandroid.kotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.join_ok.*
import kotlinx.android.synthetic.main.main.*
import org.json.JSONObject
import java.util.HashMap

class JoinOk : Common() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_ok)
        txtHeaderTitle.text="회원가입 완료";
        val url = "http://61.84.24.251:49090/siren/userinfo"
        val params = HashMap<String, String>()
        var email = intent.getStringExtra("email")
        params["email"] = email
        val jsonObject = JSONObject(params)

        //세부정보
        btnProfile.setOnClickListener {
            startActivity(Intent(this,DetailModify::class.java))
        }
        btnHome.setOnClickListener {
            startActivity(Intent(this,Main::class.java))
        }
        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    println(" Response: $response")
                    txtJoinWelcome.setText(response.getString("name")+"님, \n회원가입을 환영합니다")

                }catch (e:Exception){
                    println(" Exception: $e")
                    //txtPw.text = "Exception: $e"
                }

            }, Response.ErrorListener{
                // Error in request
                println(" Volley error: $it")
                //txtId.text = "Volley error: $it"
            }
        )
    }
}
