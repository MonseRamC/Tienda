package company.project;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditarProductoFragment extends Fragment {

    private TextView tv_producto;
    private EditText et_name;
    private EditText et_descripcion;
    private EditText et_precio;
    private EditText et_cantidad;
    private EditText et_peso;

    private Button btnGuardar;

    private String Producto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_editar_producto, container, false);

        tv_producto = root.findViewById(R.id.tv_producto);

        et_name = root.findViewById(R.id.et_name);
        et_descripcion = root.findViewById(R.id.et_descripcion);
        et_precio = root.findViewById(R.id.et_precio);
        et_cantidad = root.findViewById(R.id.et_cantidad);
        et_peso = root.findViewById(R.id.et_peso);

        btnGuardar = root.findViewById(R.id.btn_guardar_producto);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        Producto = sharedPreferences.getString("SelectedProductos", "");
        tv_producto.setText(Producto);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference().child("Productos").child(Producto);

                refUsers.child("name").setValue(et_name.getText().toString());
                refUsers.child("descripcion").setValue(et_descripcion.getText().toString());
                refUsers.child("precio").setValue(et_precio.getText().toString());
                refUsers.child("cantidad").setValue(et_cantidad.getText().toString());
                refUsers.child("peso").setValue(et_peso.getText().toString());

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SelectedProductos", et_name.getText().toString());
                editor.apply();

                Toast.makeText(getContext(),"Cambios Aplicados",Toast.LENGTH_SHORT).show();

                Navigation.findNavController(view).navigate(R.id.lectorQRFragment);
            }
        });


        return root;
    }

}
