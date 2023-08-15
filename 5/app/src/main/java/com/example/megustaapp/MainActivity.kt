package com.example.megustaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.msspa_megusta_library.MeGustaManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val megustaManager = MeGustaManager(this)
        findViewById<Button>(R.id.bt_launch).setOnClickListener {
            megustaManager.launchJavascript(onsuccess = {
                 val result = it
            }, onError = {})
        }
    }


}