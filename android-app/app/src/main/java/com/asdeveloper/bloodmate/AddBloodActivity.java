package com.asdeveloper.bloodmate;

import static com.asdeveloper.bloodmate.R.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Models.BloodRequests;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBloodActivity extends AppCompatActivity {

    EditText etPatientName, etHospitalName, etLocation, etDescription, etNote;
    Spinner spinnerCity, spinnerBloodGroup, spinnerUrgency;
    Button btnSubmitRequest, btnDeadline;
    Calendar deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_blood);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeComponents();
        btnDeadline.setOnClickListener(view -> {
                    SharedResources.showDatePickerDialog("Select Deadline Date", Calendar.getInstance(), Calendar.getInstance().getTimeInMillis()+2592000000L, getSupportFragmentManager())
                            .addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onPositiveButtonClick(Long selection) {
                                    if (selection < Calendar.getInstance().getTimeInMillis()){
                                        Toast.makeText(AddBloodActivity.this, "Please select a future date", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        deadline = Calendar.getInstance();
                                        deadline.setTimeInMillis(selection);
                                        String date = deadline.get(Calendar.DAY_OF_MONTH) + "/" + (deadline.get(Calendar.MONTH) + 1) + "/" + deadline.get(Calendar.YEAR);
                                        btnDeadline.setText(date);
                                        btnDeadline.setBackgroundColor(color.green);
                                    }

                                }
                            });
                });
        btnSubmitRequest.setOnClickListener(v -> {
            if (isValidData()){
                BloodRequests bloodRequest = new BloodRequests();
                bloodRequest.setPatientName(etPatientName.getText().toString());
                bloodRequest.setHospitalName(etHospitalName.getText().toString());
                bloodRequest.setLocation(etLocation.getText().toString());
                bloodRequest.setCity(spinnerCity.getSelectedItem().toString());
                bloodRequest.setBloodGroup(spinnerBloodGroup.getSelectedItem().toString());
                bloodRequest.setUrgencyLevel(spinnerUrgency.getSelectedItem().toString());
                bloodRequest.setDonationCompleted(false);
                bloodRequest.setDescription(etDescription.getText().toString());
                bloodRequest.setDeadline(deadline.getTimeInMillis());
                bloodRequest.setNote(etNote.getText().toString());
                addBloodRequest(bloodRequest);
            }
        });
    }

    public void initializeComponents(){
        etPatientName = findViewById(R.id.etPatientName);
        etHospitalName = findViewById(R.id.etHospitalName);
        etLocation = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);
        etNote = findViewById(R.id.etNote);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        spinnerUrgency = findViewById(R.id.spinnerUrgency);
        btnDeadline = findViewById(R.id.btnDeadline);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
    }

    public boolean isValidData(){
        if (etPatientName.getText().toString().isEmpty()){
            SharedResources.showError("Invalid Name", false, "Please enter a valid Patient name", this, "").show();
            return false;
        }
        else if (etHospitalName.getText().toString().isEmpty()){
            SharedResources.showError("Invalid Hospital Name", false, "Please enter a valid hospital name", this, "").show();
            return false;
        }
        else if (etLocation.getText().toString().isEmpty()){
            SharedResources.showError("Invalid Location", false, "Please enter a valid location", this, "").show();
            return false;
        }
        else if (spinnerCity.getSelectedItemPosition() == 0){
            SharedResources.showError("Invalid City", false, "Please select a city", this, "").show();
            return false;
        }
        else if (spinnerBloodGroup.getSelectedItemPosition() == 0){
            SharedResources.showError("Invalid Blood Group", false, "Please select a blood group", this, "").show();
            return false;
        }
        else if (spinnerUrgency.getSelectedItemPosition() == 0){
            SharedResources.showError("Invalid Urgency Level", false, "Please select a urgency level", this, "").show();
            return false;
        }
        else if (deadline == null && btnDeadline.getText().equals("Select Deadline Date")) {
            SharedResources.showError("Invalid Deadline", false, "Please select a deadline", this, "").show();
            return false;
        }

        return true;
    }
    public void addBloodRequest(BloodRequests bloodRequest){
        CustomProgress progress = new CustomProgress(this, "Please wait...", "Adding blood request...", true);
        progress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<BloodRequests> call = service.addBloodRequest(SharedResources.getId(this), bloodRequest);
        call.enqueue(new Callback<BloodRequests>() {
            @Override
            public void onResponse(@NonNull Call<BloodRequests> call, @NonNull Response<BloodRequests> response) {
                if (response.isSuccessful() && response.body() != null){
                    progress.dismissDialog();
                    Toast.makeText(AddBloodActivity.this, "Blood request added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    progress.dismissDialog();
                    Toast.makeText(AddBloodActivity.this, "Failed to add blood request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BloodRequests> call, @NonNull Throwable throwable) {
                progress.dismissDialog();
                Toast.makeText(AddBloodActivity.this, "Failed to add blood request", Toast.LENGTH_SHORT).show();
            }
        });
    }
}