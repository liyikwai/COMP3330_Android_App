package com.example.knowcourseapp.models;

import android.content.Context;

import com.example.knowcourseapp.network.NetworkUtility;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Upvote {
    private String author;
    private String review;

    public Upvote(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    public static Upvote upvoteReview(int reviewPk, Context context) {
        Upvote upvote = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String url = "http://10.0.2.2:8000/api/reviews/"
                + reviewPk + "/upvotes/";
        Future<String> result = executor.submit(() -> NetworkUtility.postJson(url,"", context));
        try {
            Gson gson = new Gson();
            upvote = gson.fromJson(result.get(), Upvote.class);
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return upvote;
    }

}
