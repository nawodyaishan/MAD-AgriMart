package lk.sltc.agrimarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccount;
    private EditText InputName, InputMobileNo, InputEmail, InputPassword, InputAddress;
    private ProgressDialog loadingBar;
    private String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccount = (Button) findViewById(R.id.btn_create_account);
        InputName = (EditText) findViewById(R.id.reg_name);
        InputMobileNo = (EditText) findViewById(R.id.reg_mobile);
        InputEmail = (EditText) findViewById(R.id.reg_email);
        InputPassword = (EditText) findViewById(R.id.reg_password);
        InputAddress = (EditText) findViewById(R.id.reg_address);
        loadingBar = new ProgressDialog(this);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String mobile = InputMobileNo.getText().toString();
        String email = InputEmail.getText().toString().trim();
        String password = InputPassword.getText().toString();
        String address = InputAddress.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter your Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(mobile) || !(mobile.length() == 10) )
        {
            Toast.makeText(this,"Please enter valid Mobile no.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email) || !email.matches(emailPattern))
        {
            Toast.makeText(this,"Please enter valid Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password) || password.length() < 6 )
        {
            Toast.makeText(this,"Your Password must have minimum 6 characters", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address))
        {
            Toast.makeText(this,"Please enter your Address", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatMobileNumber(name, mobile, email, password, address);
        }
    }

    private void ValidatMobileNumber(final String name, final String mobile, final String email, final String password, final String address)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(mobile).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("mobile", mobile);
                    userdataMap.put("name", name);
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);
                    userdataMap.put("address", address);

                    RootRef.child("Users").child(mobile).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congratulations! Your account created successfully!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Error, Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "This number is in use", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}