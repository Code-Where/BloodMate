package com.asdeveloper.bloodmate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asdeveloper.bloodmate.Adapter.BloodRequestsAdapter;
import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Models.BloodRequests;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PreviousDonationsActivity extends AppCompatActivity {
    RecyclerView donationRecyclerView;
    BloodRequestsAdapter adapter;
    ChipNavigationBar tabBar;
    TextView txtPreviousDonations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_previous_donations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tabBar = findViewById(R.id.tabBar);
        donationRecyclerView = findViewById(R.id.donationRecyclerView);
        donationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtPreviousDonations = findViewById(R.id.txtPreviousDonations);
        adapter = new BloodRequestsAdapter(new ArrayList<>(), this);
        setDonations(false);
        tabBar.setItemSelected(R.id.requestsMenu, true);
        navigationBarAction();

    }

    public void navigationBarAction(){
        tabBar.setOnItemSelectedListener(i -> {
            setDonations(i != R.id.requestsMenu);
        });
    }

    @SuppressLint("SetTextI18n")
    public void setDonations(Boolean isDonations){
        CustomProgress customProgress = new CustomProgress(this, "Please Wait...", "We are fetching your donations", false);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<List<BloodRequests>> call = null;
        if (!isDonations) {
            txtPreviousDonations.setText("Previous Requests");
            call = service.filterBloodRequests(null, null, null, SharedResources.getId(this), null, null);
        }
        else {
            txtPreviousDonations.setText("Previous Donations");
            call = service.filterBloodRequests(null, null, null, null, SharedResources.getId(this), null);
        }
        call.enqueue(new retrofit2.Callback<List<BloodRequests>>() {
            @Override
            public void onResponse(@NonNull Call<List<BloodRequests>> call, @NonNull Response<List<BloodRequests>> response) {
                if (response.isSuccessful() && response.body() != null){
                    customProgress.dismissDialog();
                    if (response.body().size() > 0){
                        adapter.updateList(response.body());
                        donationRecyclerView.setAdapter(adapter);
                    }
                    else {
                        adapter.updateList(response.body());
                            Toast.makeText(PreviousDonationsActivity.this, "No Blood " + (isDonations ?"Donations" :"Requests") +" Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    customProgress.dismissDialog();
                    Toast.makeText(PreviousDonationsActivity.this, "Failed to Load Donations/Requests", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BloodRequests>> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Toast.makeText(PreviousDonationsActivity.this, "Failed to Load Donations/Requests", Toast.LENGTH_SHORT).show();
            }
        });
    }
}