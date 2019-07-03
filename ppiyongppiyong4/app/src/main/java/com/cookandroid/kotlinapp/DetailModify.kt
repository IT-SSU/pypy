package com.cookandroid.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.detail_modify.*
import kotlinx.android.synthetic.main.header.*
import org.json.JSONObject

class DetailModify : Common() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_modify)
        txtHeaderTitle.text="상세정보 수정";
        btnHeaderSetting.visibility = View.GONE

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {

                val url = "http://61.84.24.251:49090/siren/updatedetail"
                val params = HashMap<String, String>()

                params["disease1"] = txtDisease1.text.toString()
                params["disease2"] = txtDisease2.text.toString()
                params["medicine1"] = txtMedichine1.text.toString()
                params["medicine2"] = txtMedichine2.text.toString()
                params["weight"] = user_Weight.text.toString()
                params["hospital"] = user_Hospital.text.toString()
                params["around1"] = txtContact1.text.toString()

                val jsonObject = JSONObject(params)

                // Volley post request with parameters
                val request = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                    Response.Listener { response ->
                        // Process the json
                        try {
                            println(" Response: $response")

                            val intent = Intent(this, Setting::class.java)
                            startActivity(intent)

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
