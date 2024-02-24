package com.example.backendsabkasewak.db

data class NoticeItemSec(
    val imageUrl: String? = "",
    val pdf: String? = "",
    val title: String? = "",
    val link: String? = "",
    val date: String,
    val key: String = ""  // Add a key property for Firebase unique identifier
)
