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
import kotlinx.android.synthetic.main.member_modify.*
import org.json.JSONObject

class MemberModify : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_modify)
        txtHeaderTitle.text="기본정보 수정";
        btnHeaderSetting.visibility = View.GONE

        val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)

        val url = "http://61.84.24.251:49090/siren/userinfo"// 회원 정보 수정으로 변경
        val params = HashMap<String, String>()
        params["email"] = settings.getString("email",null)
        val jsonObject = JSONObject(params)

        // Volley post request with parameters
        val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    println(" Response: $response")
                    txtName.setText(response.getString("name"))
                    txtPhone.setText(response.getString("phone"))
                    txtBirth.setText(response.getString("birth"))


                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
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

        btnSubmit.setOnClickListener {
            //txtName
            //txtPhone
            //txtBirth

            val url = "http://61.84.24.251:49090/siren/userInfoUpdate"// 회원 정보 수정으로 변경
            val params = HashMap<String, String>()
            params["userNum"] = settings.getString("userNum",null)
            //params["userNum"] = txtEmail.text.toString() // 로그인 할때 저장한 데이터 가져와서 넣기(변경해야함)
            params["name"] = txtName.text.toString()
            params["phone"] = txtPhone.text.toString()
            params["birth"] = txtBirth.text.toString()

            val jsonObject = JSONObject(params)

            // Volley post request with parameters
            val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                Response.Listener { response ->
                    // Process the json
                    try {
                        println(" Response: $response")
                        if (response.getString("result").equals("T")){
                            val editor: SharedPreferences.Editor = settings.edit() //데이터를 추가 할때사용
                            editor.putString("name",response.getString("name"))
                            editor.commit()

                            Toast.makeText(this, "회원정보가 수정 되었습니다.", Toast.LENGTH_SHORT).show()
                        }else
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
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
