package astrology.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;

public class Upload_details extends AppCompatActivity {

    String main_key,key,k;
    private EditText title, description;
    private Button submit;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_details);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        submit = findViewById(R.id.submit);



        final Intent intent = getIntent();

        main_key = intent.getStringExtra("main_key");
        key = intent.getStringExtra("key");
        k = intent.getStringExtra("k");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("main_category").child(main_key).child("sub_category").child(key).child("sub_sub_category").child(k).child("files");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void uploadFile() {

        final String Title = title.getText().toString();
        final String Discription = description.getText().toString();

        Data data = new Data(Title,Discription);

        String uploadId = mDatabaseRef.push().getKey();
        assert uploadId != null;

        mDatabaseRef.child(uploadId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Upload_details.this, "Upload Successsfull",
                        Toast.LENGTH_SHORT).show();
                recall();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_details.this, "Upload not Successsfull",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void recall() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}