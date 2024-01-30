// Notice.kt
package com.example.backendsabkasewak

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.backendsabkasewak.databinding.ActivityNoticeBinding
import com.example.backendsabkasewak.db.NoticeItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Result : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeBinding
    private lateinit var database: DatabaseReference

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                saveFileToDatabase(uri, "image")
            }
        }

    private val pickPdf =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                saveFileToDatabase(uri, "pdf")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference().child("Result")

        binding.imgchoose.setOnClickListener {
            openGalleryForImage()
        }
        binding.pdfchoose.setOnClickListener {
            openPdfFile()
        }

        binding.submit.setOnClickListener {
            submitData()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    private fun openPdfFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        pickPdf.launch(intent)
    }

    private fun submitData() {
        val title = binding.tittle.text.toString()
        val link = binding.link.text.toString()
        val imageUri = binding.imgview.tag?.toString() ?: ""
        val pdfUri = binding.pdfchoose.tag?.toString() ?: ""

//        if (title.isNotEmpty() && link.isNotEmpty() && imageUri.isNotEmpty() && pdfUri.isNotEmpty()) {
        val currentDate = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())
        val entryKey = database.child(currentDate).push().key

        entryKey?.let {
            val noticeItem = NoticeItem(title,link,imageUri,pdfUri)
            database.child(currentDate).child(entryKey).setValue(noticeItem)

            Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()

            // Clear input fields
            binding.tittle.text.clear()
            binding.link.text.clear()
            binding.imgview.setImageDrawable(null)

        }
//        } else {
//            // Show a toast message if any field is empty
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun saveFileToDatabase(uri: Uri?, fileType: String) {
        uri?.let {
//            val timestamp = SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault()).format(Date())
//            val key = database.child(timestamp).key
//            key?.let {
//                val noticeItem = NoticeItem("", "", "", "")
//                database.child(key).setValue(noticeItem)


            if (fileType == "image") {
                Glide.with(this@Result)
                    .load(uri)
                    .into(binding.imgview)
                binding.imgview.tag = uri.toString()
            } else if (fileType == "pdf") {
                // Handle displaying PDF as needed
                // You may use a library or open the PDF in an external app
                binding.pdfchoose.tag = uri.toString()
            }

        }
    }
}

