package com.example.mytools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class AddTools extends AppCompatActivity {

    EditText etID, etName;
    Button btnUpload, btnDate, btnSubmit;
    ImageView ivBack, iv;

    // FOR ADDING INTO FIREBASE
    int id;
    String name, date, role;
    Boolean availability;

    // FOR UPLOAD IMAGE TO FIREBASE
    String imageURL;
    FirebaseStorage storage;
    StorageReference storageReference;

    ProgressBar loadingProgress;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    // FIREBASE
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tools);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        etID = findViewById(R.id.etID);
        etName = findViewById(R.id.etName);

        btnUpload = findViewById(R.id.btnUpload);
        btnDate = findViewById(R.id.btnDate);
        btnSubmit = findViewById(R.id.btnSubmit);

        ivBack = findViewById(R.id.ivBack);
        iv = findViewById(R.id.iv);

        loadingProgress = findViewById(R.id.regProgressBar);

        db = FirebaseFirestore.getInstance();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewAct = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intentNewAct);
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        btnDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                };

                // Create the DatePicker Dialog
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDateDialog = new DatePickerDialog(AddTools.this,
                        myDateListener, mYear, mMonth, mDay);

                myDateDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    availability = true;

                    if (date.equals("Select your date")) {
                        Toast.makeText(AddTools.this, "Please Choose Your Date", Toast.LENGTH_LONG).show();
                    } else {
                        Inventory inventory = new Inventory(id, name, imageURL, date, availability);

                        db.collection("Inventory")
                                .document(String.valueOf(id))
                                .set(inventory)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                          @Override
                                                          public void onSuccess(Void aVoid) {
                                                              Toast.makeText(AddTools.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                                                              Intent intent = new Intent(AddTools.this, MainActivity.class);
                                                              intent.putExtra("newID", id);
                                                              startActivity(intent);
                                                          }
                                                      }


                                ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddTools.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }



                }
            }
        });






    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/+" + UUID.randomUUID().toString());

            UploadTask uploadTask = ref.putFile(filePath);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        imageURL = task.getResult().toString();
                    } else {
                        // Handle failures
                    }
                }
            });

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(AddTools.this, "Uploaded", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e){
                    Toast.makeText(AddTools.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // VALIDATE THE INPUT BY USER
    private Boolean validate() {
        Boolean result = false;

        id = Integer.parseInt(etID.getText().toString());
        name = etName.getText().toString();
        date = btnDate.getText().toString();

        role = "Inventory";


        if (!validateID() | !validateName()) {
            //Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }

        return result;
    }


    private boolean validateID() {
        String codeInput = etID.getText().toString().trim();

        if (codeInput.isEmpty()) {
            etID.setError("Field can't be empty");
            return false;
        } else if (codeInput.length() < 4) {
            etID.setError("ID must be at least 4 characters");
            return false;
        } else {
            etID.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = etName.getText().toString().trim();

        if (nameInput.isEmpty()) {
            etName.setError("Field can't be empty");
            return false;
        } else if (nameInput.length() > 30) {
            etName.setError("Name must be not more than 30 characters");
            return false;
        } else {
            etName.setError(null);
            return true;
        }
    }





}
