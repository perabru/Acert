package com.brunocom.acert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastraActivity extends AppCompatActivity {

    //Objeto Firebase
    private FirebaseAuth mAuth;
    private Button btnCadastrar;

    private TextView txtNome;
    private TextView txtEmail;
    private TextView txtSenha;
    private TextView txtTelefone;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_activity);

        mAuth = FirebaseAuth.getInstance();
        btnCadastrar = findViewById(R.id.btnCadastrar2);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        txtTelefone = findViewById(R.id.txtTelefone);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = txtNome.getText().toString();
                String textoEmail = txtEmail.getText().toString();
                String textoSenha = txtSenha.getText().toString();
                String textoTelefone = txtTelefone.getText().toString();

                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){
                            //Realizar o cadastro do usuario

                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);
                            usuario.setTelefone(textoTelefone);
                            usuario.setMensaggem("-");
                            usuario.setEtapa("-");
                            usuario.setProcesso("-");
                            usuario.setSituacao("-");
                            usuario.setLink("-");

                            cadastrarUsuario();






                        }else{
                            Toast.makeText(CadastraActivity.this,"Preencha a senha", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(CadastraActivity.this,"Preencha o email", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CadastraActivity.this,"Preencha o nome", Toast.LENGTH_SHORT).show();

                }


            }
        });



    }
    private void cadastrarUsuario(){

                        //-----------------------------------Método para criar usuário
                        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                                            usuario.setIdUsuario(idUsuario);
                                            usuario.salvar();
                                            finish();
                                            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                                            startActivity(intent);

                                            // Sign in success, update UI with the signed-in user's information
                                            //Log.d(TAG, "createUserWithEmail:success");
                                           // FirebaseUser user = mAuth.getCurrentUser();
                                            // updateUI(user);
                                        } else {
                                            String excecao ="";
                                            try{
                                                throw task.getException();
                                            } catch(FirebaseAuthWeakPasswordException e){
                                                excecao ="Digite uma senha mais forte";

                                            } catch (FirebaseAuthInvalidCredentialsException e){
                                                excecao = "Por favor, digite um email válido";
                                            }
                                            catch (FirebaseAuthUserCollisionException e){
                                                excecao = "Essa conta ja foi cadastrada";
                                            }
                                            catch (Exception e){
                                                excecao ="Erro ao cadastrar usuário: "+e.getMessage();
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(CadastraActivity.this,"Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                        //------------------------------------FIM




    }




}