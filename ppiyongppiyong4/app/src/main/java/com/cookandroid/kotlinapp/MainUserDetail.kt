package com.cookandroid.kotlinapp

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.detail_list_item.*
import kotlinx.android.synthetic.main.detail_list_view.*
import kotlinx.android.synthetic.main.join.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.txtEmail
import org.jetbrains.anko.notificationManager
import org.json.JSONObject
import org.json.JSONArray

//data class UserDiseaseList2 (val userNum: Int, val diseaseCode:String, val medicineCode:String, val medicine: String)

class MainUserDetail : AppCompatActivity() {

    fun showProtectDetail(){
        //데이터 저장 하기
        val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = settings.edit() //데이터를 추가 할때사용

        val url = "http://61.84.24.251:49090/siren/protectInfo"//보호자 정보 가져오기// 변경 settings.getString("userNum","null")
        val params = HashMap<String, String>()
        params["userNum"] = settings.getString("userNum","1")
        //저장 되어있는 값 가져오기
        var jsonObject = JSONObject(params) //json 데이터를 사용할수있게 생성

        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Listener { response ->
                // Process the json
                try {
                    println(" Response: $response")
                    println("성공")
                    var protectDetailList = arrayListOf<ProtectTel>() //보호자 리스트

                    if (response.getString("result").equals("T")) {

                        val jsonArray = response?.getJSONArray("protect")// ?? 확인
                        var i = 0
                        for (i in 0..jsonArray?.length()!! - 1) {
                            jsonObject = jsonArray?.getJSONObject(i)!!
                            if (jsonObject != null) {
                                protectDetailList.add(
                                    ProtectTel(
                                        jsonObject.getInt("userNum"),
                                        jsonObject.getString("protectCode"),
                                        jsonObject.getString("protectName"),
                                        jsonObject.getString("protectPhone"),
                                        jsonObject.getString("protectRelation")
                                    )
                                )
                                protectDetailListSave.add(
                                    ProtectTel(
                                        jsonObject.getInt("userNum"),
                                        jsonObject.getString("protectCode"),
                                        jsonObject.getString("protectName"),
                                        jsonObject.getString("protectPhone"),
                                        jsonObject.getString("protectRelation")
                                    )
                                )
                            }
                        }
                        //println(userDetailList[0].disease)
                        secondAdapter = DetailProtectAdapter(this, protectDetailList) { ProtectTel ->
                            println("아이템 클릭")
                            val builder = AlertDialog.Builder(this)

                            val dialogView = layoutInflater.inflate(R.layout.edit_box_protect, null) //새로 만들기

                            val dialog = builder.create()

                            var protectName = dialogView.findViewById<EditText>(R.id.edit_protect_name)      //보호자 이름
                            val protectRelation = dialogView.findViewById<EditText>(R.id.edit_protect_relation)    //보호자와의 관계
                            val protectPhone = dialogView.findViewById<EditText>(R.id.edit_protect_phone)
                            val updateButton = dialogView.findViewById<Button>(R.id.btn_protect_update)          //수정
                            val deleteButton = dialogView.findViewById<Button>(R.id.btn_protect_delete)          //삭제

                            protectName.setText(ProtectTel.protectName) //보호자
                            protectRelation.setText(ProtectTel.protectRelation) //보호자와의 관계
                            protectPhone.setText(ProtectTel.protectPhone)   //보호자 전화번호

                            dialog.setView(dialogView)// ???

                            /////////////////수정 버튼
                            updateButton.setOnClickListener {
                                val url = "http://61.84.24.251:49090/siren/protectUpdate"//수정 페이지 만들어서 적용
                                val params = HashMap<String, String>()
                                params["userNum"] = ProtectTel.userNum.toString()  //유저 넘버, 질병 코드와 수정할 질병명 및 세부사항
                                params["protectCode"] = ProtectTel.protectCode
                                params["protectName"] = protectName.text.toString()//UserDetail.diseaseName
                                params["protectRelation"] = protectRelation.text.toString()
                                params["protectPhone"] = protectPhone.text.toString()
                                ProtectTel.protectName = protectName.text.toString()
                                ProtectTel.protectRelation = protectRelation.text.toString()
                                ProtectTel.protectPhone = protectPhone.text.toString()

                                val jsonObject = JSONObject(params)
                                // Volley post request with parameters
                                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                    Listener { response ->
                                        // Process the json
                                        try {
                                            println(" Response: $response")

                                        } catch (e: Exception) {
                                            println(" Exception: $e")
                                        }
                                    }, Response.ErrorListener {
                                        // Error in request
                                        println(" Volley error: $it")
                                        //txtId.text = "Volley error: $it"
                                    }
                                )
                                request.retryPolicy = DefaultRetryPolicy(
                                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                )
                                // Add the volley post request to the request queue
                                VolleySingleton.getInstance(this).addToRequestQueue(request)
                                secondAdapter.updateData()//변경된 내용 업데이트

                            }
                            ////////////////////// 수정 버튼 end

                            ////////////////////// 삭제 버튼
                            deleteButton.setOnClickListener {
                                val url = "http://61.84.24.251:49090/siren/protectDelete"//삭제 페이지 만들어서 적용
                                val params = HashMap<String, String>()
                                params["userNum"] = ProtectTel.userNum.toString()  //유저 넘버, 질병 코드와 수정할 질병명 및 세부사항
                                params["protectCode"] = ProtectTel.protectCode
                                protectDetailList.remove(ProtectTel)
                                val jsonObject = JSONObject(params)
                                // Volley post request with parameters
                                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                    Listener { response ->
                                        // Process the json+
                                        try {
                                            println(" Response: $response")

                                            //println(response.getString("result"))
                                        } catch (e: Exception) {
                                            println(" Exception: $e")
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
                                secondAdapter.updateData()//변경된 내용 업데이트
                                dialog.dismiss()//다이얼 로그 닫기
                            }
                            ////////////////////// 삭제 버튼 end
                            dialog.show()
                        }

                        secondRecyclerView.adapter = secondAdapter

                        val lm1 = LinearLayoutManager(this)
                        secondRecyclerView.layoutManager = lm1
                        secondRecyclerView.setHasFixedSize(true)


                        //for((index,value )in userDetailList.withIndex()){
                        //    println("index :$index, value:$value")
                        //}

                    } else{

                    }
                    //Toast.makeText(this, "아이디나 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
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
    fun showDetail(){

        val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = settings.edit() //데이터를 추가 할때사용
        //저장 되어있는 값 가져오기
        val url = "http://61.84.24.251:49090/siren/detailInfo"//디테일 정보
        val params = HashMap<String, String>()
        params["userNum"] = settings.getString("userNum","1")
        var jsonObject = JSONObject(params) //json 데이터를 사용할수있게 생성

        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Listener { response ->
                // Process the json
                try {
                    println(" Response: $response")
                    println("성공")
                    var userDetailList = arrayListOf<UserDetail>() //병 + 세부사항 리스트

                    if (response.getString("result").equals("T")) {
                        //num1 = response.getString("number").toInt()
                        //println(response.getString("number"))
                        //response.getJSONArray("detail")

                        val jsonArray = response?.getJSONArray("detail")// ?? 확인
                        var i = 0
                        for (i in 0..jsonArray?.length()!! - 1) {
                            jsonObject = jsonArray?.getJSONObject(i)!!
                            if (jsonObject != null) {
                                userDetailList.add(
                                    UserDetail(
                                        jsonObject.getInt("userNum"),
                                        jsonObject.getString("detailCode"),
                                        jsonObject.getString("diseaseName"),
                                        jsonObject.getString("detailContent")
                                    )
                                )
                                userDetailListSave.add(
                                    UserDetail(
                                        jsonObject.getInt("userNum"),
                                        jsonObject.getString("detailCode"),
                                        jsonObject.getString("diseaseName"),
                                        jsonObject.getString("detailContent")
                                    )
                                )
                                //userDetailList.add(UserDetail(1,"1detail",jsonObject.getString("disease"),"감기약등")
                            }

                        }
                        //jsonArray?.length()
//                        while(true){
//
//                            i++
//                        }

                        //println(userDetailList[0].disease)
                        firstAdapter = DetailListAdapter(this, userDetailList) { UserDetail ->
                            println("아이템 클릭")
                            val builder = AlertDialog.Builder(this)

                            val dialogView = layoutInflater.inflate(R.layout.edit_box, null)

                            val dialog = builder.create()

                            var diseaseText = dialogView.findViewById<EditText>(R.id.edit_disease)      //병
                            val medicineText = dialogView.findViewById<EditText>(R.id.edit_medicine)    //세부사항
                            val updateButton = dialogView.findViewById<Button>(R.id.btn_update)          //수정
                            val deleteButton = dialogView.findViewById<Button>(R.id.btn_delete)          //삭제

                            diseaseText.setText(UserDetail.diseaseName)//병이름
                            medicineText.setText(UserDetail.detailContent)//세부사항 내용


                            dialog.setView(dialogView)// ???

                            /////////////////수정 버튼
                            updateButton.setOnClickListener {
                                val url = "http://61.84.24.251:49090/siren/detailUpdate"//수정 페이지 만들어서 적용
                                val params = HashMap<String, String>()
                                params["userNum"] = UserDetail.userNum.toString()  //유저 넘버, 질병 코드와 수정할 질병명 및 세부사항
                                params["detailCode"] = UserDetail.detailCode
                                params["diseaseName"] = diseaseText.text.toString()//UserDetail.diseaseName
                                params["detailContent"] = medicineText.text.toString()
                                UserDetail.diseaseName = diseaseText.text.toString()
                                UserDetail.detailContent = medicineText.text.toString()

                                val jsonObject = JSONObject(params)
                                // Volley post request with parameters
                                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                    Listener { response ->
                                        // Process the json
                                        try {
                                            println(" Response: $response")

                                        } catch (e: Exception) {
                                            println(" Exception: $e")
                                        }
                                    }, Response.ErrorListener {
                                        // Error in request
                                        println(" Volley error: $it")
                                        //txtId.text = "Volley error: $it"
                                    }
                                )
                                request.retryPolicy = DefaultRetryPolicy(
                                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                )
                                // Add the volley post request to the request queue
                                VolleySingleton.getInstance(this).addToRequestQueue(request)
                                firstAdapter.updateData()//변경된 내용 업데이트

                            }
                            ////////////////////// 수정 버튼 end

                            ////////////////////// 삭제 버튼
                            deleteButton.setOnClickListener {
                                val url = "http://61.84.24.251:49090/siren/detailDelete"//삭제 페이지 만들어서 적용
                                val params = HashMap<String, String>()
                                params["userNum"] = UserDetail.userNum.toString()  //유저 넘버, 질병 코드를 보내면 삭제
                                params["detailCode"] = UserDetail.detailCode
                                userDetailList.remove(UserDetail)
                                val jsonObject = JSONObject(params)
                                // Volley post request with parameters
                                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                    Listener { response ->
                                        // Process the json+
                                        try {
                                            println(" Response: $response")

                                            //println(response.getString("result"))
                                        } catch (e: Exception) {
                                            println(" Exception: $e")
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
                                firstAdapter.updateData()//변경된 내용 업데이트
                                dialog.dismiss()//다이얼 로그 닫기
                            }
                            ////////////////////// 삭제 버튼 end
                            dialog.show()
                        }

                        fristRecyclerView.adapter = firstAdapter

                        val lm1 = LinearLayoutManager(this)
                        fristRecyclerView.layoutManager = lm1
                        fristRecyclerView.setHasFixedSize(true)


                        //for((index,value )in userDetailList.withIndex()){
                        //    println("index :$index, value:$value")
                        //}

                    } else{

                    }
                    //Toast.makeText(this, "아이디나 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
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


    var userDetailListSave = arrayListOf<UserDetail>() // 디테일
    var protectDetailListSave = arrayListOf<ProtectTel>() // 디테일
    var firstAdapter = DetailListAdapter(this, userDetailListSave) {}
    var secondAdapter = DetailProtectAdapter(this, protectDetailListSave) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_list_view)

        showDetail()
        showProtectDetail()
        //val mapper = jacksonObjectMapper() //json 파싱을 위한 함수?
///////////////////////////////////request end

///////////////////////////////////추가 버튼
        btn_list_add.setOnClickListener {
            val url = "http://61.84.24.251:49090/siren/detailInsert"//삭제 페이지 만들어서 적용
            val params = HashMap<String, String>()

            //val detailinfo = userDetailListSave[userDetailListSave.size-1].detailCode.toInt()+1
            //println(detailinfo)
            params["userNum"] = userDetailListSave[0].userNum.toString()//유저 번호 나중에 변경 (보내는 정보)
            //params["detailCode"] = detailinfo.toString()
            params["diseaseName"] = "병 이름"
            params["detailContent"] = "세부사항"

            val jsonObject = JSONObject(params)
            // Volley post request with parameters
            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Listener { response ->
                    // Process the json+
                    try {
                        println(" Response: $response")

                        //println(response.getString("result"))
                    } catch (e: Exception) {
                        println(" Exception: $e")
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
            Handler().postDelayed({
                showDetail()
                //method
            }, 100) //딜레이가 없으면 db를 처리하기 전에 데이터를 가져오기대문에 딜레이 추가


            //firstAdapter.updateData()//변경된 내용 업데이트
        }
///////////////////////////////////추가 버튼 end

 //////////////////////////////////연락처 추가
        btn_protect_add.setOnClickListener {
            val url = "http://61.84.24.251:49090/siren/protectInsert"//삭제 페이지 만들어서 적용
            val params = HashMap<String, String>()

            params["userNum"] = protectDetailListSave[0].userNum.toString()//유저 번호 나중에 변경 (보내는 정보)
            //params["detailCode"] = detailinfo.toString()
            params["protectName"] = "보호자 이름"
            params["protectRelation"] = "관계"
            params["protectPhone"] = "전화번호"

            val jsonObject = JSONObject(params)
            // Volley post request with parameters
            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Listener { response ->
                    // Process the json+
                    try {
                        println(" Response: $response")

                        //println(response.getString("result"))
                    } catch (e: Exception) {
                        println(" Exception: $e")
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
            Handler().postDelayed({
                showProtectDetail()
                //method
            }, 100) //딜레이가 없으면 db를 처리하기 전에 데이터를 가져오기대문에 딜레이 추가


            //firstAdapter.updateData()//변경된 내용 업데이트
        }
///////////////////////////////////추가 버튼 end

    }
}





