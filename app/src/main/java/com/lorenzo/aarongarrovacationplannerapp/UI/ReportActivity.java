package com.lorenzo.aarongarrovacationplannerapp.UI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lorenzo.aarongarrovacationplannerapp.R;
import com.lorenzo.aarongarrovacationplannerapp.database.Repository;
import com.lorenzo.aarongarrovacationplannerapp.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private Repository repository;
    private ReportAdapter reportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        // Enable the back button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewReport);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the current date and time and format it
        TextView reportDateTime = findViewById(R.id.reportDateTime);
        String currentDateTime = getCurrentDateTime();
        reportDateTime.setText("Date: " + currentDateTime);

        // Initialize repository and fetch data
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();

        // Initialize and set the adapter for RecyclerView
        reportAdapter = new ReportAdapter(allVacations);
        recyclerView.setAdapter(reportAdapter);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }



