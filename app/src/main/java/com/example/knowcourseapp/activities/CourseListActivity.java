package com.example.knowcourseapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knowcourseapp.R;
import com.example.knowcourseapp.adapters.CoursesAdapter;
import com.example.knowcourseapp.models.Course;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);


        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        String token = preferences.getString(getString(R.string.token), null);
        if (token == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        RecyclerView recyclerView = findViewById(R.id.courses);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
        if (searchQuery != null) {
            courses = Course.searchCourses(searchQuery);
            System.out.println(courses);
        } else {
            courses = Course.getAllCourses();
        }
        if (courses == null) {
            Toast.makeText(this, "Network Connection Error", Toast.LENGTH_LONG).show();
            return;
        }
        if (courses.isEmpty()) {
            Toast.makeText(this, "No Courses Found", Toast.LENGTH_LONG).show();
            return;
        }
        CoursesAdapter adapter = new CoursesAdapter(courses);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println(query);
        }
    }
}
