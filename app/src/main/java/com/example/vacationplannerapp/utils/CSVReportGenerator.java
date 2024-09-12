package com.example.vacationplannerapp.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CSVReportGenerator {

    public static void handleActivityResult(Context context, Uri uri, List<String[]> reportData) {
        saveReportToUri(context, uri, reportData);
    }

    private static void saveReportToUri(Context context, Uri uri, List<String[]> data) {
        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

            CSVWriter csvWriter = new CSVWriter(writer);

            // Add title row
            String[] title = {"Vacation Report", "Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())};
            csvWriter.writeNext(title);

            // Add headers
            String[] headers = {"Vacation ID", "Name", "Start Date", "End Date", "Location"};
            csvWriter.writeNext(headers);

            // Add data rows
            csvWriter.writeAll(data);

            // Flush and close the writer
            csvWriter.flush();
            Toast.makeText(context, "Report generated successfully!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to generate report.", Toast.LENGTH_SHORT).show();
        }
    }
}
