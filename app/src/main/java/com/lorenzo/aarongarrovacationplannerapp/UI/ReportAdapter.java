package com.lorenzo.aarongarrovacationplannerapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lorenzo.aarongarrovacationplannerapp.R;
import com.lorenzo.aarongarrovacationplannerapp.entities.Vacation;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private final List<Vacation> vacationList;

    public ReportAdapter(List<Vacation> vacationList) {
        this.vacationList = vacationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vacation vacation = vacationList.get(position);
        holder.vacationName.setText(vacation.getVacationName());
        holder.vacationPrice.setText(String.format("$%.2f", vacation.getVacationPrice()));
        holder.vacationHotel.setText(vacation.getVacationHotel());
        holder.vacationStartDate.setText(vacation.getVacationStartDate());
        holder.vacationEndDate.setText(vacation.getVacationEndDate());
    }

    @Override
    public int getItemCount() {
        return vacationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vacationName;
        public TextView vacationPrice;
        public TextView vacationHotel;
        public TextView vacationStartDate;
        public TextView vacationEndDate;

        public ViewHolder(View itemView) {
            super(itemView);
            vacationName = itemView.findViewById(R.id.vacationName);
            vacationPrice = itemView.findViewById(R.id.vacationPrice);
            vacationHotel = itemView.findViewById(R.id.vacationHotel);
            vacationStartDate = itemView.findViewById(R.id.vacationStartDate);
            vacationEndDate = itemView.findViewById(R.id.vacationEndDate);
        }
    }
}
