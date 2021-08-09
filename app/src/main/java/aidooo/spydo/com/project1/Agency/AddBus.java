
package aidooo.spydo.com.project1.Agency;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

import aidooo.spydo.com.project1.R;
import aidooo.spydo.com.project1.commonForApp.Main_home;

public class AddBus extends AppCompatActivity {

    private String fromStationNameTxt, toStationNameTxt, journeyDate, user, _fullname, _email, _phonenum, _gender, busNameTxt, busTypeTxt, seatNoTxt;
    private AutoCompleteTextView busName;
    private AutoCompleteTextView seatNo;
    private AutoCompleteTextView busType,fromStationName,toStationName;
    private String ticketKey;
    private TextInputLayout busNameLayout,seatNoLayout,busTypeLayout,fromStation,toStation;
    HashMap<String, String> busMAp;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        busName = findViewById(R.id.busNameText);
        seatNo = findViewById(R.id.busSeatText);
        busType = findViewById(R.id.busTypeNameText);
        fromStationName = findViewById(R.id.FromStationNameText);
        toStationName = findViewById(R.id.ToStationNameText);

        fromStation = findViewById(R.id.FromStationName);
        toStation = findViewById(R.id.ToStationName);
        busNameLayout = findViewById(R.id.busName);
        seatNoLayout = findViewById(R.id.busSeat);
        busTypeLayout = findViewById(R.id.busTypeName);


        reference = FirebaseDatabase.getInstance().getReference();

        user = getIntent().getStringExtra("user");

        itemsforSeatNo();
        itemsforBusType();

    }

    private void itemsforSeatNo() {
        String[] item = new String[]{

                "30",
                "40",

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AddBus.this,
                R.layout.dropdown_items,
                item
        );

        seatNo.setAdapter(adapter);
    }


    public void itemsforBusType() {

        String[] item = new String[]{

                "AC",
                "Non_AC"

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AddBus.this,
                R.layout.dropdown_items,
                item
        );

        busType.setAdapter(adapter);


    }

    public void booking(View view) {

        if (!validateBusType() | !validateBusName() | !validateSeatNo() | !validateFromStaton() | !validateToStaton()) {
            return;
        }


        busNameTxt = busName.getText().toString().trim();
        busTypeTxt = busType.getText().toString().trim();
        seatNoTxt = seatNo.getText().toString().trim();
        fromStationNameTxt = fromStationName.getText().toString().trim();
        toStationNameTxt = toStationName.getText().toString().trim();


        final ProgressDialog _loadingBar;
        _loadingBar = new ProgressDialog(this);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("busName");

        busMAp = new HashMap<>();

        busMAp.put("uid", user);
        busMAp.put("fromStation", fromStationNameTxt);
        busMAp.put("toStation", toStationNameTxt);
        busMAp.put("BusName", busNameTxt);
        busMAp.put("busTotalSeatNo", seatNoTxt);
        busMAp.put("BusType", busTypeTxt);


        _loadingBar.setTitle("Booking");
        _loadingBar.setMessage("Please Wait...");
        _loadingBar.setCanceledOnTouchOutside(true);
        _loadingBar.show();

        reference.child(fromStationNameTxt).child(toStationNameTxt).child(busTypeTxt).child(user).setValue(busMAp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(AddBus.this, "Bus added successfully", Toast.LENGTH_SHORT).show();
                            _loadingBar.dismiss();
                            Intent intent = new Intent(getApplicationContext(), AgencyMainDashboardActivity.class);
                            intent.putExtra("currentUser", user);
                            startActivity(intent);
                            finish();

                        } else {
                            String massage = task.getException().toString().trim();
                            _loadingBar.dismiss();
                            Toast.makeText(AddBus.this, "Error :" + massage, Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private boolean validateBusType() {
        String val = busTypeLayout.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            busTypeLayout.setError("Field can not be empty");
            return false;
        } else {
            busTypeLayout.setError(null);
            busTypeLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateBusName() {
        String val = busNameLayout.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            busNameLayout.setError("Field can not be empty");
            return false;
        } else {
            busNameLayout.setError(null);
            busNameLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateSeatNo() {
        String val = seatNoLayout.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            seatNoLayout.setError("Field can not be empty");
            return false;
        } else {
            seatNoLayout.setError(null);
            seatNoLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFromStaton() {
        String val = fromStation.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            fromStation.setError("Field can not be empty");
            return false;
        } else {
            fromStation.setError(null);
            fromStation.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateToStaton() {
        String val = toStation.getEditText().getText().toString().trim();
        String val1 = fromStation.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            toStation.setError("Field can not be empty");
            return false;
        }
        else if (val.equals(val1)){
            toStation.setError("From Station and To Station Can't be same");
            return false;
        }
        else {
            toStation.setError(null);
            toStation.setErrorEnabled(false);
            return true;
        }
    }



}