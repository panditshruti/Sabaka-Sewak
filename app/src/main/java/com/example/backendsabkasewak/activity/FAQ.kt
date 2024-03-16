package com.example.backendsabkasewak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.backendsabkasewak.R
import com.example.backendsabkasewak.databinding.ActivityFaqBinding

class FAQ : AppCompatActivity() {
    private lateinit var binding:ActivityFaqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}