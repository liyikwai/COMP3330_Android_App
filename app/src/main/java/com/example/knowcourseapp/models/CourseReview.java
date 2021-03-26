package com.example.knowcourseapp.models;

import android.content.Context;

import com.example.knowcourseapp.network.NetworkUtility;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CourseReview {
    private int pk;
    private String author;
    private String dateCreated;
    private int rating;
    private String yearTaken;
    private String subclass;
    private String professor;
    private String assessment;
    private int grade;
    private int workload;
    private String review;
    private String suggestions;
    private List<Upvote> upvotes;

    private static Map<Integer, String> gradesMap;
    private static Map<Integer, String> workloadMap;

    static {

        gradesMap = new HashMap<>();
        gradesMap.put(12, "A+");
        gradesMap.put(11, "A");
        gradesMap.put(10, "A-");
        gradesMap.put(9, "B+");
        gradesMap.put(8, "B");
        gradesMap.put(7, "B-");
        gradesMap.put(6, "C+");
        gradesMap.put(5, "C");
        gradesMap.put(4, "C-");
        gradesMap.put(3, "D+");
        gradesMap.put(2, "D");
        gradesMap.put(1, "F");
        gradesMap.put(0, "Pass");
        gradesMap.put(-1, "Not Enough Reviews");

        workloadMap = new HashMap<>();

        workloadMap.put(-1, "Not Enough Reviews");
        workloadMap.put(0, "Easy");
        workloadMap.put(1, "Average");
        workloadMap.put(2, "Above Average");
        workloadMap.put(3, "Hell");
    }

    public static String intToGrade(int num) {
        return gradesMap.get(num);
    }

    public static String intToWorkload(int num) {
        return workloadMap.get(num);
    }

    // No upvotes constructor for creating a review
    public CourseReview(String author,
                        String dateCreated,
                        int rating,
                        String yearTaken,
                        String subclass,
                        String professor,
                        String assessment,
                        int grade,
                        int workload,
                        String review,
                        String suggestions) {
        this.author = author;
        this.dateCreated = dateCreated;
        this.rating = rating;
        this.yearTaken = yearTaken;
        this.subclass = subclass;
        this.professor = professor;
        this.assessment = assessment;
        this.grade = grade;
        this.workload = workload;
        this.review = review;
        this.suggestions = suggestions;
    }

    public int getPk() {
        return pk;
    }

    public String getAuthor() {
        return author;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getRating() {
        return rating;
    }

    public String getYearTaken() {
        return yearTaken;
    }

    public String getSubclass() {
        return subclass;
    }

    public String getProfessor() {
        return professor;
    }

    public String getAssessment() {
        return assessment;
    }

    public int getGrade() {
        return grade;
    }

    public int getWorkload() {
        return workload;
    }

    public String getReview() {
        return review;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public List<Upvote> getUpvotes() {
        return upvotes;
    }

    public static List<CourseReview> getCourseReviews(String courseCode) {
        List<CourseReview> courseReviews = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String url = "http://10.0.2.2:8000/api/courses/" + courseCode + "/reviews/";
        Future<String> result = executor.submit(() -> NetworkUtility.getJson(url));
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            courseReviews = gson.fromJson(result.get(), new TypeToken<List<CourseReview>>(){}.getType());
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return courseReviews;
    }


    public static int gradeToInt(String grade) {
        int gradeNum = -1;
        for (int key : gradesMap.keySet()) {
            if (gradesMap.get(key).equals(grade)) {
                gradeNum = key;
                break;
            }
        }
        return gradeNum;
    }

    public static int workLoadToInt(String workload) {
        int workloadNum = -1;
        for (int key : workloadMap.keySet()) {
            if (workloadMap.get(key).equals(workload)) {
                workloadNum = key;
                break;
            }
        }
        return workloadNum;
    }
}
