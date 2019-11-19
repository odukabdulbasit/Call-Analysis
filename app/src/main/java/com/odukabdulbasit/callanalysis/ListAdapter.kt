package com.odukabdulbasit.callanalysis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.row_layout.view.*

//Main activitydeki listeme burda oluştrduğum madel texviewi adapte ediyorum.
class ListAdapter(val context : Context, val list: ArrayList<CallDetails> ) : BaseAdapter() { //base adapterden kalıtım aldım.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //Oluşturmuş olduğum row.xml'i  bir view'e atıyorum.
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent,false)

        //view'e model içerik hazırlıyorum.
        view.list_item.text = "Caller name: ${list[position].name}\n "+
                "Phone number: ${list[position].number}\n "+
                "Call duration: ${list[position].duration}\n "+
                "Call type: ${list[position].type}\n "+
                "Call time: ${list[position].dayTime}"

        return  view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}