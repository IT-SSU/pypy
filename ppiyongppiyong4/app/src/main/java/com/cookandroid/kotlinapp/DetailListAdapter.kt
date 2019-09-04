package com.cookandroid.kotlinapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject
import android.view.ContextMenu
import android.text.method.TextKeyListener.clear
import android.arch.lifecycle.ViewModel





class DetailListAdapter(val context: Context, val userDetailList: ArrayList<UserDetail>, val itemClick: (UserDetail) -> Unit):RecyclerView.Adapter<DetailListAdapter.Holder>(){
/* (1) Adapter의 파라미터에 람다식 itemClick을 넣는다. */

    fun updateData() {
        notifyDataSetChanged()
    }

    ////////////////////////////리싸이러뷰 안에 버트 이벤트 end

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder{
        val view = LayoutInflater.from(context).inflate(R.layout.detail_list_item, parent, false)
        /* (4) Holder의 파라미터가 하나 더 추가됐으므로, 이곳에도 추가해준다. */
        return Holder(view,itemClick)
    }

    override fun getItemCount(): Int {
        return userDetailList.size
    }

    override fun onBindViewHolder(holder: Holder?, position: Int){
        holder?.bind(userDetailList[position], context)

    }



        //item 클릭 이벤트
    inner class Holder(itemView: View?,itemClick: (UserDetail) -> Unit) : RecyclerView.ViewHolder(itemView) {/* (2) Holder에서 클릭에 대한 처리를 할 것이므로, Holder의 파라미터에 람다식 itemClick을 넣는다. */
        val disease = itemView?.findViewById<TextView>(R.id.disease)
        val detail = itemView?.findViewById<TextView>(R.id.detail)


        fun bind (userDetail:UserDetail, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */

            disease?.text = userDetail.diseaseName
            detail?.text = userDetail.detailContent //db변경및 받아 오는값 변경

            itemView.setOnClickListener {itemClick(userDetail)}
            /* (3) itemView가 클릭됐을 때 처리할 일을 itemClick으로 설정한다.
            (Dog) -> Unit 에 대한 함수는 나중에 MainActivity.kt 클래스에서 작성한다. */
        }
    }
}

