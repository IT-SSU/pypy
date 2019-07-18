package com.cookandroid.kotlinapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.join.*
import org.json.JSONObject

class Join : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)
        txtHeaderTitle.text="회원가입";
        btnHeaderSetting.visibility = View.GONE

        var EmailChk = false

        //중복체크 버튼
        btnEmailChk.setOnClickListener{
            val url = "http://61.84.24.251:49090/siren/emailoverlap"
            val params = HashMap<String,String>()
            params["email"] = txtEmail.text.toString()
            val jsonObject = JSONObject(params)
            // Volley post request with parameters
            val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                Response.Listener { response ->
                    // Process the json
                    try {
                        println(" Response: $response")

                        if(response.getString("result").equals("T")){

                            Toast.makeText(this, "사용 가능한 이메일 입니다.", Toast.LENGTH_SHORT).show()
                            EmailChk = true
                        }else {
                            Toast.makeText(this, "이미 사용 중인 이메일 입니다.", Toast.LENGTH_SHORT).show()
                            EmailChk = false
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
        }
        //비밀번호 체크 아직 진행중
        txtPwChk.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (txtPw.text.toString() == txtPwChk.text.toString()){
                    imgvPwChk.setImageResource(R.drawable.abc_ic_arrow_drop_right_black_24dp)
                }else {
                    imgvPwChk.setImageResource(R.drawable.abc_action_bar_item_background_material)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun afterTextChanged(s: Editable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        //뒤로 가기
        btnHeaderBack.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }
        
        //회원가입 버튼
        btnSubmit.setOnClickListener{
            val url = "http://61.84.24.251:49090/siren/insert"
            //textView.text = ""
            // Post parameters
            // Form fields and values
            val params = HashMap<String,String>()
            params["email"] = txtEmail.text.toString()
            params["user_password"] = txtPw.text.toString()
            params["name"] = txtName.text.toString()
            params["date_of_birth"] = txtBirth.text.toString()
            params["tel"] = txtPhone.text.toString()

            val jsonObject = JSONObject(params)
            // Volley post request with parameters
            val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                Response.Listener { response ->
                    // Process the json
                    try {
                        println(" Response: $response")

                        if(response.getString("result").equals("T")){
                            Toast.makeText(this, " 회원가입을 축하합니다.",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, JoinOk::class.java)
                            startActivity(intent)
                        }else {
                            Toast.makeText(this, " 회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                            if ( EmailChk == false ) {
                                Toast.makeText(this, " 아이디 중복을 확인 해주세요.", Toast.LENGTH_SHORT).show()
                            }

                            if ( txtPw.text.equals(txtPwChk.text) ){
                            }else
                                txvPw.setText("비밀번호 : 재입력한 비밀번호을 확인 해주세요.")
                            txvPw.setTextColor(Color.parseColor("#F24150"))

                            if(txtName.getText().toString().replace(" ", "").equals("")){
                                txvName.setText("이름 : 이름을 입력해주세요.")
                                txvName.setTextColor(Color.parseColor("#F24150"))
                            }
                            if(txtBirth.getText().toString().replace(" ", "").equals("")){
                                txvBirth.setText("생년월일 : 생년월일을 입력해주세요.")
                                txvBirth.setTextColor(Color.parseColor("#F24150"))
                            }
                            if(txtPhone.getText().toString().replace(" ", "").equals("")){
                                txvPhone.setText("핸드폰 번호 : 번호를 입력해주세요.")
                                txvPhone.setTextColor(Color.parseColor("#F24150"))
                            }
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
        }
    }
}
