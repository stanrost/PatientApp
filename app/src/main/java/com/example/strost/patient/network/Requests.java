package com.example.strost.patient.network;

import com.example.strost.patient.model.entities.Exercise;
import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.entities.PatientResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by strost on 20-3-2017.
 */

public interface Requests {

    final String BASE_URL = "https://api.backendless.com/v1/";

    @GET("data/Patient")
    Call<PatientResponse> getPatients();

    @GET("data/Device")
    Call<PatientResponse> getDevices();

    @GET("data/Patient/{request_id}")
    Call<Patient> getPatient(@Path("request_id") String requestId);

    @PUT("data/Exercise/{request_id}")
    Observable<Exercise> putRequest(@Path("request_id") String requestId, @Body Exercise requestBody);

    @PUT("data/Patient/{request_id}")
    Observable<Patient> putPatientRequest(@Path("request_id") String requestId, @Body Patient requestBody);

    @DELETE("data/Device/{request_id}")
    Call<ResponseBody> deleteDevice(@Path("request_id") String requestId);

}
