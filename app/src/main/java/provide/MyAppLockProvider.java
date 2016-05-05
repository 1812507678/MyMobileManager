package provide;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.haijun.db.MyLockAppDBHelper;

public class MyAppLockProvider extends ContentProvider {

    private SQLiteDatabase readableDatabase;
    private MyLockAppDBHelper myLockAppDBHelper;

    public MyAppLockProvider() {
    }

    @Override
    public boolean onCreate() {
        myLockAppDBHelper = new MyLockAppDBHelper(getContext(), "applack.db", null, 1);
        readableDatabase = myLockAppDBHelper.getReadableDatabase();
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        readableDatabase.delete("lockApp",selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return  0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        readableDatabase.insert("lockApp",null,values);
        getContext().getContentResolver().notifyChange(uri,null);
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor lockApp = readableDatabase.query("lockApp", projection, selection, selectionArgs, null, null,null,null);
        return lockApp;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
       return 0;
    }
}
