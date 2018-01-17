package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Freddy on 12/28/2017.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //OFF IN CH17: setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());
        //Explicit call to activity's FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //Manages Fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        /*  add() creates and commits a fragment transaction
                fragment transactions are used to add, remove, attach, detach, replace fragments in fragment list
                    Two Parameters: 1) a container view ID.   2) newly created Fragment

                beginTransaction() creates and returns an instance of FragmentTransaction
                which uses fluent interface -- methods that configure FragmentTransaction,
                return a FragmentTransaction, instead of void
                Laymans Terms: Create a new Fragment Transaction, include one add operation in it, and then commit it  */
    }
}

