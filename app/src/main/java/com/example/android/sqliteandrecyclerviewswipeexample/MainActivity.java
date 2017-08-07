package com.example.android.sqliteandrecyclerviewswipeexample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.sqliteandrecyclerviewswipeexample.data.WaitlistContract;
import com.example.android.sqliteandrecyclerviewswipeexample.data.WaitlistDbHelper;

public class MainActivity extends AppCompatActivity {

    EditText mGuestNameEditText;

    EditText mGuestPartyNumberEditText;

    Button mGuestAddButton;

    SQLiteDatabase mSqLiteDatabase;

    SQLiteOpenHelper mSqLiteOpenHelper;

    RecyclerView mRecyclerView;

    GuestlistAdapter mGuestlistAdapter;

    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGuestNameEditText = (EditText) findViewById(R.id.guest_name_edit_text);
        mGuestPartyNumberEditText = (EditText) findViewById(R.id.party_number_edit_text);
        mGuestAddButton = (Button) findViewById(R.id.add_to_waitlist_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.all_guests_list_view);

        mSqLiteOpenHelper = new WaitlistDbHelper(this);
        mSqLiteDatabase = mSqLiteOpenHelper.getWritableDatabase();

        mGuestlistAdapter = new GuestlistAdapter(getAllGuests());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mGuestlistAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // TODO: reference
        mGuestAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mGuestNameEditText.getText().length() == 0 ||
                        mGuestPartyNumberEditText.getText().length() == 0) {
                    return;
                }
                //default party size to 1
                int partySize = 1;
                try {
                    //mNewPartyCountEditText inputType="number", so this should always work
                    partySize = Integer.parseInt(mGuestPartyNumberEditText.getText().toString());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }

                // Add guest info to mDb
                addNewGuest(mGuestNameEditText.getText().toString(), partySize);

                // Update the cursor in the adapter to trigger UI to display the new list

                mGuestlistAdapter.swapCursor(getAllGuests());

                //clear UI text fields
                mGuestPartyNumberEditText.clearFocus();
                mGuestNameEditText.getText().clear();
                mGuestPartyNumberEditText.getText().clear();

            }
        });

        // TODO: reference
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                long id = (long) viewHolder.itemView.getTag();

                removeGuest(id);

                mGuestlistAdapter.swapCursor(getAllGuests());


            }

        }).attachToRecyclerView(mRecyclerView);

    }

    // TODO: reference
    private Cursor getAllGuests() {
        return mSqLiteDatabase.query(
                WaitlistContract.WaitListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitListEntry.COLUMN_TIMESTAMP
        );
    }

    // TODO: reference
    private long addNewGuest(String name, int partySize) {

        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitListEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitListEntry.COLUMN_PARTY_SIZE, partySize);
        return mSqLiteDatabase.insert(WaitlistContract.WaitListEntry.TABLE_NAME, null, cv);

    }

    // TODO: reference
    private boolean removeGuest(long id) {

        return mSqLiteDatabase.delete(
                WaitlistContract.WaitListEntry.TABLE_NAME,
                WaitlistContract.WaitListEntry._ID + "=" + id,
                null) > 0;

    }

}
