package com.example.knowcourseapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.knowcourseapp.R;
import com.example.knowcourseapp.network.NetworkUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SignUpActivity extends Activity {

    TextView usernameView;
    TextView emailView;
    TextView passwordView;
    TextView passwordRepeatView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameView = findViewById(R.id.usernameInput);
        emailView = findViewById(R.id.emailInput);
        passwordView = findViewById(R.id.passwordInput);
        passwordRepeatView = findViewById(R.id.passwordRepeatInput);
    }

    public void handleSubmit(View v) {
        boolean errorPresent = false;
        if (TextUtils.isEmpty(usernameView.getText())) {
            usernameView.setError("Cannot be blank");
            errorPresent = true;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(emailView.getText()).matches()) {
            emailView.setError("Enter Valid Email");
            errorPresent = true;
        }

        if (TextUtils.isEmpty(passwordView.getText())) {
            passwordView.setError("Cannot be blank");
            errorPresent = true;
        }
        if (TextUtils.isEmpty(passwordRepeatView.getText())) {
            passwordRepeatView.setError("Cannot be blank");
            errorPresent = true;
        }
        if (!TextUtils.equals(passwordView.getText(), passwordRepeatView.getText())) {
            passwordView.setError("Passwords do not match");
            passwordRepeatView.setError("Passwords do not match");
            errorPresent = true;
        }
        if (!errorPresent) {
            Map<String, String> formData = new HashMap<>();
            formData.put("username", usernameView.getText().toString());
            formData.put("email", emailView.getText().toString());
            formData.put("password", passwordView.getText().toString());
            Map<String, Map<String, String>> wrapper = new HashMap<>();
            wrapper.put("user", formData);
            Gson gson = new Gson();
            String json = gson.toJson(wrapper);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            String url = getString(R.string.server_address) + getString(R.string.user_registration);
            Future<String> response = executor.submit(() -> NetworkUtility.postJson(url, json, v.getContext()));
            try {
                String responseJson = response.get();
                if (responseJson.contains(getString(R.string.username_exists))) {
                    usernameView.setError(getString(R.string.username_exists));
                    return;
                }
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
    }
}
