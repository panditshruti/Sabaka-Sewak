package com.example.backendsabkasewak

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.backendsabkasewak.databinding.ActivityNoticeBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class Notice : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeBinding
    private lateinit var database: DatabaseReference

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            saveFileToDatabase(uri, "image")
        }

    private val pickPdf =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            saveFileToDatabase(uri, "pdf")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference().child("Notice")
        binding.imgchoose.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.pdfchoose.setOnClickListener {
            openFilePicker("application/pdf")
        }
        binding.okbtn.setOnClickListener {
            // Get values from the input fields
            val title = binding.tittle.text.toString()
            val link = binding.link.text.toString()

            // Check if the title and link are not empty
            if (title.isNotEmpty() && link.isNotEmpty()) {
                // Get the current date in the format "yyyy_MM_dd"
                val currentDate = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())

                // Create a unique key for the entry
                val entryKey = database.child(currentDate).push().key
                entryKey?.let {
                    // Save the title and link to Firebase Database with the current date as the key
                    // Assuming the type for title and link is "text"
                    database.child(currentDate).child(entryKey).child("title").setValue(title)
                    database.child(currentDate).child(entryKey).child("link").setValue(link)
                    Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()

                    // Clear the input fields after uploading
                    binding.tittle.text.clear()
                    binding.link.text.clear()
                }
            } else {
                // Show a toast message if either title or link is empty
                Toast.makeText(this, "Please enter title and link", Toast.LENGTH_SHORT).show()
            }
        }


        // Fetch data from Firebase Database
        fetchDataFromDatabase()
    }

    private fun openFilePicker(mimeType: String) {
        // Launch the file picker based on the MIME type
        when (mimeType) {
            "image/*" -> pickImage.launch(mimeType)
            "application/pdf" -> pickPdf.launch(mimeType)
            else -> throw IllegalArgumentException("Unsupported MIME type: $mimeType")
        }
    }

    private fun saveFileToDatabase(uri: Uri?, fileType: String) {
        // Check if the URI is not null
        uri?.let {
            // Get the current timestamp
            val timestamp = SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault()).format(Date())

            // Save the file URI to Firebase Database with the timestamp as the key
            val key = database.child(timestamp).key
            key?.let {
                database.child(key).child("type").setValue(fileType)
                database.child(key).child("uri").setValue(uri.toString())
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchDataFromDatabase() {
        // Add a ValueEventListener to listen for changes in the database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Iterate through the children of the "notice" node
                for (childSnapshot in snapshot.children) {
                    // Get the value (URI and type) from the database
                    val uriString = childSnapshot.child("uri").getValue(String::class.java)
                    val fileType = childSnapshot.child("type").getValue(String::class.java)

                    // Load the image or display the PDF based on the type
                    uriString?.let {
                        if (fileType == "image") {
                            Glide.with(this@Notice)
                                .load(Uri.parse(it))
                                .into(binding.imgview)
                        } else if (fileType == "pdf") {
                            // Handle displaying PDF as needed
                            // You can use a PDF viewer library or open the PDF in an external app
                        } else {
                            //djkfkd
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors, if any
                // For example, you can log the error message
                error.toException().printStackTrace()
            }
        })
    }

}
