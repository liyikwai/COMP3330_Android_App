package com.example.knowcourseapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.knowcourseapp.R;
import com.example.knowcourseapp.fragments.CoursePrerequisitesFragment;
import com.example.knowcourseapp.models.Course;
import com.example.knowcourseapp.models.CourseReview;

import java.util.ArrayList;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView courseTitleView;
    TextView courseDescriptionView;
    TextView averageWorkload;
    TextView averageGrade;
    TextView averageRating;
    Button writeReviewButton;
    Course course;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_datails);
        courseTitleView = findViewById(R.id.courseTitle);
        courseDescriptionView = findViewById(R.id.courseDescription);
        averageRating = findViewById(R.id.averageRating);
        averageGrade = findViewById(R.id.averageGrade);
        averageWorkload = findViewById(R.id.averageWorkload);
        writeReviewButton = findViewById(R.id.writeReview);


        Bundle extras = getIntent().getExtras();
        initializeLayoutContent(extras.getString("courseCode"));
        if (course != null) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("prerequisites", (ArrayList<String>) course.getPrerequisites());
            CoursePrerequisitesFragment fragment = new CoursePrerequisitesFragment();
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.prerequisitesFrame, fragment, "");
            transaction.commitAllowingStateLoss();
        }

    }

    private void initializeLayoutContent(String courseCode) {
        course = Course.getCourse(courseCode);
        if (course != null) {
            getSupportActionBar().setTitle(course.getCode());
            writeReviewButton.setOnClickListener((v) -> {
                Intent intent = new Intent(v.getContext(), CreateReviewActivity.class);
                intent.putExtra("courseCode", course.getCode());
                startActivity(intent);
            });
            String courseTitle = course.getTitle();
            courseTitleView.setText(courseTitle);
            courseDescriptionView.setText(course.getDescription());
            String rating;
            if (course.getAverageRating() == -1) {
                rating = "Not Enough Reviews";
            } else {
                rating = course.getAverageRating() + " out of 5";
            }
            averageRating.setText(rating);
            averageGrade.setText(CourseReview.intToGrade(course.getAverageGrade()));
            averageWorkload.setText(CourseReview.intToWorkload(course.getAverageWorkload()));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeLayoutContent(course.getCode());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), CourseReviewListActivity.class);
        intent.putExtra("courseCode", course.getCode());
        startActivity(intent);


    }
}
