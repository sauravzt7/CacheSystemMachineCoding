package com.machinecoding.factory;

import com.machinecoding.backend.SQLiteStorageBackend;
import com.machinecoding.backend.StorageBackend;

import static com.machinecoding.constants.Constants.DB_URL;
import static com.machinecoding.constants.Constants.TABLE_NAME;

public class StorageBackendFactory {

    public static StorageBackend getSQLiteStorageBackend(){
        return new SQLiteStorageBackend(DB_URL, TABLE_NAME);
    }

}
