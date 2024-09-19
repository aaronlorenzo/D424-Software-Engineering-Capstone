package com.lorenzo.aarongarrovacationplannerapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lorenzo.aarongarrovacationplannerapp.dao.ExcursionDAO;
import com.lorenzo.aarongarrovacationplannerapp.dao.VacationDAO;
import com.lorenzo.aarongarrovacationplannerapp.entities.Excursion;
import com.lorenzo.aarongarrovacationplannerapp.entities.Vacation;


@Database(entities = {Vacation.class, Excursion.class}, version=5, exportSchema = false) //change version number to update db
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            //.allowMainThreadQueries() //synchronous
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}