package com.asdeveloper.bloodmate.Retrofit;

import com.asdeveloper.bloodmate.Models.BloodRequests;
import com.asdeveloper.bloodmate.Models.Camps;
import com.asdeveloper.bloodmate.Models.User;
import com.asdeveloper.bloodmate.Models.UserHealth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    @GET("users/phone")
    Call<User> getUserByPhone(@Query("phone") String phone);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") Long userId);

    @POST("users")
    Call<User> addUserData(@Body User user);

    @PUT("users/{id}")
    Call<User> updateUserData(@Path("id") Long userId, @Body User user);

    @PUT("users/changePassword/{phone}")
    Call<User> updatePassword(@Path("phone") String phone, @Body RequestBody password);

    @GET("health-info/user/{userId}")
    Call<UserHealth> getMedicalRecord(@Path("userId") long userId);

    @POST("health-info/{userId}")
    Call<UserHealth> saveMedicalRecord(@Path("userId") long userId, @Body UserHealth record);

    @GET("blood-requests/{id}")
    Call<BloodRequests> getBloodRequestById(@Path("id") Long requestId);

    @GET("blood-requests/filter")
    Call<List<BloodRequests>> filterBloodRequests(
            @Query("city") String city,
            @Query("bloodGroup") String bloodGroup,
            @Query("urgencyLevel") String urgencyLevel,
            @Query("requesterId") Long requesterId,
            @Query("donorId") Long donorId,
            @Query("unassigned") Boolean unassigned
    );

    @POST("blood-requests/{requesterId}")
    Call<BloodRequests> addBloodRequest(@Path("requesterId") Long requesterId, @Body BloodRequests bloodRequest);

    @PUT("blood-requests/donationComplete/{id}")
    Call<BloodRequests> updateDonationStatus(@Path("id") Long requestId);

    @PUT("blood-requests/{requestId}/assign-donor/{donorId}")
    Call<BloodRequests> assignDonor(@Path("requestId") Long requestId, @Path("donorId") Long donorId);

    @PUT("blood-requests/{requestId}/unassign-donor")
    Call<BloodRequests> unassignDonor(@Path("requestId") Long requestId);

    @GET("/api/banners")
    Call<List<HashMap<String, Object>>> getBanners();

    @GET("/camps/filter")
    Call<List<Camps>> getCamps(
            @Query("title") String title,
            @Query("location") String location,
            @Query("city") String city,
            @Query("organizerBy") String organizerBy
    );

    @PUT("/api/notifications/saveToken/{id}")
    Call<String> setFcmToken(@Path("id") Long id);
}
