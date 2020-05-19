package com.jude.kspinner

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var names = listOf("Select", "Jude", "Francis", "Brian")
    private var cities = arrayOf("Select", "Mumbai", "Delhi", "Kolkata", "Chennai")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner.setAdapter(names)
        spinner.setListener {
            onItemSelection { _, _, position ->
                Log.i("Log", "Value => ${spinner.getItemAtPosition(position)}")
            }
        }
    }
}
