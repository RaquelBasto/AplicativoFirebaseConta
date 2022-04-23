package com.example.projetofirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaPrincipal extends AppCompatActivity {

    private TextView textNomeUsuario, textEmailUsuario;

    private Button btn_deslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        getSupportActionBar().hide();
        IniciarComponentes();


        btn_deslogar.setOnClickListener(new View.OnClickListener() { // para deslogar o usuario
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(TelaPrincipal.this,FormLogin.class); // intenção para mudar de tela
                startActivity(intent); // inicializar a intenção de mudar de tela
                finish(); // para finalizar
            }
        });
    }


    // todo vez que entrar nessa tela ira iniciar esse ciclo de vida

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                if (documentSnapshot != null){
                    textNomeUsuario.setText(documentSnapshot.getString("nome"));
                    textEmailUsuario.setText(email);
                }
            }
        });

    }

    private void IniciarComponentes(){
        textEmailUsuario = findViewById(R.id.textEmailUsuario);
        textNomeUsuario = findViewById(R.id.textNomeUsuario);
        btn_deslogar = findViewById(R.id.btn_deslogar);
    }
}