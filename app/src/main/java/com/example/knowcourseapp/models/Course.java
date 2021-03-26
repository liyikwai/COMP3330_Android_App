package com.example.knowcourseapp.models;

import com.example.knowcourseapp.network.NetworkUtility;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Course {
    private String code;
    private String title;
    private String description;
    private List<String> prerequisites;
    private int averageGrade;
    private int averageWorkload;
    private int averageRating;

    public Course(String code, String title, String description, List<String> prerequisites, int averageGrade, int averageWorkload, int averageRating) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.prerequisites = prerequisites;
        this.averageGrade = averageGrade;
        this.averageWorkload = averageWorkload;
        this.averageRating = averageRating;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public int getAverageGrade() {
        return averageGrade;
    }

    public int getAverageWorkload() {
        return averageWorkload;
    }

    public int getAverageRating() {
        return averageRating;
    }

    public static List<Course> getAllCourses() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> res = executorService.submit(() -> NetworkUtility.getJson("http://10.0.2.2:8000/api/courses"));
        List<Course> list = getCourseList(res);
        executorService.shutdown();
        return list;
    }

    public static List<Course> searchCourses(String query) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> res = executor.submit(() -> NetworkUtility.getJson("http://10.0.2.2:8000/api/courses/", query));

        List<Course> list = getCourseList(res);
        executor.shutdown();
        return list;
    }

    private static List<Course> getCourseList(Future<String> res) {
        List<Course> list = null;
        try {
            Gson gson = new Gson();
            String response = res.get();
            list = gson.fromJson(response, new TypeToken<List<Course>>(){}.getType());
        } catch (InterruptedException | ExecutionException ex) {
            ex.getCause().printStackTrace();
        }
        return list;
    }

    public static Course getCourse(String courseCode) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Course course = null;
        String url = "http://10.0.2.2:8000/api/courses/" + courseCode + "/";
        Future<String> response = executorService.submit(() -> NetworkUtility.getJson(url));
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            course = gson.fromJson(response.get(), Course.class);
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        executorService.shutdown();
        return course;
    }


}
