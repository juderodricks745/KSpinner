package com.jude.kspinner

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var cities = arrayOf("Select", "Mumbai", "Delhi", "Kolkata", "Chennai")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner3.setAdapter(cities)
        spinner3.setListener {
            onItemSelection { parent, view, position ->
                Log.i("Log", "Value => ${parent.getItemAtPosition(position) as String}")
            }
        }
    }
}
