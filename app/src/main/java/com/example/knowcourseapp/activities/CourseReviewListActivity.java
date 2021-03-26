package com.example.knowcourseapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knowcourseapp.R;
import com.example.knowcourseapp.adapters.CourseReviewAdapter;
import com.example.knowcourseapp.models.CourseReview;

import java.util.List;

public class CourseReviewListActivity extends Activity {

    List<CourseReview> courseReviews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_reviews);
        RecyclerView recyclerView = findViewById(R.id.courseReviews);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        Bundle extras = getIntent().getExtras();
        courseReviews = CourseReview.getCourseReviews(extras.getString("courseCode"));
        System.out.println(courseReviews == null);
        if (courseReviews.isEmpty()) {
            TextView view = findViewById(R.id.noReviewsMessage);
            view.setText("No Reviews for this Course");
            return;
        }
        CourseReviewAdapter adapter = new CourseReviewAdapter(courseReviews);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

