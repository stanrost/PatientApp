package com.example.strost.patient;

import android.content.Context;

import com.example.strost.patient.model.entities.Patient;
import com.example.strost.patient.model.request.PutPatientRequest;
import com.example.strost.patient.network.SendEmail;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * Created by strost on 30-3-2017.
 */

public class ForgotPassword {

    private Context mContext;

    public void forgotPassword(String email, List<Patient> patients, Context context){
        Patient rightPatient = null;
        mContext = context;
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getEmail().equals(email)){
                rightPatient = patients.get(i);
            }
        }
        String password = randomString();

            sendEmail(rightPatient, password);
            updatePatient(rightPatient, password);

    }

    private void sendEmail(Patient rightPatient, String password) {
        SendEmail sE = new SendEmail();
        String title = mContext.getString(R.string.email_title);
        String email = rightPatient.getEmail();
        String body = mContext.getString(R.string.email_greeting) + " " +rightPatient.getFirstName()+ " " +rightPatient.getLastName()+ ", <br><br>"  +mContext.getString(R.string.email_body_part_1)+" <br>" + mContext.getString(R.string.email_body_part_2)+ " " + rightPatient.getEmail() + "<br>" + mContext.getString(R.string.email_body_part_3) + " " +password  + "<br>"+ mContext.getString(R.string.email_body_part_4) + "<br><br>"+ mContext.getString(R.string.email_body_part_5) +"<br>" + mContext.getString(R.string.email_body_part_6);
        sE.sendEmail(title, email, body);
    }

    private void updatePatient(Patient rightPatient, String password) {
        PasswordEncryption pE = new PasswordEncryption();
        PutPatientRequest pPR = new PutPatientRequest();
        rightPatient.setChangedGeneratedPassword(false);
        rightPatient.setPassword(pE.encryptPassword(password));
        pPR.updatePatient(rightPatient);
    }

    public static String randomString() {
        char[] characterSet = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[8];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }

}

