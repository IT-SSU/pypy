package com.cookandroid.kotlinapp

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.DefaultRetryPolicy
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
        val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
        val url = "http://61.84.24.251:49090/siren/userinfo"
        val params = HashMap<String, String>()
        var email = settings.getString("email", null)
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
                    val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = settings.edit() //데이터를 추가 할때사용
                    editor.putString("userNum",response.getString("userNum"))
                    editor.putString("email",response.getString("email"))
                    editor.putString("name",response.getString("name")) //스프링 코드 변경
                    editor.commit()
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
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        // Add the volley post request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }
}
