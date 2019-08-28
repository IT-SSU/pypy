package com.cookandroid.kotlinapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.json.JSONObject
import java.io.IOException
import java.util.*
import android.os.Handler


class Main : Common(), OnMapReadyCallback {
    private val REQUEST_ACCESS_FINE_LOCATION = 1000
    lateinit var geocoder : Geocoder
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack
    var lat: Double = 0.0
    var long: Double = 0.0
    var address = ""


    private val polylineOptions = PolylineOptions().width(5f).color(Color.RED)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setpermission()
        val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)

        txtName.setText(settings.getString("name",null))

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationInit()

        btnReport.setOnClickListener {
            setpermission()
            val settings: SharedPreferences = getSharedPreferences("userNumber", MODE_PRIVATE)
            var protectDetailList = arrayListOf<ProtectTel>() //보호자 리스트
            var userDetailList = arrayListOf<UserDetail>() //병 + 세부사항 리스트

            var url = "http://61.84.24.251:49090/siren/smsSendData" //회원 병, 세부사항 목록
            var params = HashMap<String, String>()
            params["userNum"] = settings.getString("userNum",null)
            params["email"] = settings.getString("email",null)
            var jsonObject = JSONObject(params)

            val request1 = JsonObjectRequest(Request.Method.POST,url,jsonObject,
                Response.Listener { response ->
                    // Process the json
                    try {
                        println(" Response: $response")
                        if (response.getString("result").equals("T")) {
                            //num1 = response.getString("number").toInt()
                            //println(response.getString("number"))
                            //response.getJSONArray("detail")
                            val jsonArray = response?.getJSONArray("detail")// ?? 확인
                            //세부사항 데이터 저장
                            for (i in 0..jsonArray?.length()!! - 1) { //병 및 세부사항 저장
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

                                }
                            }
                            // 보호자 연락처 저장
                            val jsonArray2 = response?.getJSONArray("protect")// ?? 확인
                            for (i in 0..jsonArray2?.length()!! - 1) {
                                jsonObject = jsonArray2?.getJSONObject(i)!!
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
                                }
                            }

                            // 저장한 데이터
                            val smsManager = SmsManager.getDefault()
                            //처음으로 보낼 메세지 만들기
                            var message = "${txtMyAdd.text}위치에 ${response.getString("name")}환자가 발생 "
                            for (j in userDetailList){// 리스트 수만큼 추가
                                message = message + """질병 "${j.diseaseName}" 세부사항 "${j.detailContent}" """
                            }
                            println()
                            //두번째로는 보호자 번호에 맞게 문자를 보내기
                            for (i in protectDetailList) {
                                var phoneNumber = i.protectPhone
                                message = message.substring(0,70)
                                println("보호자 번호 "+phoneNumber+"메세지 내용 "+message+"메세지 길이"+message.length)

                                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                                //smsManager.sendTextMessage("01093098508", null, message, null, null)
                                Toast.makeText(getApplicationContext(), "${phoneNumber} 전송 성공", Toast.LENGTH_LONG).show()
                            }

                            //val message = "${txtMyAdd.text}에 ${response.getString("name")} 환자가 발생했다."
                            //print(message)
                            //smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                            //smsManager.sendTextMessage("01093098508", null, message, null, null)
                            //Toast.makeText(getApplicationContext(), "전송 성공", Toast.LENGTH_LONG).show();
                            //startActivity(intent)

                        }
                    }catch (e:Exception){
                        println(" Exception: $e")
                        Toast.makeText(getApplicationContext(), "전송 실패1", Toast.LENGTH_LONG).show();
                        //txtPw.text = "Exception: $e"
                    }
                }, Response.ErrorListener{
                    // Error in request
                    println(" Volley error: $it")
                    //txtId.text = "Volley error: $it"
                }
            )
            request1.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            // Add the volley post request to the request queue
            VolleySingleton.getInstance(this).addToRequestQueue(request1)

        }

    }
    private val TAG="Permission"
    private val REQUEST_RECORRD_CODE=1


    private fun setpermission(){
        val permission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)
        if (permission!=PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"권한 거부")
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.SEND_SMS)){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("권한 허용 어쩌구 저쩌구")
            builder.setTitle("Permission requiered")
            builder.setPositiveButton("OK"){
                dialog, which->
                Log.d(TAG,"Clicked")
                makeRepuest()
            }
            val dialog = builder.create()
             dialog.show()

        }
        else{
            makeRepuest()
        }
    }

    private fun makeRepuest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),
            REQUEST_RECORRD_CODE)
    }

    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationRequest.interval = 10000

        locationRequest.fastestInterval = 5000
    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val seoul = LatLng(97.5662952, 126.97794509999994)
        mMap.addMarker(MarkerOptions().position(seoul).title("Marker in Seoul"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul))


        permissionCheck(cancel = {
            showPermissionInfoDialog()
        }, ok = {
            mMap.isMyLocationEnabled = true
        })
    }

    override fun onResume() {
        super.onResume()


        permissionCheck(cancel = {

            showPermissionInfoDialog()
        }, ok = {

            addLocationListener()
        })
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null);
    }

    private fun showPermissionInfoDialog() {
        alert("현재 위치 정보를 얻기 위해서는 위치 권한이 필요합니다", "권한이 필요한 이유") {
            yesButton {

                ActivityCompat.requestPermissions(this@Main,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION)
            }
            noButton { }
        }.show()

    }

    override fun onPause() {
        super.onPause()

        removeLocationListener()
    }

    private fun removeLocationListener() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty()
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    addLocationListener()
                } else {

                    toast("권한 거부 됨")
                }
                return
            }
            REQUEST_RECORRD_CODE ->{
                if(grantResults.isEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG,"권한 거부 실화?")
                }
                else{
                    Log.d(TAG,"권한 허용")
                }
            }
        }
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                cancel()
            } else {

                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION)
            }
        } else {

            ok()
        }
    }

    inner class MyLocationCallBack : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {

            super.onLocationResult(locationResult)
            val location = locationResult?.lastLocation
            location?.run {

                val latLng = LatLng(latitude, longitude)

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                Log.d("MapsActivity", "위도: $latitude, 경도: $longitude")

                lat = latitude
                long = longitude
                Add()

                polylineOptions.add(latLng)
                mMap.addPolyline(polylineOptions)

            }

        }


    }

    protected fun Add()  {
        lateinit var address : List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            address = geocoder.getFromLocation(lat,long,10)

            //edtGPS.setText("위치"+address)
            Log.i("위치","$address")
        }catch (e: IOException){
            e.printStackTrace()
        }

        var cityName :String
        cityName = address.get(0).getAddressLine(0)
        var country : String = address.get(0).countryName
        cityName = cityName.replace(country,"")
        txtMyAdd.setText(cityName)
    }





}
