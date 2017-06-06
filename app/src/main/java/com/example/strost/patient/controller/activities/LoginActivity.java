package com.example.strost.patient.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Messaging;
import com.example.strost.patient.ForgotPassword;
import com.example.strost.patient.PasswordEncryption;
import com.example.strost.patient.R;
import com.example.strost.patient.network.RegisterDevice;
import com.example.strost.patient.SaveSharedPreference;
import com.example.strost.patient.model.entities.Device;
import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.request.GetPatientsRequest;
import com.example.strost.patient.model.request.PutPatientRequest;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private List<Patient> mPatients = new ArrayList<Patient>();
    private List<String> mEmailList = new ArrayList<String>();

    private EditText mEmailText, mPasswordText;
    private Button mLoginButton;
    private TextView mForgotPassword;
    private String mEmail = "";

    private final String EMAIL_KEY = "Email";
    private final String PATIENT_KEY = "Patient";
    private final String PASSWORD_KEY = "Password";
    private final String UNKNOWN_DEVICE_ID_KEY = "unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = getIntent().getStringExtra(EMAIL_KEY);
        mEmailText = (EditText) findViewById(R.id.etEmail);
        mPasswordText = (EditText) findViewById(R.id.etPassword);
        mLoginButton = (Button) findViewById(R.id.btnLogin);
        mForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        mEmailText.setText(mEmail);

        getPatients();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextPage();
            }
        });
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                forgotPasswordAction();
            }
        });
    }

    private void forgotPasswordAction() {
        final ForgotPassword fP = new ForgotPassword();

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater= this.getLayoutInflater();
        View layout=inflater.inflate(R.layout.dialog_forgotpassword, null);
        builder.setView(layout);
        final EditText emailAdress =(EditText)layout.findViewById(R.id.etSetEmail);
        final TextView error =(TextView)layout.findViewById(R.id.tvErrorMessage);
        emailAdress.setText(mEmailText.getText().toString());
        builder.setMessage(getString(R.string.email_will_be_send_to))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }
                ).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }});

        final AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;

                mPasswordText.setText("");
                if(emailAdress.getText().toString().contains("@") && emailAdress.getText().toString().contains(".") && mEmailList.contains(emailAdress.getText().toString())) {
                    fP.forgotPassword(emailAdress.getText().toString(), mPatients, getApplicationContext());
                    Toast.makeText(LoginActivity.this, getString(R.string.email_is_send_to) + " " + emailAdress.getText().toString(), Toast.LENGTH_LONG).show();
                    wantToCloseDialog = true;
                }
                else{
                    error.setText(getString(R.string.email_is_wrong));
                }

                if(wantToCloseDialog)
                    alert.dismiss();

            }
        });
    }

    public void getPatients(){

        GetPatientsRequest gPR = new GetPatientsRequest();
        mPatients = gPR.getPatients();

        for (Patient p : mPatients) {
            mEmailList.add(p.getEmail());
        }
    }

    public void nextPage(){
        int registred = 0;
        String patientObjectId = "";
        PasswordEncryption pE = new PasswordEncryption();
        Patient mPatient = null;
        for (int i = 0; i < mPatients.size(); i++) {
            if (mEmailText.getText().toString().equals(mPatients.get(i).getEmail()) && pE.encryptPassword(mPasswordText.getText().toString()).equals(mPatients.get(i).getPassword())) {
                registred++;
                patientObjectId = mPatients.get(i).getObjectId();
                mPatient = mPatients.get(i);

            }
        }
        if(registred > 0) {
            if(!mPatient.getDataPolicyAccepted()){
                accecptPolicyDialog(mPatient);
            }
            else {
                chooseNextPage(mPatient);
            }


        }
        else{
            Toast.makeText(LoginActivity.this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
        }
    }

    public void accecptPolicyDialog(final Patient rightPatient){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.data_policy))
                .setMessage(getString(R.string.data_policy_discription))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }
                ).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }});

        final AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rightPatient.setDataPolicyAccepted(true);
                final PutPatientRequest ppr = new PutPatientRequest();
                Runnable runnable = new Runnable() {
                    public void run() {
                        ppr.updatePatient(rightPatient);
                    }
                };
                Thread mythread = new Thread(runnable);
                mythread.start();
                try {
                    mythread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                chooseNextPage(rightPatient);
            }
        });
    }

    public void chooseNextPage(Patient rightPatient){

        if(rightPatient.getChangedGeneratedPassword()) {
            Intent detailIntent = new Intent(this, MainPageActivity.class);
            detailIntent.putExtra(PATIENT_KEY, rightPatient);
            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_LONG).show();
            startActivity(detailIntent);
        }
        else if(!rightPatient.getChangedGeneratedPassword()) {
            Intent detailIntent = new Intent(this, ChangePasswordActivity.class);
            detailIntent.putExtra(PATIENT_KEY, rightPatient);
            detailIntent.putExtra(PASSWORD_KEY, mPasswordText.getText().toString());
            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_LONG).show();
            startActivity(detailIntent);
        }
        SaveSharedPreference sp = new SaveSharedPreference();
        setDevice(rightPatient);
        sp.setCaretakerId(this, "" + rightPatient.getObjectId());

        finish();

    }

    public void setDevice(Patient p){
        List<Device> devices = p.getDevices();
        int deviceExists = 0;
        for (int i = 0; i < devices.size(); i++) {
            if(devices.get(i).getDeviceId().equals(Messaging.DEVICE_ID)){
                deviceExists++;
            }
        }
        if(deviceExists == 0) {

            if(!Messaging.DEVICE_ID.equals(UNKNOWN_DEVICE_ID_KEY)) {
                Device d = new Device();
                d.setDeviceId(Messaging.DEVICE_ID);
                devices.add(d);
                p.setDevices(devices);
                PutPatientRequest ppr = new PutPatientRequest();
                ppr.updatePatient(p);
                RegisterDevice rD = new RegisterDevice();
                rD.registerDevice();
            }
        }

    }

}
