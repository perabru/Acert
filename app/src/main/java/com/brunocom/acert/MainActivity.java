package com.brunocom.acert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private Button btnEntrar;

    private TextView txtEmail;
    private TextView lblLoginAdm;
    private TextView txtSenha;
    FirebaseAuth mAuth;
    private TextView lblEsqueciSenha;


    //When initializing your Activity, check to see if the user is currently signed in.

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnEntrar = findViewById(R.id.btnEntrar);

        lblLoginAdm = findViewById(R.id.lblLoginAdm);

        txtEmail = findViewById(R.id.txtEmailMainActivity);
        txtSenha = findViewById(R.id.txtSenhaMainActivity);
        lblEsqueciSenha = findViewById(R.id.lblEsqueciSenha);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastraActivity.class);
                startActivity(intent);
            }
        });

        lblEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RecuperarSenhaActivity.class);
                startActivity(intent);

            }
        });


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logarUsuario();
                /*Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(intent);*/
            }
        });

        lblLoginAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), LoginAdminActivity.class);
                startActivity(intent);
                finish();

               /* Intent intent = new Intent(getApplicationContext(), PainelAdmActivity.class);
                startActivity(intent);*/

            }
        });
    }


    private void logarUsuario() {


// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Conectando se já existe
        mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Falha ao logar",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }







}
