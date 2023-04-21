package com.example.quizapp

data class QuestionModel(
    val question: String,
    val answerArrayList: ArrayList<String>,
    val correctOption: String,
    var userAnswer: Int,
    var bookMarkStatus: Int
)
