package com.wiredin.anagramsolver.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Outline;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.wiredin.anagramsolver.R;
import com.wiredin.anagramsolver.listeners.NotifyDialogListener;

/**
 * Created by Thomas on 10/19/2014.
 */
public class SortDialogFragment extends DialogFragment {

    public static enum SortMode {
        POINTS, ALPHABETICAL, LENGTH
    }
    public static enum SortDirection {
        ASCENDING, DESCENDING
    }

    public static final String SORT_BY_LABEL = "Sort By";
    public static final String SORT_LABEL = "Sort";
    public static final String CANCEL_LABEL = "Cancel";
    public String[] SORTING_MODES, SORTING_DIRECTIONS;

    public SortMode selectedSortMode;
    public int selectedSortModePosition;

    public SortDirection selectedSortDirection;
    public int selectedSortDirectionPosition;

    private ListView sortingModesListView, sortingDirectionsListView;


    private NotifyDialogListener mListener;

    public SortDialogFragment() {
        selectedSortMode = SortMode.ALPHABETICAL;
        selectedSortDirection = SortDirection.ASCENDING;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NotifyDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SORTING_MODES = new String[]{"Alphabetical", "Length", "Points"};
        SORTING_DIRECTIONS = new String[]{"Ascending", "Descending"};
        selectedSortMode = SortMode.LENGTH;

        DialogInterface.OnClickListener sortListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("dialog", "SORT " + which);
                int checkedItem = sortingModesListView.getCheckedItemPosition();
                switch (checkedItem) {
                    case 0:
                        selectedSortMode = SortMode.ALPHABETICAL;
                        selectedSortModePosition = 0;
                        break;
                    case 1:
                        selectedSortMode = SortMode.LENGTH;
                        selectedSortModePosition = 1;
                        break;
                    case 2:
                        selectedSortMode = SortMode.POINTS;
                        selectedSortModePosition = 2;
                        break;
                }

                checkedItem = sortingDirectionsListView.getCheckedItemPosition();
                switch (checkedItem) {
                    case 0:
                        selectedSortDirection = SortDirection.ASCENDING;
                        selectedSortDirectionPosition = 0;
                        break;
                    case 1:
                        selectedSortDirection = SortDirection.DESCENDING;
                        selectedSortDirectionPosition = 1;
                        break;
                }
                mListener.onDialogPositiveClick(SortDialogFragment.this);
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(SortDialogFragment.this);
            }
        };

        View dialogLayoutView = getActivity().getLayoutInflater().inflate(R.layout.sort_settings_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setView(dialogLayoutView)
                .setTitle(SORT_BY_LABEL)
                .setPositiveButton(SORT_LABEL, sortListener)
                .setNegativeButton(CANCEL_LABEL, cancelListener);

        sortingModesListView = (ListView) dialogLayoutView.findViewById(R.id.listview_sort_modes);
        ArrayAdapter<CharSequence> sortingModesAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.dialog_adapter_row, SORTING_MODES);
        sortingModesListView.setAdapter(sortingModesAdapter);
        sortingModesListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        sortingModesListView.setItemChecked(selectedSortModePosition, true);

        sortingDirectionsListView = (ListView) dialogLayoutView.findViewById(R.id.listview_sort_direction);
        ArrayAdapter<CharSequence> sortingDirectionsAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.dialog_adapter_row, SORTING_DIRECTIONS);
        sortingDirectionsListView.setAdapter(sortingDirectionsAdapter);
        sortingDirectionsListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        sortingDirectionsListView.setItemChecked(selectedSortDirectionPosition, true);

        return builder.create();
    }


    public SortMode getSelectedSortMode() {
        return selectedSortMode;
    }

    public SortDirection getSelectedSortDirection() {
        return selectedSortDirection;
    }



}
