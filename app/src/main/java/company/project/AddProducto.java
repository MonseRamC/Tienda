package company.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProducto extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context contextHere;

    private EditText nombre;
    private EditText descripcion;
    private EditText contacto;
    private EditText articulos;
    private EditText precio;

    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proveedor);

        contextHere = getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nombre = findViewById(R.id.et_nombre_proveedor);
        descripcion = findViewById(R.id.et_descripcion);
        contacto = findViewById(R.id.et_contacto);
        articulos = findViewById(R.id.et_articulos);
        precio = findViewById(R.id.et_precio);

        btnAgregar = findViewById(R.id.btn_agregar_proveedor);


    }



    private void agregarProveedor(String nombre, String descripcion, double cantidad, double peso, double precio){
        Producto producto = new Producto(nombre,descripcion,cantidad,peso,precio,0,false);
        mDatabase.child("Proveedores").child(nombre).setValue(producto);

        SimpleAlertDialog myAlert = new SimpleAlertDialog();
        myAlert.show(AddProducto.this,"Proveedor agregado Correctamente");
    }


}