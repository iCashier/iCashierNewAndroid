package com.icashier.app.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.List;
import java.util.Locale;

/**
 * Created by techugo on 3/2/17.
 */

public class AddressUtil {

    public static String getDistanceInKmStr(Location pickupLocation, Location dropLocation) {
        float distanceInMeters = pickupLocation.distanceTo(dropLocation);
        String stDistanceInMeters = String.format("%.01f", distanceInMeters);
        float distanceInKm = Float.parseFloat(stDistanceInMeters) / 1000;
        return String.format("%.02f", distanceInKm);
    }

    public static String getTimeInKmStr(Location pickupLocation, Location dropLocation) {
        float distanceInMeters = pickupLocation.distanceTo(dropLocation);
        String stDistanceInMeters = String.format("%.01f", distanceInMeters);
        float distanceInKm = Float.parseFloat(stDistanceInMeters) / 1000;
        int speedInMetersPerMinute = 40;
        float estimatedDriveTimeInMinutes = distanceInKm / speedInMetersPerMinute;

        return "" + (int) (estimatedDriveTimeInMinutes * 60);
    }


    /**
     * Get Address info from location using Android Geocoder class
     *
     * @param loc
     * @return
     */
    public static void getAddress(final Context context, final Location loc, final AddressCallback addressCallback) {

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                try {
                    Geocoder geocoder;
                    List<Address> addresses = null;

                    geocoder = new Geocoder(context, Locale.getDefault());
                    addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = "";
                   // addresses =null;
                    // Utility.showToast(this, state);
                    if (addresses != null && addresses.size() > 0) {
                        final String city = addresses.get(0).getLocality();
                        final String postalCode = addresses.get(0).getPostalCode();
                        final String state = addresses.get(0).getAdminArea();
                        Address returnedAddress = addresses.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder("");

                       /* for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                        }*/
                        address = returnedAddress.getAddressLine(0);
                        final String addressStr = address;
                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                addressCallback.onAddress(addressStr,state, city, postalCode);
                            }
                        };
                        new Handler(context.getMainLooper()).post(myRunnable);

                    } else {
                        //address = "";
                       // getAddressFromApi(context, loc, addressCallback);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    //getAddressFromApi(context, loc, addressCallback);
                }
            }
        };

        handler.sendEmptyMessage(0);
    }

    /*private static void getAddressFromApi(final Context context, final Location loc, final AddressCallback addressCallback) {
        if (loc != null) {
            new RestClient(context).getRequestGeneric("address", "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + loc.getLatitude() + "," + loc.getLongitude() + "&sensor=true&key=AIzaSyCDlcD6M8lCTAeZgAFlzIk8bOx281EZWRI", new RestClient.ResponseListener() {
                @Override
                public void onResponse(String tag, String response) {

                    String jsonStr = response;
                    String address = null;
                    String state = null;
                    String city = null;
                    String postalCode = null;
                    try {
                        List<Address> res = new ArrayList<Address>();
                        try {
                            address = new JSONObject(jsonStr).getJSONArray("results").getJSONObject(0).getString("formatted_address");

                            JSONArray address_components = new JSONObject(jsonStr).getJSONArray("results").getJSONObject(0).getJSONArray("address_components");

                            for (int i = 0; i < address_components.length(); i++) {
                                JSONObject zero2 = address_components.getJSONObject(i);
                                String long_name = zero2.getString("long_name");
                                JSONArray mtypes = zero2.getJSONArray("types");
                                String type = mtypes.getString(0);

                                if (long_name != null || long_name.length() > 0) {
                                    if (type.equalsIgnoreCase("locality")) {
                                        city = long_name;
                                    }
                                    else if(type.equalsIgnoreCase("postal_code"))
                                    {
                                        postalCode = long_name;
                                    }else if(type.equalsIgnoreCase("administrative_area_level_1"))
                                    {
                                        state = long_name;
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            addressCallback.onAddress(null,null, null, null);
                            e.printStackTrace();
                        }
                        addressCallback.onAddress(address,state, city, postalCode);

                    } catch (Exception e) {
                        e.printStackTrace();
                        addressCallback.onAddress(null,null, null, null);
                    }
                }
            }, new RestClient.ErrorListener() {
                @Override
                public void onError(String tag, String errorMsg) {
                    String errorStr = errorMsg;
                    addressCallback.onAddress(null,null, null, null);
                }
            });
        } else {
            AlertUtil.showToastShort(context, "Failed to get your location address !");
        }
    }

*/
    public interface AddressCallback {
        void onAddress(String address, String state, String city, String postalCode);
    }

}
