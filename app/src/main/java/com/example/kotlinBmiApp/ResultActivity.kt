package com.example.kotlinBmiApp

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class ResultActivity : AppCompatActivity() {

    var height   = 0.0
    var weight   = 0
    var age      = 0
    var bmi      = 0.0
    var lowBmi   = 0
    var highBmi  = 0
    var isMan: Boolean? = null

    lateinit var textViewBmi: TextView
    lateinit var textViewStatus: TextView
    lateinit var textViewOverWeight: TextView
    lateinit var textViewOverWeightText: TextView
    lateinit var textViewHealthyRange: TextView
    lateinit var textViewAgeRange: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        viewFinder()

        //get Extra
        height = intent.getDoubleExtra(MainActivity.HEIGHT_EXTRA, 0.0) / 100
        weight = intent.getIntExtra(MainActivity.WEIGHT_EXTRA, 0)
        isMan  = intent.getBooleanExtra(MainActivity.IS_MAN_EXTRA, false)
        age    = intent.getIntExtra(MainActivity.AGE_EXTRA, 0)

        Log.d("isMan" , isMan.toString())
        //calculate Bmi
        bmi = calculateBmi().roundDecimal(1)

        //set Bmi to textView
        textViewBmi.text = bmi.toString()

        /* set Status to textView
        Because this index is based on the women index and
        the healthy bmi of men is one unit more than women, we subtract the bmi of men minus one */
        textViewStatus.text = calculateBmiRes(if (isMan!!) bmi - 1 else bmi)

        /* The healthy range is 19 to 24 for women and 20 to 25 for men */
        if (isMan!!)
        {
            lowBmi = 20
            highBmi = 25
        }
        else
        {
            lowBmi = 19
            highBmi = 24
        }

        calculateOverOrUnderWeight()

        //set healthy range Based on user weight
        val healthyRange = " ${calculateLowerWeight(height).roundDecimal(1)} " +
                "تا ${calculateHigherWeight(height).roundDecimal(1)} کیلوگرم "
        textViewHealthyRange.text = healthyRange

        //set healthy bmi based on user age
        textViewAgeRange.text =  bmiAgeAppropriate().toString()

        //open more info activity
        val linearLayoutLink: LinearLayout = findViewById(R.id.link_linear)
        linearLayoutLink.setOnClickListener{
            val intent: Intent = Intent(applicationContext, MoreInfoActivity::class.java)
            startActivity(intent)
        }

        //recalculate button
        val textViewRecalculate: TextView = findViewById(R.id.re_calculate_button)
        textViewRecalculate.setOnClickListener{
            onBackPressed()
            finish()
        }

    }

    private fun calculateBmi(): Double =  ( weight.toDouble() / (height.pow(2)) )

    private fun calculateBmiRes(bmiParameter: Double): String
    {
        return when(bmiParameter)
        {
            in 0.0..15.0 -> "کمبود وزن بسیار شدید"
            in 15.1..16.0 -> " کمبود وزن شدید"
            in 16.1..19.0 -> "کمبود وزن"
            in 19.1..24.0 -> "محدوده وزن طبیعی و سالم"
            in 24.1..30.0 -> "اضافه وزن"
            in 30.1..35.0 -> "چاقی رده یک (متوسط)"
            in 35.1..40.0 -> " چاقی رده دو (شدید)"
            else            -> "چاقی رده سه (بسیار شدید)"
        }
    }

    private fun calculateOverOrUnderWeight() {

         when {
            bmi < lowBmi  -> {
                val text = ( ( calculateLowerWeight(height) - weight )
                    .roundDecimal(1) ).toString() +" " + "کیلوگرم"

                textViewOverWeightText.text = "میزان کمبود وزن :"
                textViewOverWeight.text = text
            }

            bmi > highBmi -> {
                val text =( ( weight - calculateHigherWeight(height) )
                    .roundDecimal(1) ).toString() +" "+ "کیلوگرم"
                textViewOverWeightText.text = "میزان اضافه وزن :"
                textViewOverWeight.text = text
            }

            //if weight is healthy, Hide textView related to overweight or underweight
            else -> {
                textViewOverWeightText.visibility = View.GONE
                textViewOverWeight.visibility     = View.GONE
            }
        }
    }

    private fun calculateLowerWeight(height: Double): Double = lowBmi * height.pow(2)

    private fun calculateHigherWeight(height: Double): Double = highBmi * height.pow(2)

    private fun bmiAgeAppropriate(): Int = when (age)
    {
        in 18..24 -> 22
        in 25..34 -> 23
        in 35..44 -> 24
        in 45..54 -> 25
        in 55..64 -> 26
        else      -> 27
    }
    private fun Double.roundDecimal(digit: Int): Double = "%.${digit}f".format(this).toDouble()

    private fun viewFinder()
    {
        textViewBmi            = findViewById(R.id.bmiTextView)
        textViewStatus         = findViewById(R.id.status_tv)
        textViewOverWeight     = findViewById(R.id.weight_status_tv)
        textViewOverWeightText = findViewById(R.id.overWeightText_tv)
        textViewHealthyRange   = findViewById(R.id.healthy_range_tv)
        textViewAgeRange       = findViewById(R.id.age_range_tv)
    }


}




/*        button.setOnClickListener {
            //getting w & h from editText
            weight = textViewWeight.text.toString().toDouble()
            height = textViewHeight.text.toString().toDouble() / 100

            //calculate BMI
            bmi = ( weight / (height.pow(2)) ).roundDecimal(1)

            //radio code...
            val radioId = radioGroup.checkedRadioButtonId

            if (radioId == R.id.man_RB)
            {
                isMale = true
                lowBmi = 20
                highBmi = 25
            }
            else
            {
                isMale = false
                lowBmi = 19
                highBmi = 24
            }

            //fill textView
            textView.text = "شاخص توده بدنی:\n $bmi"
            textViewRes.text   = " تحلیل: " + "\n" + calculateBmiRes(if (isMale) bmi - 1 else bmi)
            textViewRange.text = "  محدوده وزن سالم: \n
             ${calculateLowerWeight(height).roundDecimal(1)}  تا ${calculateHigherWeight(height).roundDecimal(1)} "

            textViewOverWeight.visibility = View.VISIBLE
            if(calculateOverWeight().length > 1)
            textViewOverWeight.text = calculateOverWeight()
            else textViewOverWeight.visibility = View.GONE




        }

    private fun calculateBmiRes(bmiParameter: Double): String
    {
        return when(bmiParameter)
        {
           in 0.0  .. 15.0 -> "کمبود وزن بسیار شدید"
           in 15.1 .. 16.0 -> " کمبود وزن شدید"
           in 16.1 .. 19.0 -> "کمبود وزن"
           in 19.1 .. 24.0 -> "محدوده وزن طبیعی و سالم"
           in 24.1 .. 30.0 -> "اضافه وزن"
           in 30.1 .. 35.0 -> "چاقی رده یک (چاقی متوسط)"
           in 35.1 .. 40.0 -> " چاقی رده دو (چاقی شدید)"
           else            -> "چاقی رده سه (چاقی بسیار شدید)"
        }
    }


    fun calculateLowerWeight(height: Double ): Double = lowBmi * height.pow(2)

    fun calculateHigherWeight(height: Double ): Double = highBmi * height.pow(2)

    private fun calculateOverWeight() : String {

        return when {
            bmi < lowBmi -> "مقدار کمبود وزن :" + ( ( calculateLowerWeight(height) - weight ).roundDecimal(1) ) .toString()
            bmi > highBmi -> "مقدار اضافه وزن :" + ( ( weight - calculateHigherWeight(height) ).roundDecimal(1) )  .toString()
            else -> ""
        }
    }

    fun Double.roundDecimal(digit: Int): Double = "%.${digit}f".format(this).toDouble()
*/