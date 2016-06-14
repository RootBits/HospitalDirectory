package com.rootbits.hospitaldirectory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rootbits.hospitaldirectory.Database.Constant;
import com.rootbits.hospitaldirectory.Database.DistrictInfo;
import com.rootbits.hospitaldirectory.Database.DivisionInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by RootBits on 6/14/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    Context context;
    public static boolean flag;
    public static int oldV = 1, newV = 1;

    private static String databasePath = "/data/data/com.rootbits.bdpolicedirectory/databases/";


    public DatabaseHandler(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            databasePath = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            databasePath = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (DatabaseHandler.newV > DatabaseHandler.oldV) {
            dbExist = false;
        }
        if (dbExist) {
            //do nothing  com.rootbits.bangladictionary.DataBase already exist
            Log.e("DB", "Data Base found");
        } else {
            //By calling this method and empty com.rootbits.bangladictionary.DataBase will be created into the default system path
            //of your application so we are gonna be able to overwrite that com.rootbits.bangladictionary.DataBase with our com.rootbits.bangladictionary.DataBase.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {

                throw new Error("Error copying www.rootbits.com.quotes.database");
            }
        }
    }

    /**
     * Check if the www.rootbits.com.quotes.database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = databasePath + Constant.DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //com.rootbits.bangladictionary.DataBase does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(Constant.DB_NAME);
        // Path to the just created empty db
        String outFileName = databasePath + Constant.DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[myInput.available()];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    //------------------------------------------------------All Query Start----------------------------------------------------------


    public ArrayList<DivisionInfo> selectDivision() {
        ArrayList<DivisionInfo> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(Constant.TBL_DIVISION, new String[]{Constant.DIVISION_DID, Constant.DIVISION_DNAME}, null,
                    null, null, null, Constant.DIVISION_DNAME + " COLLATE NOCASE ASC");
            if (cursor.moveToFirst()) {
                do {
                    DivisionInfo info = new DivisionInfo(cursor.getString(0), cursor.getString(1));
                    data.add(info);
                    //Log.e("SQL", cursor.getString(0) + "  " + cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException ex) {
            Log.e("SQL", String.valueOf(ex));
        } finally {
            db.close();
            //cursor.close();
        }
        return data;
    }

    public ArrayList<DistrictInfo> selectDistrict(String id) {
        ArrayList<DistrictInfo> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(Constant.TBL_DISTRIC, new String[]{Constant.DISTRIC_DID, Constant.DISTRIC_NAME}, Constant.DIVISION_DID + "=?",
                    new String[]{id}, null, null, Constant.DISTRIC_NAME + " COLLATE NOCASE ASC");
            if (cursor.moveToFirst()) {
                do {
                    DistrictInfo info = new DistrictInfo(cursor.getString(0), cursor.getString(1));
                    data.add(info);
                    //Log.e("SQL", cursor.getString(0) + "  " + cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException ex) {
            Log.e("SQL", String.valueOf(ex));
        } finally {
            db.close();
            //cursor.close();
        }
        return data;
    }


    public String selectDistrictID(String name) {
        String data = "";
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(Constant.TBL_DISTRIC, new String[]{Constant.DISTRIC_DID}, Constant.DISTRIC_NAME + "=?",
                    new String[]{name}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    data = cursor.getString(0);
                    // Log.e("SQL", cursor.getString(0));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException ex) {
            Log.e("SQL", String.valueOf(ex));
        } finally {
            db.close();
            //cursor.close();
        }
        return data;
    }
}