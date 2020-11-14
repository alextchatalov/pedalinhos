package com.example.pedalinhos.database;

import android.content.Context;

import androidx.room.Room;

import com.example.pedalinhos.database.AppDatabase;

public class Connection {

    private static AppDatabase db;

    public static AppDatabase getConnection(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "database").allowMainThreadQueries().build();
        }

        return db;
    }
}
