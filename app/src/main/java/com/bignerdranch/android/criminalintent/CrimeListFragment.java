package com.bignerdranch.android.criminalintent;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Freddy on 12/28/2017.
 * Creates a LinearLayout list of Crimes using a RecyclerView,
 * adapter, and ViewHolder.
 *
 * This fragment is displayed over activity_fragment.xml and is managed by the Fragment manager
 * created in SingleFragmentActivity
 *
 * CrimeListFragment is a fragment that inflates fragment_crime_list.xml in order to Create
 * a scrollable list for users to navigate.
 */

public class CrimeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /*
    Required interface for hosting activities
    */
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    /*
    Inform Fragment Manager that CrimeListFragment needs to receive menu callbacks
    Fragment Manager is responsible for calling Fragment.onCreateOptionsMenu(MenuInflater)
    when Activity receives its onCreateOptionsMenu(..)
    */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Explicitly telling FragmentManager that fragment should receive a call to onCreateOptionsMenu(..).
        setHasOptionsMenu(true);
    }


    //Inflate Fragment xml that will be displayed in container
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        //RecyclerView needs a LayoutManager. LinearLayout is a type of manager that stacks vertically (LISTS)
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState!= null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }
    //When User Returns to List UI will Update with most Recent Information rather than resetting
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    /* Populates the Menu instance with the items defined in file.
       Does nothing without informing FragmentManager */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    /*
    After onCreateOptions creates a menu and onCreate lets Fragment Manager know that
    CrimeListFragment needs menu Callbacks, onOptionsItemSelected takes the Menu Item
     user has Selected. Case 1: If the Menu ID matches new_crime then it creates a new Crime,
     adds it to the CrimeLab LIST<> and passes the Intent to CrimePagerActivity
     so that information is updated when user has clicked the list to enter CrimeFragment
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                //get(Context context)
                CrimeLab.get(getActivity()).addCrime(crime);
                /*
                Pass CrimeId to PagerActivity so that it may add a new Crime to the FragmentManager and be
                displayed on the ViewHolder: Crime Holder
                */
                //Ch17 Deleted Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                //startActivity(intent);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            //Updates Status Bar when User Clicks SHow Subtitle
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    //Update Support bar to show Number of Crimes Currently Created
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //Updates the List the user sees with Relevant information
    //CH 17 private
    public void  updateUI(){
        //CrimeLab creates an ArrayList of Crime Objects if none exist.
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        //An ArrayList named crimes created and set equal to the CrimeLab created ArrayList of Crime Objects
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            //Adapter passes information needed to ViewHolder as it can access CrimeLab and Crime information
            mAdapter = new CrimeAdapter(crimes);
            //Layout View of List of Crimes
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            //Crimes List<> is updated and is displayed properly when using Back Button
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();}
        updateSubtitle();
    }


    //ADAPTER AND VIEW HOLDER BELOW!

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        /*onCreateViewHolder is called by the RecyclerView when it
        needs a new ViewHolder to display an item with. In this method, you create a
        LayoutInflater and use it to construct a new CrimeHolder.*/

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override //Simpler, the Smoother the Scroll
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }
        
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        //Sets Crime When UI has been refreshed using Back Button
        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
    }


    /*  Once Hooked up to an XML File, you need a view Holder for the Linear Layout.
        The code Bellow attacked the xml file list_item_crime that will fill each individual
        recycler linear layout view
    */

    /*
    Define ViewHolder that will inflate and own your layout. Each ViewHolder is individual
    for each slot. You can set each ViewHolder to be an onClickListener so that users can
    click on each slot individually
    */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;
        //XML LOGIC
        public CrimeHolder (LayoutInflater inflater, ViewGroup parent){
            //ViewHolder's Constructor
            super(inflater.inflate(R.layout.list_item_crime,parent,false));
            itemView.setOnClickListener(this); //itemView is view for ENTIRE ROW
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title); //list_item
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }
        //RUNTIME LOGIC in BIND
        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            //Turn Operator. If Solved set Visible, if not Solved set GONE
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);

        }

        @Override
        public void onClick(View view){
            //Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!" , Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getActivity(), CrimeActivity.class);
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            //Ch17 Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            //Ch17 startActivity(intent);
            mCallbacks.onCrimeSelected(mCrime);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }
}
