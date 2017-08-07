package com.example.android.sqliteandrecyclerviewswipeexample;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sqliteandrecyclerviewswipeexample.data.WaitlistContract;

class GuestlistAdapter extends RecyclerView.Adapter<GuestlistAdapter.GuestListViewHolder>{

    private Cursor cursor;

    GuestlistAdapter(Cursor cursor) {

        this.cursor = cursor;

    }

    @Override
    public GuestListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.view_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new GuestListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(GuestListViewHolder holder, int position) {

        if(!cursor.moveToPosition(position))
            return;

        String guestName = cursor.getString(cursor.getColumnIndex(WaitlistContract.WaitListEntry.COLUMN_GUEST_NAME));

        int partySize = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WaitlistContract.WaitListEntry.COLUMN_PARTY_SIZE)));

        long id = cursor.getLong(cursor.getColumnIndex(WaitlistContract.WaitListEntry._ID));

        holder.guestNameTextview.setText(guestName);

        holder.partySizeTextview.setText(String.valueOf(partySize));

        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {

        return cursor.getCount();

    }

    void swapCursor(Cursor newCursor) {

        // Always close the previous mCursor first
        if (cursor != null) cursor.close();

        cursor = newCursor;

        if (newCursor != null) {

            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();

        }

    }

    class GuestListViewHolder extends RecyclerView.ViewHolder {

        TextView partySizeTextview;

        TextView guestNameTextview;

        GuestListViewHolder(View itemView) {

            super(itemView);

            partySizeTextview = itemView.findViewById(R.id.party_size_text_view);

            guestNameTextview = itemView.findViewById(R.id.name_text_view);

        }

    }

}
