package astrology.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Display_sub_details extends AppCompatActivity {
    String main_key,key,k;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Data> mUploads;
    String firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sub_details);

        mUploads = new ArrayList<>();
        final ProgressBar mProgressCircle = findViewById(R.id.mProgressCircle);
        ImageView image_banner = findViewById(R.id.details_banner);
        TextView textView = findViewById(R.id.banner_name);
        final ListView listView = findViewById(R.id.list_view);
        firebaseAuth = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        TextView buttonbtn = findViewById(R.id.add_data);

        mProgressCircle.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        main_key = intent.getStringExtra("main_key");
        key = intent.getStringExtra("key");
//        String url = intent.getStringExtra("url");
//        String name = intent.getStringExtra("name");
//
//        textView.setText(name);
//        Picasso.get().load(url).fit().centerCrop().into(image_banner);



        buttonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Display_sub_details.this,Upload_sub_details.class);
                intent.putExtra("main_key",main_key);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("main_category").child(main_key).child("sub_category").child(key).child("files");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Data data = postSnapshot.getValue(Data.class);
                    data.setKey(postSnapshot.getKey());
                    mUploads.add(data);
                }

                int size = mUploads.size();

                if(size == 0){
                    Toast.makeText(Display_sub_details.this,"No data found",Toast.LENGTH_LONG).show();
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }
                else {
                    String[] uploads = new String[mUploads.size()];
                    String[] title = new String[mUploads.size()];
                    String[] discription = new String[mUploads.size()];

                    for (int i = 0; i < uploads.length; i++) {
                        title[i] = mUploads.get(i).getTitle();
                        discription[i] = mUploads.get(i).getDiscription();
                    }
                    customAdapter adapter = new customAdapter(getApplicationContext(), title, discription);
                    listView.setAdapter(adapter);

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            onDeleteClick(position);
                            return true;
                        }
                    });
                    mProgressCircle.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onDeleteClick(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this?")
                .setPositiveButton("No", null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data selectedItem = mUploads.get(position);
                        final String selectedKey = selectedItem.getKey();

                        mDatabaseRef.child(selectedKey).removeValue();
                        Toast.makeText(Display_sub_details.this, "Item deleted", Toast.LENGTH_SHORT).show();
//                        finish();
//                        startActivity(getIntent());
                    }

                }).show();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}