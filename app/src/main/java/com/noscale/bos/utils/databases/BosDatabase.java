package com.noscale.bos.utils.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.noscale.bos.utils.Instance;
import com.noscale.bos.utils.databases.tables.RequestMessageTable;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class BosDatabase extends Instance {

    private SQLiteDatabase database;
    private RequestMessageTable requestMessageTable;

    public BosDatabase(Context context, String tag) {
        super(context, tag);
        database = context.openOrCreateDatabase(Database.DATABASE_NAME_CONFIG, Database.DATABASE_MODE_CONFIG, null);
    }

    @Override
    public void setup() {
        requestMessageTable = new RequestMessageTable();
        requestMessageTable.setDatabase(database);
    }

    public void close () {
        database.close();
    }

    public RequestMessageTable getRequestMessageTable () {
        return requestMessageTable;
    }

}
