package com.example.backendsabkasewak.FormFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.backendsabkasewak.R
import com.example.backendsabkasewak.adapter.DetailsAdapter
import com.example.backendsabkasewak.databinding.FragmentLibraryBinding
import com.example.backendsabkasewak.databinding.FragmentSchoolBinding
import com.example.backendsabkasewak.db.DetailsItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SchoolFragment : Fragment() {
        private lateinit var binding: FragmentSchoolBinding
        private lateinit var db: DatabaseReference
        private lateinit var arrayList: ArrayList<DetailsItem>
        private lateinit var detailsAdapter: DetailsAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentSchoolBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            db = FirebaseDatabase.getInstance().reference.child("School Student Details")
            arrayList = arrayListOf()
            detailsAdapter = DetailsAdapter(arrayList, requireContext())
            binding.recyclerview.adapter = detailsAdapter
            binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
            fetchDetails()
        }

        private fun fetchDetails() {
            db.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("TestsFragment", "onDataChange called")

                    if (snapshot.exists()) {
                        arrayList.clear() // Clear the existing data before adding new data

                        for (data in snapshot.children) {
                            val name = data.child("name").value as? String
                            val mobile = data.child("phone No-").value as? String
                            val email = data.child("email").value as? String
                            val address = data.child("address").value as? String
                            val examCategories = data.child("schoolExam").value as? String
                            val currentDate = data.child("CurrentDate").value as? String
                            val image1 = data.child("uri1").value as? String

                            Log.d(
                                "Details",
                                "Name: $name, mobile: $mobile, email: $email, address: $address"
                            )

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
                    Log.e("TestsFragment", "onCancelled: $error")
                    // Handle onCancelled event
                }
            })
        }
    }

