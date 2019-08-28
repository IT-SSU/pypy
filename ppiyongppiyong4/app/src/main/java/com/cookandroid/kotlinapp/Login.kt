package com.cookandroid.kotlinapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject

class Login : Common() {
//아이디이메일 xxxxx@xxxx.xxx 이형식이 맞지 않으면 다시 쓰라고 표시
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        txtHeaderTitle.text="로그인";
        btnHeaderSetting.visibility = View.GONE
        btnHeaderBack.visibility = View.GONE

        //로그아웃으로 돌아왔을때 clear!!
        //소스 확실하지 않음!!
//        txtEmail.setText("");
//        txtPw.setText("");
//        chkAutoLogin.isChecked=true;
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        getIntent().removeExtra("email");

        val btnlogin = findViewById<Button>(R.id.btnLogin)

        var blPassword = true
        img_eye.setOnClickListener {
            // InputType  129 : password  145 : textVisiblePassword
            if(blPassword){
                txtPw.setInputType(145);
                blPassword = false;
            }else {
                blPassword = true;
                txtPw.setInputType(129);
            }
        }

      //  val btn = findViewById<Button>(R.id.btnLogin)

        //로그인 버튼
        btnlogin.setOnClickListener{
            val url = "http://61.84.24.251:49090/siren/email_login"
            val params = HashMap<String, String>()
            params["email"] = txtEmail.text.toString()
            params["password"] = txtPw.text.toString()
            val jsonObject = JSONObject(params)

            // Volley post request with parameters
            val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                com.android.volley.Response.Listener { response ->
                    // Process the json
                    try {
                        println(" Response: $response")
                        if (response.getString("result").equals("T")){
                            //로그인 성고하면 사용자의 userNum을 저장한다
                            val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = settings.edit() //데이터를 추가 할때사용
                            editor.putString("userNum",response.getString("userNum"))
                            editor.putString("email",response.getString("email"))
                            editor.putString("name",response.getString("name"))
                            editor.commit()
                            ///
                            val intent = Intent(this, Main::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            intent.putExtra("email",response.getString("email"))
                             startActivity(intent)
                        }else
                            Toast.makeText(this, "아이디나 비밀번호가 잘못되었습니다.",Toast.LENGTH_SHORT).show()
                        //println(response.getString("result"))
                        //txtId.text = "Response: $response"

                    }catch (e:Exception){
                        println(" Exception: $e")
                        //txtPw.text = "Exception: $e"
                    }

                }, com.android.volley.Response.ErrorListener{
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

        // PW찾기
        btnFind.setOnClickListener {
            startActivity(Intent(this,PW_find::class.java))
        }//END PW찾기

        // 회원가입
        btnJoin.setOnClickListener {
            startActivity(Intent(this,Join::class.java))
        }//END 회원가입


    }
}
