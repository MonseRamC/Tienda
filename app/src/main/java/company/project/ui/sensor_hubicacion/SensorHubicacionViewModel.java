package company.project.ui.sensor_hubicacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SensorHubicacionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SensorHubicacionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SensorHubicacion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}