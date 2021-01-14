package lk.sltc.agrimarket.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import lk.sltc.agrimarket.MainActivity;
import lk.sltc.agrimarket.R;

public class SellerRegisterActivity extends AppCompatActivity {
    private Button sellerRegister, sellerLogin;
    private EditText nameInput, mobileInput, emailInput, passwordInput, addressInput;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        sellerRegister = (Button) findViewById(R.id.seller_register);
        sellerLogin = (Button) findViewById(R.id.seller_goto_login);
        nameInput = (EditText) findViewById(R.id.seller_name);
        mobileInput = (EditText) findViewById(R.id.seller_mobile);
        emailInput = (EditText) findViewById(R.id.seller_email);
        passwordInput = (EditText) findViewById(R.id.seller_password);
        addressInput = (EditText) findViewById(R.id.seller_address);
        loadingBar = new ProgressDialog(this);


        sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        sellerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {
        String name = nameInput.getText().toString();
        String mobile = mobileInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();

        if (!name.equals("") && !mobile.equals("") && !email.equals("") && !password.equals("") && !address.equals("")){
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateMobileNumber(name, mobile, email, password, address);

        }
        else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidateMobileNumber(final String name, final String mobile, final String email, final String password, final String address)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Sellers").child(mobile).exists()))
                {
                    HashMap<String, Object> sellerdataMap = new HashMap<>();
                    sellerdataMap.put("mobile", mobile);
                    sellerdataMap.put("name", name);
                    sellerdataMap.put("email", email);
                    sellerdataMap.put("password", password);
                    sellerdataMap.put("address", address);

                    RootRef.child("Sellers").child(mobile).updateChildren(sellerdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SellerRegisterActivity.this, "Congratulations! Your account created successfully!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(SellerRegisterActivity.this, SellerHomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(SellerRegisterActivity.this, "Error, Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(SellerRegisterActivity.this, "This number is in use", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(SellerRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
