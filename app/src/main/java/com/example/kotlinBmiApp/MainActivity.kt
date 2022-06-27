package com.example.kotlinBmiApp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.aagito.imageradiobutton.RadioImageGroup
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams


class MainActivity : AppCompatActivity() , View.OnClickListener
{
    companion object{
        private var INCREASE_WEIGHT = "+W"
        private var DECREASE_WEIGHT = "-W"
        private var INCREASE_AGE    = "+A"
        private var DECREASE_AGE    = "-A"
        const val IS_MAN_EXTRA ="isMan"
        const val HEIGHT_EXTRA ="height"
        const val WEIGHT_EXTRA ="weight"
        const val AGE_EXTRA    ="age"

    }
    var weight = 60
    var height: Double = 170.0
    var bmi = 0.0
    var isMale: Boolean? = null
    var lowBmi = 0
    var highBmi = 0
    var age = 25

    lateinit var toolbar: Toolbar
    lateinit var radioImageGroup: RadioImageGroup
    lateinit var seekBar: IndicatorSeekBar
    lateinit var textViewHeight: TextView
    lateinit var imageViewWeightAdd: ImageView
    lateinit var imageViewWeightMinus: ImageView
    lateinit var textViewWeight: TextView
    lateinit var textViewAge: TextView
    lateinit var imageViewAgeAdd : ImageView
    lateinit var imageViewAgeMinus : ImageView
    lateinit var layoutNotice: LinearLayout
    lateinit var buttonCalculate: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewFinder()

        //set Toolbar
        setSupportActionBar(toolbar)

        //radio button...
        radioImageGroup.setOnCheckedChangeListener(object :
            RadioImageGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(
                radioGroup: View,
                radioButton: View?,
                isChecked: Boolean,
                checkedId: Int
            ) {
                manageSound(R.raw.radio_sound)
                if (R.id.male == checkedId) {
                    isMale = true
                }
                else  isMale = false
            }
        })

        //seek bar...
        seekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                textViewHeight.text = seekParams.progress.toString()
                manageSound(R.raw.sound6)
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBars: IndicatorSeekBar?) {
                height = seekBars!!.progress.toDouble()
            }
        }

        //on long button code
        //weight
        onLongPress(imageViewWeightAdd, INCREASE_WEIGHT)
        onLongPress(imageViewWeightMinus, DECREASE_WEIGHT)
        //age
        onLongPress(imageViewAgeAdd, INCREASE_AGE)
        onLongPress(imageViewAgeMinus, DECREASE_AGE)

    }

    private fun onLongPress(view: View, str: String)
    {
        view.setOnLongClickListener {
            val handler = Handler(Looper.myLooper()!!)
            val runnable : Runnable = object : Runnable {

                override fun run() {
                    handler.removeCallbacks(this)
                    if (view.isPressed) {

                       when(str)
                       {
                           INCREASE_WEIGHT -> increaseWeight()
                           DECREASE_WEIGHT -> decreaseWeight()
                           INCREASE_AGE -> increaseAge()
                           DECREASE_AGE -> decreaseAge()

                       }
                        handler.postDelayed(this, 40)
                    }
                }
            }
            handler.postDelayed(runnable, 0)
            true
        }

    }

    private fun increaseWeight()
    {
        if(weight < 130)
        {
            weight++
            textViewWeight.text = weight.toString()
           manageSound(R.raw.sound5)
        }
    }

    private fun decreaseWeight()
    {
        if(weight > 30) {
            weight--
            manageSound(R.raw.sound5)
            textViewWeight.text = weight.toString()
        }
    }


    private fun increaseAge()
    {
        layoutNotice.visibility = View.INVISIBLE
        if(age < 100) {
            age++
            textViewAge.text = age.toString()
            manageSound(R.raw.sound5)
        }
    }

    private fun decreaseAge()
    {
        if(age > 18) {
            age--
            textViewAge.text = age.toString()
            manageSound(R.raw.sound5)
        }
        if (age == 18)
            layoutNotice.visibility = View.VISIBLE
        else
            layoutNotice.visibility = View.INVISIBLE
    }


    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.weight_add -> increaseWeight()
            R.id.weight_minus -> decreaseWeight()
            R.id.age_add -> increaseAge()
            R.id.age_minus -> decreaseAge()

            //calculate button
            R.id.calculate_button -> {

                val intent: Intent = Intent(applicationContext, ResultActivity::class.java)
                intent.putExtra(IS_MAN_EXTRA, isMale)
                intent.putExtra(HEIGHT_EXTRA, height)
                intent.putExtra(WEIGHT_EXTRA, weight)
                intent.putExtra(AGE_EXTRA, age)
                startActivity(intent)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.aboutUs)
        {
            //do something
            val intent: Intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    //play what sound we want
    private fun manageSound(rawPath: Int)
    {
        var mediaPlayer: MediaPlayer?
        mediaPlayer = MediaPlayer.create(applicationContext, rawPath)
        mediaPlayer.setOnCompletionListener { mp ->
            mp.reset()
            mp.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    private fun viewFinder()
    {
        toolbar                = findViewById(R.id.main_toolbar)
        radioImageGroup        = findViewById(R.id.radioImageGroup)
        seekBar                = findViewById(R.id.seekBar)
        textViewHeight         = findViewById(R.id.height_tv)
        imageViewWeightAdd     = findViewById(R.id.weight_add)
        imageViewWeightMinus   = findViewById(R.id.weight_minus)
        textViewWeight         = findViewById(R.id.weight_tv)
        textViewAge            = findViewById(R.id.age_tv)
        imageViewAgeAdd        = findViewById(R.id.age_add)
        imageViewAgeMinus      = findViewById(R.id.age_minus)
        layoutNotice           = findViewById(R.id.warning_layout)
        buttonCalculate        = findViewById(R.id.calculate_button)

        //onclick
        imageViewWeightAdd.setOnClickListener(this)
        imageViewWeightMinus.setOnClickListener(this)
        imageViewAgeAdd.setOnClickListener(this)
        imageViewAgeMinus.setOnClickListener(this)
        buttonCalculate.setOnClickListener(this)
    }

}




