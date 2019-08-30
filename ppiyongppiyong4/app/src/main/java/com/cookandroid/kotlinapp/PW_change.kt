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
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.pw_change.*
import org.json.JSONObject

class PW_change : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pw_change)
        btnHeaderSetting.visibility = View.GONE
        txtHeaderTitle.text="비밀번호 변경";

        txtNewPw
        txtNowPw

        btnSubmit.setOnClickListener {
            val reg1 = Regex("\\(([A-Z])\\w+\\)")
//            if(){
//
//            }else{
//                Toast.makeText(this, "비밀변호가 형식을 맟쳐주세요", Toast.LENGTH_SHORT).show()
//            }
            val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
            val url = "http://61.84.24.251:49090/siren/passwordUpdate"
            val params = HashMap<String, String>()
            //현재 비밀번호가 true일때 실행
            params["email"] = settings.getString("email",null)
            params["password"] = txtNowPw.text.toString()
            params["passwordNew"] = txtNewPw.text.toString()
            val jsonObject = JSONObject(params)
            val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                Response.Listener { response ->
                    // Process the json
                    try {
                        println(" Response: $response")
                        if (response.getString("result").equals("T")){
                            //로그인 성고하면 사용자의 userNum을 저장한다
                            println("비밀번호 변경완료")
                            Toast.makeText(this, "비밀변호가 변경 되었습니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, "현재 비밀번호가 잘못되었습니다.",Toast.LENGTH_SHORT).show()
                        }
                        //println(response.getString("result"))
                        //txtId.text = "Response: $response"

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
            startActivity(Intent(this,Setting::class.java))
        }

    }
}
