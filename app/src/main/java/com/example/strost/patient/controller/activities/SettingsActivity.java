package com.example.strost.patient.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.DataPermission;
import com.backendless.Messaging;
import com.example.strost.patient.R;
import com.example.strost.patient.SaveSharedPreference;
import com.example.strost.patient.model.entities.Device;
import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.request.RemoveDeviceRequest;


/**
 * Created by strost on 28-2-2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private Patient mPatient;
    private final String PATIENT_KEY = "Patient";
    private final String EMAIL_KEY = "Email";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);

        final Button logoutButton = (Button) findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialog();
            }
        });

        Button changePasswordButton = (Button) findViewById(R.id.btnChangePassword);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               goToChangePassword();
            }
        });
    }

    public void goToChangePassword(){
        Intent detailIntent = new Intent(this, ChangePasswordActivity.class);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        startActivity(detailIntent);
    }

    public void openDialog() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        logout();
                        Toast.makeText(SettingsActivity.this, getString(R.string.logged_out), Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_logout)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }


    public void logout(){
        removeDevice();
        SaveSharedPreference sp = new SaveSharedPreference();
        sp.setCaretakerId(this, "");
        Intent detailIntent = new Intent(this, LoginActivity.class);
        detailIntent.putExtra(EMAIL_KEY, mPatient.getEmail());
        startActivity(detailIntent);
        finish();
    }

    public void removeDevice(){
        String objectId = "";
        for (Device d : mPatient.getDevices()){
            if (d.getDeviceId().equals(Messaging.DEVICE_ID)){
                objectId = d.getObjectId();
            }
        }


        if (!objectId.equals("")){
            RemoveDeviceRequest rDR = new RemoveDeviceRequest();
            rDR.removeDevice(objectId);
        }


    }

    public void goBack(){
        Intent detailIntent = new Intent(this, MainPageActivity.class);
        detailIntent.putExtra(PATIENT_KEY, mPatient);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        finish();
        return;
    }
}
