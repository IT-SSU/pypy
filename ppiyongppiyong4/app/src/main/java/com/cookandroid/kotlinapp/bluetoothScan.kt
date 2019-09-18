package com.cookandroid.kotlinapp

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.bluetooth_scan.*
import org.jetbrains.anko.toast

class BluetoothScan: Common() {

    private var mBluetoothAdapter:BluetoothAdapter? = null
    private lateinit var mPairedDevice: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
    }

    //onCreate() 메소드 시작
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_scan)

        //블루투스를 지원하는 디바이스인지 판별하는 메소드
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(mBluetoothAdapter == null){
            toast("블루투스를 지원 하지 않는 디바이스 입니다.")
            return
        }

        if(!mBluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }


        select_device_refresh.setOnClickListener {
            mPairedDevicedDeviceList()
        }
    }//onCreate() 메소드 끝

    //블루투스를 지원 하는 디바이스 불러오는 메소드
    private fun mPairedDevicedDeviceList() {
        mPairedDevice = mBluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (!mPairedDevice.isEmpty()){
            for (device: BluetoothDevice in mPairedDevice){
                list.add(device)
                Log.i("device", ""+device)
            }
        }else{
            toast("연결된 블루투스 장치가 없습니다.")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        select_device_list.adapter = adapter as ListAdapter?
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            val device:BluetoothDevice = list[position]
            val address:String = device.address
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS,address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(mBluetoothAdapter!!.isEnabled){
                    toast("블루투스가 사용 설정되었습니다.")
                }else {
                    toast("블루투스가 사용이 중지 되었습니다.")
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                toast("블루투스 사용이 취소되었습니다.")
            }
        }
    }
}
