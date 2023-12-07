package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class Massageadapters(val context: Context, val massageList: ArrayList<Massage>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1;
    val ITEM_SEND = 2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       if (viewType == 1){
           val view: View = LayoutInflater.from(context).inflate(R.layout.received, parent, false)
           return ReceivedViewHolder(view)
       }else{
           val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
           return SendViewHolder(view)
       }
    }

    override fun getItemCount(): Int {
      return massageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if(holder.javaClass == SendViewHolder::class.java){

           val currentMassage = massageList[position]
           val viewHolder = holder as SendViewHolder
           holder.sendMassage.text = currentMassage.massage
       } else {
           val currentMassage = massageList[position]
           val viewHolder = holder as ReceivedViewHolder
           holder.receivedMassage.text = currentMassage.massage
       }
    }

    override fun getItemViewType(position: Int): Int {
        var currentMassage = massageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMassage.senderId)){
            return ITEM_SEND
        }else{
            return ITEM_RECEIVE
        }
    }

    class SendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sendMassage = itemView.findViewById<TextView>(R.id.txt_send_massage)
    }

    class ReceivedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receivedMassage = itemView.findViewById<TextView>(R.id.txt_received_massage)
    }
}