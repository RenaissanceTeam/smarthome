package smarthome.library.common.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import smarthome.library.common.BaseController;
import smarthome.library.common.DeviceDataSource;
import smarthome.library.common.IotDevice;

import static smarthome.library.common.storage.DbContract.DbEntry.GUID_COLUMN;
import static smarthome.library.common.storage.DbContract.DbEntry.JSON_COLUMN;
import static smarthome.library.common.storage.DbContract.DbEntry.JSON_INDEX;
import static smarthome.library.common.storage.DbContract.VERSION;

public class JsonDataSource extends SQLiteOpenHelper implements DeviceDataSource<IotDevice> {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS %s (" +
                    GUID_COLUMN + " INTEGER PRIMARY KEY," +
                    JSON_COLUMN + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS %s";

    private Gson gson;
    private Class<? extends IotDevice> deviceType;
    private String tableName;

    public JsonDataSource(Context context, Class<? extends IotDevice> deviceType,
                          List<Class<? extends BaseController>> controllerTypes) {
        super(context, deviceType.getSimpleName() + ".db", null, VERSION);

        RuntimeTypeAdapterFactory<BaseController> adapter =
                RuntimeTypeAdapterFactory
                        .of(BaseController.class, "classType");
        for (Class<? extends BaseController> controllerType : controllerTypes) {
            adapter.registerSubtype(controllerType);
        }

        gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();

        this.deviceType = deviceType;


        tableName = deviceType.getSimpleName();
        onCreate(getWritableDatabase());
    }

    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = String.format(SQL_CREATE_ENTRIES, tableName);
        db.execSQL(sqlQuery);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = String.format(SQL_DELETE_ENTRIES, tableName);
        db.execSQL(sqlQuery);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    @Override
    public List<IotDevice> getAll() {
        List<IotDevice> result = new ArrayList<>();
        try (Cursor query = getReadableDatabase().query(tableName, null, null, null, null, null, null)) {
            if (query.moveToFirst())
            {
                do
                {
                    String json = query.getString(JSON_INDEX);
                    IotDevice device = gson.fromJson(json, deviceType);
                    result.add(device);
                }
                while (query.moveToNext());
            }
        }

        return result;
    }

    @Override
    public boolean add(IotDevice device) {
        ContentValues cv = getContentValues(device);
        getWritableDatabase().insert(tableName, null, cv);
        return true;
    }

    private ContentValues getContentValues(IotDevice device) {
        ContentValues cv = new ContentValues();
        cv.put(GUID_COLUMN, device.guid);
        cv.put(JSON_COLUMN, device.gsonned());
        return cv;
    }

    @Override
    public boolean contains(IotDevice device) {
        String rawQuery = "SELECT * FROM " + tableName + " WHERE " + GUID_COLUMN + " = " + device.guid;
        try (Cursor query = getReadableDatabase().rawQuery(rawQuery, null)) {
            return (query != null && query.moveToFirst());
        }
    }

    @Override
    public IotDevice get(Long guid) {
        String rawQuery = "SELECT * FROM " + tableName + " WHERE " + GUID_COLUMN + " = " + guid;
        try (Cursor query = getReadableDatabase().rawQuery(rawQuery, null)) {
            if (query.moveToFirst()) {
                String json = query.getString(JSON_INDEX);
                return gson.fromJson(json, deviceType);
            } else return null;
        }
    }

    @Override
    public void update(IotDevice device) {
        getWritableDatabase().update(tableName, getContentValues(device), GUID_COLUMN + "=" + device.guid, null);
    }

    @Override
    public void delete(IotDevice device) {
        String rawQuery = "DELETE FROM " + tableName + " WHERE " + GUID_COLUMN + " = " + device.guid;
        getWritableDatabase().execSQL(rawQuery);
    }

    @Override
    public void clearAll() {
        String rawQuery = "DELETE FROM " + tableName;
        getWritableDatabase().execSQL(rawQuery);
    }
}
