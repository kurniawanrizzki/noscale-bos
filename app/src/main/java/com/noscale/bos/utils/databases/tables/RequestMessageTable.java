package com.noscale.bos.utils.databases.tables;

import android.content.ContentValues;
import android.database.Cursor;

import com.noscale.bos.models.RequestMessage;
import com.noscale.bos.utils.databases.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by kurniawanrizzki on 21/05/18.
 */

public class RequestMessageTable extends Database {

    private static final String TABLE_NAME = "REQUEST_MESSAGE_TABLE";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_SRC = "SRC";
    private static final String COLUMN_CONTENT = "CONTENT";
    private static final String COLUMN_STATUS = "STATUS";
    private static final String COLUMN_LAST_ATTEMPT = "LAST_ATTEMPT";
    private static final String COLUMN_RECEIVED_TIME = "RECEIVED_TIME";
    private static final String COLUMN_FORWARDED_TIME = "FORWARDED_TIME";

    @Override
    protected void createTable() {
        String query = buildTableCreationQuery(
                TABLE_NAME,
                new String[]{COLUMN_ID, "integer primary key autoincrement"},
                new String[]{COLUMN_SRC, "text not null"},
                new String[]{COLUMN_CONTENT, "text not null"},
                new String[]{COLUMN_STATUS, "integer default "+ RequestMessage.Status.WAITING},
                new String[]{COLUMN_LAST_ATTEMPT, "integer default 0"},
                new String[]{COLUMN_RECEIVED_TIME, "integer not null"},
                new String[]{COLUMN_FORWARDED_TIME, "integer default 0"}
        );
        database.execSQL(query);
    }

    @Override
    public long insert(Object item) {
        RequestMessage reqMessage = (RequestMessage) item;
        ContentValues values = new ContentValues();
        values.put(COLUMN_SRC, reqMessage.getSrc());
        values.put(COLUMN_CONTENT, reqMessage.getContent());
        values.put(COLUMN_RECEIVED_TIME, reqMessage.getReceivedTime());
        return database.insert(TABLE_NAME, null, values);
    }

    public boolean isRequestMessageAllowToForwarded (String src, String content) {
        String whereClause =
                COLUMN_SRC+" = ? AND "
                        +COLUMN_CONTENT+" = ? AND "
                        +COLUMN_STATUS+" = "+RequestMessage.Status.WAITING;

        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                whereClause,
                new String[]{src, content},
                null, null, null);

        if (cursor.getCount() > 0) {
            return false;
        }

        cursor.close();
        return true;
    }

    public List<RequestMessage> getMessageRequestScheduledData (int limit) {
        List<RequestMessage> messageRequestScheduledData = null;

        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                COLUMN_STATUS+" = ?",
                new String[]{String.valueOf(RequestMessage.Status.WAITING)},
                null,
                null,
                COLUMN_ID+" DESC",
                String.valueOf(limit)
        );

        if ((null != cursor) && (cursor.getCount() > 0)) {
            messageRequestScheduledData = new ArrayList<>();
            while (cursor.moveToNext()) {
                RequestMessage message = (RequestMessage) cursorToObject(cursor);
                messageRequestScheduledData.add(message);
            }
        }

        cursor.close();
        return messageRequestScheduledData;
    }

    @Override
    public long update(Object item, long id) {
        return 0;
    }

    private long updateRequestMessage (int lastAttempt, int status, long forwardedTime, long id) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_ATTEMPT, lastAttempt);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_FORWARDED_TIME, forwardedTime);

        return database.update(
                TABLE_NAME, values,COLUMN_ID+" = ?", new String[]{String.valueOf(id)}
        );
    }

    public long updateRequestMessage (long id) {
        return updateRequestMessage(
                0,
                RequestMessage.Status.DELIVERED,
                Calendar.getInstance(Locale.getDefault()).getTimeInMillis(),
                id
        );
    }

    public long updateRequestMessage (int lastAttempt, int status, long id) {
        return updateRequestMessage(
                lastAttempt,
                status,
                0,
                id
        );
    }

    @Override
    public List<?> getList() {
        return null;
    }

    @Override
    public Object cursorToObject(Cursor cursor) {
        RequestMessage message = new RequestMessage();
        message.setId(cursor.getLong(0));
        message.setSrc(cursor.getString(1));
        message.setContent(cursor.getString(2));
        message.setStatus(cursor.getInt(3));
        message.setLastAttempt(cursor.getInt(4));
        message.setReceivedTime(cursor.getLong(5));
        message.setForwardedTime(cursor.getLong(6));
        return message;
    }

}
