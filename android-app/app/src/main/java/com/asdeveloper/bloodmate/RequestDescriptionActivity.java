package com.asdeveloper.bloodmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Models.BloodRequests;
import com.asdeveloper.bloodmate.Models.User;
import com.asdeveloper.bloodmate.Models.UserHealth;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDescriptionActivity extends AppCompatActivity {
    TextView patientName, urgencyLevel, requiredBlood, requestCreatedOn, note, description, location, hospitalName, donorName, donorGender, donorBloodGroup, headName, headBloodGroup, headElligibility, requestStatus;
    String donorPhone;
    Button acceptRequest, callDonorBtn, bloodRecievedBtn, unacceptRequest;
    ImageButton backBtn;
    LinearLayout donorContainer;
    BloodRequests bloodRequests;
    Long requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeComponents();
        requestId = getIntent().getLongExtra("requestId", -1);
        setRequestData(requestId);
        backBtn.setOnClickListener(v -> finish());
        callDonorBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + donorPhone));
            startActivity(intent);
        });
        acceptRequest.setOnClickListener(view -> {
            acceptRequest();
        });
        bloodRecievedBtn.setOnClickListener(view -> {
            updateDonationStatus();
        });
        unacceptRequest.setOnClickListener(view -> {
            unAcceptRequest();
        });
    }
    public void initializeComponents(){
        patientName = findViewById(R.id.patientNameTxt);
        urgencyLevel = findViewById(R.id.urgencyLevelTxt);
        requiredBlood = findViewById(R.id.requiredBloodTxt);
        requestCreatedOn = findViewById(R.id.requestDateTxt);
        requestCreatedOn = findViewById(R.id.requestDateTxt);
        note = findViewById(R.id.noteTxt);
        description = findViewById(R.id.descriptionTxt);
        hospitalName = findViewById(R.id.hospitalNameTxt);
        location = findViewById(R.id.locationTxt);
        acceptRequest = findViewById(R.id.btnAcceptRequest);
        backBtn = findViewById(R.id.goBackBtn);
        donorName = findViewById(R.id.donorNameTxt);
        donorGender = findViewById(R.id.donorGenderTxt);
        donorBloodGroup = findViewById(R.id.donorBloodGroupTxt);
        headName = findViewById(R.id.headNameTxt);
        headBloodGroup = findViewById(R.id.headBloodTxt);
        headElligibility = findViewById(R.id.headDonorElligibility);
        callDonorBtn = findViewById(R.id.contactDonorBtn);
        donorContainer = findViewById(R.id.donorDetailsContainer);
        bloodRecievedBtn = findViewById(R.id.btnDonationDone);
        unacceptRequest = findViewById(R.id.btnUnAccept);
        requestStatus = findViewById(R.id.donationStatusTxt);
    }
    public void setRequestData(Long requestID){
        CustomProgress customProgress = new CustomProgress(RequestDescriptionActivity.this, "Please Wait...", "We are fetching your request details", false);
        customProgress.showDialog();
        if (requestID != -1){
            Service service = Client.getClient().create(Service.class);
            Call<BloodRequests> call = service.getBloodRequestById(requestID);
            call.enqueue(new Callback<BloodRequests>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<BloodRequests> call, @NonNull Response<BloodRequests> response) {
                    customProgress.dismissDialog();
                    if (response.isSuccessful() && response.body() != null){
                        bloodRequests = response.body();
                        getUserData();
                        patientName.setText(bloodRequests.getPatientName());
                        urgencyLevel.setText(bloodRequests.getUrgencyLevel());
                        requiredBlood.setText(bloodRequests.getBloodGroup());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(bloodRequests.getRequestDate());
                        requestCreatedOn.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                        note.setText(bloodRequests.getNote());
                        description.setText(bloodRequests.getDescription());
                        hospitalName.setText(bloodRequests.getHospitalName());
                        location.setText(bloodRequests.getLocation() + ", " + bloodRequests.getCity());
                        requestStatus.setText(bloodRequests.getDonationCompleted() ? "Completed" : "Pending");
                        if (bloodRequests.getDonor() != null){

                            if (bloodRequests.getRequester().getId() == SharedResources.getId(RequestDescriptionActivity.this)) {
                                donorContainer.setVisibility(LinearLayout.VISIBLE);
                                donorName.setText(bloodRequests.getDonor().getName());
                                String gender = "";
                                switch (bloodRequests.getDonor().getGender()){
                                    case 'M':
                                        gender = "Male";
                                        break;
                                    case 'F':
                                        gender = "Female";
                                        break;
                                    default:
                                        gender = "Other";
                                        break;
                                }
                                donorGender.setText(gender);
                                donorBloodGroup.setText(bloodRequests.getDonor().getBloodgroup());
                                donorPhone = bloodRequests.getDonor().getPhone();
                                acceptRequest.setVisibility(View.GONE);
                                unacceptRequest.setVisibility(View.GONE);
                                if (bloodRequests.getDonationCompleted()) {
                                    bloodRecievedBtn.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    bloodRecievedBtn.setVisibility(View.GONE);
                                }

                            }
                            else if (bloodRequests.getDonor().getId() == SharedResources.getId(RequestDescriptionActivity.this)){
                                donorContainer.setVisibility(LinearLayout.VISIBLE);
                                acceptRequest.setVisibility(View.GONE);
                                bloodRecievedBtn.setVisibility(View.GONE);
                                if (bloodRequests.getDonationCompleted()) {
                                    unacceptRequest.setVisibility(View.GONE);
                                }
                                else {
                                    unacceptRequest.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        else {
                            donorContainer.setVisibility(LinearLayout.GONE);
                            if (bloodRequests.getRequester().getId() == SharedResources.getId(RequestDescriptionActivity.this)){
                                acceptRequest.setVisibility(View.GONE);
                                bloodRecievedBtn.setVisibility(View.GONE);
                                unacceptRequest.setVisibility(View.GONE);
                            }
                            else {
                                acceptRequest.setVisibility(View.VISIBLE);
                                bloodRecievedBtn.setVisibility(View.GONE);
                                unacceptRequest.setVisibility(View.GONE);
                            }
                        }
                    }
                    else {
                        customProgress.dismissDialog();
                        Toast.makeText(RequestDescriptionActivity.this, "Failed to load data1", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BloodRequests> call, @NonNull Throwable throwable) {
                    Log.e("TAG", "onResponse: " + throwable.getMessage());
                    Toast.makeText(RequestDescriptionActivity.this, "Failed to load data2" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else {
            customProgress.dismissDialog();
            Toast.makeText(RequestDescriptionActivity.this, "Failed to load data3", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getUserData(){
        CustomProgress customProgress = new CustomProgress(RequestDescriptionActivity.this, "Please Wait...", "We are fetching your account details", false);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<UserHealth> call = service.getMedicalRecord(SharedResources.getId(RequestDescriptionActivity.this));
        call.enqueue(new Callback<UserHealth>() {
            @Override
            public void onResponse(@NonNull Call<UserHealth> call, @NonNull Response<UserHealth> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null){
                    headName.setText(response.body().getUser().getName());
                    headBloodGroup.setText(response.body().getUser().getBloodgroup());
                    if (response.body().isWillDonate() && isCompatible(response.body().getUser().getBloodgroup(), bloodRequests.getBloodGroup())){
                        headElligibility.setText("Elligible");
                        acceptRequest.setEnabled(true);
                    }
                    else {
                        headElligibility.setText("Not Elligible");
                        acceptRequest.setEnabled(false);
                    }
                }
                else {
                    Toast.makeText(RequestDescriptionActivity.this, "Failed to load data4"+response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserHealth> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Log.e("TAG", "onResponse: " + throwable.getMessage());
                Toast.makeText(RequestDescriptionActivity.this, "Failed to load data5" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public boolean isCompatible(String donor, String recipient) {
        switch (recipient) {
            case "A+":
                return donor.equals("A+") || donor.equals("A-") || donor.equals("O+") || donor.equals("O-");
            case "A-":
                return donor.equals("A-") || donor.equals("O-");
            case "B+":
                return donor.equals("B+") || donor.equals("B-") || donor.equals("O+") || donor.equals("O-");
            case "B-":
                return donor.equals("B-") || donor.equals("O-");
            case "AB+":
                return true; // Universal recipient
            case "AB-":
                return donor.equals("A-") || donor.equals("B-") || donor.equals("AB-") || donor.equals("O-");
            case "O+":
                return donor.equals("O+") || donor.equals("O-");
            case "O-":
                return donor.equals("O-");
            default:
                return false;
        }
    }

    public void acceptRequest(){
        CustomProgress customProgress = new CustomProgress(RequestDescriptionActivity.this, "Please Wait...", "We are accepting your request", false);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<BloodRequests> call = service.assignDonor(bloodRequests.getId(), SharedResources.getId(RequestDescriptionActivity.this));
        call.enqueue(new Callback<BloodRequests>() {
            @Override
            public void onResponse(@NonNull Call<BloodRequests> call, @NonNull Response<BloodRequests> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(RequestDescriptionActivity.this, "Request Accepted Successfully", Toast.LENGTH_SHORT).show();
                    setRequestData(requestId);
                }
                else {
                    Toast.makeText(RequestDescriptionActivity.this, "Failed to accept request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BloodRequests> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Toast.makeText(RequestDescriptionActivity.this, "Failed to accept request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unAcceptRequest(){
        CustomProgress customProgress = new CustomProgress(RequestDescriptionActivity.this, "Please Wait...", "We are Unaccepting your request", false);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<BloodRequests> call = service.unassignDonor(bloodRequests.getId());
        call.enqueue(new Callback<BloodRequests>() {
            @Override
            public void onResponse(@NonNull Call<BloodRequests> call, @NonNull Response<BloodRequests> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(RequestDescriptionActivity.this, "Request Unaccepted Successfully", Toast.LENGTH_SHORT).show();
                    setRequestData(requestId);
                }
                else {
                    Toast.makeText(RequestDescriptionActivity.this, "Failed to unaccept request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BloodRequests> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Toast.makeText(RequestDescriptionActivity.this, "Failed to unaccept request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDonationStatus(){
        Service service = Client.getClient().create(Service.class);
        Call<BloodRequests> call = service.updateDonationStatus(bloodRequests.getId());
        call.enqueue(new Callback<BloodRequests>() {
            @Override
            public void onResponse(@NonNull Call<BloodRequests> call, @NonNull Response<BloodRequests> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(RequestDescriptionActivity.this, "Blood Recieved Status Updated", Toast.LENGTH_SHORT).show();
                    setRequestData(requestId);
                }
                else {
                    Toast.makeText(RequestDescriptionActivity.this, "Failed to update blood recieved status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BloodRequests> call, @NonNull Throwable throwable) {
                Toast.makeText(RequestDescriptionActivity.this, "Failed to update blood recieved status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}