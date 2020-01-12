package com.batch.labtimecard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_person_add.setOnClickListener {
            openEditPersonScreen()
        }
    }

    private fun openEditPersonScreen() {
        Toast.makeText(applicationContext, "押されたよ", Toast.LENGTH_SHORT).show()
    }
}
