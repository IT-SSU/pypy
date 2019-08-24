package com.cookandroid.kotlinapp

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.identification.*
import kotlinx.android.synthetic.main.identification.txtPw
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.setting.*
import org.json.JSONObject

class Identification : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.identification)
        txtHeaderTitle.text="본인확인";
        btnHeaderSetting.visibility = View.GONE

        btnSubmit.setOnClickListener {
            val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
            val url = "http://61.84.24.251:49090/siren/email_login"
            val params = HashMap<String, String>()
            params["email"] =settings.getString("email",null)
            params["password"] = txtPw.text.toString()

            val jsonObject = JSONObject(params)
            // Volley post request with parameters
            val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                Response.Listener { response ->
                    // Process the json
                    try {
                        println(" Response: $response")
                        if (response.getString("result").equals("T")){

                            startActivity(Intent(this,MemberModify::class.java))
                        }else
                            Toast.makeText(this, "비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                    }catch (e:Exception){
                        println(" Exception: $e")
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
}
