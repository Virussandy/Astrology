package astrology.in;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

public class Viewdata extends AppCompatActivity {

    private PDFView pdfview;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdata);


        pdfview = findViewById(R.id.pdf_view);
        toolbar = findViewById(R.id.toolbar);


        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String file_name = intent.getStringExtra("file_name");

        toolbar.setTitle(file_name);

        new RetrivePdfStream().execute(url);
    }

    class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {

        ProgressDialog progressDialog;

        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Viewdata.this);
            progressDialog.setTitle("getting content...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfview.fromStream(inputStream).load();
            progressDialog.dismiss();
        }
    }
}