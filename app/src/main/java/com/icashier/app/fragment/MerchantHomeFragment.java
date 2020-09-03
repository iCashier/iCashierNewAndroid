package com.icashier.app.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.activity.newplaces.NewPlaceActivity;
import com.icashier.app.adapter.RestaurantImagesAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentMerchantHomeBinding;
import com.icashier.app.dialog.AutomaticOrderDetailDialog;
import com.icashier.app.dialog.TodaySpecialDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.AppValidator;
import com.icashier.app.helper.ImagePickerUtil;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.SelectImageListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.MerchantDetailResponse;
import com.icashier.app.model.OrderListResponse;
import com.icashier.app.printer.AutoPrinterFuntionality;


import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class MerchantHomeFragment extends Fragment {

    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    HomeActivity context;
    RestClient.ApiRequest apiRequest;
    FragmentMerchantHomeBinding binding;
    MarkerOptions markerOptions;
    MerchantDetailResponse.ResultBean merchantData;
    String lat = "", lng = "", services = "";
    List<String> servicesList = new ArrayList<>();
    boolean isSmoking, isDelivery, isLiveTv, isReservation, isParking, isPickup, isCatering;
    File imgLogo;
    boolean getData;
    RestaurantImagesAdapter restaurantImagesAdapter;
    LatLng latLng = new LatLng(1.289545, 103.849972);
    List<String> imagesList = new ArrayList<>();
    ArrayList<File> fileList = new ArrayList<>();
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    //********************Method to convert view to bitmap for marker***********************//
    public static Bitmap createDrawableFromView(Context context) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_marker, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (HomeActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_merchant_home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setMap();
        setImagesAdapter();
        setOnClickListener();
        selectServices();
        adjustUi();
      //  autoPrint();
        return binding.getRoot();
    }

    //==================MEthod to set images adapter==============//
    private void setImagesAdapter() {
        restaurantImagesAdapter = new RestaurantImagesAdapter(context, imagesList, new SelectImageListener() {
            @Override
            public void onImageSelected(int position) {
                openMultiImagePicker(binding.rvImages, position);
            }

            @Override
            public void onCrossClicked(int position) {
                if (imagesList.get(position).contains("http") || imagesList.get(position).contains("https")) {
                    callDeleteImageApi(position);
                } else {

                    fileList.remove(position);
                    imagesList.remove(position);
                    restaurantImagesAdapter.notifyDataSetChanged();
                }
            }
        });
        binding.rvImages.setAdapter(restaurantImagesAdapter);
        binding.rvImages.setLayoutManager(new GridLayoutManager(context, 3));
    }

    //================Method to adjust UI for arabic==============//
    private void adjustUi() {

        Utilities.rotateViews(binding.imgScrollLeft, binding.imgSrcollRight);
        if (Locale.getDefault().equals(new Locale("ar"))) {
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(0, Utilities.dipToPixelsInt(context, 15), 0, Utilities.dipToPixelsInt(context, 15));
            params.setMarginStart(Utilities.dipToPixelsInt(context, 75));
            params.setMarginEnd(Utilities.dipToPixelsInt(context, 15));
            binding.frame.setLayoutParams(params);
        }

        if (SharedPrefManager.getInstance(context).getInt(AppConstant.IS_PARENT, 1) == 0) {
           /* binding.imgSmokingArea.setEnabled(false);
            binding.imgCatering.setEnabled(false);
            binding.imgDelivery.setEnabled(false);
            binding.imgLiveTv.setEnabled(false);
            binding.imgPickup.setEnabled(false);
            binding.imgParking.setEnabled(false);
            binding.imgReservation.setEnabled(false);*/
            binding.etTittle.setEnabled(false);
            binding.imgCamera1.setEnabled(false);
        }
    }

    //===========Method to set onClick Listeners===============//
    private void setOnClickListener() {

        binding.tvLocation.setOnClickListener(V -> {
            getPlaceAutoComplete();
        });

        binding.llContainer.setOnClickListener(V -> {
            Utilities.hideSoftKeyboard(context);
            context.closeDrawer();
        });

        binding.imgCamera1.setOnClickListener(V -> {
            openImagePicker(binding.imgLogo, 0);
        });


        binding.flSave.setOnClickListener(V -> {
            if (isInputValid()) {
                callSaveMerchantApi();
            }
        });

        binding.tvTodaySpecial.setOnClickListener(V -> {
            new TodaySpecialDialog(context).show();
        });

        binding.tvAddDeal.setOnClickListener(V -> {
            context.showAddDeal();
        });

    }

    //================Method to validate user input================//
    private boolean isInputValid() {
        if (!binding.etTittle.getText().toString().trim().equals("")) {
            if (!binding.etTagLine.getText().toString().trim().equals("")) {
                if (!binding.tvLocation.getText().toString().equals("")) {
                    if (servicesList.size() > 0 || true) {
                        if (imgLogo != null || merchantData != null && !merchantData.getLogo().equals("")) {
                            if (fileList.size() > 0) {
                                if (AppValidator.isValidURL(binding.clParentLayout, context, binding.etSite, getString(R.string.invalid_website_add))) {
                                    if (AppValidator.isValidPhone(binding.clParentLayout, context, binding.etPhone, context.getString(R.string.valid_mobile))) {
                                        if (AppValidator.isValidPhone(binding.clParentLayout, context, binding.etWhatsapp, getString(R.string.invalid_whatapp))) {
                                            if (AppValidator.isValidURL(binding.clParentLayout, context, binding.etFB, getString(R.string.invalid_facebook_add))) {
                                                if (AppValidator.isValidURL(binding.clParentLayout, context, binding.etInsta, getString(R.string.invalid_insta_add))) {
                                                    return AppValidator.isValidURL(binding.clParentLayout, context, binding.etTwitter, getString(R.string.invalid_twitter_add));
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                AlertUtil.toastMsg(context, getString(R.string.empty_restaurant_image));
                            }
                        } else {
                            AlertUtil.toastMsg(context, getString(R.string.empty_logo));
                        }
                    } else {
                        AlertUtil.toastMsg(context, getString(R.string.empty_services));
                    }
                } else {
                    AlertUtil.toastMsg(context, getString(R.string.empty_location));
                }
            } else {
                AlertUtil.toastMsg(context, getString(R.string.empty_tagline));
            }

        } else {
            AlertUtil.toastMsg(context, getString(R.string.empty_title));
        }
        return false;
    }

    //===========Mehtod to select services===============//
    private void selectServices() {
        binding.imgSmokingArea.setOnClickListener(V -> {
            if (isSmoking) {
                isSmoking = false;
                binding.imgSmokingArea.setSelected(false);
                servicesList.remove(AppConstant.SMOKING_AREA_SERVICE);
            } else {
                isSmoking = true;
                binding.imgSmokingArea.setSelected(true);
                servicesList.add(AppConstant.SMOKING_AREA_SERVICE);
            }
        });
        binding.imgDelivery.setOnClickListener(V -> {
            if (isDelivery) {
                isDelivery = false;
                binding.imgDelivery.setSelected(false);
                servicesList.remove(AppConstant.DELIVERY_SERVICE);
            } else {
                isDelivery = true;
                binding.imgDelivery.setSelected(true);
                servicesList.add(AppConstant.DELIVERY_SERVICE);
            }
        });

        binding.imgLiveTv.setOnClickListener(V -> {
            if (isLiveTv) {
                isLiveTv = false;
                binding.imgLiveTv.setSelected(false);
                servicesList.remove(AppConstant.LIVE_TV_SERVICE);
            } else {
                isLiveTv = true;
                binding.imgLiveTv.setSelected(true);
                servicesList.add(AppConstant.LIVE_TV_SERVICE);
            }
        });

        binding.imgReservation.setOnClickListener(V -> {
            if (isReservation) {
                isReservation = false;
                binding.imgReservation.setSelected(false);
                servicesList.remove(AppConstant.RESERVATION_SERVICE);
            } else {
                isReservation = true;
                binding.imgReservation.setSelected(true);
                servicesList.add(AppConstant.RESERVATION_SERVICE);
            }
        });

        binding.imgParking.setOnClickListener(V -> {
            if (isParking) {
                isParking = false;
                binding.imgParking.setSelected(false);
                servicesList.remove(AppConstant.PARKING_SERVICE);
            } else {
                isParking = true;
                binding.imgParking.setSelected(true);
                servicesList.add(AppConstant.PARKING_SERVICE);
            }
        });

        binding.imgPickup.setOnClickListener(V -> {
            if (isPickup) {
                isPickup = false;
                binding.imgPickup.setSelected(false);
                servicesList.remove(AppConstant.PICKUP_SERVICE);

            } else {
                isPickup = true;
                binding.imgPickup.setSelected(true);
                servicesList.add(AppConstant.PICKUP_SERVICE);
            }
        });

        binding.imgCatering.setOnClickListener(V -> {
            if (isCatering) {
                isCatering = false;
                binding.imgCatering.setSelected(false);
                servicesList.remove(AppConstant.CATERING_SERVICE);

            } else {
                isCatering = true;
                binding.imgCatering.setSelected(true);
                servicesList.add(AppConstant.CATERING_SERVICE);
            }
        });
    }



    //============Method to pick image from camera or gallery========================//
    private void openImagePicker(View imageView, int positon) {
        //to ask permissions at runtime
        PermissionsUtil.askPermissions(context, PermissionsUtil.CAMERA, PermissionsUtil.STORAGE, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    ImagePickerUtil.selectImage(context, new ImagePickerUtil.ImagePickerListener() {
                        @Override
                        public void onImagePicked(File imageFile, String tag) {
                            if (imageView == binding.imgLogo) {
                                imgLogo = imageFile;
                                binding.imgLogo.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                            } else {

                                if (positon < imagesList.size()) {
                                    imagesList.set(positon, imageFile.getAbsolutePath());
                                    fileList.set(positon, imageFile);
                                } else {
                                    imagesList.add(imageFile.getAbsolutePath());
                                    fileList.add(imageFile);
                                }
                                restaurantImagesAdapter.notifyDataSetChanged();
                            }
                        }
                    }, "img" + new Random().nextInt(), false);
                }

            }
        });

    }

    //============Method to pick image from camera or gallery========================//
    private void openMultiImagePicker(View imageView, int positon) {
        //to ask permissions at runtime
        PermissionsUtil.askPermissions(context, PermissionsUtil.CAMERA, PermissionsUtil.STORAGE, new PermissionsUtil.PermissionListener() {
            @Override
            public void onPermissionResult(boolean isGranted) {
                if (isGranted) {
                    final CharSequence[] items = {context.getString(R.string.camera), context.getString(R.string.gallery)};
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                    builder.setTitle(null);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items[item].equals(context.getString(R.string.camera))) {
                                ImagePickerUtil.pickFromCameraWithCrop(context, new ImagePickerUtil.ImagePickerListener() {
                                    @Override
                                    public void onImagePicked(File imageFile, String tag) {
                                        if (positon < imagesList.size()) {
                                            imagesList.set(positon, imageFile.getAbsolutePath());
                                            fileList.set(positon, imageFile);
                                        } else {
                                            imagesList.add(imageFile.getAbsolutePath());
                                            fileList.add(imageFile);
                                        }
                                        restaurantImagesAdapter.notifyDataSetChanged();
                                    }
                                }, "img" + new Random().nextInt());
                            } else if (items[item].equals(context.getString(R.string.gallery))) {
                                Intent intent = new Intent(context, AlbumSelectActivity.class);
                                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 6 - fileList.size());
                                startActivityForResult(intent, Constants.REQUEST_CODE);


                            }
                        }
                    });
                    builder.show();
                }

            }
        });

    }

    //*****************Method to open google place autocomplete activity*************//
    private void getPlaceAutoComplete() {
        // binding.tvLocation.setEnabled(false);
       /* if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyCBzbAuqFFpPrbfwntfCwFAzHAQyCDaGqE");
        }
        try {

            // Set the fields to specify which types of place data to return.
            List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)//.setCountry("AR") //Arabic
                    .build(context);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

            // this is prev one
*//*
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(context);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);*//*
        }
        // prev one
       *//* catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            AlertUtil.toastMsg(context,context.getString(R.string.error_generic));
            binding.tvLocation.setEnabled(true);
        }*//* catch (Exception e) {
            e.printStackTrace();
            AlertUtil.toastMsg(context,context.getString(R.string.error_generic));
            binding.tvLocation.setEnabled(true);

        }*/

        Intent in=new Intent(context, NewPlaceActivity.class);
        startActivity(in);


    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPrefManager prefManager=SharedPrefManager.getInstance(context);
        if(prefManager.getString("SAVE_LOC_TEMP","")!=null &&
                !prefManager.getString("SAVE_LOC_TEMP","").trim().isEmpty()){
            binding.tvLocation.setEnabled(true);
            if(prefManager.getString("SAVE_LAT_TEMP","")!=null &&
                    !prefManager.getString("SAVE_LAT_TEMP","").trim().isEmpty()){
                lat = prefManager.getString("SAVE_LAT_TEMP","");
                lng = prefManager.getString("SAVE_LON_TEMP","");
                LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                googleMap.clear();
                //googleMap.addMarker(markerOptions.position(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            }
           // prefManager.saveString("SAVE_LAT_TEMP", String.valueOf(task.getPlace().getLatLng().latitude));
          //  prefManager.saveString("SAVE_LON_TEMP", String.valueOf(task.getPlace().getLatLng().longitude));
            binding.tvLocation.setText(prefManager.getString("SAVE_LOC_TEMP",""));
            lat = lat;
            lng = lng;

        }

      //  autoPrint();
    }

    public void autoPrint(){
        // this is for automatic print from notification
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!((Activity) context).isFinishing()) {
                    try {
                        SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
                        List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
                        if (tempPrintList.size() > 0) {
                            for (int i = 0; i < tempPrintList.size(); i++) {
                               /* AutomaticOrderDetailDialog orderDetailDialog = new AutomaticOrderDetailDialog(context, tempPrintList.get(i), i);
                                orderDetailDialog.show();
                                orderDetailDialog.setCancelable(true);
                                orderDetailDialog.setOnDismissListener(D -> {
                                    orderDetailDialog.dismiss();
                                });
                                orderDetailDialog.startPrintingReceipt();*/
                                AutoPrinterFuntionality orderDetailDialog = new AutoPrinterFuntionality(context, tempPrintList.get(i), i);
                                orderDetailDialog.startPrintingReceipt();
                            }
                        }
                    } catch (WindowManager.BadTokenException e) {
                        Log.e("WindowManagerBad ", e.toString());
                    }
                }
            }
        });
    }

    // create bucket through prefence for automatic printing
    public List<OrderListResponse.ResultBean> loadSharedPreferencesLogList(SharedPrefManager pref) {
        List<OrderListResponse.ResultBean> callLog = new ArrayList<OrderListResponse.ResultBean>();
        // SharedPreferences mPrefs = context.getSharedPreferences(Constant.CALL_HISTORY_RC, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(AppConstant.PRINTER_PUSH_NOTIFICATION, "");
        if (json.isEmpty()) {
            callLog = new ArrayList<OrderListResponse.ResultBean>();
        } else {
            Type type = new TypeToken<List<OrderListResponse.ResultBean>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    public void saveSharedPreferencesLogList(SharedPrefManager pref, List<OrderListResponse.ResultBean> printLog) {
        Gson gson = new Gson();
        String json = gson.toJson(printLog);
        pref.saveString(AppConstant.PRINTER_PUSH_NOTIFICATION, json);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPrefManager prefManager=SharedPrefManager.getInstance(context);
        prefManager.saveString("SAVE_LOC_TEMP", "");
        prefManager.saveString("SAVE_LAT_TEMP","");
        prefManager.saveString("SAVE_LON_TEMP", "");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SharedPrefManager prefManager=SharedPrefManager.getInstance(context);
        prefManager.saveString("SAVE_LOC_TEMP", "");
        prefManager.saveString("SAVE_LAT_TEMP","");
        prefManager.saveString("SAVE_LON_TEMP", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPrefManager prefManager=SharedPrefManager.getInstance(context);
        prefManager.saveString("SAVE_LOC_TEMP", "");
        prefManager.saveString("SAVE_LAT_TEMP","");
        prefManager.saveString("SAVE_LON_TEMP", "");
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

                    //to call method after map is ready
                    callGetMerchantDetailApi();


                    googleMap.setOnCameraIdleListener(() -> {

                        lat = "" + googleMap.getCameraPosition().target.latitude;
                        lng = "" + googleMap.getCameraPosition().target.longitude;
                        String address = Utilities.getCurrentAddress(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, context);

                        if (address != null) {
                            binding.tvLocation.setText(address);
                        }
                    });

                    PermissionsUtil.askPermission(context, PermissionsUtil.LOCATION, isGranted -> {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        googleMap.setOnMyLocationButtonClickListener(() -> {

                            new Handler().postDelayed(() -> {
                                lat = "" + googleMap.getCameraPosition().target.latitude;
                                lng = "" + googleMap.getCameraPosition().target.longitude;
                                String address = Utilities.getCurrentAddress(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, context);

                                if (address != null) {
                                    binding.tvLocation.setText(address);
                                }
                            }, 2000);

                            return false;
                        });
                    });


                }
            });
        }

        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(binding.frameMap.getId(), mapFragment).commitAllowingStateLoss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                binding.tvLocation.setEnabled(true);

                com.google.android.libraries.places.api.model.Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("NEW PLACES", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                //Toast.makeText(AutocompleteFromIntentActivity.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
                String address = place.getAddress();

                //  Place place = PlaceAutocomplete.getPlace(context, data);
                binding.tvLocation.setText(place.getAddress());
                lat = String.valueOf(place.getLatLng().latitude);
                lng = String.valueOf(place.getLatLng().longitude);

                LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                googleMap.clear();
                //googleMap.addMarker(markerOptions.position(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                binding.tvLocation.setEnabled(true);

                //AlertUtil.toastMsg(context,context.getString(R.string.error_generic));

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                binding.tvLocation.setEnabled(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utilities.hideSoftKeyboard(context);
                    }
                }, 200);

            }
        } else if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            for (Image myImages : images) {

                imagesList.add(myImages.path);
                fileList.add(new File(myImages.path));
            }
            restaurantImagesAdapter.notifyDataSetChanged();
        }
    }

    //==========================Method to get merchant detail api=====================//
    private void callDeleteImageApi(int position) {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
            params.put("deleteimage", "" + merchantData.getImage().get(position).getId());


            HashMap<String, String> headers = new HashMap<>();
            headers.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
            headers.put("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken", SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN, ""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_IMAGE)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 200) {
                                        imagesList.remove(position);
                                        fileList.remove(position);
                                        restaurantImagesAdapter.notifyDataSetChanged();
                                    } else if (genericResponse.getCode() == 301) {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callDeleteImageApi(position);
                                            }
                                        });
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

    //==========================Method to get merchant detail api=====================//
    private void callGetMerchantDetailApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
            params.put("type", "get");
            apiRequest = new RestClient.ApiRequest(context);


            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SAVE_MERCHANT)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                MerchantDetailResponse merchantDetailResponse = new Gson().fromJson(response, MerchantDetailResponse.class);
                                if (merchantDetailResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (merchantDetailResponse.getCode() == 201) {

                                        if (merchantDetailResponse.getResult() != null) {
                                            getData = false;
                                            merchantData = merchantDetailResponse.getResult();
                                            setData();

                                        } else {
                                            setDefaultLocation();
                                        }
                                    } else if (merchantDetailResponse.getCode() == 301) {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetMerchantDetailApi();
                                            }
                                        });
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
                                    // AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            //AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    //===================Mehtod to set data====================//
    private void setData() {

        if (merchantData.getImage() != null) {
            for (int i = 0; i < merchantData.getImage().size(); i++) {
                imagesList.add(ServerConstants.IMAGE_BASE_URL + merchantData.getImage().get(i).getImage());
                fileList.add(null);
            }
            restaurantImagesAdapter.notifyDataSetChanged();
        }
        binding.etTagLine.setText(merchantData.getTagline());
        binding.etTittle.setText(merchantData.getTitle().trim());
        if (merchantData.getTitle().length() > 0) {
            binding.etTittle.setSelection(merchantData.getTitle().length() - 1);
        }
        binding.etNetwork.setText(merchantData.getNetwork());
        binding.etPassword.setText(merchantData.getPassword());

        if (merchantData.getLocation().equals("")) {
            setDefaultLocation();
        } else {
            binding.tvLocation.setText(merchantData.getLocation());
            lat = "" + merchantData.getLat();
            lng = "" + merchantData.getLng();
            latLng = new LatLng(Double.parseDouble(merchantData.getLat()), Double.parseDouble(merchantData.getLng()));
            googleMap.clear();
            //googleMap.addMarker(markerOptions.position(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

        }
        if (merchantData.getServices().contains(AppConstant.SMOKING_AREA_SERVICE)) {
            binding.imgSmokingArea.setSelected(true);
            isSmoking = true;
            servicesList.add(AppConstant.SMOKING_AREA_SERVICE);
        }
        if (merchantData.getServices().contains(AppConstant.DELIVERY_SERVICE)) {
            binding.imgDelivery.setSelected(true);
            isDelivery = true;
            servicesList.add(AppConstant.DELIVERY_SERVICE);
        }
        if (merchantData.getServices().contains(AppConstant.LIVE_TV_SERVICE)) {
            binding.imgLiveTv.setSelected(true);
            isLiveTv = true;
            servicesList.add(AppConstant.LIVE_TV_SERVICE);
        }
        if (merchantData.getServices().contains(AppConstant.RESERVATION_SERVICE)) {
            binding.imgReservation.setSelected(true);
            isReservation = true;
            servicesList.add(AppConstant.RESERVATION_SERVICE);
        }
        if (merchantData.getServices().contains(AppConstant.PARKING_SERVICE)) {
            binding.imgParking.setSelected(true);
            isParking = true;
            servicesList.add(AppConstant.PARKING_SERVICE);
        }
        if (merchantData.getServices().contains(AppConstant.PICKUP_SERVICE)) {
            binding.imgPickup.setSelected(true);
            isPickup = true;
            servicesList.add(AppConstant.PICKUP_SERVICE);
        }
        if (merchantData.getServices().contains(AppConstant.CATERING_SERVICE)) {
            binding.imgCatering.setSelected(true);
            isCatering = true;
            servicesList.add(AppConstant.CATERING_SERVICE);
        }

        if (!merchantData.getLogo().equals(""))
            Utilities.setImagePicasso(context, binding.imgLogo, ServerConstants.IMAGE_BASE_URL + merchantData.getLogo());


        binding.etSite.setText(merchantData.getSite());
        binding.etInsta.setText(merchantData.getInstagram());
        binding.etFB.setText(merchantData.getFacebook());
        binding.etPhone.setText(merchantData.getPhone());
        binding.etWhatsapp.setText(merchantData.getWhatsapp());
        binding.etTwitter.setText(merchantData.getTwitter());


    }

    //============Method to set default location on map================//
    private void setDefaultLocation() {
        LatLng riyadh = new LatLng(24.774265, 46.738586);
        googleMap.clear();
        //googleMap.addMarker(markerOptions.position(riyadh));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(riyadh, 17.0f));
        String locationName = Utilities.getCurrentAddress(riyadh.latitude, riyadh.longitude, context);
        binding.tvLocation.setText(locationName);
    }

    //==========================Method to save merchant detail api=====================//
    private void callSaveMerchantApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("title", binding.etTittle.getText().toString().trim());
            params.put("tagline", binding.etTagLine.getText().toString().trim());
            params.put("network", binding.etNetwork.getText().toString().trim());
            params.put("password", binding.etPassword.getText().toString().trim());
            params.put("location", binding.tvLocation.getText().toString().trim());
            params.put("lat", lat);
            params.put("lng", lng);
            params.put("site", binding.etSite.getText().toString().trim());
            params.put("phone", binding.etPhone.getText().toString().trim());
            params.put("whatsapp", binding.etWhatsapp.getText().toString().trim());
            params.put("facebook", binding.etFB.getText().toString().trim());
            params.put("twitter", binding.etTwitter.getText().toString().trim());
            params.put("instagram", binding.etInsta.getText().toString().trim());

            services = "";
            for (int i = 0; i < servicesList.size(); i++) {
                if (!services.equals("")) {
                    services = services + "," + servicesList.get(i);
                } else {
                    services = servicesList.get(i);
                }
            }
            params.put("services", services);


            HashMap<String, File> fileHashMap = new HashMap<>();
            if (imgLogo != null)
                fileHashMap.put("logo", imgLogo);

            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < fileList.size(); i++) {
                if (fileList.get(i) != null) {
                    files.add(fileList.get(i));
                }
            }

            HashMap<String, String> headers = new HashMap<>();
            headers.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
            headers.put("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken", SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN, ""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SAVE_MERCHANT)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setFileParams(fileHashMap)
                    .setFileParams("image", files)
                    .setHeaders(headers)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                MerchantDetailResponse merchantDetailResponse = new Gson().fromJson(response, MerchantDetailResponse.class);
                                if (merchantDetailResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (merchantDetailResponse.getCode() == 201) {
                                        if (merchantDetailResponse.getResult() != null) {
                                            servicesList.clear();
                                            imagesList.clear();
                                            fileList.clear();
                                            merchantData = merchantDetailResponse.getResult();
                                            setData();

                                        } else {
                                            setDefaultLocation();
                                        }
                                        AlertUtil.toastMsg(context, merchantDetailResponse.getMessage());

                                    } else if (merchantDetailResponse.getCode() == 301) {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callSaveMerchantApi();
                                            }
                                        });
                                    } else {

                                        AlertUtil.toastMsg(context, merchantDetailResponse.getMessage());
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

    private void clearFields() {
        binding.imgSmokingArea.setSelected(false);
        binding.imgCatering.setSelected(false);
        binding.imgDelivery.setSelected(false);
        binding.imgLiveTv.setSelected(false);
        binding.imgPickup.setSelected(false);
        binding.imgParking.setSelected(false);
        binding.imgReservation.setSelected(false);
        servicesList.clear();

        binding.etTwitter.setText("");
        binding.etWhatsapp.setText("");
        binding.tvLocation.setText("");
        binding.etFB.setText("");
        binding.etPhone.setText("");
        binding.etPassword.setText("");
        binding.etTittle.setText("");
        binding.etTagLine.setText("");
        binding.etSite.setText("");
        binding.etNetwork.setText("");
        binding.tvLocation.setText("");

        binding.imgLogo.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_logo));
        //binding.imgItem.setImageDrawable(getResources().getDrawable(R.drawable.placeholder1));
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
