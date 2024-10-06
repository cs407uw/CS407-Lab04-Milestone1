package com.cs407.lab4_milestone1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
        withContext(Dispatchers.Main) {
            button.text = getString(R.string.download)
            progBar.visibility = View.VISIBLE
        }
        for (downloadProgress in 0 .. 100 step 10) {
            Log.d(TAG, "Download progress $downloadProgress%")
            progBar.progress = downloadProgress
            delay(1000)
        }
        withContext(Dispatchers.Main) {
            progBar.visibility = View.INVISIBLE
        }
    }

    fun startDownload(view: View) {
        job = CoroutineScope(Dispatchers.Default).launch {
            val progBar = findViewById<ProgressBar>(R.id.progress_bar)
            mockFileDownloader()
        }
    }

    fun stopDownload(view: View) {
        job?.cancel()

        val startButton = findViewById<Button>(R.id.start)
        val progBar = findViewById<ProgressBar>(R.id.progress_bar)

        progBar.visibility = View.INVISIBLE
        startButton.text = getString(R.string.start)
    }

}