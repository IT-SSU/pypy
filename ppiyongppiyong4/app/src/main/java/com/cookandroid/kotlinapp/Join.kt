package com.cookandroid.kotlinapp

import android.content.Intent
import android.content.SharedPreferences
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
import java.util.regex.Pattern

class Join : Common() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)
        txtHeaderTitle.text = "회원가입";
        btnHeaderSetting.visibility = View.GONE

        var EmailChk = false
        var PwChk = false

        //이메일 형식 체크 - 이메일 정규식 표현
        //중복체크 버튼
        btnEmailChk.setOnClickListener {
            //패턴 설정
            val reEmail = "([\\w-+]+(?:\\.[\\w-+]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,7})"

            val pEmail = Pattern.compile(reEmail, Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
            val mEmail = pEmail.matcher(txtEmail.getText().toString())
            //이메일 정규화를 확인
            if (!mEmail.matches()) {
                //패턴에 맞지않으면
                Toast.makeText(this, "Email형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                EmailChk = false
                println(EmailChk)
            } else {
                println(EmailChk)
                val url = "http://61.84.24.251:49090/siren/emailoverlap"
                val params = HashMap<String, String>()
                //서버쪽으로 Email을 보낸다.
                params["email"] = txtEmail.text.toString()
                //json 형식으로
                val jsonObject = JSONObject(params)
                // Volley post request with parameters
                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    Response.Listener { response ->
                        // Process the json
                        try {
                            println(" Response: $response")

                            if (response.getString("result").equals("T")) {

                                Toast.makeText(this, "사용 가능한 이메일 입니다.", Toast.LENGTH_SHORT).show()
                                EmailChk = true
                            } else {
                                Toast.makeText(this, "이미 사용 중인 이메일 입니다.", Toast.LENGTH_SHORT).show()
                                EmailChk = false
                            }
                            //println(response.getString("result"))
                            //txtId.text = "Response: $response"
                        } catch (e: Exception) {
                            println(" Exception: $e")
                            //txtPw.text = "Exception: $e"
                        }

                    }, Response.ErrorListener {
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
        //뒤로 가기
        btnHeaderBack.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        //회원가입 버튼
        btnSubmit.setOnClickListener {

            //패턴 설정
            val reEmail = "([\\w-+]+(?:\\.[\\w-+]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,7})"

            val pEmail = Pattern.compile(reEmail, Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
            val mEmail = pEmail.matcher(txtEmail.getText().toString())
            //이메일 정규화를 확인
            if (!mEmail.matches()) {
                //패턴에 맞지않으면
                Toast.makeText(this, "Email형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                println(EmailChk)
            } else {
                val rePswd =
                    """^(?=.*[A-Za-z])(?=.*\d)(?=.*[${'$'}@${'$'}!%*#?&])[A-Za-z\d${'$'}@${'$'}!%*#?&]{8,}${'$'}"""
                //특수문자 !%*#?& 최소8자이상 영문
                val pPswd = Pattern.compile(rePswd, Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
                val mPswd = pPswd.matcher(txtPw.getText().toString())
                //비밀번호 정규화를 확인
                if (!mPswd.matches()) {
                    Toast.makeText(this, "비밀번호형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val password = txtPwChk.getText().toString()
                    val password2 = txtPwChk.getText().toString()
                    if (password.equals(password2)) {
                        PwChk = true
                        val reBirth =
                          "((?:(?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))(?:[0]?[1-9]|[1][012])(?:(?:[0-2]?\\d{1})|(?:[3][01]{1})))(?![\\d])"   // YYYYMMDD 1
                        //생년월일 정규식
                        val pBirth =
                            Pattern.compile(reBirth, Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
                        val mBirth = pBirth.matcher(txtBirth.getText().toString())
                        //생년월일 정규식를 확인
                        if (!mBirth.matches()) {
                            //패턴에 맞지않으면
                            Toast.makeText(this, "생년월일형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            val Phone1 = txtPhone.getText().toString() //010
                            val Phone2 = txtPhone2.getText().toString() //1234
                            val Phone3 = txtPhone3.getText().toString() //5678
                            val Phone = Phone1 + "-" + Phone2 + "-" + Phone3 // 01012345678

                            val rePhone = """^\d{3}-\d{3,4}-\d{4}${'$'}"""
                            //핸드폰 번호 정규식
                            val pPhone = Pattern.compile(rePhone, Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
                            val mPhone = pPhone.matcher(Phone)
                            if (!mPhone.matches()){
                                Toast.makeText(this, "핸드폰 번호 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }else{
                                val url = "http://61.84.24.251:49090/siren/insert"
                                //textView.text = ""
                                // Post parameters
                                // Form fields and values
                                val params = HashMap<String, String>()
                                //서버쪽으로 Email, Password, Name, Birth, Phone을 보낸다.
                                params["email"] = txtEmail.text.toString()
                                params["password"] = txtPw.text.toString()
                                params["name"] = txtName.text.toString()
                                params["birth"] = txtBirth.text.toString()
                                params["phone"] = Phone

                                //json 형식으로
                                val jsonObject = JSONObject(params)
                                // Volley post request with parameters
                                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                    Response.Listener { response ->
                                        // Process the json
                                        try {
                                            println(" Response: $response")
                                            //응답받은 result 값이 T 이면 회원 가입
                                            if (response.getString("result").equals("T") && PwChk == true && ChkPrivacy.isChecked) {
                                                Toast.makeText(
                                                    this,
                                                    " 회원가입을 축하합니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                val intent = Intent(this, JoinOk::class.java)
                                                val settings: SharedPreferences =
                                                    getSharedPreferences("userNumber", MODE_PRIVATE)
                                                val editor: SharedPreferences.Editor =
                                                    settings.edit() //데이터를 추가 할때사용
                                                editor.putString("email", txtEmail.getText().toString())
                                                editor.putString(
                                                    "name",
                                                    txtName.getText().toString()
                                                ) //스프링 코드 변경
                                                editor.commit()
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    " 회원가입에 실패하셨습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                if (PwChk == true) {
                                                } else {
                                                    txvPw.setText("비밀번호 : 재입력한 비밀번호을 확인 해주세요.")
                                                    txvPw.setTextColor(Color.parseColor("#F24150"))
                                                }
                                                if (txtName.getText().toString().replace(
                                                        " ",
                                                        ""
                                                    ).equals("")
                                                ) {
                                                    txvName.setText("이름 : 이름을 입력해주세요.")
                                                    txvName.setTextColor(Color.parseColor("#F24150"))
                                                }
                                                if (txtBirth.getText().toString().replace(
                                                        " ",
                                                        ""
                                                    ).equals("")
                                                ) {
                                                    txvBirth.setText("생년월일 : 생년월일을 입력해주세요.")
                                                    txvBirth.setTextColor(Color.parseColor("#F24150"))
                                                }
                                                if (txtPhone.getText().toString().replace(
                                                        " ",
                                                        ""
                                                    ).equals("")
                                                ) {
                                                    txvPhone.setText("핸드폰 번호 : 번호를 입력해주세요.")
                                                    txvPhone.setTextColor(Color.parseColor("#F24150"))
                                                }
                                            }

                                            //println(response.getString("result"))
                                            //txtId.text = "Response: $response"
                                        } catch (e: Exception) {
                                            println(" Exception: $e")
                                            //txtPw.text = "Exception: $e"
                                        }

                                    }, Response.ErrorListener {
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
                    } else {
                        PwChk = false
                        Toast.makeText(this, "재입력한비밀번호가 맞지않습니다.", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
    }
}