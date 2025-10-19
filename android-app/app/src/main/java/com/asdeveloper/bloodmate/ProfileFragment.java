package com.asdeveloper.bloodmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Interfaces.OnForgetPasswordSubmit;
import com.asdeveloper.bloodmate.Interfaces.OnNumberCheckListener;
import com.asdeveloper.bloodmate.Interfaces.OnOtpVerificationListner;
import com.asdeveloper.bloodmate.Models.User;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    EditText nameET, emailET, phoneEt, dobET, bloodGroupET, genderET;
    Button editBtn, saveBtn, logoutBtn, changePasswordBtn, contactUsBtn;
    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents();
        setUserData();
        logoutBtn.setOnClickListener(v -> {
            SharedResources.setUserId(requireContext(), -1);
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });
        changePasswordBtn.setOnClickListener(v -> {
            SharedResources.showForgetPassword(requireContext(), user.getPhone(), (pass, confPass, isValid) -> {
                if (isValid){
                    SharedResources.updateUserPassword(requireContext(), user.getPhone(), pass);
                }
                else{
                    Toast.makeText(requireContext(), "Check Password And Confirm Password Again", Toast.LENGTH_SHORT).show();
                }
            });
        });
        contactUsBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));

            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"lukasfischer009@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "BloodMate Support Request");
            intent.putExtra(Intent.EXTRA_TEXT, "Phone Number : " + user.getPhone()+"\n\n");

            try {
                view1.getContext().startActivity(Intent.createChooser(intent, "Send email"));
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(view1.getContext(), "No email app installed.", Toast.LENGTH_SHORT).show();
            }
        });
        editBtn.setOnClickListener(view1 -> {
            nameET.setEnabled(true);
            emailET.setEnabled(true);
            phoneEt.setEnabled(true);
            logoutBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);
        });
        saveBtn.setOnClickListener(view1 -> {
            if (user.getPhone().equals(phoneEt.getText().toString())){
                SharedResources.otpVerification(requireActivity(), user.getPhone(), false, isOtpVerified -> {
                    if (isOtpVerified){
                        user.setName(nameET.getText().toString());
                        user.setEmailid(emailET.getText().toString());
                        user.setPhone(phoneEt.getText().toString());
                        updateProfile(user);
                    }
                    else {
                        Toast.makeText(requireContext(), "Incorrect Otp", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                SharedResources.isNumberRegistered(requireContext(), phoneEt.getText().toString(), isRegistered -> {
                    if (isRegistered){
                        Toast.makeText(requireContext(), "Phone Number Already Registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SharedResources.otpVerification(requireActivity(), user.getPhone(), false, isOtpVerified -> {
                            if (isOtpVerified){
                                user.setName(nameET.getText().toString());
                                user.setEmailid(emailET.getText().toString());
                                user.setPhone(phoneEt.getText().toString());
                                updateProfile(user);
                            }
                            else {
                                Toast.makeText(requireContext(), "Incorrect Otp", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void initializeComponents(){
        nameET = requireView().findViewById(R.id.userName);
        emailET = requireView().findViewById(R.id.userEmail);
        phoneEt = requireView().findViewById(R.id.userPhone);
        dobET = requireView().findViewById(R.id.userDOB);
        bloodGroupET = requireView().findViewById(R.id.userBloodGroup);
        genderET = requireView().findViewById(R.id.userGender);
        editBtn = requireView().findViewById(R.id.btnEditProfile);
        saveBtn = requireView().findViewById(R.id.btnSaveProfile);
        logoutBtn = requireView().findViewById(R.id.btnLogoutProfile);
        changePasswordBtn = requireView().findViewById(R.id.btnEditProfilePassword);
        contactUsBtn = requireView().findViewById(R.id.btnContactUs);
    }

    private void setUserData(){
        CustomProgress customProgress = new CustomProgress(getContext(), "Please Wait...", "We are fetching your account details", false);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<User> call = service.getUserById(SharedResources.getId(requireContext()));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null){
                    user = response.body();
                    nameET.setText(user.getName());
                    emailET.setText(user.getEmailid());
                    phoneEt.setText(user.getPhone());
                    Calendar dob = Calendar.getInstance();
                    dob.setTimeInMillis(user.getDob());
                    String dobStr = dob.get(Calendar.DAY_OF_MONTH) + "/" + (dob.get(Calendar.MONTH) + 1) + "/" + dob.get(Calendar.YEAR);
                    dobET.setText(dobStr);
                    bloodGroupET.setText(user.getBloodgroup());
                    if (user.getGender() == 'M')
                        genderET.setText("Male");
                    else if (user.getGender() == 'F')
                        genderET.setText("Female");
                    else
                        genderET.setText("Others");
                }
                else{
                    Toast.makeText(getContext(), "Failed to Load Your Profile", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Toast.makeText(getContext(), "Failed to Load Your Profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile(User u){
        CustomProgress customProgress = new CustomProgress(requireContext(), "Updating...", "Please Wait While We are updating your profile", false);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<User> call = service.updateUserData(user.getId(), u);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null){
                    user = response.body();
                    nameET.setText(user.getName());
                    emailET.setText(user.getEmailid());
                    phoneEt.setText(user.getPhone());
                    nameET.setEnabled(false);
                    emailET.setEnabled(false);
                    phoneEt.setEnabled(false);
                    logoutBtn.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.GONE);
                }
                else {
                    setUserData();
                    Toast.makeText(requireContext(), "Failed to Update Profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                setUserData();
                Toast.makeText(requireContext(), "Failed to Update Profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}