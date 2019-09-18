package com.cookandroid.kotlinapp

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.control.*
import java.io.IOException
import java.util.*

class ControlActivity: AppCompatActivity(){
    companion object {
        var mMyUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var mBluetoothSocket: BluetoothSocket? = null
        lateinit var mProgress: ProgressDialog
        lateinit var mBluetoothAdapter: BluetoothAdapter
        var mIsConnected: Boolean = false
        lateinit var mAddress:String
    }

    //onCreate() 메소드 시작
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control)
        mAddress = intent.getStringExtra(BluetoothScan.EXTRA_ADDRESS)

        ConnectToDevice(this).execute()

        //버튼들이 클릭 되었을때 발생하는 이벤트
        btnOn.setOnClickListener { sendCommand("a") }
        btnOff.setOnClickListener { sendCommand("b") }
        btnDisconnect.setOnClickListener{ disconnect()}
    }//onCreate() 메소드 끝

    private fun sendCommand(input: String){
        if(mBluetoothSocket != null){
            try{
                mBluetoothSocket!!.outputStream.write(input.toByteArray())
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
    private fun disconnect(){
        if (mBluetoothSocket != null){
            try{
                mBluetoothSocket!!.close()
                mBluetoothSocket = null
                mIsConnected =false
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>(){
        private  var connectSuccess: Boolean =true
        private  val context: Context

        init {
            this.context = c
        }
        override fun onPreExecute() {
            super.onPreExecute()
            mProgress = ProgressDialog.show(context, "연결중...", "잠시만 기다려주세요.")
        }

        override fun doInBackground(vararg params: Void?): String? {
           try{
                if(mBluetoothSocket == null || !mIsConnected){
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(mAddress)
                    mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(mMyUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    mBluetoothSocket!!.connect()
                }
           }catch(e:IOException) {
               connectSuccess = false
               e.printStackTrace()
           }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(!connectSuccess){
                Log.i("data", "couldn't connect")
            } else {
                mIsConnected = true
            }
            mProgress.dismiss()
        }

    }
}