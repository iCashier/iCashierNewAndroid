package com.icashier.app.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.activity.CompletePlansActivity;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.activity.LoginActivity;
import com.icashier.app.activity.OtpActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogNewOutletBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.SigninResponse;

import java.util.HashMap;
import java.util.List;

import static com.icashier.app.fragment.MerchantHomeFragment.createDrawableFromView;

public class CreateOutletDialog extends FragmentActivity {

    CreateOutletDialog context;
    DialogNewOutletBinding binding;
    double lat, lng;
    private SupportMapFragment mapFragment;
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    List<SigninResponse.ResultBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.setContentView(context, R.layout.dialog_new_outlet);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);

        getExtras();
        setMap();
        binding.tvLocation.setOnClickListener(V -> {
           // getPlaceAutoComplete();
        });

        binding.btnCancel.setOnClickListener(V -> {
            finish();
        });

        binding.btnCreate.setOnClickListener(V -> {
            callCreateOutletApi();
        });

        binding.disableClick.setOnClickListener(V->{

        });

        binding.btnView.setOnClickListener(V->{
            new SelectOutletDialog(context,list).show();
        });
    }

    //========Method to get extras from intent==========//
    private void getExtras() {
        lat = getIntent().getDoubleExtra(AppConstant.LATITUDE, 0);
        lng = getIntent().getDoubleExtra(AppConstant.LONGITUDE, 0);
        list=new Gson().fromJson(getIntent().getStringExtra(AppConstant.OUTLET), new TypeToken<List<SigninResponse.ResultBean>>() {
        }.getType());
    }

    //===============Method to set map=====================//
    private void setMap() {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gMap) {
                    googleMap = gMap;
                    markerOptions = new MarkerOptions();
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context)));
                    LatLng location = new LatLng(lat, lng);
                    googleMap.clear();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));
                    String locationName = Utilities.getCurrentAddress(location.latitude, location.longitude, context);
                    binding.tvLocation.setText(locationName);


                    googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {

                            lat = googleMap.getCameraPosition().target.latitude;
                            lng = googleMap.getCameraPosition().target.longitude;
                            String address = Utilities.getCurrentAddress(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, context);

                            if (address != null) {
                                binding.tvLocation.setText(address);
                            }
                        }
                    });

                    PermissionsUtil.askPermission(context, PermissionsUtil.LOCATION, new PermissionsUtil.PermissionListener() {
                        @Override
                        public void onPermissionResult(boolean isGranted) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                @Override
                                public boolean onMyLocationButtonClick() {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            lat = googleMap.getCameraPosition().target.latitude;
                                            lng = googleMap.getCameraPosition().target.longitude;
                                            String address = Utilities.getCurrentAddress(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, context);

                                            if (address != null) {
                                                binding.tvLocation.setText(address);
                                            }
                                        }
                                    }, 2000);

                                    return false;
                                }
                            });
                        }
                    });


                }
            });
        }

        // R.id.map is a FrameLayout, not a Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMapView, mapFragment).commitAllowingStateLoss();
    }

    //*****************Method to open google place autocomplete activity*************//
    private void getPlaceAutoComplete() {
        // binding.tvLocation.setEnabled(false);

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(context);
            context.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            /*AlertUtil.toastMsg(context,context.getString(R.string.error_generic));
            binding.tvLocation.setEnabled(true);*/
        } catch (GooglePlayServicesNotAvailableException e) {
            /*AlertUtil.toastMsg(context,context.getString(R.string.error_generic));
            binding.tvLocation.setEnabled(true);*/

        }
    }

    public void setData(double latitude, double longitude, String address) {
        LatLng location = new LatLng(latitude, longitude);
        lat = latitude;
        lng = longitude;
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));
        binding.tvLocation.setText(address);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(context, data);
                setData(place.getLatLng().latitude, place.getLatLng().longitude, place.getAddress().toString());


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                //AlertUtil.toastMsg(context,context.getString(R.string.error_generic));

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.hideSoftKeyboard(context);
                    }
                }, 200);

            }
        }
    }


    //==========================Method to call change online status api=====================//
    private void callCreateOutletApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("email", getIntent().getStringExtra(AppConstant.EMAIL));
            params.put("password", getIntent().getStringExtra(AppConstant.PASSWORD));
            params.put("device_type","android");
            params.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));
            params.put("lat", "" + lat);
            params.put("lng", "" + lng);
            params.put("location",binding.tvLocation.getText().toString());


            RestClient.ApiRequest apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CREATE_NEW_OUTLET)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                SigninResponse loginResponse = new Gson().fromJson(response, SigninResponse.class);
                                if (loginResponse != null) {
                                    AlertUtil.hideProgressDialog();

                                    if (loginResponse.getCode() == 200) {
                                        if (!loginResponse.getResult().getPlanid().equals("")) {
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(loginResponse.getResult()));
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, loginResponse.getResult().getUid());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT);
                                            SharedPrefManager.getInstance(context).saveInt(AppConstant.IS_PARENT,loginResponse.getResult().getIsParent());
                                            //Clears previous activities
                                            Utilities.clearAllgoToActivity(context, HomeActivity.class);
                                        } else {
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                            Intent intent = new Intent(context, CompletePlansActivity.class);
                                            intent.putExtra(AppConstant.SIGNIN_RESPONSE, loginResponse.getResult());
                                            startActivity(intent);

                                        }
                                    } else if (loginResponse.getCode() == 202) {
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(loginResponse.getResult()));
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                        AlertUtil.toastMsg(context, loginResponse.getMessage());
                                        Intent intent = new Intent(context, OtpActivity.class);
                                        intent.putExtra(AppConstant.COUNTRY_CODE, loginResponse.getResult().getCountry_code());
                                        intent.putExtra(AppConstant.PHONE_NO, loginResponse.getResult().getMobile());
                                        startActivity(intent);
                                    } else if (loginResponse.getCode() == 203) {
                                        Intent intent = new Intent(context, CreateOutletDialog.class);
                                        intent.putExtra(AppConstant.LATITUDE, lat);
                                        intent.putExtra(AppConstant.LONGITUDE, lng);

                                        startActivity(intent);
                                    } else {

                                        AlertUtil.toastMsg(context, loginResponse.getMessage());
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }
}
