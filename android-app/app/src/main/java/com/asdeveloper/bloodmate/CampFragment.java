package com.asdeveloper.bloodmate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.asdeveloper.bloodmate.Adapter.CampsAdapter;
import com.asdeveloper.bloodmate.Dialogs.CustomProgress;
import com.asdeveloper.bloodmate.Models.Camps;
import com.asdeveloper.bloodmate.Retrofit.Client;
import com.asdeveloper.bloodmate.Retrofit.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampFragment extends Fragment {
    SearchView searchView;
    private Handler searchHandler;
    private Runnable searchRunnable;
    Spinner cities;
    private CampsAdapter campAdapter;
    private RecyclerView recyclerView;

    public CampFragment() {
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
        return inflater.inflate(R.layout.fragment_camp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView);
        searchHandler = new Handler();
        cities = view.findViewById(R.id.spinnerCities);
        recyclerView = view.findViewById(R.id.campsRecycler);
        campAdapter = new CampsAdapter(new ArrayList<>());
        recyclerView.setAdapter(campAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        setCampData("", "");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (cities.getSelectedItemPosition() == 0)
                    setCampData(query, null);
                else
                    setCampData(query, cities.getSelectedItem().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    if (cities.getSelectedItemPosition() == 0)
                        setCampData(newText, null);
                    else
                        setCampData(newText, cities.getSelectedItem().toString());
                };

                searchHandler.postDelayed(searchRunnable, 1000);
                return true;
            }
        });
    }
    public void setCampData(String text, String city){
        CustomProgress customProgress = new CustomProgress(requireContext(), "Please Wait", "Updating Camps Details", true);
        customProgress.showDialog();
        Service service = Client.getClient().create(Service.class);
        Call<List<Camps>> call = service.getCamps(text, text, city, text);
        call.enqueue(new Callback<List<Camps>>() {
            @Override
            public void onResponse(@NonNull Call<List<Camps>> call, @NonNull Response<List<Camps>> response) {
                customProgress.dismissDialog();
                if (response.isSuccessful() && response.body() != null)
                    campAdapter.updateList(response.body());
                else
                    Toast.makeText(requireContext(), "Failed to Load Camps", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<List<Camps>> call, @NonNull Throwable throwable) {
                customProgress.dismissDialog();
                Toast.makeText(requireContext(), "Failed to Load Camps", Toast.LENGTH_SHORT).show();
            }
        });
    }
}