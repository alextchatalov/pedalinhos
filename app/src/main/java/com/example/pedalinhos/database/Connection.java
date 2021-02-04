package com.example.pedalinhos.database;

import android.content.Context;
import androidx.room.Room;

public class Connection {

    private static AppDatabase db;

    private Connection() {
        // Do nothing
    }

    public static AppDatabase getConnection(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "database").allowMainThreadQueries().build();
        }

        return db;
    }
}
