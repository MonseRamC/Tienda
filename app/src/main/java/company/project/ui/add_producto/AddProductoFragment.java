package company.project.ui.add_producto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import company.project.Producto;
import company.project.R;


public class AddProductoFragment extends Fragment {

    private AddProductoViewModel addProductoViewModel;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context contextHere;

    private EditText nombre;
    private EditText descripcion;
    private EditText precio;
    private EditText cantidad;
    private EditText peso;

    private Button btnAgregar;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addProductoViewModel =
                new ViewModelProvider(this).get(AddProductoViewModel.class);
        root = inflater.inflate(R.layout.fragment_add_producto, container, false);


        contextHere = root.getContext();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nombre = root.findViewById(R.id.et_nombre_producto);
        descripcion = root.findViewById(R.id.et_descripcion);
        precio = root.findViewById(R.id.et_precio);
        cantidad = root.findViewById(R.id.et_cantidad);
        peso = root.findViewById(R.id.et_peso);

        btnAgregar = root.findViewById(R.id.btn_agregar_producto);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarProveedor(
                        nombre.getText().toString(),
                        descripcion.getText().toString(),
                        Double.valueOf(precio.getText().toString()),
                        Double.valueOf(cantidad.getText().toString()),
                        Double.valueOf(peso.getText().toString())
                );

            }
        });




        addProductoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }






    private void agregarProveedor(String nombre, String descripcion, double precio, double cantidad, double peso){
        Producto producto = new Producto(nombre,descripcion,precio,cantidad,peso,0,false);
        mDatabase.child("Productos").child(nombre).setValue(producto);

        AlertDialog.Builder builder = new AlertDialog.Builder(contextHere);

        // Set the message show for the Alert time
        builder.setMessage("El Producto se ha agregado con exito");

        // Set Alert Title
        builder.setTitle("Operacion concluida");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Hecho", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            //finish();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}