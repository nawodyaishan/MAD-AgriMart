package lk.sltc.agrimarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import lk.sltc.agrimarket.Prevalent.Prevalent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText mobileNo, question1, question2;
    private Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title);
        titleQuestions = findViewById(R.id.title_questions);
        mobileNo = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyBtn = findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mobileNo.setVisibility(View.GONE);

        if (check.equals("settings"))
        {
            pageTitle.setText("Set Questions");
            titleQuestions.setText("Set the answers for following security questions");
            verifyBtn.setText("Set");

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer1 = question1.getText().toString().toLowerCase();
                    String answer2 = question2.getText().toString().toLowerCase();

                    if (question1.equals("") && question2.equals("")){
                        Toast.makeText(ResetPasswordActivity.this, "Please answer both questions", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getMobile());

                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("answer1", answer1);
                        userdataMap.put("answer2", answer2);

                        ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ResetPasswordActivity.this, "You have set the security questions successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            });
        }
        else if (check.equals("login"))
        {
            mobileNo.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });

        }
    }

    private void verifyUser(){
        String mobile = mobileNo.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        if (!mobile.equals("") && !answer1.equals("") && !answer2.equals("")){
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(mobile);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String phone = dataSnapshot.child("mobile").getValue().toString();

                        if (dataSnapshot.hasChild("Security Questions")){
                            String ans1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this, "Incorrect answer for first question", Toast.LENGTH_SHORT).show();
                            }
                            else if (!ans2.equals(answer2)){
                                Toast.makeText(ResetPasswordActivity.this, "Incorrect answer for second question", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Enter New Password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (!newPassword.getText().toString().equals("")){
                                            ref.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security questions", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this, "This mobile number not exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
        }
    }
}