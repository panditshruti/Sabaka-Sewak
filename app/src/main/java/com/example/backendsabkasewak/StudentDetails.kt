package com.example.backendsabkasewak

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backendsabkasewak.adapter.DetailsAdapter
import com.example.backendsabkasewak.databinding.ActivityStudentDetailsBinding
import com.example.backendsabkasewak.db.DetailsItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentDetails : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailsBinding
    private lateinit var db: DatabaseReference
    private lateinit var arrayList: ArrayList<DetailsItem>
    private lateinit var detailsAdapter: DetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference.child("Student Details")
        arrayList = arrayListOf()
        detailsAdapter = DetailsAdapter(arrayList, this@StudentDetails)
        binding.recyclerview.adapter = detailsAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        fetchDetails()
    }

    private fun fetchDetails() {
        db.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    arrayList.clear() // Clear the existing data before adding new data

                    for (data in snapshot.children) {
                        val name = data.child("name").value as? String
                        val mobile = data.child("phone No-").value as? String
                        val email = data.child("email").value as? String
                        val address = data.child("address").value as? String
                        val examCategories = data.child("schoolExam").value as? String
                        val currentDate = data.child("CurrentDate").value as? String
                        val image1 = data.child("uri").value as? String

                        Log.d("Details", "Name: $name, mobile: $mobile, email: $email, address: $address")

                        val detailsItem = DetailsItem(
                            "Name - $name!!",
                            "Mobile-no - $mobile!!",
                            "Email-Id - $email!!",
                            "Address - $address!!",
                            examCategories!!,
                            "Date - $currentDate!!",
                            image1!!
                        )
                        arrayList.add(detailsItem)
                    }

                    detailsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }
}
