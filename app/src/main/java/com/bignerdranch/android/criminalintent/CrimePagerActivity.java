package com.bignerdranch.android.criminalintent;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Freddy on 1/2/2018.
 * Will replace CrimeActivity.java which each time called brings up a CrimeFragment.
 * Allows to open an instance of CrimeFragment but be able to Swipe Left or Right
 * to access next instances of CrimeLab List<>.
 */

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    //This is received from CrimeListFragment which calls a new Intent in its private CrimeHolder Class
    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        //A List of Crimes is Returned
        mCrimes = CrimeLab.get(this).getCrimes();
        //FragmentManager allows the View to see which Fragments exist
        FragmentManager fragmentManager = getSupportFragmentManager();
        //adapter is set to Listen to the Fragments held by FragmentManager
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            /*
            getItem(int) fetches the Crime instance for the given position
            in the data set. It then uses that Crime's ID to create and return
            a properly configured CrimeFragment.

            Takes the Current position at returns the Fragment associated with current position
            */
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for(int index = 0; index < mCrimes.size(); index++){
            if(mCrimes.get(index).getId().equals(crimeId)){
                mViewPager.setCurrentItem(index);
                break;
            }
        }

    }
    //Callbacks must be implemented in all activites that host CrimeFragment
    @Override
    public void onCrimeUpdated(Crime crime){

    }
}
