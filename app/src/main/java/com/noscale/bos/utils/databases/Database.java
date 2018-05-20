package com.noscale.bos.utils.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public abstract class Database {

    public static final String DATABASE_NAME_CONFIG = "bos_db";
    public static final int DATABASE_MODE_CONFIG = Context.MODE_PRIVATE;

    protected SQLiteDatabase database;

    public void setDatabase (SQLiteDatabase database) {
        this.database = database;
        createTable();
    }

    public SQLiteDatabase getDatabase () {
        return database;
    }

    protected String buildTableCreationQuery (String tableName, String[]... args) {
        String tableCreationQuery = null;

        if (args.length > 0) {
            int count = 0;
            tableCreationQuery = "CREATE TABLE IF NOT EXISTS "+tableName+" (";
            for (String[] arg:args) {
                String columnName = arg[0];
                String type = arg[1];
                String delimiter = (count < (args.length - 1))?",":"";
                tableCreationQuery += columnName+" "+type+delimiter;
                count++;
            }
            tableCreationQuery += ");";
        }

        return tableCreationQuery;
    }

    protected abstract void createTable ();
    public abstract long insert (Object item);
    public abstract long update (Object item, long id);
    public abstract List<?> getList ();
    public abstract Object cursorToObject (Cursor cursor);

}
