package com.beaconify.detect.beaconify;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.beaconify.detect.beaconify.Helper.DialogHelper;
import com.beaconify.detect.beaconify.Helper.JsonHelper;
import com.beaconify.detect.beaconify.Model.Login.LoginResponseFail;
import com.beaconify.detect.beaconify.Model.Login.LoginResponseSuccess;
import com.beaconify.detect.beaconify.Networking.HttpRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login_button;
    private EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.login_button = (Button) findViewById(R.id.login_button);
        this.usernameField = (EditText) findViewById(R.id.username_field);
        this.passwordField = (EditText) findViewById(R.id.password_field);

        this.login_button.setOnClickListener(this);

        LoginResponseSuccess loginResponseSuccess = readUserInfo();

        if(loginResponseSuccess != null)
        {
            Cache.token = loginResponseSuccess.token;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                LoginRequest loginRequest = new LoginRequest();
                com.beaconify.detect.beaconify.Model.Login.LoginRequest loginDetails = new com.beaconify.detect.beaconify.Model.Login.LoginRequest(usernameField.getText().toString(), passwordField.getText().toString());
                loginRequest.execute(loginDetails);
                break;
        }
    }

    public LoginResponseSuccess readUserInfo () {
        File directory = getApplicationContext().getFilesDir();
        File file = new File(directory, "UserInfo.json");
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();

            return (LoginResponseSuccess) JsonHelper.getInstance().toObject(text.toString(), LoginResponseSuccess.class);

        }
        catch (IOException e) {
            return null;
        }
    }


    private class LoginRequest extends AsyncTask<com.beaconify.detect.beaconify.Model.Login.LoginRequest, Void, Response> {
        private DialogHelper dialogHelper;

        @Override
        protected void onPreExecute() {
            dialogHelper = new DialogHelper(LoginActivity.this);
            dialogHelper.showProgressDialog("Signing in");
        }

        @Override
        protected Response doInBackground(com.beaconify.detect.beaconify.Model.Login.LoginRequest... loginDetails) {
            try {
                HttpRequest httpRequest = new HttpRequest();
                String json = JsonHelper.getInstance().toJson(loginDetails[0]);
                return httpRequest.post(AppContext.baseUrl + AppContext.loginEndpoint, json);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response result) {
            dialogHelper.hideProgressDialog();
            if(result == null)
            {
                createDefaultErrorMessage();
            }

            try {
                switch (result.code()) {
                    case 200:
                        LoginResponseSuccess loginResponseSuccess = (LoginResponseSuccess) JsonHelper.getInstance().toObject(result.body().string(), LoginResponseSuccess.class);
                        createLoginRecord(loginResponseSuccess);
                        Cache.token = loginResponseSuccess.token;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case 400:
                        LoginResponseFail loginResponseFail = (LoginResponseFail) JsonHelper.getInstance().toObject(result.body().string(), LoginResponseFail.class);
                        DialogHelper dialogHelper = new DialogHelper(LoginActivity.this);
                        dialogHelper.createAlertDialog("Login Error", loginResponseFail.getMessage());
                        break;
                    default:
                        createDefaultErrorMessage();
                }
            } catch (Exception e) {
                createDefaultErrorMessage();
            }
        }

        private void createLoginRecord(LoginResponseSuccess loginResponseSuccess)
        {
            String filename = "UserInfo.json";
            String fileContent = JsonHelper.getInstance().toJson(loginResponseSuccess);

            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContent.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void createDefaultErrorMessage() {
            DialogHelper dialogHelper = new DialogHelper(LoginActivity.this);
            dialogHelper.createAlertDialog("Login Error", "unexpected error");
        }
    }
}
