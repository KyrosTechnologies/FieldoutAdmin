package com.kyros.technologies.fieldout.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Thirunavukkarasu on 19-11-2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG="MyFirebaseIIDService";
    private com.kyros.technologies.fieldout.sharedpreference.PreferenceManager store;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        store= com.kyros.technologies.fieldout.sharedpreference.PreferenceManager.getInstance(getApplicationContext());
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Refreshed token: "+refreshedToken);
        store.putFCMToken(refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        //TODO: Implement this method for waggonstation server is ready.
    }
}
