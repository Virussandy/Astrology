package astrology.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText username, useremail, userphone, userpassword, usercpassword;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        TextView register = findViewById(R.id.register);
         username = findViewById(R.id.full_name);
         useremail = findViewById(R.id.email);
        userphone = findViewById(R.id.phone);
        userpassword = findViewById(R.id.password);
        usercpassword = findViewById(R.id.cpassword);
        Button register_btn = findViewById(R.id.register_button);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("User");
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = username.getText().toString();
                final String email = useremail.getText().toString();
                final String phone = userphone.getText().toString();
                String password = userpassword.getText().toString();
                String cpassword = usercpassword.getText().toString();


                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this, "Enter Full Name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter Email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(Register.this, "Enter Phone",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals(cpassword)){

                    firebaseAuth.createUserWithEmailAndPassword(email , password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        User user = new User(
                                                name, email, phone
                                        );
                                       reference
                                               .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                               .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               Toast.makeText(Register.this, "Registration Successsfull",
                                                       Toast.LENGTH_SHORT).show();
                                           }
                                       });

                                    } else {

                                    }

                                }
                            });

                }



            }
        });
    }
}