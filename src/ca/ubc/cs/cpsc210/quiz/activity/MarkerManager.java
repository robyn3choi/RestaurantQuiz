package ca.ubc.cs.cpsc210.quiz.activity;

import android.graphics.Color;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcarter on 14-11-06.
 *
 * Manager for markers plotted on map
 */
public class MarkerManager {
    private GoogleMap map;
    private List<Marker> markers;
    //Todo Added to track arrow
    private Marker arrow;

    /**
     * Constructor initializes manager with map for which markers are to be managed.
     *
     * @param map the map for which markers are to be managed
     */
    public MarkerManager(GoogleMap map) {
        this.map = map;
        markers = new ArrayList<Marker>();
    }

    /**
     * Get last marker added to map
     *
     * @return last marker added
     */
    public Marker getLastMarker() {
        return markers.get(markers.size() - 1);
    }

    /**
     * Add green marker to show position of restaurant
     *
     * @param point the point at which to add the marker
     * @param title the marker's title
     */
    public void addRestaurantMarker(LatLng point, String title) {
        Marker restaurant = map.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(120.0f)));
    }

    /**
     * Add a marker to mark latest guess from user.  Only the most recent two positions selected
     * by the user are marked.  The distance from the restaurant is used to create the marker's title
     * of the form "xxxx m away" where xxxx is the distance from the restaurant in metres (truncated to
     * an integer).
     * <p/>
     * The colour of the marker is based on the distance from the restaurant:
     * - red, if the distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param latLng
     * @param distance
     */
    public void addMarker(LatLng latLng, double distance) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title((int) distance + " m away")
                .icon(BitmapDescriptorFactory.defaultMarker(getColour(distance)))
                .alpha(0.7f));

        markers.add(0,marker);
        if (markers.size() > 2) {
            markers.get(2).remove();
            markers.remove(2);
        }
    }

    /**
     * Remove markers that mark user guesses from the map
     */
    public void removeMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    /**
     * Produce a colour on a linear scale between red and green based on distance:
     * <p/>
     * - red, if distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param distance distance from restaurant
     * @return colour of marker
     */
    private float getColour(double distance) {
        if (distance >= 3000) {
            return 0.0f;
        }
        float hue = new Float(120.0 * (1.0-distance/3000.0));
        return hue;
    }

    //Todo: Added
    /**
     * Adds arrow to map, points it towards bearing given by parameter
     * used for hints
     * sets arrow field
     * @param position, bearing
     * @return arrow
     */
    public Marker addArrow(LatLng position, Float bearing){
        Marker arrow = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
                .anchor(0.11f,0.79f)
                .flat(true)
                .position(position)
                .rotation(bearing-45.0f));
        this.arrow = arrow;
        return arrow;
    }

    /**
     * Removes the arrow from map and clears field
     * Called when user clicks on a marker again or on map
     * @throws java.lang.NullPointerException when arrow not set (ie when no hint used yet)
     */
    //Todo: Added to remove arrow
    public void removeArrow() {
        try {
            arrow.remove();
            arrow = null;
        } catch (NullPointerException e) {
        }
    }
}