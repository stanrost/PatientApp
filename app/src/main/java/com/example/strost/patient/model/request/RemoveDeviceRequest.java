package com.example.strost.patient.model.request;

import com.example.strost.patient.model.entities.Device;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.strost.patient.application.ApplicationEx.restAdapter;

/**
 * Created by strost on 13-4-2017.
 */

public class RemoveDeviceRequest {

    public void removeDevice(String ObjectID){
        Call<ResponseBody> deleteRequest = restAdapter.deleteDevice(ObjectID);
        deleteRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // use response.code, response.headers, etc.
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // handle failure
            }
        });
    }
}
