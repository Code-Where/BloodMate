package com.asdeveloper.bloodmate;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Interfaces.OnForgetPasswordSubmit;
import com.asdeveloper.bloodmate.Interfaces.OnNumberCheckListener;
import com.asdeveloper.bloodmate.Interfaces.OnOtpVerificationListner;
import com.asdeveloper.bloodmate.Models.FcmToken;
import com.asdeveloper.bloodmate.Models.User;
import com.asdeveloper.bloodmate.Models.UserHealth;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukeshsolanki.OtpView;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedResources {
    private static PhoneAuthProvider.ForceResendingToken resendingToken = null;
    public static void showHidePassword(ImageButton showPassBtn, EditText passwordTxt) {
        if (passwordTxt.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
            showPassBtn.setImageResource(R.drawable.visible);
            passwordTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            showPassBtn.setImageResource(R.drawable.invisible);
            passwordTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        passwordTxt.setSelection(passwordTxt.getText().length());
    }

    public static boolean isValidCredentials(EditText phoneTxt, EditText passwordTxt, Context context){
        int passwordError = -1;
        if(phoneTxt.getText().toString().isEmpty()){
            showError("Error", false, "Please enter your phone number", context, "").show();
            return false;
        }
        else if (phoneTxt.getText().toString().length() != 10 || phoneTxt.getText().toString().startsWith("0")){
            showError("Error", false, "Please enter a valid phone number", context, "").show();
            return false;
        }
        else if (passwordTxt.getText().toString().isEmpty()){
            showError("Error", false, "Please enter your password", context, "").show();
            return false;
        }
        else if (passwordTxt.getText().toString().length() < 8){
            showError("Error", false, "Password must be at least 8 characters", context, "").show();
            return false;
        }
        else if ((passwordError = isValidPassword(passwordTxt.getText().toString())) != 1){
            switch (passwordError) {
                case -1:
                    showError("Error", false, "Password must contain at least one uppercase letter", context, "").show();
                    break;
                case -2:
                    showError("Error", false, "Password must contain at least one lowercase letter", context,"").show();
                    break;
                case -3:
                    showError("Error", false, "Password must contain at least one digit", context, "").show();
                    break;
                case -4:
                    showError("Error", false, "Password must contain at least one special character", context, "").show();
                    break;
                default:
                    showError("Error", false, "Something went wrong", context, "").show();
            }
            return false;
        }
        return true;
    }

    public static boolean validatePersonalInfo(Context context, EditText nameTxt, EditText dobTxt, char gender, Spinner bloodGroupSpinner){
        if(nameTxt.getText().toString().trim().isEmpty()){
            showError("Name Error", false, "Please enter your name", context, "").show();
            return false;
        }
        else if (dobTxt.getText().toString().isEmpty()) {
            showError("Date Of Birth Error", false, "Please select your date of birth", context, "").show();
            return false;
        }
        else if (gender == 'a') {
            showError("Gender Error", false, "Please select your gender", context, "").show();
            return false;
        }
        else if (bloodGroupSpinner.getSelectedItemPosition() == 0) {
            showError("Blood Group Error", false, "Please select your blood group", context, "").show();
            return false;
        }
        return true;
    }

    public static boolean validateContactInfo(Context context, EditText phoneTxt, EditText emailTxt, EditText passwordTxt, EditText confirmPasswordTxt){
        if (!isValidCredentials(phoneTxt, passwordTxt, context)) {
            return false;
        }
        else if (!(confirmPasswordTxt.getText().toString().equals(passwordTxt.getText().toString()))){
            showError("Password Error", false, "Passwords does not match with confirm password", context, "").show();
            return false;
        }
        return true;
    }

    public static boolean validateRegistration(Context context, EditText weightTxt){
        if (weightTxt.getText().toString().isEmpty()){
            showError("Error", false, "Please enter your weight", context, "").show();
            return false;
        }
        return true;
    }
    public static int isValidPassword(String password){
        boolean hasUppercase = false, hasLowercase = false, hasDigit = false, hasSpecialChar = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                if (Character.isUpperCase(password.charAt(i))) {
                    hasUppercase = true;
                } else {
                    hasLowercase = true;
                }
            }
            else if(Character.isDigit(password.charAt(i))){
                hasDigit = true;
            }
            else{
                hasSpecialChar = true;
            }
            if (hasUppercase && hasLowercase && hasDigit && hasSpecialChar) {
                return 1;
            }
        }
        if (!hasUppercase) { return -1;}
        else if (!hasLowercase) { return -2;}
        else if (!hasDigit) { return -3;}
        else if (!hasSpecialChar) { return -4;}
        return  0;
    }

    public static MaterialDatePicker<Long> showDatePickerDialog(String title, Calendar selectDate, Long limitinmillis, FragmentManager fm) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(title)
                .setSelection(selectDate.getTimeInMillis())
                .setCalendarConstraints(new CalendarConstraints.Builder().setEnd(limitinmillis).build())
                .build();

        datePicker.show(fm, "DATE_PICKER");
        return datePicker;
    }
    public static BottomSheetDialog showError(String Title, Boolean haveSubtitle, String message, Context context, String SubTitle){
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.error_layout, null);
        dialog.setDismissWithAnimation(true);
        TextView error = view.findViewById(R.id.error_title);
        error.setText(Title);
        error = view.findViewById(R.id.error_description);
        error.setText(message);
        error = view.findViewById(R.id.error_subheading);
        if ((haveSubtitle)) {
            error.setVisibility(View.VISIBLE);
            error.setText(SubTitle);
        }
        else {
            error.setVisibility(View.GONE);
        }
        dialog.setContentView(view);
        return dialog;
    }

    public static long getId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("BloodMate", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isLoggedIn", false)){
            return sharedPreferences.getLong("userId", -1);
        }
        else {
            return -1;
        }
    }
    public static void setUserId(Context context, long id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("BloodMate", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putLong("userId", id);
        editor.apply();
    }

    public static void registerUser(Context context, User user, UserHealth userHealth){
        CustomProgress progress = new CustomProgress(context, "Registering You", "Please wait...", false);
        progress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<User> call = service.addUserData(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null){
                    User user = response.body();
                    setUserId(context, user.getId());
                    saveHealthInfo(context, user.getId(), userHealth, service);
                    progress.dismissDialog();
                }
                else {
                    showError("Error Occured", false, "We are unable to register you. Please try again later", context, "").show();
                    progress.dismissDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                progress.dismissDialog();
                Log.e("error", "onFailure: " + throwable.getMessage() );
                showError("Error Occured", false, "We are unable to register you. Please try again later", context, "").show();
            }
        });
    }

    public static void saveHealthInfo(Context context, Long id, UserHealth userHealth, Service service){
        CustomProgress progress = new CustomProgress(context, "Registering You", "Please wait...", false);
        progress.showDialog();
        Call<UserHealth> call = service.saveMedicalRecord(id, userHealth);
        call.enqueue(new Callback<UserHealth>() {
            @Override
            public void onResponse(@NonNull Call<UserHealth> call, @NonNull Response<UserHealth> response) {
                if (response.isSuccessful() && response.body() != null){
                    progress.dismissDialog();
                    Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, HomeActivity.class));
                }
                else {
                    progress.dismissDialog();
                    showError("Error Occured", false, "We are unable to register you. Please try again later", context, "").show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserHealth> call, @NonNull Throwable throwable) {
                progress.dismissDialog();
                showError("Error Occured", false, "We are unable to register you. Please try again later", context, "").show();
            }
        });
    }

    public static void showForgetPassword(Context context, String phone, OnForgetPasswordSubmit listener){
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.changepassword_layout, null);
        dialog.setDismissWithAnimation(true);
        dialog.setCancelable(false);
        dialog.setContentView(view);

        TextView tv = view.findViewById(R.id.forgetPasswordNumber);
        Button changeNum = view.findViewById(R.id.forgetPasswordChangeNumberBtn);
        Button submit = view.findViewById(R.id.changePasswordSubmit);
        ImageButton showPassword = view.findViewById(R.id.forgetShowPassBtn);
        ImageButton showConfirmPassword = view.findViewById(R.id.forgetShowConfirmPassBtn);
        EditText passwordET = view.findViewById(R.id.forgetPasswordET);
        EditText confirmPasswordET = view.findViewById(R.id.forgetConfirmPasswordET);
        String ph = "+91 " + phone;
        tv.setText(ph);
        changeNum.setOnClickListener(view1 -> {
            dialog.dismiss();
        });

        submit.setOnClickListener(view1 -> {
            if (isValidPassword(passwordET.getText().toString()) != 1){
                Toast.makeText(context, "Password is not valid", Toast.LENGTH_SHORT).show();
            }
            else{
                listener.onSubmit(passwordET.getText().toString(), confirmPasswordET.getText().toString(), passwordET.getText().toString().equals(confirmPasswordET.getText().toString()));
                if (passwordET.getText().toString().equals(confirmPasswordET.getText().toString()))
                    dialog.dismiss();
            }
        });

        showPassword.setOnClickListener(view1 -> {
            showHidePassword(showPassword, passwordET);
        });
        showConfirmPassword.setOnClickListener(view1 -> {
            showHidePassword(showConfirmPassword, confirmPasswordET);
        });
        dialog.show();
    }

    public static void updateUserPassword(Context context, String phone, String newPassword) {
        CustomProgress progress = new CustomProgress(context, "Updating Password", "Please wait...", false);
        progress.showDialog();

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), newPassword);

        Service service = Client.getClient().create(Service.class);
        Call<User> call = service.updatePassword(phone, requestBody);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull retrofit2.Response<User> response) {
                progress.dismissDialog();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                progress.dismissDialog();
                Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void showOTPView(Activity context, String phone, String verificationId, OnOtpVerificationListner listner){
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.otp_layout, null);
        dialog.setContentView(view);
        dialog.setDismissWithAnimation(true);
        dialog.setCancelable(false);

        TextView phoneTv = view.findViewById(R.id.otpPhoneNumber);
        OtpView otpView = view.findViewById(R.id.otpView);
        TextView resendOTP = view.findViewById(R.id.resendOtpTV);
        Button changeNumber = view.findViewById(R.id.changeNumberBtn);
        Button submitOtp = view.findViewById(R.id.verifyOtpBtn);
        countDownLabel(resendOTP);
        String ph = "+91 " + phone;
        phoneTv.setText(ph);
        submitOtp.setOnClickListener(view1 -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, Objects.requireNonNull(otpView.getText()).toString());
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            listner.onResult(true);
                        }
                        else {
                            listner.onResult(false);
                        }
                    });
        });
        resendOTP.setOnClickListener(view1 -> {
            Toast.makeText(context, "Resending OTP", Toast.LENGTH_SHORT).show();
            if (resendOTP.isEnabled()){
                SharedResources.otpVerification(context, phone, true, listner);
            }
            countDownLabel(resendOTP);
        });
        changeNumber.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void sendOtp(Activity context, String phone, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks, boolean isResend){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context)
                        .setCallbacks(callbacks);

        if (isResend && resendingToken != null) {
            builder.setForceResendingToken(resendingToken);
        }

        PhoneAuthProvider.verifyPhoneNumber(builder.build());
    }
    public static void isNumberRegistered(Context context, String phone, OnNumberCheckListener listener) {
        CustomProgress progress = new CustomProgress(context, "Please Wait", "We Are Fetching Your Details", false);
        progress.showDialog();

        Service service = Client.getClient().create(Service.class);
        Call<User> call = service.getUserByPhone(phone);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull retrofit2.Response<User> response) {
                progress.dismissDialog();
                if (response.isSuccessful() && response.body() != null && response.body().getPhone() != null) {
                    listener.onResult(true);
                } else {
                    listener.onResult(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                Log.e("error", Objects.requireNonNull(throwable.getMessage()));
                progress.dismissDialog();
                listener.onResult(false);
            }
        });
    }

    public static void countDownLabel(TextView textView) {
        textView.setEnabled(false);
        textView.setTextColor(Color.GRAY);
        CountDownTimer countDownTimer = new CountDownTimer(70000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {
                textView.setText("Resend OTP( " + l/60000 + "m : " + l/1000 % 60 + "s )");
            }

            @Override
            public void onFinish() {
                textView.setText("Resend OTP");
                textView.setTextColor(Color.BLUE);
                textView.setEnabled(true);
            }
        };
        countDownTimer.start();
    }
    public static void otpVerification(Activity context, String phone, boolean isResend, OnOtpVerificationListner listner){
        CustomProgress progress = new CustomProgress(context, "Please Wait.....", "We Are Sending You OTP For Phone Verification", false);
        progress.showDialog();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                listner.onResult(true);
                Toast.makeText(context, "Otp Verified", Toast.LENGTH_SHORT).show();
                progress.dismissDialog();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("otpVerification" , Objects.requireNonNull(e.getMessage()));
                listner.onResult(false);
                SharedResources.showError("Error", false, "We are unable to verify your number. Please try again later", context, "").show();
                progress.dismissDialog();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                resendingToken = forceResendingToken;
                Toast.makeText(context, "OTP Sended", Toast.LENGTH_SHORT).show();
                SharedResources.showOTPView(context, phone, s, isOtpVerified -> {
                    if (isOtpVerified){
                        Toast.makeText(context, "OTP Verified", Toast.LENGTH_SHORT).show();
                        listner.onResult(true);
                    }
                    else {
                        Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        listner.onResult(false);
                    }
                });
                progress.dismissDialog();
            }
        };
        SharedResources.sendOtp(context, phone, callbacks, isResend);

    }


//    public static void saveFcmToken(Context context, String token){
//        Service service = Client.getClient().create(Service.class);
//        Call<User> call = service.getUserById(SharedResources.getId(context));
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
//                if (response.isSuccessful() && response.body() != null){
//                    Call<String> call1 = service.setFcmToken(response.body().getFcmToken().getId());
//                    call1.enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
//                            Log.e("token", "onResponse: " + response );
//                        }
//
//                        @Override
//                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
//                            Log.e("token", "onFailure: " + throwable.getMessage());
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
//
//            }
//        });
//    }
}