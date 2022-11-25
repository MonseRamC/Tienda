package company.project;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import company.project.ui.ver_productos.VerProductosViewModel;

public class HomeHomeFragment extends Fragment implements SensorEventListener {

    ImageView click_image_id;

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private StorageReference photoReference;

    private VerProductosViewModel sensorCamaraViewModel;

    private TextView userNameText;
    private TextView userHubicacionText;

    private TextView userRoleText;
    private TextView userHeaderNameText;

    private Button btnNuevaFoto;
    private Button btnNuevaHubicacion;

    private TextView tv_temperature;
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private boolean tempAvailable = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        tv_temperature = root.findViewById(R.id.tv_temperatura);
        sensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null ){
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            tempAvailable = true;
        }else {
            tv_temperature.setText("Temperatura ambiente : No Data");
        }

        click_image_id = root.findViewById(R.id.user_foto);
        click_image_id.setBackgroundColor(getResources().getColor(R.color.gray));

        storageReference = FirebaseStorage.getInstance().getReference();
        photoReference = storageReference.child("images/"+ mAuth.getUid() +".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                click_image_id.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        userNameText = root.findViewById(R.id.user_info);
        userHubicacionText = root.findViewById(R.id.user_hubicacion);

        DatabaseReference refUsers = mDataBase.child("Usuarios");
        refUsers.child(mAuth.getUid()).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userNameText.setText("Usuario : " + task.getResult().getValue().toString());

                    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);
                    userHeaderNameText = (TextView) header.findViewById(R.id.header_name);
                    userHeaderNameText.setText(task.getResult().getValue().toString());
                }
            }
        });
        refUsers.child(mAuth.getUid()).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {


                    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);
                    userRoleText = (TextView) header.findViewById(R.id.header_role);
                    userRoleText.setText(task.getResult().getValue().toString());
                }
            }
        });
        refUsers.child(mAuth.getUid()).child("hubicacion").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().getValue().toString().equals("No establecida")){
                        userHubicacionText.setText("Hubicacion : " + task.getResult().getValue().toString());
                    }
                    else{
                        userHubicacionText.setText(task.getResult().getValue().toString());
                    }

                }
            }
        });


        btnNuevaFoto = root.findViewById(R.id.btn_nuevafoto);
        btnNuevaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.sensorCamara);
            }
        });

        btnNuevaHubicacion = root.findViewById(R.id.btn_nuevahubicacion);
        btnNuevaHubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_hubicacion);
            }
        });

        return root;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tv_temperature.setText("Temperatura ambiente : " + event.values[0] + " °C");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if(tempAvailable){
            sensorManager.registerListener(this,tempSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(tempAvailable){
            sensorManager.unregisterListener(this);
        }
    }
}