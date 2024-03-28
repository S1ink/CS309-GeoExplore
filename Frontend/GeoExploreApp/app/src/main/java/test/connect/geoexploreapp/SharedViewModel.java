package test.connect.geoexploreapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Double> latitude = new MutableLiveData<>();
    private final MutableLiveData<Double> longitude = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isCreateEmergencyNotification = new MutableLiveData<>();


    public void setLocation(double lat, double lon) {
        latitude.setValue(lat);
        longitude.setValue(lon);
    }

    public LiveData<Double> getLatitude() {
        return latitude;
    }

    public LiveData<Double> getLongitude() {
        return longitude;
    }

    public void setCreateEmergencyNotification(boolean isCreate) {
        Log.d("SharedViewModel","" + isCreate);
        isCreateEmergencyNotification.setValue(isCreate);
    }

    public LiveData<Boolean> getCreateEmergencyNotification() {
        return isCreateEmergencyNotification;
    }
}
