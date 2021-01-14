package lk.sltc.agrimarket.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import lk.sltc.agrimarket.Model.Sellers;
import lk.sltc.agrimarket.Prevalent.Prevalent2;
import lk.sltc.agrimarket.R;

public class SellerLoginActivity extends AppCompatActivity {
    private EditText InputMobileNo, InputPassword;
    private Button Login;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        Login = (Button) findViewById(R.id.seller_login_btn);
        InputMobileNo = (EditText) findViewById(R.id.seller_login_mobile);
        InputPassword = (EditText) findViewById(R.id.seller_login_password);
        loadingBar = new ProgressDialog(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSeller();
            }
        });
    }

    private void LoginSeller() {
        String mobile = InputMobileNo.getText().toString();
        String password = InputPassword.getText().toString();

        if (!mobile.equals("") && !password.equals("")){
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccess(mobile, password);
        }
        else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void AllowAccess(final String mobile, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Sellers").child(mobile).exists()) {
                    Sellers sellersData = dataSnapshot.child("Sellers").child(mobile).getValue(Sellers.class);

                    if (sellersData.getMobile().equals(mobile)) {
                        if (sellersData.getPassword().equals(password)) {
                            Toast.makeText(SellerLoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                            Prevalent2.currentOnlineUser = sellersData;
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(SellerLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(SellerLoginActivity.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}