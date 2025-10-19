package com.asdeveloper.bloodmate;




import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.asdeveloper.bloodmate.Models.User;
import com.asdeveloper.bloodmate.Models.UserHealth;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    ImageButton dobBtn, showPasswordBtn, showConfirmPasswordBtn, lastDonationBtn, prevLayoutBtn;
    EditText nameET, phoneET, emailET, dobET, passwordET, confirmPasswordET, lastDonationET, weightET;
    RadioGroup genderRG;
    TextView goToLoginBtn;
    Button registerBtn;
    MaterialSwitch surgerySwitch, willingToDonateSwitch;
    CheckBox termsCheckBox, diabetesCheckBox, heartDiseaseCheckBox, hypertensionCheckBox, asthmaCheckBox, thyroidCheckBox;
    Spinner bloodGroupSpinner;
    Calendar selectedDate;
    char gender = 'a';
    byte visibleLayout = 0;
    private String otp;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();

        genderRG.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.maleRadio) {
                gender = 'M';
            }
            else if (checkedId == R.id.femaleRadio) {
                gender = 'F';
            }
            else if (checkedId == R.id.otherRadio) {
                gender = 'O';
            }
        });

        dobBtn.setOnClickListener(v -> {
            showCalenderDialog(dobET);
        });

        lastDonationBtn.setOnClickListener(view -> {
            showCalenderDialog(lastDonationET);
        });


        showPasswordBtn.setOnClickListener(view -> {
            SharedResources.showHidePassword(showPasswordBtn, passwordET);
        });

        showConfirmPasswordBtn.setOnClickListener(view -> {
            SharedResources.showHidePassword(showConfirmPasswordBtn, confirmPasswordET);
        });

        offWillingToDonate();

        registerBtn.setOnClickListener(view -> {
            if(visibleLayout == 0 && SharedResources.validatePersonalInfo(this, nameET, dobET, gender, bloodGroupSpinner)){
                visibleLayout++;
                ShowHideLayouts();
            }
            else if (visibleLayout == 2 && SharedResources.validateContactInfo(this, phoneET, emailET, passwordET, confirmPasswordET)){
                User user = new User();
                user.setName(nameET.getText().toString());
                user.setDob(selectedDate.getTimeInMillis());
                user.setGender(gender);
                user.setBloodgroup((String) bloodGroupSpinner.getSelectedItem());
                if (!lastDonationET.getText().toString().isEmpty()){
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar donationDate = simpleDateFormat.getCalendar();
                    user.setLastdonation(donationDate.getTimeInMillis());
                }
                user.setPhone(phoneET.getText().toString());
                user.setPassword(passwordET.getText().toString());
                if (!emailET.getText().toString().isEmpty()){
                    user.setEmailid(emailET.getText().toString());
                }
                SharedResources.otpVerification(this, user.getPhone(), false, isOtpVerified -> {
                    if (isOtpVerified){
                        SharedResources.registerUser(RegisterActivity.this, user, new UserHealth(diabetesCheckBox.isChecked(), hypertensionCheckBox.isChecked(), asthmaCheckBox.isChecked(), thyroidCheckBox.isChecked(), heartDiseaseCheckBox.isChecked(), Integer.parseInt(weightET.getText().toString()), surgerySwitch.isChecked(), willingToDonateSwitch.isChecked()));
                    }
                });
            }
            else if(visibleLayout == 1 && SharedResources.validateRegistration(this, weightET)) {
                visibleLayout++;
                ShowHideLayouts();
            }
        });

        prevLayoutBtn.setOnClickListener(view -> {
            if (visibleLayout > 0){
                visibleLayout--;
                ShowHideLayouts();
            }
        });

        goToLoginBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });





    }
    @Override
    public void onBackPressed() {
        if(visibleLayout == 0){
            super.onBackPressed();
        }
        else {
            visibleLayout--;
            ShowHideLayouts();
        }
    }

    private void initializeComponents(){
        nameET = findViewById(R.id.registerNameTxt);
        dobET = findViewById(R.id.registerDobTxt);
        dobBtn = findViewById(R.id.showCalenderBtn);
        genderRG = findViewById(R.id.radioGroup);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSelection);
        phoneET = findViewById(R.id.registerNumberTxt);
        emailET = findViewById(R.id.registerEmailTxt);
        passwordET = findViewById(R.id.registerPasswordTxt);
        showPasswordBtn = findViewById(R.id.registerShowPassBtn);
        confirmPasswordET = findViewById(R.id.registerConfirmPasswordTxt);
        showConfirmPasswordBtn = findViewById(R.id.showConfirmPassBtn);
        lastDonationET = findViewById(R.id.registerLastDonationTxt);
        lastDonationBtn = findViewById(R.id.lastDonationCalanederBtn);
        diabetesCheckBox = findViewById(R.id.diabetesCheckBox);
        heartDiseaseCheckBox = findViewById(R.id.heartCheckBox);
        hypertensionCheckBox = findViewById(R.id.hypertensionCheckBox);
        asthmaCheckBox = findViewById(R.id.asthmaCheckBox);
        thyroidCheckBox = findViewById(R.id.thyroidCheckBox);
        weightET = findViewById(R.id.registerWeightTxt);
        surgerySwitch = findViewById(R.id.surgerySwitch);
        willingToDonateSwitch = findViewById(R.id.willingToDonateSwitch);
        termsCheckBox = findViewById(R.id.checkBox6);
        prevLayoutBtn = findViewById(R.id.registerPrevBtn);
        registerBtn = findViewById(R.id.registerBtn);
        goToLoginBtn = findViewById(R.id.loginHeretxt);
        selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(Calendar.getInstance().getTimeInMillis()-568025136000L);
    }
    private void ShowHideLayouts(){
        switch (visibleLayout){
            case 0 :
                findViewById(R.id.registerInformationContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.registerContactContainer).setVisibility(View.GONE);
                findViewById(R.id.registerHealthContainer).setVisibility(View.GONE);
                termsCheckBox.setVisibility(View.GONE);
                prevLayoutBtn.setVisibility(View.GONE);
                registerBtn.setText("Next");
                break;
            case 1 :
                findViewById(R.id.registerInformationContainer).setVisibility(View.GONE);
                findViewById(R.id.registerContactContainer).setVisibility(View.GONE);
                findViewById(R.id.registerHealthContainer).setVisibility(View.VISIBLE);
                termsCheckBox.setVisibility(View.GONE);
                prevLayoutBtn.setVisibility(View.VISIBLE);
                registerBtn.setText("Next");
                break;
            case 2 :
                findViewById(R.id.registerInformationContainer).setVisibility(View.GONE);
                findViewById(R.id.registerContactContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.registerHealthContainer).setVisibility(View.GONE);
                termsCheckBox.setVisibility(View.VISIBLE);
                prevLayoutBtn.setVisibility(View.VISIBLE);
                registerBtn.setText("Submit");
                break;
            default:
                Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void showCalenderDialog(EditText ed){
        MaterialDatePicker<Long> datePicker = SharedResources.showDatePickerDialog("Date Of Birth", selectedDate, Calendar.getInstance().getTimeInMillis()-568025136000L, getSupportFragmentManager());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate.setTimeInMillis(selection);
            String SelectDate = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" + (selectedDate.get(Calendar.MONTH) + 1) + "/" + selectedDate.get(Calendar.YEAR);
            ed.setText(SelectDate);
        });
    }
    private void offWillingToDonate(){
        surgerySwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                willingToDonateSwitch.setChecked(false);
            }
        });
        diabetesCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                willingToDonateSwitch.setChecked(false);
            }
        });
        heartDiseaseCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                willingToDonateSwitch.setChecked(false);
            }
        });
        hypertensionCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                willingToDonateSwitch.setChecked(false);
            }
        });
        asthmaCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                willingToDonateSwitch.setChecked(false);
            }
        });
        thyroidCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                willingToDonateSwitch.setChecked(false);
            }
        });
        willingToDonateSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                if (surgerySwitch.isChecked() || diabetesCheckBox.isChecked() || heartDiseaseCheckBox.isChecked() || hypertensionCheckBox.isChecked() || asthmaCheckBox.isChecked() || thyroidCheckBox.isChecked()){
                    willingToDonateSwitch.setChecked(false);
                    SharedResources.showError("Donation Warning", false, "You Cannot Donate as you have recent surgery or diseases", this, "").show();
                }
                else {
                    willingToDonateSwitch.setChecked(true);
                }
            }
        });
    }


}