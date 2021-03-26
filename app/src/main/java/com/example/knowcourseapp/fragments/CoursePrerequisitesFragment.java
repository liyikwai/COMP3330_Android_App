package com.example.knowcourseapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.knowcourseapp.activities.CourseDetailActivity;
import com.example.knowcourseapp.R;

public class CoursePrerequisitesFragment extends ListFragment {

    public CoursePrerequisitesFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] prerequisites = getArguments().getStringArrayList("prerequisites").toArray(new String[0]);
            ArrayAdapter<String> adapter;
            if (prerequisites.length == 0) {
               adapter  = new ArrayAdapter<>(getActivity(), R.layout.prerequisite_list_item,
                       new String[]{getString(R.string.no_prerequisites)});
            } else {
                adapter = new ArrayAdapter<>(getActivity(), R.layout.prerequisite_list_item, prerequisites);
            }
            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String courseCode = ((TextView) v).getText().toString();

        // In case No prerequisites, make the field non-clickable
        if (!courseCode.equals(getString(R.string.no_prerequisites))) {
            Intent intent = new Intent(v.getContext(), CourseDetailActivity.class);
            intent.putExtra("courseCode", courseCode);
            v.getContext().startActivity(intent);
        }
    }

}
