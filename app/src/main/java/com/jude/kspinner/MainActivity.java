package com.jude.kspinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.spinnerlibrary.KSpinner;

public class MainActivity extends AppCompatActivity
{
    KSpinner spinner, spinner1, spinner2, spinner3;
    TextView spinnerText, itemSelection, spinnerOpenClose;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (KSpinner) findViewById(R.id.spinner);
        spinner1 = (KSpinner) findViewById(R.id.spinner1);
        spinner2 = (KSpinner) findViewById(R.id.spinner2);
        spinner3 = (KSpinner) findViewById(R.id.spinner3);
        button = (Button) findViewById(R.id.button);
        spinnerText = (TextView) findViewById(R.id.spinnerText);
        itemSelection = (TextView) findViewById(R.id.itemSelection);
        spinnerOpenClose = (TextView) findViewById(R.id.spinnerOpenClose);
        String[] cityStringList = {"Select", "Mumbai", "Delhi", "Kolkata", "Chennai"};

        spinner.setAdapter(cityStringList);
        spinner1.setAdapter(cityStringList);
        spinner2.setAdapter(cityStringList);
        spinner2.setSpinnerText("Cochin");
        spinner3.setAdapter(cityStringList);

        spinner3.setOnItemSelectedListener(new KSpinner.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position)
            {
                spinnerText.setText("Item Selected : "+(String) spinner3.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(boolean isNothingSelected)
            {
                itemSelection.setText("Is Item Selected : "+(isNothingSelected ? "No" : "Yes"));
            }

            @Override
            public void onOpenOrClose(boolean isOpened)
            {
                spinnerOpenClose.setText("Is spinner open/close : "+(isOpened ? "OPENED" : "CLOSED"));
            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, spinner2.getDropDownText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
