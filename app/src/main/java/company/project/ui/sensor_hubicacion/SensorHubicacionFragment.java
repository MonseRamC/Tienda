package company.project.ui.sensor_hubicacion;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import company.project.R;
import company.project.ui.ver_productos.VerProductosViewModel;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SensorHubicacionFragment extends Fragment {

    private VerProductosViewModel sensorHubicacionViewModel;

    private TextView lat;
    private TextView lon;
    private TextView hub;
    private Button btnHubicacion;
    private Button btnSetHubicacion;
    private boolean hubfound;

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sensorHubicacionViewModel =
                new ViewModelProvider(this).get(VerProductosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sensor_hubicacion, container, false);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lat = root.findViewById(R.id.latitude);
        lon = root.findViewById(R.id.longitude);
        hub = root.findViewById(R.id.hubicacion);
        hubfound = false;
        btnHubicacion = root.findViewById(R.id.btn_hubicacion);
        btnHubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){


                    try{

                        LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location != null){
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();

                            lat.setText("Latitude : " + String.valueOf(latitude));
                            lon.setText("Longitude : " + String.valueOf(longitude));

                            OkHttpClient client = new OkHttpClient();

                            Request request = new Request.Builder()
                                    .url("https://address-from-to-latitude-longitude.p.rapidapi.com/geolocationapi?lat="+latitude+"&lng="+longitude)
                                    .get()
                                    .addHeader("X-RapidAPI-Key", "ed654ae60emsh9e29ccddfb1f818p13f12fjsnd98febc861d4")
                                    .addHeader("X-RapidAPI-Host", "address-from-to-latitude-longitude.p.rapidapi.com")
                                    .build();

                            Response response = client.newCall(request).execute();
                            String res = response.body().string();

                            int start = res.indexOf("address") + 10;
                            String sub = res.substring(start , res.length());
                            int end = sub.indexOf("\"");
                            String addr = sub.substring(0, end);

                            hub.setText("Hubicacion : " + addr );
                            hubfound = true;
                        }
                        else{
                            Toast.makeText(getContext(),"Error de GPS",Toast.LENGTH_SHORT).show();
                        }


                    }
                    catch (IOException io){
                        Toast.makeText(getContext(),"Error de GPS",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(),"No tiene permisos de GPS",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSetHubicacion = root.findViewById(R.id.btn_set_hubicacion);
        btnSetHubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hubfound){
                    FirebaseUser user = mAuth.getCurrentUser();
                    DatabaseReference refUsers = mDataBase.child("Usuarios");
                    refUsers.child(user.getUid()).child("hubicacion").setValue(hub.getText().toString());
                    Toast.makeText(getContext(),"Hubicacion Actualizada",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"Debe especificar una hubicacion",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }


}