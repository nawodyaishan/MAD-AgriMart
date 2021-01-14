package lk.sltc.agrimarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;
import lk.sltc.agrimarket.Admin.AdminHomeActivity;
import lk.sltc.agrimarket.Model.Users;
import lk.sltc.agrimarket.Prevalent.Prevalent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText InputMobileNo, InputPassword;
    private Button Login;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = (Button) findViewById(R.id.btn_login);
        InputMobileNo = (EditText) findViewById(R.id.login_mobile);
        InputPassword = (EditText) findViewById(R.id.login_password);
        AdminLink = (TextView) findViewById(R.id.admin_panel);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel);
        loadingBar = new ProgressDialog(this);
        forgotPassword = (TextView) findViewById(R.id.frgt_pw);
        chkBoxRememberMe = (CheckBox) findViewById(R.id.chkb_stay);
        Paper.init(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {      //When user wants to login as admin
            @Override
            public void onClick(View view) {
                Login.setText("Admin Login");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                chkBoxRememberMe.setVisibility(View.INVISIBLE);
                forgotPassword.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {      //Return to the user login
            @Override
            public void onClick(View view) {
                Login.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                chkBoxRememberMe.setVisibility(View.VISIBLE);
                forgotPassword.setVisibility(View.VISIBLE);
                parentDbName = "Users";
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });
    }

    private void LoginUser()
    {
        String mobile = InputMobileNo.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(mobile))
        {
            Toast.makeText(this,"Please enter your Mobile no.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter your Password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(mobile,password);
        }
    }

    private void AllowAccessToAccount(final String mobile, final String password) {

        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, mobile);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()     //Checking data that user input for login
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(mobile).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(mobile).getValue(Users.class);

                    if (usersData.getMobile().equals(mobile))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))   //When trying to login as admin
                            {
                                Toast.makeText(LoginActivity.this, "Logged in as Admin Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))    //When trying to login as user
                            {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}