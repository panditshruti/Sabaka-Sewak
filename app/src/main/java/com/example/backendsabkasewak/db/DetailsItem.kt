package com.example.backendsabkasewak.db

data class DetailsItem(

    val name: String,
    val mobbile: String,
    val emailId:String,
    val address:String,
    val schoolExam:String,
    val sscExam:String,
    val railwayExam:String,
    val defence:String,
    val policeExam:String,
    val civilExam:String,
    val bankingExam:String,
    val entranceExam:String,
    val currentExam:String,
    val date:String,
    val image1:String ? = " "
)
