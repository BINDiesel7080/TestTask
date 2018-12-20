package com.example.diesel.testtask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.diesel.testtask.db.Thing;

import java.util.List;

public class ThingListAdapter
        extends RecyclerView.Adapter<ThingListAdapter.ThingViewHolder> {

    interface ThingListAdapterCallBack {
        void thingItemClicked(View v);

        void thingItemPopupMenuEditItemClicked(View v);

        void thingItemPopupMenuDeleteItemClicked(View v);
    }

    ThingListAdapterCallBack mListener;

    void setOnThingItemClickedListener(ThingListAdapterCallBack mListener) {
        this.mListener = mListener;
    }

    class ThingViewHolder extends RecyclerView.ViewHolder {

        private final TextView thingItemTextView;
        private final CheckBox thingItemCheckBox;
        private final TextView thingItemID;

        private ThingViewHolder(final View itemView) {
            super(itemView);
            thingItemID = itemView.findViewById(R.id.uid);
            thingItemTextView = itemView.findViewById(R.id.content);
            thingItemCheckBox = itemView.findViewById(R.id.checkBox);
            thingItemCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView picView;
                    picView = itemView.findViewById(R.id.item_pic);
                    if (thingItemCheckBox.isChecked()) {
                        picView.setImageResource(android.R.drawable.star_on);
                    } else {
                        picView.setImageResource(android.R.drawable.star_off);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.thingItemClicked(v);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<Thing> mThings; // Cached copy of things

    ThingListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ThingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_item, parent,
                false);
        return new ThingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ThingViewHolder holder, int position) {
        if (mThings != null) {
            Thing current = mThings.get(position);
            holder.thingItemID.setText(Integer.toString(current.getID()));
            holder.thingItemTextView.setText(current.getTitle());
            holder.thingItemCheckBox
                    .setChecked(current.getChecked() ? true : false);
        } else {
            // Covers the case of data not being ready yet.
            holder.thingItemTextView.setText("No Thing!");
            holder.thingItemCheckBox.setChecked(false);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                popupMenu.inflate(R.menu.item_fragment_context);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.context_item_edit:
                                mListener.thingItemPopupMenuEditItemClicked(holder.itemView);
                                break;
                            case R.id.context_item_delete:
                                mListener.thingItemPopupMenuDeleteItemClicked(holder.itemView);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    void setThings(List<Thing> things) {
        mThings = things;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mThings != null)
            return mThings.size();
        else return 0;
    }
}
