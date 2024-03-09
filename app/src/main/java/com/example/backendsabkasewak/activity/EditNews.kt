package com.example.backendsabkasewak.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backendsabkasewak.adapter.NoticeAdapterN
import com.example.backendsabkasewak.databinding.ActivityEditNewsBinding

import com.example.backendsabkasewak.db.NoticeItemSec
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditNews : AppCompatActivity() {

    private lateinit var binding: ActivityEditNewsBinding
    private lateinit var db: DatabaseReference
    private lateinit var arrayList: ArrayList<NoticeItemSec>
    private lateinit var noticeAdapter: NoticeAdapterN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference.child("News")
        arrayList = arrayListOf()

        noticeAdapter = NoticeAdapterN(arrayList, this@EditNews)
        binding.recyclerview.adapter = noticeAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        fetchNotice()

        binding.news.setOnClickListener {
            val intent = Intent(this, Notice::class.java)
            startActivity(intent)
        }
    }

    private fun fetchNotice() {
        db.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val title = data.child("title").getValue(String::class.java) ?: ""
                        val link = data.child("link").getValue(String::class.java) ?: ""
                        val img = data.child("imageUrl").getValue(String::class.java) ?: ""
                        val pdf = data.child("pdfUrl").getValue(String::class.java) ?: ""
                        val date = data.child("date").getValue(String::class.java) ?: ""
                        val prise = data.child("prise").getValue(String::class.java) ?: ""

                        Log.d("Notice", "Title: $title, Link: $link, Image: $img")

                        val key = data.key ?: ""
                        arrayList.add(NoticeItemSec(img, pdf, title, link,prise, date, key))
                    }

                    noticeAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })

        noticeAdapter.setOnItemClickListener(object : NoticeAdapterN.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                if (position >= 0 && position < arrayList.size) {
                    val selectedItem = arrayList[position]
                    val selectedKey = selectedItem.key

                    if (!selectedKey.isNullOrBlank()) {
                        db.child(selectedKey).removeValue()
                    }
                }
            }


        })
    }
}