package company.project.ui.ver_productos;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerProductosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VerProductosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SensorHubicacion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}