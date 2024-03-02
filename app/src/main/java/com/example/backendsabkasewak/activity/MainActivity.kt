package com.example.backendsabkasewak.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.backendsabkasewak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notice.setOnClickListener {
            val intent = Intent(this, Notice::class.java)
            startActivity(intent)
        }

        binding.result.setOnClickListener {
            val intent = Intent(this, Result::class.java)
            startActivity(intent)
        }

        binding.studentdetails.setOnClickListener {
            val intent = Intent(this, StudentDetails::class.java)
            startActivity(intent)
        }

        binding.news.setOnClickListener {
            val intent = Intent(this, News::class.java)
            startActivity(intent)
        }
    }
}
