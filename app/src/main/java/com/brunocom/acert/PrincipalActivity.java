package com.brunocom.acert;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PrincipalActivity extends AppCompatActivity {

    private String email;

    private Button btnSair;
    private Button btnUpload;
    private Button btnSair1;

    String urlImage = "";


    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
private ImageView imageView;


    private TextView txtProcesso;
    private TextView txtSituacao;
    private TextView txtMensagem;
    private TextView txtEtapa;
    private TextView txtLink;



    ProgressDialog progressDialog;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
// instance for firebase storage and StorageReference
        FirebaseStorage storage;
        StorageReference storageReference;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

             email = user.getEmail();
        }
// get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        txtProcesso = findViewById(R.id.txtProcessoPA);
        txtSituacao = findViewById(R.id.txtSituacaoPA);
        txtMensagem = findViewById(R.id.txtMensagemPA);
        txtEtapa = findViewById(R.id.txtEtapaPA);
        txtLink = findViewById(R.id.txtLinkPA);
        btnSair = findViewById(R.id.btnSair);

        btnSair1 = findViewById(R.id.btnSair1);
        btnUpload = findViewById(R.id.btnUpload);

        imageView = findViewById(R.id.imageView);



        carregarInfo();


        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();*/
                SelectImage();

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarArquivo();




            }
        });

        btnSair1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }


    private void carregarInfo(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios").child(Base64Custom.codificarBase64(email));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // String teste = dataSnapshot.getValue().toString();

                /*String teste = dataSnapshot.child("etapa").getValue().toString();
                Log.d("AQUI --------->ETAPA",teste);*/




                txtProcesso.setText(dataSnapshot.child("processo").getValue().toString());
                txtSituacao.setText(dataSnapshot.child("situacao").getValue().toString());
                txtMensagem.setText(dataSnapshot.child("mensaggem").getValue().toString());
                txtEtapa.setText(dataSnapshot.child("etapa").getValue().toString());
                txtLink.setText(dataSnapshot.child("link").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }



    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }



    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media

                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }



        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        if (filePath != null) {

            // Code for showing progressDialog while uploading
            progressDialog
                    = new ProgressDialog(this);

            progressDialog.setTitle("Preparando imagem");
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.ic_local_florist_black_24dp,null));
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref  = storageReference.child("images/"+ UUID.randomUUID().toString());



            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            urlImage = task.getResult().toString();
                                            txtLink.setText(urlImage);
                                        }
                                    });

                                    // Image uploaded successfully
                                    // Dismiss dialog

                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(PrincipalActivity.this,
                                                    "Imagem pronta faça o upload",
                                                    Toast.LENGTH_SHORT)
                                            .show();




                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(PrincipalActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");


                                }
                            });





        }

    }
    private void carregarArquivo(){






        if(txtLink.getText().toString() != null) {


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseRef = database.getReference();

            String email = user.getEmail();


            mDatabaseRef.child("usuarios").child(Base64Custom.codificarBase64(email)).
                    child("link").setValue(txtLink.getText().toString());

            Toast
                    .makeText(PrincipalActivity.this,
                            "Upload realizado com sucesso!",
                            Toast.LENGTH_SHORT)
                    .show();
        }


    }




}






