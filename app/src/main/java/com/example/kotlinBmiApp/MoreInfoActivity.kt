package com.example.kotlinBmiApp

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MoreInfoActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_info)

        val textViewTitle = findViewById<TextView>(R.id.toolbar_tv)
        textViewTitle.text = " جیست؟ " + "BMI"

        progressBar = findViewById(R.id.progressBar)

        val webView = findViewById<WebView>(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://fa.wikipedia.org/wiki/%D8%B4%D8%A7%D8%AE%D8%B5_%D8%AA%D9%88%D8%AF%D9%87_%D8%A8%D8%AF%D9%86%DB%8C")

    }

    inner class WebViewClient : android.webkit.WebViewClient() {

        // Load the URL
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        // ProgressBar will disappear once page is loaded
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }
    }
}