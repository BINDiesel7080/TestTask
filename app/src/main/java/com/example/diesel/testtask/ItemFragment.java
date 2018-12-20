package com.example.diesel.testtask;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.diesel.testtask.db.Thing;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

public class ItemFragment extends Fragment
        implements ThingItemDialogFragment.ThingItemDialogListener,
        ThingListAdapter.ThingListAdapterCallBack {

    private ThingViewModel mThingViewModel;

    private OnListFragmentInteractionListener mListener;
    private ThingItemDialogFragment dialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        return new ItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThingViewModel = ViewModelProviders.of(this)
                .get(ThingViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_item_list, container, false);
        View view = layout.findViewById(R.id.list);

        // Set the adapter
        view = view.findViewById(R.id.list);
        if (view instanceof RecyclerView) {
            registerForContextMenu(view);
            RecyclerView recyclerView = (RecyclerView) view;

            final ThingListAdapter adapter
                    = new ThingListAdapter(getActivity());
            recyclerView.setAdapter(adapter);
            mThingViewModel.getAllThings().observe(this,
                    new Observer<List<Thing>>() {
                        @Override
                        public void onChanged(@Nullable List<Thing> things) {
                            adapter.setThings(things);
                        }
                    });
            adapter.setOnThingItemClickedListener(this);
            FloatingActionButton fab = layout.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showThingItemDialog(null);
                }
            });
        }
        return layout;
    }

    @Override
    public void thingItemClicked(View v) {
        showThingItemDialog(v);
    }

    @Override
    public void thingItemPopupMenuEditItemClicked(View v) {
        showThingItemDialog(v);
    }

    @Override
    public void thingItemPopupMenuDeleteItemClicked(View v) {
        String title = ((TextView) v.findViewById(R.id.content)).getText().toString();
        boolean checked = ((CheckBox) v.findViewById(R.id.checkBox)).isChecked();
        String uid = ((TextView) v.findViewById(R.id.uid)).getText().toString();
        mThingViewModel.delete(new Thing(Integer.parseInt(uid), title, checked));
    }

    void showThingItemDialog(View v) {
        String title = "";
        boolean checked = false;
        String uid = "";
        Bundle dialogBundle;

        if (v != null) {
            title = ((TextView) v.findViewById(R.id.content)).getText().toString();
            checked = ((CheckBox) v.findViewById(R.id.checkBox)).isChecked();
            uid = ((TextView) v.findViewById(R.id.uid)).getText().toString();
        }
        dialogBundle = new Bundle();
        dialogBundle.putString("TITLE", title);
        dialogBundle.putBoolean("CHECKED", checked);
        dialogBundle.putString("UID", uid);
        dialog = new ThingItemDialogFragment();
        dialog.setArguments(dialogBundle);
        dialog.registerThingItemDialogListener(this);
        dialog.show(getActivity().getSupportFragmentManager(),
                Common.TAG_ThingItemDialogFragment);
    }

    @Override
    public void onDialogPositiveClick(ThingItemDialogFragment dialog) {
        if (dialog.uid.getText().toString().equals("")) {
            mThingViewModel.insert(
                    new Thing(0,
                            dialog.title.getText().toString(),
                            dialog.checked.isChecked())
            );
        } else
            mThingViewModel.update(
                    new Thing(Integer.parseInt(dialog.uid.getText().toString()),
                            dialog.title.getText().toString(),
                            dialog.checked.isChecked())
            );
    }

    @Override
    public void onDialogNegativeClick(ThingItemDialogFragment dialog) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Thing thing);
    }
}