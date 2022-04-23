package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
// import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText edit_email;
    private EditText edit_senha;
    private Button btn_entrar;
    private ProgressBar progressBar;
    public String[] mensagens = {"Preencha todos os campos","Erro ao logar usuário"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        //Para esconder a barra roxa
        getSupportActionBar().hide();

        IniciarComponentes();

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FormLogin.this,FormCadastro.class);
                startActivity(intent);
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    //Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                    //                    snackbar.setBackgroundTint(Color.WHITE); // Cor de fundo é branco
                    //                    snackbar.setTextColor(Color.BLACK); // Cor do texto
                    //                    snackbar.show();

                        //this, "string", Toast.LENGTH_LONG
                    Toast.makeText(FormLogin.this, mensagens[0], Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    AutenticarUsuario();
                }

            }
        });
    }

    private void AutenticarUsuario(){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    },3000);
                }
                else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = mensagens[1];
                    }
                    Toast.makeText(FormLogin.this, erro, Toast.LENGTH_SHORT).show();
                    return;
                    //Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    //                    snackbar.setBackgroundTint(Color.WHITE); // Cor de fundo é branco
                    //                    snackbar.setTextColor(Color.BLACK); // Cor do texto
                    //                    snackbar.show();


                }

            }
        });

    }


    @Override
    protected void onStart() { // se o usuario já esta com a conta logada ele entrar para a tela de perfil
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null){
            TelaPrincipal();

        }
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this,TelaPrincipal.class);
        startActivity(intent);
        finish();
    }
    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        btn_entrar = findViewById(R.id.btn_entrar);
        progressBar = findViewById(R.id.progressbar);


    }
}