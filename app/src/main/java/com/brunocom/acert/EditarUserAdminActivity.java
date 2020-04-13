package com.brunocom.acert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditarUserAdminActivity extends AppCompatActivity {

    private Button btnGravar;
    private  Object resp;
    PainelAdmActivity pa = new PainelAdmActivity();

    private TextView txtNome;
    private TextView txtEmail;
    private TextView txtId;
    private TextView txtTelefone;
    private TextView txtProcesso;
    private TextView txtSituacao;
    private TextView txtMensagem;
    private TextView txtEtapa;
    private TextView txtLink;
    private Usuario usuario;

    public  static final String CHANNEL_1_ID = "channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_user_admin);

         usuario = new Usuario();
        btnGravar = findViewById(R.id.btnGravar);

        txtNome = findViewById(R.id.txtNomeEUAA);
        txtEmail= findViewById(R.id.txtEmailEUAA);
        txtId = findViewById(R.id.idUsuarioEUAA);
        txtTelefone = findViewById(R.id.telefoneEUAA);
        txtProcesso = findViewById(R.id.processoEUAA);
        txtSituacao = findViewById(R.id.situacaoEUAA);
        txtMensagem = findViewById(R.id.mensagemEUAA);
        txtEtapa = findViewById(R.id.etapaEUAA);
        txtLink = findViewById(R.id.txtLinkEUAA);

        Intent intent = getIntent();

         resp = intent.getSerializableExtra("info");
        carregarUsuario();

        //Log.d("AQUI 3----->", resp.toString());


        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                usuario.setNome(txtNome.getText().toString());
                usuario.setEmail(txtEmail.getText().toString());
                usuario.setEtapa(txtEtapa.getText().toString());
                usuario.setIdUsuario(txtId.getText().toString());
                usuario.setLink(txtLink.getText().toString());
                usuario.setMensaggem(txtMensagem.getText().toString());
                usuario.setSituacao(txtSituacao.getText().toString());
                usuario.setTelefone(txtTelefone.getText().toString());
                usuario.setProcesso(txtProcesso.getText().toString());

                usuario.salvar();

                Toast.makeText(EditarUserAdminActivity.this, "LOCALIZADO", Toast.LENGTH_SHORT).show();
               // FirebaseAuth.getInstance().signOut();


               // finish();

            }
        });





    }



    /*String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                                            usuario.setIdUsuario(idUsuario);
                                            usuario.salvar();
    finish();*/

    private void carregarUsuario(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios").child(Base64Custom.codificarBase64(resp.toString()));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // String teste = dataSnapshot.getValue().toString();

                /*String teste = dataSnapshot.child("etapa").getValue().toString();
                Log.d("AQUI --------->ETAPA",teste);*/


               txtNome.setText(dataSnapshot.child("nome").getValue().toString());
               txtEmail.setText(dataSnapshot.child("email").getValue().toString());
               txtId.setText(dataSnapshot.child("idUsuario").getValue().toString());
               txtTelefone.setText(dataSnapshot.child("telefone").getValue().toString());
               txtProcesso.setText(dataSnapshot.child("processo").getValue().toString());
               txtSituacao.setText(dataSnapshot.child("situacao").getValue().toString());
               txtMensagem.setText(dataSnapshot.child("mensaggem").getValue().toString());
               txtEtapa.setText(dataSnapshot.child("etapa").getValue().toString());
               txtLink.setText(dataSnapshot.child("link").getValue().toString());

               //createNotfication();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }



    private void createNotfication(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }




    }





    }
