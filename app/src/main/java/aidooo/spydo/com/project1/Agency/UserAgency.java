package aidooo.spydo.com.project1.Agency;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import aidooo.spydo.com.project1.Common.LoginSignUp.Login;
import aidooo.spydo.com.project1.R;

public class UserAgency extends AppCompatActivity {

    private Button user,agency;
    String userxt=null, agencyTxt=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_agency);

        user = (Button) findViewById(R.id.user);
        agency = (Button) findViewById(R.id.agency);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userxt = user.getText().toString();
                loginSignUpPage();

            }
        });
        agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agencyTxt = agency.getText().toString();
                loginSignUpPage();
            }
        });

    }


    public void loginSignUpPage(){

        if (userxt != null){
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("userxt",userxt);
            Pair[] pairs = new Pair[1];

            pairs[0]= new Pair<View,String>(findViewById(R.id.busttonLayout),"transition_LoginSignUp");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            startActivity(intent,options.toBundle());
            finish();
        }
        if (agencyTxt != null){

            Intent intent1 = new Intent(this, AgencyLogin.class);
            intent1.putExtra("agencyTxt",agencyTxt);
            Pair[] pairs = new Pair[1];

            pairs[0]= new Pair<View,String>(findViewById(R.id.busttonLayout),"transition_LoginSignUp");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairs);
            startActivity(intent1,options.toBundle());
            finish();
        }


    }

}