package com.asdeveloper.bloodmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.asdeveloper.bloodmate.Adapter.BloodRequestsAdapter;
import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Models.BloodRequests;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    ImageSlider imageSlider;
    BloodRequestsAdapter adapter;
    RecyclerView recyclerView;
    Spinner citiesSpinner, urgencySpinner, bloodTypeSpinner;
    static String city, bloodGroup, urgencyLevel;
    Button btnAddBlood, previousDonations;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askNotificationPermission();
        initializeComponents();
        addSlideImages();
        findBloodRequests(true);
        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    city = adapterView.getItemAtPosition(i).toString();
                }
                else {
                    city = null;
                }
                findBloodRequests(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        urgencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    urgencyLevel = adapterView.getItemAtPosition(i).toString();
                }
                else {
                    urgencyLevel = null;
                }
                findBloodRequests(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bloodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    bloodGroup = adapterView.getItemAtPosition(i).toString();
                }
                else {
                    bloodGroup = null;
                }
                findBloodRequests(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnAddBlood.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddBloodActivity.class));
        });
        previousDonations.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PreviousDonationsActivity.class));
        });

    }

    public void initializeComponents(){
        imageSlider = requireView().findViewById(R.id.image_slider);
        recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BloodRequestsAdapter(new ArrayList<>(), requireContext());
        citiesSpinner = requireView().findViewById(R.id.citiesSpinner);
        urgencySpinner = requireView().findViewById(R.id.urgencySpinner);
        bloodTypeSpinner = requireView().findViewById(R.id.bloodTypeSpinner);
        btnAddBlood = requireView().findViewById(R.id.addRequestButton);
        previousDonations = requireView().findViewById(R.id.viewDonations);
    }

    public void addSlideImages(){
        CustomProgress customProgress = new CustomProgress(getContext(), "Please Wait...", "We are fetching banners...", false);
        customProgress.showDialog();
        List<SlideModel> images = new ArrayList<>();
        Service service = Client.getClient().create(Service.class);
        Call<List<HashMap<String, Object>>> call = service.getBanners();
        call.enqueue(new Callback<List<HashMap<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<List<HashMap<String, Object>>> call, @NonNull Response<List<HashMap<String, Object>>> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null){
                    for (Map<String, Object> banner: response.body()) {
                        Log.e("Banner", (String) Objects.requireNonNull(banner.get("url")));
                        images.add(new SlideModel(Objects.requireNonNull(banner.get("url")).toString(), ScaleTypes.FIT));
                    }
                    imageSlider.setImageList(images);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HashMap<String, Object>>> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Log.e("error", "onFailure: " + throwable.getMessage());
                Toast.makeText(getContext(), "Failed to Load Banners", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void findBloodRequests(Boolean unassigned){
        CustomProgress customProgress = new CustomProgress(getContext(), "Please Wait...", "We are fetching blood requests...", false);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<List<BloodRequests>> call = service.filterBloodRequests(city, bloodGroup, urgencyLevel, null, null, unassigned);
        call.enqueue(new Callback<List<BloodRequests>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<BloodRequests>> call, @NonNull Response<List<BloodRequests>> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().size() > 0){
                        adapter.updateList(response.body());
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        adapter.updateList(response.body());
                        Toast.makeText(getContext(), "No blood requests found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BloodRequests>> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Log.e("Error", Objects.requireNonNull(throwable.getMessage()));
                Toast.makeText(getContext(), "Unable to fetch blood requests", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }

}