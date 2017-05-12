package com.mohammedal_hashedi.memorableplaces;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    Button uploadButton;
    ImageView mImageView;

    private static final int galleray_intern = 1;

    private StorageReference mStorage;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mStorage = FirebaseStorage.getInstance().getReference();
        uploadButton = (Button) findViewById(R.id.upload);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgress = new ProgressDialog(this);


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, galleray_intern);
            }
        });

    }


   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleray_intern && resultCode == RESULT_OK){
            mProgress.setMessage("Uploading Image...");
            mProgress.show();

            Uri uri = data.getData();
            StorageReference filepath = mStorage.child("photo").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.dismiss();

                    Uri downloudUri = taskSnapshot.getDownloadUrl();
                    Picasso.with(ImageActivity.this).load(downloudUri).fit().centerCrop().into(mImageView);

                    Toast.makeText(ImageActivity.this, "Uploading finished", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
