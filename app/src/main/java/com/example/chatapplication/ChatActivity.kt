package com.example.chatapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var massageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var massageadapters: Massageadapters
    private lateinit var massageList: ArrayList<Massage>
    private lateinit var mDbref: DatabaseReference

    var receiverRoom: String? = null
    var senderRoom: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("uid")
        val senderuid = FirebaseAuth.getInstance().currentUser?.uid
        mDbref = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiveruid + senderuid

        receiverRoom = senderuid + receiveruid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        massageBox = findViewById(R.id.massagebox)
        sendButton = findViewById(R.id.sendbutton)
        massageList = ArrayList()
        massageadapters = Massageadapters(this, massageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = massageadapters

        mDbref.child("chats").child(senderRoom!!).child("massagers")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    massageList.clear()

                    for(postSnapshot in snapshot.children){
                        val massage =  postSnapshot.getValue(Massage::class.java)
                        massageList.add(massage!!)
                    }
                    massageadapters.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        sendButton.setOnClickListener{
            val massage = massageBox.text.toString()
            val massageObject = Massage(massage,senderuid)

            mDbref.child("chats").child(senderRoom!!).child("massages").push()
                .setValue(massageObject).addOnSuccessListener {
                    mDbref.child("chats").child(receiverRoom!!).child("massages").push()
                        .setValue(massageObject)
                }
            massageBox.setText("")
        }
    }
}