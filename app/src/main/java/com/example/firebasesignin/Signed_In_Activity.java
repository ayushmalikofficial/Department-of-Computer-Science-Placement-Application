package com.example.firebasesignin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signed_In_Activity extends AppCompatActivity {

    TextView name;
    TextView mail;
    TextView phone;
    TextView dob ;
    TextView lat ;
    TextView lng;
    TextView comp;
    EditText iname;
    EditText imail;
    EditText iphone;
    EditText idob;
    FirebaseFirestore db;
    Button fetch;
    Button submit;
    GeoPoint temp_location;

    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    public Date getDateFromString(String datetoSaved){

        try {
            java.util.Date date = format.parse(datetoSaved);
            return date ;
        } catch (ParseException e){
            return null ;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed__in_);
        mAuth = FirebaseAuth.getInstance();
        Button signOut = findViewById(R.id.button);
        temp_location=new GeoPoint(24,77);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {

                            mAuth.signOut();
                   //         .addOnCompleteListener(new OnCompleteListener<Void>() {
                     //           public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    Intent intent = new Intent(Signed_In_Activity.this, HomeActivity.class);
                                    startActivity(intent);

                       //         }
                       //     });


                  //  Intent intent = new Intent(Signed_In_Activity.this, HomeActivity.class);
                  //  startActivity(intent);



                }
            }
        });


        db = FirebaseFirestore.getInstance();
        comp=findViewById(R.id.company_attended);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        phone = findViewById(R.id.phone);
        dob = findViewById(R.id.d_o_b);
        lat = findViewById(R.id.latitude);
        lng = findViewById(R.id.longitude);

        iname = findViewById(R.id.iname);
        imail = findViewById(R.id.imail);
        iphone = findViewById(R.id.iphone);
        idob=findViewById(R.id.idob);

        fetch = findViewById(R.id.fetch);
        submit = findViewById(R.id.button_submit);


        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Name = iname.getText().toString();
/*
                DocumentReference docRef = db.collection("Users").document("Ws9f7EqLh9Jqu9wzsfIp");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                               // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Toast.makeText(MainActivity.this, "Got Doc", Toast.LENGTH_SHORT).show();
                             name.setText(document.getString("Name"));
                             mail.setText(document.getString("Email"));
                             phone.setText(document.get("Phone_Number").toString());
                             dob.setText(document.get("Date_Of_Birth").toString());
                            } else {
                               // Log.d(TAG, "No such document");
                                Toast.makeText(MainActivity.this, "No Such Document Exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                           // Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(MainActivity.this, "Application Failed Miserably", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
*/
                final CollectionReference questionRef = db.collection("Users");
                Query q = questionRef.whereEqualTo("Name", Name);
                q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                name.setText(document.getString("Name"));
                                mail.setText(document.getString("Email"));
                                phone.setText(document.get("Phone_Number").toString());
                                double l1=document.get("Location",GeoPoint.class).getLatitude();
                                double l2=document.get("Location",GeoPoint.class).getLongitude();
                                lat.setText(Double.toString(l1));
                                lng.setText(Double.toString(l2));
                                //Working Date
                                //java.util.Date date = document.get("Date_Of_Birth",java.util.Date.class);
                                //dob.setText(date.toString());
                                java.util.Date date= document.get("Date_Of_Birth",java.util.Date.class);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                int yy =calendar.get(Calendar.YEAR);
                                int mm =calendar.get(Calendar.MONTH)+1;
                                int dd =calendar.get(Calendar.DATE);
                                //dob.setText(date.toString());
                               dob.setText(dd+"-"+mm+"-"+yy);
                               //Successfully using List and String Arrays
                               /*
                                List<String> list = Arrays.asList("EXL","Proptiger","Evalue","Microsoft","Google");
                                String[] array=list.toArray(new String[0]);
                                comp.setText(Arrays.toString(array));
                                */
                                List<String> list = (List<String>) document.get("Companies_Attended");
                                String[] array=list.toArray(new String[0]);
                                comp.setText(Arrays.toString(array));
                            }


                        } else {
                            Toast.makeText(Signed_In_Activity.this, "You have failed miserably", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {


                                      public void onClick(View view) {
                                          String tokenID = FirebaseInstanceId.getInstance().getToken();
                                          //Timestamp time_stamp=new Timestamp(dateObject);
                                          Map<String, Object> user = new HashMap<>();
                                          user.put("Name", iname.getText().toString());
                                          user.put("Email", imail.getText().toString());
                                          user.put("tokenID",tokenID);
                                          user.put("Location",temp_location);
                                         // user.put("Date_of_Birth",getDateFromString("2017-10-15T09:27:37Z"));
                                         // user.put("Date_of_Birth",getDateFromString("2017-10-15"));
                                          user.put("Date_of_Birth",getDateFromString(idob.getText().toString()));

                                          String temp;
                                          long number = 0;
                                          if (!(iphone.getText().toString().isEmpty())) {
                                              temp = iphone.getText().toString();
                                              number = Long.parseLong(temp);
                                          }

                                          user.put("Phone_Number", number);
                                          //user.put("Date_Of_Birth",time_stamp);

                                          if (!(iname.getText().toString().isEmpty()) && !(imail.getText().toString().isEmpty()) && !(iphone.getText().toString().isEmpty())) {

                                              Toast.makeText(Signed_In_Activity.this, "You Have Entered", Toast.LENGTH_SHORT).show();

                                              db.collection("Users")
                                                      .add(user)
                                                      .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                          @Override
                                                          public void onSuccess(DocumentReference documentReference) {
                                                              //    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                              Toast.makeText(Signed_In_Activity.this, "Data Successfully Saved", Toast.LENGTH_SHORT).show();

                                                          }
                                                      })
                                                      .addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                              //  Log.w(TAG, "Error adding document", e);
                                                              Toast.makeText(Signed_In_Activity.this, "You Have Failed Miserably", Toast.LENGTH_SHORT).show();

                                                          }
                                                      });
                                          } else {
                                              Toast.makeText(Signed_In_Activity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                                          }


                                      }

                                  }
        );


    }

}




