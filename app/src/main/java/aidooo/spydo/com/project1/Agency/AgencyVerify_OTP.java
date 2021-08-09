package aidooo.spydo.com.project1.Agency;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import aidooo.spydo.com.project1.Database.userDatabaseHelperClassForAgency;
import aidooo.spydo.com.project1.R;

public class AgencyVerify_OTP extends AppCompatActivity {

    PinView pinFromUser;
    String codeBySystem;
    String phone, pasS, comPasS, fullnamE, usernamE, emaiL, age, gender;
    DatabaseReference reference, reference1;
    private FirebaseUser currentUser;
    private FirebaseAuth userAuth;
    private PhoneAuthOptions options;
    private TextView timer;
    CountDownTimer waitTimer;

    Button resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_verify__o_t_p);

        pinFromUser = findViewById(R.id.pin_view);
        resend = findViewById(R.id.resend_btn);
        timer = findViewById(R.id.resendTimer);

        userAuth = FirebaseAuth.getInstance();
        currentUser = userAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        reference1 = FirebaseDatabase.getInstance().getReference();

        phone = getIntent().getStringExtra("phone");
        usernamE = getIntent().getStringExtra("username");
        fullnamE = getIntent().getStringExtra("fullname");
        pasS = getIntent().getStringExtra("pass");
        comPasS = getIntent().getStringExtra("comPass");
        emaiL = getIntent().getStringExtra("email");

        sendVerificationCodeToUser(phone);


    }

    private void sendVerificationCodeToUser(String phone) {

        options = PhoneAuthOptions.newBuilder(userAuth)
                .setPhoneNumber(phone)       // Phone number to verify
                .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        startTimer();
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();

                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                        waitTimer.cancel();
                        waitTimer = null;

                    }

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(AgencyVerify_OTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            storeUserData();

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(AgencyVerify_OTP.this, "Verification Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    private void storeUserData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        final String currentUserID = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        reference = rootNode.getReference("agencies");
        reference1 = rootNode.getReference("agenciesByUID");


        Query checkUser = FirebaseDatabase.getInstance().getReference("agencies").orderByChild("username").equalTo(usernamE);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(AgencyVerify_OTP.this, "user already exists", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AgencyLogin.class));
                    finish();
                } else {
                    userDatabaseHelperClassForAgency addNewUser = new userDatabaseHelperClassForAgency(currentUserID, fullnamE, usernamE, emaiL, pasS, comPasS, phone);
                    reference.child(usernamE).setValue(addNewUser);
                    reference1.child(currentUserID).setValue(addNewUser);
                    Toast.makeText(AgencyVerify_OTP.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AgencyLogin.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void nxt_scren(View view) {
        String code = pinFromUser.getText().toString().trim();
        if (!code.isEmpty()) {

            verifyCode(code);

        }
    }

    public void backToRegi(View view) {
        startActivity(new Intent(getApplicationContext(), AgencyRegistration.class));
        finish();
    }

    public void resendCode(View view) {
        sendVerificationCodeToUser(phone);
        startTimer();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AgencyRegistration.class));
        finish();
    }

    public void startTimer() {

        long duration = TimeUnit.MINUTES.toMillis(2);
        waitTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resend.setClickable(false);
                resend.setTextColor(getResources().getColor(android.R.color.darker_gray));
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timer.setText("Send code in : " + sDuration);
            }

            @Override
            public void onFinish() {
                resend.setClickable(true);
                resend.setTextColor(getResources().getColor(android.R.color.black));
            }
        }.start();
    }
}