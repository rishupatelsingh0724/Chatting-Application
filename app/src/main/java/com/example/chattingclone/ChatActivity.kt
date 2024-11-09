package com.example.chattingclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingclone.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var messageAdapter: MessageAdapter
    lateinit var messsageList: ArrayList<Message>
    lateinit var dbReff: DatabaseReference
    var receiverRoom: String? = null
    var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        dbReff = FirebaseDatabase.getInstance().getReference()

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
//        senderRoom = receiverRoom + senderUid
//        receiverRoom = senderUid + receiverUid

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid


        supportActionBar?.title = name
        messsageList = ArrayList()
        messageAdapter = MessageAdapter(this, messsageList)
       binding.chatRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.chatRecyclerView.adapter=messageAdapter


        dbReff.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messsageList.clear()
                    snapshot.children.forEach {
                        val message=it.getValue(Message::class.java)
                        messsageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "This cancel", Toast.LENGTH_SHORT).show()
                }
            })


//        binding.sendMessageImg.setOnClickListener {
//            val message = binding.messageBox.text.toString().trim()
//            val messageObject = Message(message, senderUid!!)
//
//            dbReff.child("chats").child(senderRoom!!).child("messages").push()
//                .setValue(messageObject).addOnSuccessListener {
//                    dbReff.child("chats").child(receiverRoom!!).child("messages").push()
//                        .setValue(messageObject)
//                }
//            binding.messageBox.setText("")
//        }


        binding.sendMessageImg.setOnClickListener {
            val message = binding.messageBox.text.toString().trim()

            // Ensure message is not empty
            if (message.isNotEmpty()) {
                val senderUid = FirebaseAuth.getInstance().currentUser?.uid
                if (senderUid != null) {
                    val messageObject = Message(message, senderUid)

                    dbReff.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            dbReff.child("chats").child(receiverRoom!!).child("messages").push()
                                .setValue(messageObject)
                        }
                    binding.messageBox.setText("")
                } else {
                    Toast.makeText(this@ChatActivity, "Error: Sender UID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@ChatActivity, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

    }
}