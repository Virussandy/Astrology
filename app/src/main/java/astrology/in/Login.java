package astrology.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    public static SharedPreferences sp;
    private FirebaseAuth mAuth;

    String userid;
    
        @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

         final EditText email = findViewById(R.id.username);
         final EditText password = findViewById(R.id.password);

//        TextView register = findViewById(R.id.register);
         Button loginbtn = findViewById(R.id.login);

//         sp = getSharedPreferences("login",MODE_PRIVATE);

//            register.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(Login.this,Register.class);
//                    startActivity(intent);
//                }
//            });


//            if(sp.getBoolean("logged",true)){
//                finish();
//                Intent intent = new Intent(Login.this,Dashboard.class);
//                startActivity(intent);
//
//            }

            if(sharedpreference.getUserId(Login.this).length() == 0)
            {
                // call Login Activity
                loginbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final ProgressBar progressBar = findViewById(R.id.progressbar);
                        progressBar.setVisibility(View.VISIBLE);

                        if(email.getText().toString().matches("")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Enter email",
                                    Toast.LENGTH_SHORT).show();
                            email.setFocusable(true);
                        }

                        else if(password.getText().toString().matches("")){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Enter password",
                                    Toast.LENGTH_SHORT).show();
                            email.setFocusable(true);
                        }

                        else {
                            mAuth.signInWithEmailAndPassword( email.getText().toString(),password.getText().toString())
                                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                assert user != null;
                                                userid = user.getUid();
                                                sharedpreference.setUserId(getApplicationContext(),userid);
//                                            sp.edit().putBoolean("logged",true).apply();
//                                                sp.edit().putString("user", userid).apply();
                                                finish();
                                                Intent intent = new Intent(Login.this,Dashboard.class);
                                                startActivity(intent);

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Login.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }
                    }

                });

            }
            else
            {
                // Stay at the current activity.

                Intent intent = new Intent(Login.this,Dashboard.class);
                startActivity(intent);
                finish();
            }

//            if(user != null){
//                Intent intent = new Intent(Login.this,Dashboard.class);
//                startActivity(intent);
//            }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            String user_id = user.getUid();
        } else {
//            Toast.makeText(this,"Sign In Failed",Toast.LENGTH_LONG).show();
        }
    }
}