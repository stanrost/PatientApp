package com.example.strost.patient.controller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.strost.patient.PasswordEncryption;
import com.example.strost.patient.R;
import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.request.PutPatientRequest;

public class ChangePasswordActivity extends AppCompatActivity {
    private Patient mPatient;
    private String mPassword ="";
    private Boolean mChangedGeneratedPassword = false;
    private final String PATIENT_KEY = "Patient";
    private final String PASSWORD_KEY = "Password";
    private final String EMAIL_KEY = "Email";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPatient = (Patient) getIntent().getSerializableExtra(PATIENT_KEY);
        mPassword = getIntent().getStringExtra(PASSWORD_KEY);
        mChangedGeneratedPassword = mPatient.getChangedGeneratedPassword();

        final EditText oldPassword = (EditText)findViewById(R.id.etOldpassword);
        final EditText newPassword = (EditText)findViewById(R.id.etNewPassword);
        final EditText newPasswordCheck = (EditText)findViewById(R.id.etNewPasswordCheck);
        final Button changePassword = (Button) findViewById(R.id.btnChangePassword);
        oldPassword.setText(mPassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PasswordEncryption pE = new PasswordEncryption();
                if (!oldPassword.getText().toString().equals("") && !newPassword.getText().toString().equals("") && !newPasswordCheck.getText().toString().equals("")){
                    if (mPatient.getPassword().equals(pE.encryptPassword(oldPassword.getText().toString()))) {

                        if (newPassword.getText().toString().equals(newPasswordCheck.getText().toString())) {
                            mPatient.setPassword(pE.encryptPassword(newPassword.getText().toString()));
                            mPatient.setChangedGeneratedPassword(true);
                            final PutPatientRequest ppr = new PutPatientRequest();
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    ppr.updatePatient(mPatient);
                                }
                            };
                            Thread mythread = new Thread(runnable);
                            mythread.start();
                            try {
                                mythread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            goBack();
                            finish();
                        } else {
                            passwordsAreNotTheSame();
                        }
                    } else {
                        wrongOldPassword();
                    }
            }else{
                    emptyEditText();
            }

            }

        });

    }

    public void goBack(){
        if(mChangedGeneratedPassword == true) {
            Intent detailIntent = new Intent(this, SettingsActivity.class);
            detailIntent.putExtra(PATIENT_KEY, mPatient);
            startActivity(detailIntent);
        }
        else if(mChangedGeneratedPassword == false) {
            Intent detailIntent = new Intent(this, MainActivity.class);
            detailIntent.putExtra(PATIENT_KEY, mPatient);
            startActivity(detailIntent);
        }
    }


    public void wrongOldPassword(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.wrong_old_password))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void passwordsAreNotTheSame(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.new_passwords_are_not_equal))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void emptyEditText(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.incomplete_form))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if(mChangedGeneratedPassword == true) {
            goBack();
        }
        else if(mChangedGeneratedPassword == false) {
            Intent detailIntent = new Intent(this, LoginActivity.class);
            detailIntent.putExtra(EMAIL_KEY, mPatient.getEmail());
            startActivity(detailIntent);
        }
        finish();
        return;
    }

}
