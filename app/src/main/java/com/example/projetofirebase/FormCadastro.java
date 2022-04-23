package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    EditText edit_nome, edit_email, edit_senha;
    Button bt_cadastrar;
    String[] mensagens = {"Preencha todos os campos","Cadastro realizado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        getSupportActionBar().hide();
        iniciarComponentes();

        //Botão cadastrar
        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){

                    //Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                    //                    snackbar.setBackgroundTint(Color.WHITE); // Cor de fundo é branco
                    //                    snackbar.setTextColor(Color.BLACK); // Cor do texto
                    //                    snackbar.show();

                    Toast.makeText(FormCadastro.this, mensagens[0], Toast.LENGTH_SHORT).show();

                }else{
                    cadastrarUsuario();
                }
            }
        });
    }

    private void cadastrarUsuario() {

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    salvarDadosUsuarios();

                    //Snackbar snackbar = Snackbar.make(view,mensagens[1],Snackbar.LENGTH_SHORT);
                    //                    snackbar.setBackgroundTint(Color.WHITE); // Cor de fundo é branco
                    //                    snackbar.setTextColor(Color.BLACK); // Cor do texto
                    //                    snackbar.show();

                    Toast.makeText(FormCadastro.this, mensagens[1], Toast.LENGTH_SHORT).show();

                }
                else {
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){ // Se o usuário tenta cadastrar uma senha com poucos caracteres
                        erro = "Digite uma senha com no mínimo 6 caracteres";

                    }catch (FirebaseAuthUserCollisionException e) { // Se o usuario usa uma conta já cadastrada
                        erro = "Essa conta já foi cadastrada";

                    }catch(FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";

                    }catch (Exception e){
                        erro = "Erro ao cadastrar";
                    }

                    //Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    //                    snackbar.setBackgroundTint(Color.WHITE); // Cor de fundo é branco
                    //                    snackbar.setTextColor(Color.BLACK); // Cor do texto
                    //                    snackbar.show();
                    Toast.makeText(FormCadastro.this, erro, Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    private void salvarDadosUsuarios(){ // Salvar dados dos usuarios no banco de dados do Firebase

        String nome = edit_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Para pegar a instancia do banco de dados

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);

        // usuarios.put("telefone",telefone);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Obter usuario atual, e pegar o id de cada usuario

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID); // Cada usuario criado no banco vai ter um documento especifico, esse documento vai ser baseado no usuarioID
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) { // se os dados do usuarios for salvo com sucesso
                Log.d("db","Sucesso ao salvar os dados");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error","Erro ao salvar os dados" + e.toString());
            }
        });
    }

    private void iniciarComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }
}