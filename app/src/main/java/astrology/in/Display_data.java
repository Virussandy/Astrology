package astrology.in;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Display_data extends AppCompatActivity {
    private ImageView Displaydata_ImageView;
    private Button Uploaddata_btn;
    private TextView Display_text;
    private ProgressBar mProgressCircle;
    private ListView listView;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;

    String main_key, key, k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        final Intent intent = getIntent();

        listView = findViewById(R.id.list_view);
        mUploads = new ArrayList<>();
        mProgressCircle = findViewById(R.id.progress_circle);
        Displaydata_ImageView = findViewById(R.id.displaydata_banner);
        Uploaddata_btn = findViewById(R.id.upload_data);
        Display_text = findViewById(R.id.displaydata_imagetext);
        main_key = intent.getStringExtra("main_key");
        key = intent.getStringExtra("key");
        k = intent.getStringExtra("k");
        String url = intent.getStringExtra("url");
        String name = intent.getStringExtra("name");

        Picasso.get().load(url).fit().centerCrop().into(Displaydata_ImageView);
        Display_text.setText(name);

        Uploaddata_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Display_data.this, Upload_data.class);
                intent.putExtra("main_key", main_key);
                intent.putExtra("key", key);
                intent.putExtra("k", k);
                startActivity(intent);
            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("main_category").child(main_key).child("sub_category").child(key).child("sub_sub_category").child(k).child("files");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                String[] uploads = new String[mUploads.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = mUploads.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, uploads) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView text = view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.WHITE);
                        text.setTextSize(25);
                        text.setAllCaps(true);
                        return super.getView(position, convertView, parent);
                    }
                };
                listView.setAdapter(adapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Display_data.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Upload upload = mUploads.get(i);
                Intent intent = new Intent(Display_data.this, Viewdata.class);
                intent.putExtra("url", upload.getImageUrl());
                intent.putExtra("file_name", upload.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }


}