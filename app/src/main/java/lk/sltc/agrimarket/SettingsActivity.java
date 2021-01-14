package lk.sltc.agrimarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import lk.sltc.agrimarket.Prevalent.Prevalent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText nameEditText, mobileEditText, passwordEditText, addressEditText, emailEditText;
    private TextView profileChangeTextBtn, closeTextBtn, saveTextBtn;
    private Button securityQuestionBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicRef;
    private String checker = "";
    private String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        nameEditText = (EditText) findViewById(R.id.settings_name);
        mobileEditText = (EditText) findViewById(R.id.settings_mobile_no);
        passwordEditText = (EditText) findViewById(R.id.settings_password);
        addressEditText = (EditText) findViewById(R.id.settings_Address);
        emailEditText = (EditText) findViewById(R.id.settings_Email);
        profileChangeTextBtn = (TextView) findViewById(R.id.update_profile_image_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);
        saveTextBtn = (TextView) findViewById(R.id.update_settings);
        securityQuestionBtn = findViewById(R.id.security_questions_btn);


        userInfoDisplay(profileImageView, nameEditText, mobileEditText, passwordEditText, addressEditText, emailEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                }
                else{
                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri).start(SettingsActivity.this);

            }
        });

        securityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });
    }

    private void updateOnlyUserInfo() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email) || !email.matches(emailPattern)){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mobile) || !(mobile.length() == 10)){
            Toast.makeText(this, "Mobile no. is not valid", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password) || password.length() < 6){
            Toast.makeText(this, "Password is mandatory", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("mobile", mobile);
            userMap.put("address", address);
            userMap.put("email", email);
            userMap.put("password", password);
            ref.child(Prevalent.currentOnlineUser.getMobile()).updateChildren(userMap);

            startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
            Toast.makeText(SettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this, "Error, Try again", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email) || !email.matches(emailPattern)){
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mobile) || !(mobile.length() == 10)){
            Toast.makeText(this, "Mobile no. is not valid", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password) || password.length() < 6){
            Toast.makeText(this, "Password is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait. We are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileRef = storageProfilePicRef.child(Prevalent.currentOnlineUser.getMobile()+ ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                assert downloadUrl != null;
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("name", nameEditText.getText().toString());
                                userMap.put("mobile", mobileEditText.getText().toString());
                                userMap.put("address", addressEditText.getText().toString());
                                userMap.put("email", emailEditText.getText().toString());
                                userMap.put("password", passwordEditText.getText().toString());
                                userMap.put("image", myUrl);
                                ref.child(Prevalent.currentOnlineUser.getMobile()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText nameEditText, final EditText mobileEditText, final EditText passwordEditText, final EditText addressEditText, final EditText emailEditText) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getMobile());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String mobile = dataSnapshot.child("mobile").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        nameEditText.setText(name);
                        mobileEditText.setText(mobile);
                        passwordEditText.setText(password);
                        addressEditText.setText(address);
                        emailEditText.setText(email);
                    }
                    else {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String mobile = dataSnapshot.child("mobile").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        nameEditText.setText(name);
                        mobileEditText.setText(mobile);
                        passwordEditText.setText(password);
                        addressEditText.setText(address);
                        emailEditText.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}