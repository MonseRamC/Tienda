package company.project.ui.add_admin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import company.project.R;
import company.project.SimpleAlertDialog;
import company.project.User;


public class AddAdminFragment extends Fragment {

    private AddAdminViewModel homeViewModel;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText name;
    private EditText email;
    private EditText pass;
    private Button btn_registro_user;
    private Button btn_registro_admin;

    private Context contextAddAdmin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(AddAdminViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_admin, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        contextAddAdmin = root.getContext();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = root.findViewById(R.id.et_user);
        email = root.findViewById(R.id.et_email);
        pass = root.findViewById(R.id.et_password);
        btn_registro_user = root.findViewById(R.id.btn_registro_user);
        btn_registro_admin = root.findViewById(R.id.btn_registro_admin);


        btn_registro_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(  getActivity() , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    registrarRole("ADMIN","No establecida",name.getText().toString(),user);
                                    SimpleAlertDialog myAlert = new SimpleAlertDialog();
                                    myAlert.show(getActivity(),"ADMIN registrado correctamente");
                                } else {

                                }
                            }
                        });
            }
        });

        return root;
    }

    public void registrarRole(String role, String hubicacion,String userName, FirebaseUser account){
        User user = new User(role,hubicacion,userName);
        mDatabase.child("Usuarios").child(account.getUid()).setValue(user);
    }

}