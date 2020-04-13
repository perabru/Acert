package com.brunocom.acert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private Button btnRecuperarSenha;
    private TextView txtRecuperarSenhaActivity;
    private String emailRecuperarSenha;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);


        btnRecuperarSenha = findViewById(R.id.btnRecuperar);
        txtRecuperarSenhaActivity = findViewById(R.id.txtEmailRecuperarSenhaActivity);

        emailRecuperarSenha = txtRecuperarSenhaActivity.getText().toString();



        btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseAuth.getInstance().sendPasswordResetEmail(txtRecuperarSenhaActivity.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RecuperarSenhaActivity.this,"Email enviado para: "+txtRecuperarSenhaActivity.getText().toString(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                }
                            }
                        });

            }
        });
    }
}
