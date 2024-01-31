package com.example.backendsabkasewak

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.backendsabkasewak.databinding.ActivityNoticeBinding
import com.example.backendsabkasewak.db.NoticeItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class Notice : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeBinding
    private lateinit var database: DatabaseReference
    private lateinit var imageuri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference().child("Notice")

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

    fun openGalleryForImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK)
            imageuri = data?.data!!
        binding.imgview.setImageURI(imageuri)
    }

    fun openPdfFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        startActivityForResult(intent, 200)
    }

    fun submitData() {
        val title = binding.tittle.text.toString()
        val link = binding.link.text.toString()
        val pdfUri = binding.pdfchoose.tag?.toString() ?: ""

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading file...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        if (::imageuri.isInitialized) { // Check if imageuri is initialized (image is selected)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageReference = FirebaseStorage.getInstance().reference.child("images/$timestamp.jpg")

            storageReference.putFile(imageuri)
                .addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, get the download URL
                    storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Save image URL to Realtime Database
                        val currentDate = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())
                        val entryKey = database.child(currentDate).push().key

                        entryKey?.let {
                            val noticeItem = NoticeItem(title, link, downloadUri.toString(), pdfUri)
                            database.child(currentDate).child(entryKey).setValue(noticeItem)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@Notice,
                                        "Data Uploaded Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.tittle.text.clear()
                                    binding.link.text.clear()
                                    binding.imgview.setImageDrawable(null)
                                    progressDialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@Notice,
                                        "Data Upload Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    progressDialog.dismiss()
                                }
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle the error
                    Toast.makeText(this@Notice, "Image Upload Failed", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
        } else { // Image is not selected, upload text and link only
            val currentDate = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())
            val entryKey = database.child(currentDate).push().key

            entryKey?.let {
                val noticeItem = NoticeItem(title, link, "", pdfUri)
                database.child(currentDate).child(entryKey).setValue(noticeItem)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@Notice,
                            "Data Uploaded Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.tittle.text.clear()
                        binding.link.text.clear()
                        progressDialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@Notice,
                            "Data Upload Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.dismiss()
                    }
            }
        }
    }


}
