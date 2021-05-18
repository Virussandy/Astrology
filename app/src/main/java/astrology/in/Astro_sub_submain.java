package astrology.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class Astro_sub_submain extends AppCompatActivity implements ImageAdapter.OnItemClickListener {

    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    String firebaseAuth;

    String main_key;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro_sub_submain);


        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final TextView button = findViewById(R.id.upload_submain);
        ImageView imageView = findViewById(R.id.submain_banner);
        TextView textView = findViewById(R.id.submain_imagetext);
        final TextView add_data = findViewById(R.id.add_sub_details);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_View_2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(Astro_sub_submain.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(Astro_sub_submain.this);
        mStorage = FirebaseStorage.getInstance();

        final Intent intent = getIntent();

        main_key = intent.getStringExtra("main_key");
        key = intent.getStringExtra("key");
        String url = intent.getStringExtra("url");
        String name = intent.getStringExtra("name");

        Picasso.get().load(url).fit().centerCrop().into(imageView);
        textView.setText(name);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Astro_sub_submain.this, Upload_sub_submain.class);
//                i.putExtra("main_key", main_key);
//                i.putExtra("key", key);
//                startActivity(i);
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Astro_sub_submain.this, Upload_sub_submain.class);
                i.putExtra("main_key", main_key);
                i.putExtra("key", key);
                startActivity(i);
            }
        });
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Astro_sub_submain.this,Upload_sub_details.class);
                i.putExtra("main_key", main_key);
                i.putExtra("key", key);
                startActivity(i);
            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("main_category").child(main_key).child("sub_category").child(key)/*.child("sub_sub_category")*/.child("files");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                int size = mUploads.size();

                if (size == 0){

                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("main_category").child(main_key).child("sub_category").child(key).child("sub_sub_category")/*.child("files")*/;

                    mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mUploads.clear();
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                upload.setKey(postSnapshot.getKey());
                                mUploads.add(upload);
                            }

                            int size = mUploads.size();

                            if (size == 0){
                                add_data.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                            }else{
                                add_data.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.VISIBLE);
                            }
                            mAdapter.notifyDataSetChanged();
                            mProgressCircle.setVisibility(View.INVISIBLE);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(Astro_sub_submain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                    });

                }else{
                    Intent intent = new Intent(Astro_sub_submain.this,Display_sub_details.class);
                    intent.putExtra("main_key", main_key);
                    intent.putExtra("key", key);
                    finish();
                    startActivity(intent);
                }

                add_data.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Astro_sub_submain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
//        Intent intent = new Intent(this, Display_data.class);
        Intent intent = new Intent(this, Display_details.class);
        intent.putExtra("main_key",main_key);
        intent.putExtra("key",key);
        intent.putExtra("k", selectedKey);
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
                Toast.makeText(Astro_sub_submain.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}