package aidooo.spydo.com.project1.Agency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import aidooo.spydo.com.project1.Common.LoginSignUp.SignUp2ndPage;
import aidooo.spydo.com.project1.R;

public class AgencySignUp3rdPage extends AppCompatActivity {

    ScrollView scrollView;
    TextInputLayout phoneNum;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_agency_sign_up3rd_page);

        phoneNum = findViewById(R.id.phone_number);
        countryCodePicker = findViewById(R.id.countryPicker);

    }

    public void back(View view){
        startActivity(new Intent(getApplicationContext(), SignUp2ndPage.class));
        finish();
    }
    public void nxt_signUp_scrn(View view){

        if (!validatePhoneNumber()){
            return;
        }

        String usernamE = getIntent().getStringExtra("username");
        String fullnamE = getIntent().getStringExtra("fullname");
        String pasS = getIntent().getStringExtra("pass");
        String comPasS = getIntent().getStringExtra("comPass");
        String emaiL = getIntent().getStringExtra("email");

        String number = phoneNum.getEditText().getText().toString().trim();
        String fullnumber = "+"+countryCodePicker.getFullNumber()+number;


        Intent intent = new Intent(getApplicationContext(), AgencyVerify_OTP.class);

        //send to another activity
        intent.putExtra("username", usernamE);
        intent.putExtra("fullname", fullnamE);
        intent.putExtra("pass", pasS);
        intent.putExtra("comPass", comPasS);
        intent.putExtra("email", emaiL);
        intent.putExtra("phone",fullnumber);

        startActivity(intent);
        finish();
    }
   private boolean validatePhoneNumber(){
        String fullnumberBD = countryCodePicker.getFullNumber()+phoneNum.getEditText().getText().toString().trim();
        String numberBd = phoneNum.getEditText().getText().toString().trim();
        String checkPhoneNumber =  "^(?:\\+?88|0088)?01[15-9]\\d{8}$";


        if (numberBd.isEmpty()) {
            phoneNum.setError("Field can not be empty");
            return false;
        } else if (!fullnumberBD.matches(checkPhoneNumber)) {
            phoneNum.setError("Invalid phone number");
            return false;
        }
        else {
            phoneNum.setError(null);
            phoneNum.setErrorEnabled(false);
            return true;
        }
    }

    /*private boolean validatePhoneNumber_Ind(){
        String fullnumber = countryCodePicker.getFullNumber()+phoneNum.getEditText().getText().toString().trim();
        String number = phoneNum.getEditText().getText().toString().trim();
        String checkPhoneNumber_Ind = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";

        if (number.isEmpty()) {
            phoneNum.setError("Field can not be empty");
            return false;
        }else if (!fullnumber.matches(checkPhoneNumber_Ind)) {
            phoneNum.setError("Invalid phone number");
            return false;
        }
        else {
            phoneNum.setError(null);
            phoneNum.setErrorEnabled(false);
            return true;
        }
    }*/


    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AgencyRegistration.class));
        finish();
    }


}