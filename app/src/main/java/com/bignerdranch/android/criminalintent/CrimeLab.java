package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Freddy on 12/27/2017.
 * CrimeLab creates an ArrayList of Crime Objects
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /*
    Context allows use of method getActivity().
    Assesses situation and creates a Crime based on the Activity situation.
    @param: a Context
    @return: one instance of a CrimeLab
    */
    public static CrimeLab get(Context context){
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    /*
     Temporary for Chapter 8. Constructor for CrimeLab.
     @param: a Context (passed from Default constructor)
     @return: An ArrayList of 100 Crime Objects is created
    */
    private CrimeLab(Context context){
       /*
        getWritableDataBase() will do the following:
        1: Open up /data/data/com... creating a new database
        2: call onCreate(SQLiteDatabase) and save latest version number
        3: Check Version Number, if Version in DataBase Helper Class is higher
        call onUpgrade(SQLiteDatabase, int, int)

        onCreate creates a Row with Name and Columns with relevant Crime Information
        */
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();


        // mCrimes = new ArrayList<>();
        /*for(int i = 0; i < 100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); //Every other one
            mCrimes.add(crime);
        }*/
    }

    /*
    Adds a Crime to the local database mDatabase
    Will pass Crime to getContentValues which will add Crime details
    to the Database. Will add to a Row index.
    @param: A Crime Object
    @return: void
    */
    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);

        //mCrimes.add(c);
    }

    /*
    Returns a List<> of Crime Object information.
    Information is gathered from a Cursor which is Wrapped so that it may have the extra method
    that can grab Crime information from a database. The Cursor information is then stored in an
    ArrayList<>.
    @param: none
    @return: An ArrayList of Crime Object information from a database.
    */
    public List<Crime> getCrimes(){
        /*
        return mCrimes;
        return new ArrayList<>(); ch14

        Ch 14
        To pull data from a Cursor
        1st: moveToFirst() grabs first element, then reads in row data
        2nd: moveToNext() advances to a new row
        3rd: isAfterLast() pointer has reached end of data set
        */
        List<Crime> crimes = new ArrayList<>();
        //queryCrimes returns a CrimeCursorWrapper(cursor)
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally{
            cursor.close();
        }
        return crimes;
    }

    /*
    Does not Create any Files on the FileSystem, only
    RETURNS them.
    @param:A Crime Object
    @return: a File Object
    */
    public File getPhotoFile(Crime crime){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }


    /*
    Returns a Crime based on a matching randomly generated Id.
    @param: a UUID
    @return: a Crime
    */
    public Crime getCrime(UUID id){
        /*
        WhereClause: The UUID in the Database. Passes an UUID
        */
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] {id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }

        /*for(Crime crime : mCrimes){
            if (crime.getId().equals(id)){
                return crime;
            }
        }*/
        //return null;
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        /*
        WhereClause: Chooses which ROW. Then Specify values for the arguments in the where Clause
        the final String[] array
        */
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " =?",
                new String[] {uuidString});
    }

    //private Cursor queryCrimes(String whereClause, String[] whereArgs){
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME, null, //Columns - null selects all columns
                whereClause, whereArgs,
                null, //groupBy
                null,  //having
                null  //orderBy
        );
        //return cursor;
        return new CrimeCursorWrapper(cursor);
    }

    /*
    Adds Crime Private Member Variables to a data base.
    Sets crime UUID, Title, Date, and if it is Solved Or Not.
    @param: A Crime Object
    @return: A ContentValue
    */
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }
}























