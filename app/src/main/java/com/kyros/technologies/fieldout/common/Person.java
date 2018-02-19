package com.kyros.technologies.fieldout.common;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Rohin on 19-02-2018.
 */

public class Person implements ClusterItem {
    private String placeid=null;
    private LatLng mPosition=null;

    private BitmapDescriptor icon=null;
    private String google=null;

    public String getRating() {
        return rating;
    }
    public Person(){

    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;



    public Person(LatLng position, String placeid,String google ,String rating ) {
        this.placeid = placeid;
        this.google=google;
        this. mPosition = position;
        this.rating=rating;

    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    public String getPlaceid(){
        return placeid;
    }

    public void setGoogle(String google) {
        this.google = google;
    }



    public String getGoogle() {
        return google;
    }



    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }
    public void clearData(){
        this.placeid = null;
        this.google=null;
        this. mPosition = null;
        this.rating=null;

    }
}
