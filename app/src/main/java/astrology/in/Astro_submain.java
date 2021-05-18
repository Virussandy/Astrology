package astrology.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Astro_submain extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    FirebaseUser firebaseAuth;

    String main_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro_submain);

        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        TextView button = findViewById(R.id.upload_submain);
        ImageView imageView = findViewById(R.id.submain_banner);
        TextView textView = findViewById(R.id.submain_imagetext);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_View_2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(Astro_submain.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(Astro_submain.this);
        mStorage = FirebaseStorage.getInstance();

        final Intent intent = getIntent();
        main_key = intent.getStringExtra("main_key");
        String url = intent.getStringExtra("url");
        String name = intent.getStringExtra("name");

        Picasso.get().load(url).fit().centerCrop().into(imageView);
        textView.setText(name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Astro_submain.this, Upload_submain.class);
                i.putExtra("key", main_key);
                startActivity(i);
            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("main_category").child(main_key).child("sub_category");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Astro_submain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

        });

    }


    @Override
    public void onItemClick(int position) {

        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        String url = selectedItem.getImageUrl();
        String name = selectedItem.getName();
        Intent intent = new Intent(this, Astro_sub_submain.class);
        intent.putExtra("main_key", main_key);
        intent.putExtra("key", selectedKey);
        intent.putExtra("url", url);
        intent.putExtra("name", name);
        startActivity(intent);
//        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(Astro_submain.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}