package com.bignerdranch.android.criminalintent.database;

/**
 * Created by Freddy on 1/5/2018.
 * Local Database
 */

public class CrimeDbSchema {
    /*
    Inner Class that Describes Table
    Used to define String constants needed to describe
    moving pieces of table definition
    */
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
