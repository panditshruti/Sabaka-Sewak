package com.example.backendsabkasewak.db

data class DetailsItem(

    val name: String,
    val mobbile: String,
    val emailId:String,
    val address:String,
    val examCategories:ArrayList<String>,
    val date:String,
    val image1:String ? = " "
)
