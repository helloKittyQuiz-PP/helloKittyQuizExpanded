package com.example.hellokittyquiz

data class Question (val questionNumber:Int, val textResId:Int, val answer: Boolean, var attempt: Boolean){
}