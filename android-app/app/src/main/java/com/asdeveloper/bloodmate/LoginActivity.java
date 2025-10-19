package com.asdeveloper.bloodmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Models.User;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    private TextView goRegisterBtn, forgetPassword;
    private ImageButton showPassBtn;
    private EditText passwordTxt, phoneTxt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeComponents();
        goRegisterBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        showPassBtn.setOnClickListener(v -> {
            SharedResources.showHidePassword(showPassBtn, passwordTxt);
        });
        loginBtn.setOnClickListener(v -> {
            if(SharedResources.isValidCredentials(phoneTxt, passwordTxt, this)){
                login(phoneTxt.getText().toString(), passwordTxt.getText().toString());
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        forgetPassword.setOnClickListener(view -> {
            if (phoneTxt.getText().toString().trim().isEmpty() || phoneTxt.getText().toString().trim().length() != 10){
                SharedResources.showError("Phone Error", false, "Enter Your Correct Phone Number", this, "").show();
            }
            else {
                SharedResources.isNumberRegistered(this, phoneTxt.getText().toString().trim(), isRegistered -> {
                    if (isRegistered){
                        SharedResources.otpVerification(LoginActivity.this, phoneTxt.getText().toString().trim(), false, isOtpVerified -> {
                            if (isOtpVerified){
                                SharedResources.showForgetPassword(this, phoneTxt.getText().toString().trim(), (pass, confPass, isValid) ->{
                                    if (isValid){
                                        SharedResources.updateUserPassword(this, phoneTxt.getText().toString().trim(), pass);
                                    }
                                    else {
                                        Toast.makeText(this, "Password and Confirm Password didn't matched", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    else {
                        SharedResources.showError("Phone Error", false, "Phone number is not registered with us", this, "").show();
                    }
                });
            }
        });
    }
    private void initializeComponents(){
        goRegisterBtn = findViewById(R.id.registerHeretxt);
        showPassBtn = findViewById(R.id.showPassBtn);
        passwordTxt = findViewById(R.id.loginPasswordTxt);
        phoneTxt = findViewById(R.id.loginPhoneTxt);
        loginBtn = findViewById(R.id.loginBtn);
        forgetPassword = findViewById(R.id.forgetPasstxt);
    }

    private void login(String phone, String password){
        CustomProgress progress = new CustomProgress(this, "Logging You In.....", "Please Wait We Are Fetching Your Details", false);
        progress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<User> call = service.getUserByPhone(phone);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull retrofit2.Response<User> response) {
                progress.dismissDialog();
                if (response.isSuccessful()){
                    User user = response.body();
                    if (Objects.requireNonNull(user).getPhone() == null){
                        SharedResources.showError("Invalid Credentials", false, "Phone number not Registered", LoginActivity.this, "").show();
                    }
                    else if (user.getPassword().equals(password)){
                        SharedResources.setUserId(LoginActivity.this, user.getId());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                    else {
                        SharedResources.showError("Invalid Credentials", false, "Entered Password is Incorrect", LoginActivity.this, "").show();
                    }
                }
                else {
                    SharedResources.showError("Invalid Credentials", false, "Phone number not Registered", LoginActivity.this, "").show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                Log.e("error", Objects.requireNonNull(throwable.getMessage()));
                progress.dismissDialog();
            }
        });
        progress.dismissDialog();
    }


}