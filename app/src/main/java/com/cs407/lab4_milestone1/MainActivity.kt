package com.cs407.lab4_milestone1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MyActivity"
    private var job : Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private suspend fun mockFileDownloader() {
        val button = findViewById<Button>(R.id.start)
        val progBar = findViewById<ProgressBar>(R.id.progress_bar)
        val progressText = findViewById<TextView>(R.id.progressText)

        withContext(Dispatchers.Main) {
            button.text = getString(R.string.download)
            progBar.visibility = View.VISIBLE
            progressText.text = getString(R.string.progressText)
        }
        for (downloadProgress in 0 .. 100 step 10) {
            Log.d(TAG, getString(R.string.progressText).replace("{percentage}", downloadProgress.toString()))
            progBar.progress = downloadProgress
            withContext(Dispatchers.Main) {
                progressText.text = getString(R.string.progressText).replace("{percentage}", downloadProgress.toString())
                }
            delay(1000)
        }
        withContext(Dispatchers.Main) {
            button.text = getString(R.string.start)
            progressText.text = getString(R.string.completed)
            progBar.visibility = View.INVISIBLE
        }
    }

    fun startDownload(view: View) {
        if (job?.isActive == true) return
        job = CoroutineScope(Dispatchers.Default).launch {
            mockFileDownloader()
        }
    }

    fun stopDownload(view: View) {
        job?.cancel()

        val startButton = findViewById<Button>(R.id.start)
        val progressText = findViewById<TextView>(R.id.progressText)
        val progBar = findViewById<ProgressBar>(R.id.progress_bar)

        progBar.visibility = View.INVISIBLE
        progressText.text =  getString(R.string.cancelled)
        startButton.text = getString(R.string.start)
    }

}