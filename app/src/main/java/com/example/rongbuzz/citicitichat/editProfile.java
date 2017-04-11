package com.example.rongbuzz.citicitichat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class editProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorage;
    private FirebaseUser User;

    private Button submitBtn, changePassBtn, deleteBtn;
    private ImageButton addProImage;
    private EditText UsernameEt, currentPassEt, newPassEt ;

    int REQUEST_CODE = 1;

    private String PhotoDownloadUri ;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //firebase initilation
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        User = mAuth.getCurrentUser();

        //View initialization
        //EditText
        UsernameEt= (EditText) findViewById(R.id.usernameEt);
        currentPassEt = (EditText) findViewById(R.id.curentPass);
        newPassEt = (EditText) findViewById(R.id.newPass);
        //Button
        submitBtn = (Button) findViewById(R.id.sumitBtn);
        changePassBtn = (Button) findViewById(R.id.ChangePassBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        //imageButton
        addProImage = (ImageButton) findViewById(R.id.addProImg);

        // request permission
        requestPermission();

        // open gallery
        openGallery();

        // delete mStorage data
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStorage.child("UserPhoto").child(photoUri.getLastPathSegment()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //photo replaced with default photo
                            addProImage.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
                            Toast.makeText(getApplicationContext(), " Photo deleted Successfully ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Add update user profile info
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name =UsernameEt.getText().toString().trim();
                String Photouri = PhotoDownloadUri;
                // Calling method
                updateUserProfileInfo(Name,Photouri);
            }
        });

    }//onCreate end here

    //method for open gallery
    private void openGallery(){
        //image Button getting gallery intent
        addProImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryPickIntent = new Intent(Intent.ACTION_PICK);
                galleryPickIntent.setType("image/*");
                startActivityForResult(galleryPickIntent,REQUEST_CODE);
            }
        });
    }

    //request permittion
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    //on Activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE || requestCode ==RESULT_OK){
            photoUri =  data.getData();
            // add photo to imageView
            addProImage.setImageURI(photoUri);

            //
            //upload photo in firebase database
            //
            mStorage.child("UserPhoto").child(photoUri.getLastPathSegment()).putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "successfully uploaded ", Toast.LENGTH_SHORT).show();
                }
            });
            mStorage.child("UserPhoto").child(photoUri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
               PhotoDownloadUri = uri.toString();
                }
            });
        }
    }

    //checking gallery permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

    //update user profile info name and photo
    public void updateUserProfileInfo(String name, String photoUri){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(photoUri))
                .build();

        User.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //setting user profile data to firebase database
                            mRef.child("Users").child(User.getUid()).child("username").setValue(User.getDisplayName());
                            mRef.child("Users").child(User.getUid()).child("photouri").setValue(User.getPhotoUrl().toString().trim());
                            mRef.child("Users").child(User.getUid()).child("uid").setValue(User.getUid().toString().trim());

                            Toast.makeText(getApplicationContext(), " Profile Update Successful ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}//end here
