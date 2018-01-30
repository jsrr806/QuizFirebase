package com.dhirajdwivedi.quizfirebase;

import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    //this is the pic pdf code used in file chooser
    private static final int RC_SIGN_IN = 1;
    final static int PICK_PDF_CODE = 2342;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("quiz");

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView textViewStatus;
    EditText editTextFileName;
    ProgressBar progressBar;
    Button buttonUploadFile;
    TextView textViewUploads;
    Button buttonCreateQuiz;

    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Todo:
        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("assign_name/"+"t_id1");

        //getting the views
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextFileName = (EditText) findViewById(R.id.editTextFileName);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        buttonUploadFile = (Button) findViewById(R.id.buttonUploadFile);
        textViewUploads = (TextView) findViewById(R.id.textViewUploads);
        buttonCreateQuiz = (Button) findViewById(R.id.buttonCreateQuiz);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("Signed in: ", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Signed_out", "onAuthStateChanged:signed_out");
                }
            }
        };
        buttonUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
                //Toast.makeText(MainActivity.this,"This is Working!",Toast.LENGTH_SHORT).show();
            }
        });

        buttonCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createQuizIntent = new Intent(MainActivity.this,CreateQuiz.class);
                startActivity(createQuizIntent);

            }
        });
        textViewUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewUploadActivity.class));
            }
        });

        //findViewById(R.id.buttonUploadFile).setOnClickListener(this);
        //findViewById(R.id.textViewUploads).setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void getPDF(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
            startActivity(intent);
            return;
        }

        Intent intent = new Intent();
        intent.setType("application/pdf/*jpeg/*msword/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_PDF_CODE && data!=null){
            //Toast.makeText(this, "No File Chosen1.", Toast.LENGTH_SHORT).show();
            if(data.getData()!=null){
                uploadFile(data.getData());
            }
            else {
                Toast.makeText(this, "No File Chosen.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data){
        progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child(SyncStateContract.Constants.DATA + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                textViewStatus.setText("File Uploaded Successfully");

                Upload upload = new Upload(editTextFileName.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    textViewStatus.setText((int) progress + "% Uploading...");
            }
        });
    }

}
