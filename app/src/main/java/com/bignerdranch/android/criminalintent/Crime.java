package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Freddy on 12/23/2017.
 * Holds crime's ID, title, date, and status and a constructor
 * that initializes the ID and the date fields
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    //Default Constructor
    public Crime(){
        this(UUID.randomUUID());
        //mId = UUID.randomUUID();
        //mDate = new Date(); //Sets to Current Date
    }

    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }

    /*ID*/
    public UUID getId() {
        return mId;
    }

    /*Title*/
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    /*Date*/
    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }

    /*Solved*/
    public boolean isSolved() {
        return mSolved;
    }
    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    /*Suspect*/
    public String getSuspect(){
        return mSuspect;
    }
    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    /*PhotoFileName*/
    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }

}

