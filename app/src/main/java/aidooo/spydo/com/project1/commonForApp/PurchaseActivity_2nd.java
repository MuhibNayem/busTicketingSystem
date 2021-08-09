
package aidooo.spydo.com.project1.commonForApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import aidooo.spydo.com.project1.R;

public class PurchaseActivity_2nd extends AppCompatActivity {

    private String fromStationNameTxt, toStationNameTxt,k, user, _fullname, _email, _phonenum, _gender, busNameTxt, busTypeTxt, seatNoTxt;
    private AutoCompleteTextView busName;
    private AutoCompleteTextView seatNo;

    private String ticketKey;
    private TextInputLayout busNameLayout, seatNoLayout, busTypeLayout;
    HashMap<String, String> ticketMap;

    private DatePicker datePicker;

    DatabaseReference reference,reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_2nd);

        busName = findViewById(R.id.busNameText);
        seatNo = findViewById(R.id.busSeatText);

        busNameLayout = findViewById(R.id.busName);
        seatNoLayout = findViewById(R.id.busSeat);


        datePicker = findViewById(R.id.date_picker);

        reference = FirebaseDatabase.getInstance().getReference();
        reference1 = FirebaseDatabase.getInstance().getReference("agenciesByUID");

        fromStationNameTxt = getIntent().getStringExtra("fromStation");
        toStationNameTxt = getIntent().getStringExtra("toStation");
        _fullname = getIntent().getStringExtra("fullname");
        _phonenum = getIntent().getStringExtra("phonenum");
        _email = getIntent().getStringExtra("email");
        _gender = getIntent().getStringExtra("gender");
        user = getIntent().getStringExtra("user");
        busTypeTxt = getIntent().getStringExtra("busTypeTxt");

        itemsforBusName();
        itemsforSeatNo();

        //busNames();

    }

    private void itemsforSeatNo() {
        String[] item = new String[]{

                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12",
                "13",
                "14",
                "15",
                "16",
                "17",
                "18",
                "19",
                "20",
                "21",
                "22",
                "23",
                "24",
                "25",
                "26",
                "27",
                "28",
                "29",
                "30",

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                PurchaseActivity_2nd.this,
                R.layout.dropdown_items,
                item
        );

        seatNo.setAdapter(adapter);
    }


    public void itemsforBusName() {

        String fromStationNameTxt0 = fromStationNameTxt;
        String toStationNameTxt1 = toStationNameTxt;

        /*Toast.makeText(this, journeyDate, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, fromStationNameTxt, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, toStationNameTxt, Toast.LENGTH_SHORT).show();*/

        String[] item = new String[]{

                "delhi express",
                "mumbai express"
                /*"chennai express",
                "patna express",
                "manali express",
                "Assam express",
                "Hyderabad express",
                "Goa express",
                "Gandhinagar express",
                "Chandigarh express",
                "Shimla express",
                "Bengaluru express",
                "Shillong express",
                "Jaipur express",
                "Kolkata express"*/

        };

        String[] item2 = new String[]{
                "patna express",
                "manali express",
        };

        String[] item3 = new String[]{
                "chennai express",
                "patna express",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                PurchaseActivity_2nd.this,
                R.layout.dropdown_items,
                item
        );

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                PurchaseActivity_2nd.this,
                R.layout.dropdown_items,
                item2
        );

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                PurchaseActivity_2nd.this,
                R.layout.dropdown_items,
                item3
        );


        if (fromStationNameTxt.equals("delhi") && toStationNameTxt.equals("mumbai")
                || fromStationNameTxt.equals("mumbai") && toStationNameTxt.equals("delhi")) {
            busName.setAdapter(adapter);
        } else if (fromStationNameTxt.equals("patna") && toStationNameTxt.equals("manali")
                || fromStationNameTxt.equals("manali") && toStationNameTxt.equals("patna")) {
            busName.setAdapter(adapter2);
        } else if (fromStationNameTxt.equals("patna") && toStationNameTxt.equals("chennai")
                || fromStationNameTxt.equals("chennai") && toStationNameTxt.equals("patna")) {
            busName.setAdapter(adapter3);
        }

    }



    public void booking(View view) {

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        String journyDate = day+"/"+month+"/"+year;

        if (!validateBusName() | !validateSeatNo()) {
            return;
        }

        ticketKey();

        seatNoTxt = seatNo.getText().toString().trim();
        busNameTxt = busName.getText().toString().trim();

        final ProgressDialog _loadingBar;
        _loadingBar = new ProgressDialog(this);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("tickets");

        ticketMap = new HashMap<>();

        ticketMap.put("fullname", _fullname);
        ticketMap.put("phoneNum", _phonenum);
        ticketMap.put("gender", _gender);
        ticketMap.put("email", _email);
        ticketMap.put("uid", user);
        ticketMap.put("fromStation", fromStationNameTxt);
        ticketMap.put("toStation", toStationNameTxt);
        ticketMap.put("BusName", busNameTxt);
        ticketMap.put("BusType", busTypeTxt);
        ticketMap.put("seatNo", seatNoTxt);
        ticketMap.put("TicketKey", ticketKey);
        ticketMap.put("journeyDate", journyDate);

        _loadingBar.setTitle("Booking");
        _loadingBar.setMessage("Please Wait...");
        _loadingBar.setCanceledOnTouchOutside(true);
        _loadingBar.show();

        reference.child(fromStationNameTxt).child(toStationNameTxt).child(busTypeTxt).child(user).setValue(ticketMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(PurchaseActivity_2nd.this, "Booked successfully", Toast.LENGTH_SHORT).show();
                            _loadingBar.dismiss();
                            Intent intent = new Intent(getApplicationContext(), Main_home.class);
                            intent.putExtra("currentUser", user);
                            startActivity(intent);
                            finish();

                        } else {
                            String massage = task.getException().toString().trim();
                            _loadingBar.dismiss();
                            Toast.makeText(PurchaseActivity_2nd.this, "Error :" + massage, Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void ticketKey() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        ticketKey = buffer.toString();


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

    public void busNames() {
        reference.child("busName").child(fromStationNameTxt).child(toStationNameTxt).child(busTypeTxt)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            long sysBusName = dataSnapshot.getChildrenCount();

                            String key = dataSnapshot.getRef().getKey().toString().trim();

                            String count = Long.toString(sysBusName);

                            k = dataSnapshot.child(dataSnapshot.getChildren().toString()).toString();

                            Toast.makeText(PurchaseActivity_2nd.this, count, Toast.LENGTH_SHORT).show();
                            Toast.makeText(PurchaseActivity_2nd.this, key, Toast.LENGTH_SHORT).show();
                            Toast.makeText(PurchaseActivity_2nd.this, k, Toast.LENGTH_SHORT).show();
                            //String _systemUID = dataSnapshot.child("").child("currentUserID").getValue(String.class);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String item;

                        if (dataSnapshot.exists()){

                            long sysBusName = dataSnapshot.getChildrenCount();

                            String count = Long.toString(sysBusName);

                            //Toast.makeText(PurchaseActivity_2nd.this, count, Toast.LENGTH_SHORT).show();

                            //String _systemUID = dataSnapshot.child("").child("currentUserID").getValue(String.class);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}