package com.example.vacationplannerapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacationplannerapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VacationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // FloatingActionButton click listener
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
    }

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    // Handle item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.vacationsave) {
            saveVacation();
            return true;
        } else if (itemId == R.id.vacationdelete) {
            deleteVacation();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    // Method to save vacation
    private void saveVacation() {
        // Add your save logic here (e.g., saving to database)
        Toast.makeText(this, "Vacation saved!", Toast.LENGTH_SHORT).show();
    }

    // Method to delete vacation
    private void deleteVacation() {
        // Add your delete logic here (e.g., removing from database)
        Toast.makeText(this, "Vacation deleted!", Toast.LENGTH_SHORT).show();
    }
}
