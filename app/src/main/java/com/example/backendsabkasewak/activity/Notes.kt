package com.example.backendsabkasewak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.backendsabkasewak.R
import com.example.backendsabkasewak.databinding.ActivityNotesBinding

class Notes : AppCompatActivity() {
    private lateinit var binding:ActivityNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}