package com.example.vacationplannerapp.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplannerapp.R;
import com.example.vacationplannerapp.database.Repository;
import com.example.vacationplannerapp.entities.Vacation;
import com.example.vacationplannerapp.utils.CSVReportGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {

    public static final int REQUEST_CODE_CREATE_FILE = 1;
    private Repository repository;
    private VacationAdapter vacationAdapter;
    private List<Vacation> allVacations;

    // Define the permission launcher for creating documents
    private final ActivityResultLauncher<Intent> createDocumentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        new Thread(() -> {
                            List<Vacation> vacations = repository.getmAllVacations();
                            List<String[]> dataRows = new ArrayList<>();

                            // Add header row
                            String[] header = {"Vacation ID", "Name", "Start Date", "End Date", "Location"};
                            dataRows.add(header);

                            // Add data rows
                            for (Vacation vacation : vacations) {
                                String[] row = {
                                        String.valueOf(vacation.getVacationID()),
                                        vacation.getVacationName(),
                                        vacation.getVacationStartDate(),
                                        vacation.getVacationEndDate(),
                                        vacation.getVacationHotel()
                                };
                                dataRows.add(row);
                            }

                            CSVReportGenerator.handleActivityResult(VacationList.this, uri, dataRows);
                        }).start();
                    }
                }
            });

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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                generateReport();
            }
        });
    }

    private void generateReport() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "vacation_report.csv");
        createDocumentLauncher.launch(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) { // Permission request code
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateReport();
            } else {
                Toast.makeText(this, "Storage permission is required to generate report.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
