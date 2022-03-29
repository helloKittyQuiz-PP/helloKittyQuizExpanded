package com.example.hellokittyquiz

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes

private const val TAG = "QuizActivity"

class QuizActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton:Button
    private lateinit var nextButton:Button
    private lateinit var previousButton:Button
    private lateinit var oneButton:Button
    private lateinit var twoButton:Button
    private lateinit var threeButton:Button
    private lateinit var fourButton:Button
    private lateinit var backButton: Button

    private lateinit var questionTextView: TextView
    private lateinit var questionNumberView: TextView
    private lateinit var timerTextView: EditText

    private var scoreCount = 0.0
    private var attemptCount = 0

    private val questionBank = listOf(
        Question(R.string.one, R.string.kitty1, true, false),
        Question(R.string.two, R.string.kitty2, false, false),
        Question(R.string.three, R.string.kitty3, true, false),
        Question(R.string.four, R.string.kitty4, true, false))

    private var currentIndex = 0

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_quiz)

        trueButton=findViewById(R.id.true_button)
        falseButton=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        previousButton=findViewById(R.id.previous_button)
        oneButton=findViewById(R.id.button_1)
        twoButton=findViewById(R.id.button_2)
        threeButton=findViewById(R.id.button_3)
        fourButton=findViewById(R.id.button_4)
        backButton = findViewById(R.id.back_button)

        questionTextView=findViewById(R.id.questionTextView)
        questionNumberView=findViewById(R.id.questionNumberView)
        timerTextView = findViewById(R.id.timerTextView)

        timer = object: CountDownTimer(181000, 1000){
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val totalSeconds = millisUntilFinished / 1000
                val minutes = (totalSeconds / 60 )
                val remainingSeconds = totalSeconds%60

                if (remainingSeconds<10){
                    timerTextView.setText("$minutes:0$remainingSeconds")
                }else{
                    timerTextView.setText("$minutes:$remainingSeconds")
                }
            }

            override fun onFinish() {
                val finalScore = (scoreCount/questionBank.size)*100.0
                timerTextView.setText("Time's Up!\n\nYour Score is: $finalScore")
                timerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                trueButton.isEnabled = false
                falseButton.isEnabled = false
            }
        }

        timer.start()

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        updateQuestions()

        oneButton.setOnClickListener {
            currentIndex = 0
            updateQuestions()
        }

        twoButton.setOnClickListener {
            currentIndex = 1
            updateQuestions()
        }

        threeButton.setOnClickListener {
            currentIndex = 2
            updateQuestions()
        }

        fourButton.setOnClickListener {
            currentIndex = 3
            updateQuestions()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestions()
        }

        previousButton.setOnClickListener {
            currentIndex = (currentIndex - 1)
            if (currentIndex <0){
                currentIndex=questionBank.size-1
            }
            updateQuestions()
        }

        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestions()
        }

        updateQuestions()
    }

    private fun updateQuestions(){
        val id = questionBank[currentIndex].textResId
        val num = questionBank[currentIndex].questionNumber
        questionTextView.setText(id)
        questionNumberView.setText(num)

        if (questionBank[currentIndex].attempt) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }
        else{
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        crossAnswer(currentIndex)
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        questionBank[currentIndex].attempt=true

        trueButton.isEnabled = false
        falseButton.isEnabled = false

        if (userAnswer == correctAnswer){
            scoreCount++
        }
        attemptCount++

        if (attemptCount == questionBank.size){
            val finalScore = (scoreCount/attemptCount)*100.0
            timer.cancel()
            timerTextView.setText("Quiz Completed Successfully!\n\nYour Score is: $finalScore")
            timerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            val toast = Toast.makeText(this,
                "Your score is $finalScore",
                Toast.LENGTH_SHORT)
            toast.show()
            backButton.isClickable = true
            backButton.visibility = View.VISIBLE

            backButton.setOnClickListener{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun crossAnswer(answerNumber: Int) {
        when(answerNumber) {
            0 -> {
                oneButton.setBackgroundColor(Color.LTGRAY)
            }
            1 -> {
                twoButton.setBackgroundColor(Color.LTGRAY)
            }
            2 -> {
                threeButton.setBackgroundColor(Color.LTGRAY)
            }
            3 -> {
                fourButton.setBackgroundColor(Color.LTGRAY)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}

