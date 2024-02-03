package com.example.backendsabkasewak

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
    private lateinit var imageUri: Uri
    private lateinit var pdfUri: Uri

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
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            binding.imgview.setImageURI(imageUri)
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            pdfUri = data?.data!!
            binding.pdfchoose.tag = pdfUri.toString()
        }
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
        val imageUriString = binding.imgview.tag?.toString() ?: ""
        val pdfUriString = binding.pdfchoose.tag?.toString() ?: ""

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading file...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        // Check if imageUri is initialized and not equal to the default Uri
        if (::imageUri.isInitialized && imageUri != Uri.EMPTY) {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageStorageReference =
                FirebaseStorage.getInstance().reference.child("images/$timestamp.jpg")

            // Upload the image to Firebase Storage
            imageStorageReference.putFile(imageUri)
                .addOnSuccessListener { imageTaskSnapshot ->
                    // Image uploaded successfully, get the download URL
                    imageStorageReference.downloadUrl.addOnSuccessListener { imageDownloadUri ->
                        // Save image URL to Realtime Database
                        saveToDatabase(title, link, imageDownloadUri.toString(), pdfUriString, progressDialog)
                    }
                }
                .addOnFailureListener {
                    // Handle the error
                    Toast.makeText(this@Notice, "Image Upload Failed", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
        } else {
            // Image is not explicitly selected, upload text and link only
            saveToDatabase(title, link, imageUriString, pdfUriString, progressDialog)
        }
    }

    private fun saveToDatabase(title: String, link: String, imageUri: String, pdfUri: String, progressDialog: ProgressDialog) {
        val currentDate = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())
        val entryKey = database.push().key

        entryKey?.let {
            if (pdfUri.isNotEmpty()) {
                // If PDF is selected, upload it to Firebase Storage
                val pdfTimestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val pdfStorageReference =
                    FirebaseStorage.getInstance().reference.child("pdfs/$pdfTimestamp.pdf")

                pdfStorageReference.putFile(Uri.parse(pdfUri))
                    .addOnSuccessListener { pdfTaskSnapshot ->
                        // PDF uploaded successfully, get the download URL
                        pdfStorageReference.downloadUrl.addOnSuccessListener { pdfDownloadUri ->
                            // Save PDF download URL to Realtime Database along with other data
                            val noticeItem = NoticeItem(title, link, imageUri, pdfDownloadUri.toString(), currentDate)
                            database.child(entryKey).setValue(noticeItem)
                                .addOnSuccessListener {
                                    Toast.makeText(this@Notice, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()

                                    // Clear form fields after successful upload
                                    binding.tittle.text.clear()
                                    binding.link.text.clear()

                                    // Clear image view only if a new image is selected
                                    if (::imageUri.isInitialized ) {
                                        binding.imgview.setImageResource(R.drawable.gallary)
                                        binding.imgview.tag = null
                                    }

                                    binding.pdfchoose.tag = null

                                    progressDialog.dismiss()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@Notice, "Data Upload Failed", Toast.LENGTH_SHORT).show()
                                    progressDialog.dismiss()
                                }
                        }
                    }
                    .addOnFailureListener {
                        // Handle the error
                        Toast.makeText(this@Notice, "PDF Upload Failed", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
            } else {
                // If no PDF is selected, save other data without PDF URL
                val noticeItem = NoticeItem(title, link, imageUri, "", currentDate)
                database.child(entryKey).setValue(noticeItem)
                    .addOnSuccessListener {
                        Toast.makeText(this@Notice, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()

                        // Clear form fields after successful upload
                        binding.tittle.text.clear()
                        binding.link.text.clear()

                        // Clear image view only if a new image is selected
                        if (::imageUri.isInitialized ) {
                            binding.imgview.setImageResource(R.drawable.gallary)
                            binding.imgview.tag = null
                        }

                        binding.pdfchoose.tag = null

                        progressDialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@Notice, "Data Upload Failed", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
            }
        }
    }

}
