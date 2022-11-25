package company.project;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class SensorTemperaturaFragment extends Fragment implements SensorEventListener {


    private TextView tv_temperature;
    private  SensorManager sensorManager;
    private Sensor tempSensor;
    private boolean tempAvailable = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sensor_temperatura, container, false);

        tv_temperature = root.findViewById(R.id.tv_temperatura);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null ){
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            tempAvailable = true;
        }else {
            tv_temperature.setText("Temperatura ambiente : No Data");
        }

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

