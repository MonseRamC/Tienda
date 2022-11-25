package company.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText name;
    private EditText email;
    private EditText pass;
    private Button btn_registro_user;
    private Button btn_registro_admin;

    private Context contextRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        contextRegistro = getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.et_user);
        email = findViewById(R.id.et_email);
        pass = findViewById(R.id.et_password);
        btn_registro_user = findViewById(R.id.btn_registro_user);


        btn_registro_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().equals("") && !pass.getText().equals(""))
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    registrarRole("USER","No establecida",name.getText().toString(),user);
                                    updateUI(user);
                                } else {
                                    updateUI(null);
                                }


                            }
                        });
            }
        });

    }


    public void updateUI(FirebaseUser account){

        if(account != null){
            DatabaseReference refUsers = mDatabase.child("Usuarios");
            refUsers.child(account.getUid()).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Toast.makeText(getApplicationContext(),task.getResult().getValue().toString(),Toast.LENGTH_SHORT).show();
                        if(task.getResult().getValue().toString().equals("ADMIN"))
                            startActivity(new Intent(contextRegistro,HomeAdminMenu.class));
                        else
                            startActivity(new Intent(contextRegistro,HomeUserMenu.class));
                    }
                }
            });
        }else {
            Toast.makeText(this,"You Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }


    public void registrarRole(String role, String hubicacion, String userName, FirebaseUser account){
        User user = new User(role, hubicacion,userName);
        mDatabase.child("Usuarios").child(account.getUid()).setValue(user);
    }
}