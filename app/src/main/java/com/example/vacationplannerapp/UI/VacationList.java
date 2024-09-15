package com.example.vacationplannerapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplannerapp.R;
import com.example.vacationplannerapp.database.Repository;
import com.example.vacationplannerapp.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {

    private Repository repository;
    private VacationAdapter vacationAdapter;
    private List<Vacation> allVacations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        repository = new Repository(getApplication());
        setupRecyclerView();

        Button generateReportButton = findViewById(R.id.generateReportButton);
        generateReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, ReportActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search Vacations...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterVacations(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVacations(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshVacationList();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.vacationRecyclerView);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshVacationList();
    }

    private void refreshVacationList() {
        allVacations = repository.getmAllVacations();
        vacationAdapter.setVacations(allVacations);
        vacationAdapter.notifyDataSetChanged();
    }

    private void filterVacations(String query) {
        List<Vacation> filteredVacations = new ArrayList<>();
        for (Vacation vacation : allVacations) {
            if (vacation.getVacationName().toLowerCase().contains(query.toLowerCase())) {
                filteredVacations.add(vacation);
            }
        }
        vacationAdapter.setVacations(filteredVacations);
        vacationAdapter.notifyDataSetChanged();
    }
}
