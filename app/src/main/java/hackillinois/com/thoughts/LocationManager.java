package hackillinois.com.thoughts;

/**
 * Created by jatin1 on 2/20/16.
 */
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;

public class LocationManager {
    private double myLongitude;
    private double myLatitude;
    private GoogleApiClient api;

    public LocationManager(TwitterSearchActivity a) {
        api = new GoogleApiClient.Builder(a)
                .addApi(LocationServices.API)
                .build();
        api.connect();
    }

    public void findLoc() {
        api.connect();
        Location myLastLocation = LocationServices.FusedLocationApi.getLastLocation(api);
        if (myLastLocation != null) {
            this.myLongitude = myLastLocation.getLongitude();
            this.myLatitude = myLastLocation.getLatitude();
        }
    }

    public double getMyLatitude() {
        return myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setLat(double lat) {
        this.myLatitude = lat;
    }

    public void setLong(double longitude) {
        this.myLongitude = longitude;
    }
}
