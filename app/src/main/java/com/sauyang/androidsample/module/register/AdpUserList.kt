package com.sauyang.androidsample.module.register;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri

import androidx.recyclerview.widget.RecyclerView
import com.sauyang.androidsample.R
import com.sauyang.androidsample.model.UserBean
import kotlinx.android.synthetic.main.item_user.view.*
import java.io.File


class AdpUserList(val context: Context, val items : ArrayList<UserBean>, val onItemClickListener : OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var file = File(items.get(position).imagePath)
        if(file.exists()) holder.ivUser?.setImageURI(file.toUri())

        holder.tvUserName?.text = items.get(position).username
        holder.tvFullName?.text = items.get(position).firstname + " " +items.get(position).lastname
        holder.tvEmail?.text = items.get(position).emailaddress

        holder.itemView.setOnClickListener( {
            onItemClickListener.onPress(position,items.get(position))
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    val ivUser = view.ivUser
    val tvUserName = view.tvUserName
    val tvFullName = view.tvFullName
    val tvEmail = view.tvEmail

}

interface OnItemClickListener{
    fun onPress( no : Int, item : UserBean)
}
