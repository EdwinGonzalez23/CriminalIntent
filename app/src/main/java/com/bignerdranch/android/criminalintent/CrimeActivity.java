package com.bignerdranch.android.criminalintent;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity{

    //public
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        return intent;
    }

    /*
        Calls createFragment Method which is in SingleFragmentActivity
        This method returns a new CrimeFragment
        CrimeFragment.java creates a new Crime from Crime.java
        then Creates a CrimeFragment using the xml file fragment_crime.xml

        SingleFragmentActivity creates the FragmentManager and uses the XML file activity_fragment.xml
        to hold the fragments being created. In this case CrimeFragment uses fragment_crime
        on top of activity_crime.

    */
    @Override
    protected Fragment createFragment(){
        //Calls CrimeFragment which creates a new Crime and the Fragment using the fragment_crime.xml file
        //return new CrimeFragment();
        UUID crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeID);
    }
}










//The None Abstract Class Way
/*public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //Creating a Fragment! (Generic Code)

        //Explicit call to activity's FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //Manages Fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new CrimeFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
            *//*  add() creates and commits a fragment transaction
                fragment transactions are used to add, remove, attach, detach, replace fragments in fragment list
                    Two Parameters: 1) a container view ID.   2) newly created Fragment

                beginTransaction() creates and returns an instance of FragmentTransaction
                which uses fluent interface -- methods that configure FragmentTransaction,
                return a FragmentTransaction, instead of void
                Laymans Terms: Create a new Fragment Transaction, include one add operation in it, and then commit it  *//*
        }
    }
}*/

