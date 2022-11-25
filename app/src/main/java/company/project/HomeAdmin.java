package company.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeAdmin extends AppCompatActivity {

    private Button btnAgregarProveedor;
    private Context contextHomeAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        contextHomeAdmin = getApplicationContext();

        btnAgregarProveedor = findViewById(R.id.btn_to_agregar_proveedor);
        btnAgregarProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(contextHomeAdmin, AddProducto.class));
            }
        });

    }
}