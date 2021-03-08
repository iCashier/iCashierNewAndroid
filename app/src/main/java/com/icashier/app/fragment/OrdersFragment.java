package com.icashier.app.fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.CartAdapter;
import com.icashier.app.adapter.MenuCategoryAdapter;
import com.icashier.app.adapter.MenuItemsAdapter;
import com.icashier.app.adapter.OrderListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentOrdersBinding;
import com.icashier.app.dialog.CalculatorDialog;
import com.icashier.app.dialog.ChooseExtrasCartDialog;
import com.icashier.app.dialog.ChooseExtrasDialog;
import com.icashier.app.dialog.InStockDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CalculatorListener;
import com.icashier.app.listener.CartItemListener;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.listener.EditCategoryDialogListener;
import com.icashier.app.listener.MenuListener;
import com.icashier.app.listener.OkClickListener;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.MenuResponse;
import com.icashier.app.model.OrderListResponse;
import com.icashier.app.model.SigninResponse;
import com.icashier.app.model.SoldOutResponse;
import com.icashier.app.printer.AutoPrinterFuntionality;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class OrdersFragment extends Fragment {

    public List<CartItemModel> cartItems = new ArrayList<>();
    HomeActivity context;
    FragmentOrdersBinding binding;
    RestClient.ApiRequest apiRequest;
    SigninResponse.ResultBean signinResponse;
    List<MenuResponse.MerchantBean.CategoriesBean> categoryList = new ArrayList<>();
    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> itemList = new ArrayList<>();
    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> mealList = new ArrayList<>();
    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> dealList = new ArrayList<>();

    OrderListAdapter orderListAdapter;
    MenuCategoryAdapter categoryAdapter;
    MenuItemsAdapter itemsAdapter;
    CartAdapter cartAdapter;
    OrderListAdapter filteredAdapter;
    int selectedItem = -1, selectedCategory = 0;
    LinearLayoutManager mLayoutManager;
    int pageNo = 1, sizeIndex = 0, countIndex = 1;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    List<CartItemModel.ExtrasBean> cartExtrasBeanList = new ArrayList<>();
    List<OrderListResponse.ResultBean> orderList = new ArrayList<>();
    List<OrderListResponse.ResultBean> filteredList = new ArrayList<>();
    float totalCartPrice, customAmount;
    boolean isDinein, isDelivery, isPickup, isInstaDelivery, isSwiftDelivery, isCashierOrder;
    private boolean loading = true;
    private String selectedPrice;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            if (intent.hasExtra(AppConstant.PUSH_TYPE)) {
                // start automatic printer
                SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(context);
                if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
                    SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
                    List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
                    if (tempPrintList.size() > 0) {
                        autoPrint();
                    }
                }
                if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_NEW_ORDER)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_WAITER_CALLING)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_REORDER)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_WAITER_ACK)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_CASHIER_ORDER)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_PAYMENT_RECEIVED)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_ORDER_STATUS_CHANGED)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_PAYMENT_REQ)) {
                    callGetOrdersApi();
                } else if (intent.getStringExtra(AppConstant.PUSH_TYPE).equals(AppConstant.NOTI_ORDER_PAYMENT_RECEIVED)) {
                    callGetOrdersApi();
                }
            }


        }
    };

    public static void saveSharedPreferencesLogList(SharedPrefManager pref, List<OrderListResponse.ResultBean> printLog) {
        Gson gson = new Gson();
        String json = gson.toJson(printLog);
        pref.saveString(AppConstant.PRINTER_PUSH_NOTIFICATION, json);
    }

    public static List<OrderListResponse.ResultBean> loadSharedPreferencesLogList(SharedPrefManager pref) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false);
        signinResponse = new Gson().fromJson(SharedPrefManager.getInstance(context).getString(AppConstant.USER_INFO, ""), SigninResponse.ResultBean.class);
        getCartItems();
        setMenuAdapter();
        callGetMenuApi();
        setOnClickListeners();
        setPaging();

        handleItemSelection();
        return binding.getRoot();
    }

    private void getCartItems() {
        String cartHistory = SharedPrefManager.getInstance(context).getString(AppConstant.CART_HISTORY, "");
        if (!cartHistory.equals("") && !cartHistory.equals("[]")) {
            cartItems = new Gson().fromJson(SharedPrefManager.getInstance(context).getString(AppConstant.CART_HISTORY, ""),
                    new TypeToken<ArrayList<CartItemModel>>() {
                    }.getType());
            binding.tvEmptyCart.setVisibility(View.GONE);
            binding.llCheckout.setVisibility(View.VISIBLE);
            calculateCartPrice();
            binding.tvClear.setVisibility(View.VISIBLE);
            if (SharedPrefManager.getInstance(context).getFloat(AppConstant.CUSTOM_AMOUNT, 0) != 0) {
                setCustomAmount(SharedPrefManager.getInstance(context).getFloat(AppConstant.CUSTOM_AMOUNT, 0));
            }
        }
    }

    public void setCustomAmount(float amount) {

        if (amount > 0) {
            binding.llCustomAmount.setVisibility(View.VISIBLE);
            SharedPrefManager.getInstance(context).saveFloat(AppConstant.CUSTOM_AMOUNT, amount);

        } else {
            binding.llCustomAmount.setVisibility(View.GONE);
        }

        customAmount = amount;
        binding.tvSubTotal.setText("SR " + Utilities.formatPrice((float) (Math.round((totalCartPrice) * 100.0) / 100.0)));
        binding.tvCustomAmount.setText("SR " + Utilities.formatPrice((float) (Math.round((amount) * 100.0) / 100.0)));
        binding.tvTotalPriceCart.setText("SR " + Utilities.formatPrice((float) (Math.round((totalCartPrice + amount) * 100.0) / 100.0)));
    }

    //=================Method to select delivery mode=============//
    private void setDeliverySelector(ImageView img) {

        if (img == binding.imgDineIn) {
            if (!isDinein) {
                binding.imgDineIn.setSelected(true);
                binding.imgPickup.setSelected(false);
                binding.imgDelivery.setSelected(false);
                binding.imgSwiftDelivery.setSelected(false);
                binding.imgInstaDelivery.setSelected(false);
                binding.imgCashier.setSelected(false);
                isCashierOrder = false;
                isDinein = true;
                isPickup = false;
                isInstaDelivery = false;
                isSwiftDelivery = false;
                isDelivery = false;
                filterList();
            } else {
                binding.imgDineIn.setSelected(false);
                isDinein = false;
                filterList();
            }
        } else if (img == binding.imgDelivery) {
            if (!isDelivery) {
                binding.imgDineIn.setSelected(false);
                binding.imgPickup.setSelected(false);
                binding.imgDelivery.setSelected(true);
                binding.imgSwiftDelivery.setSelected(false);
                binding.imgInstaDelivery.setSelected(false);
                binding.imgCashier.setSelected(false);
                isCashierOrder = false;
                isDinein = false;
                isPickup = false;
                isInstaDelivery = false;
                isSwiftDelivery = false;
                isDelivery = true;
                filterList();
            } else {
                binding.imgDelivery.setSelected(false);
                isDelivery = false;
                filterList();
            }

        } else if (img == binding.imgPickup) {
            if (!isPickup) {
                binding.imgDineIn.setSelected(false);
                binding.imgPickup.setSelected(true);
                binding.imgDelivery.setSelected(false);
                binding.imgSwiftDelivery.setSelected(false);
                binding.imgSwiftDelivery.setSelected(false);
                binding.imgInstaDelivery.setSelected(false);
                binding.imgCashier.setSelected(false);
                isCashierOrder = false;
                isDinein = false;
                isPickup = true;
                isInstaDelivery = false;
                isSwiftDelivery = false;
                isDelivery = false;
                filterList();
            } else {
                binding.imgPickup.setSelected(false);
                isPickup = false;
                filterList();
            }

        } else if (img == binding.imgSwiftDelivery) {
            if (!isSwiftDelivery) {
                binding.imgDineIn.setSelected(false);
                binding.imgPickup.setSelected(false);
                binding.imgDelivery.setSelected(false);
                binding.imgSwiftDelivery.setSelected(true);
                binding.imgInstaDelivery.setSelected(false);
                binding.imgCashier.setSelected(false);
                isCashierOrder = false;
                isDinein = false;
                isPickup = false;
                isInstaDelivery = false;
                isSwiftDelivery = true;
                isDelivery = false;
                filterList();
            } else {
                binding.imgSwiftDelivery.setSelected(false);
                isSwiftDelivery = false;
                filterList();
            }
        } else if (img == binding.imgInstaDelivery) {
            if (!isInstaDelivery) {
                binding.imgDineIn.setSelected(false);
                binding.imgPickup.setSelected(false);
                binding.imgDelivery.setSelected(false);
                binding.imgSwiftDelivery.setSelected(false);
                binding.imgInstaDelivery.setSelected(true);
                binding.imgCashier.setSelected(false);
                isCashierOrder = false;
                isDinein = false;
                isPickup = false;
                isInstaDelivery = true;
                isSwiftDelivery = false;
                isDelivery = false;
                filterList();
            } else {
                binding.imgInstaDelivery.setSelected(false);
                isInstaDelivery = false;
                filterList();
            }
        } else if (img == binding.imgCashier) {
            if (!isCashierOrder) {
                binding.imgDineIn.setSelected(false);
                binding.imgPickup.setSelected(false);
                binding.imgDelivery.setSelected(false);
                binding.imgSwiftDelivery.setSelected(false);
                binding.imgInstaDelivery.setSelected(false);
                binding.imgCashier.setSelected(true);
                isDinein = false;
                isPickup = false;
                isInstaDelivery = false;
                isSwiftDelivery = false;
                isDelivery = false;
                isCashierOrder = true;
                filterList();
            } else {
                binding.imgCashier.setSelected(false);
                isCashierOrder = false;
                filterList();
            }
        }
    }

    //===========Mehtod to select item size===========//
    private void setSizeList() {

        if (!itemList.get(selectedItem).getType().equals("meal") && !itemList.get(selectedItem).getType().equals("deal")) {

            if (sizeIndex < itemList.get(selectedItem).getSize_price().size() && sizeIndex >= 0) {

                if (itemList.get(selectedItem).getSize_price().get(sizeIndex).getSize().equalsIgnoreCase(AppConstant.DEFAULT_SIZE) && Utilities.isArabic()) {
                    binding.tvSize.setText(getString(R.string.default_size));
                } else {
                    binding.tvSize.setText(itemList.get(selectedItem).getSize_price().get(sizeIndex).getSize());
                }
                selectedPrice = itemList.get(selectedItem).getSize_price().get(sizeIndex).getPrice();
                itemList.get(selectedItem).setSelectedPrice(selectedPrice);
                itemsAdapter.notifyDataSetChanged();
            }

            if (sizeIndex == 0) {
                binding.flSizeDown.setSelected(true);
            } else {
                binding.flSizeDown.setSelected(false);
            }

            if (sizeIndex == itemList.get(selectedItem).getSize_price().size() - 1) {
                binding.flSizeUp.setSelected(true);
            } else {
                binding.flSizeUp.setSelected(false);
            }
            if (itemList.size() == 1) {
                binding.flSizeUp.setSelected(true);
                binding.flSizeDown.setSelected(true);
            }
        }
    }

    //===========Mehtod to select item size===========//
    private void setSizeListForCart() {

        if (!cartItems.get(selectedItem).getType().equals("meal") && !cartItems.get(selectedItem).getType().equals("deal")) {

            if (sizeIndex < cartItems.get(selectedItem).getSize_price().size() && sizeIndex >= 0) {

                if (cartItems.get(selectedItem).getSize_price().get(sizeIndex).getSize().equalsIgnoreCase(AppConstant.DEFAULT_SIZE) && Utilities.isArabic()) {
                    binding.tvSize.setText(getString(R.string.default_size));
                } else {
                    binding.tvSize.setText(cartItems.get(selectedItem).getSize_price().get(sizeIndex).getSize());
                }
                selectedPrice = cartItems.get(selectedItem).getSize_price().get(sizeIndex).getPrice();
            }

            if (sizeIndex == 0) {
                binding.flSizeDown.setSelected(true);
            } else {
                binding.flSizeDown.setSelected(false);
            }

            if (sizeIndex == cartItems.get(selectedItem).getSize_price().size() - 1) {
                binding.flSizeUp.setSelected(true);
            } else {
                binding.flSizeUp.setSelected(false);
            }
            if (itemList.size() == 1) {
                binding.flSizeUp.setSelected(true);
                binding.flSizeDown.setSelected(true);
            }
        }
    }

    //==============Method to add item to cart===============//
    private void addToCart() {
        CartItemModel cartItemModel = new CartItemModel();
        cartItemModel.setId(itemList.get(selectedItem).getId());
        cartItemModel.setSpecialitem(itemList.get(selectedItem).getSpecialitem());
        cartItemModel.setMid(itemList.get(selectedItem).getMid());
        //cartItemModel.setCategory(itemList.get(selectedItem).getCategory());
        //cartItemModel.setSub_category(itemList.get(selectedItem).getSub_category());
        cartItemModel.setName(itemList.get(selectedItem).getName());
        cartItemModel.setTitleAr(itemList.get(selectedItem).getTitleAr());
        cartItemModel.setQty(itemList.get(selectedItem).getQty());
        cartItemModel.setSale_percent(itemList.get(selectedItem).getSale_percent());
        cartItemModel.setSale_price(itemList.get(selectedItem).getSale_price());
        cartItemModel.setAbout(itemList.get(selectedItem).getAbout());
        cartItemModel.setImage(itemList.get(selectedItem).getImage());
        cartItemModel.setNumbers(itemList.get(selectedItem).getNumbers());
        cartItemModel.setLike(itemList.get(selectedItem).getLike());
        cartItemModel.setType(itemList.get(selectedItem).getType());
        cartItemModel.setSub_category_name(itemList.get(selectedItem).getSub_category_name());
        cartItemModel.setSale_count(itemList.get(selectedItem).getSale_count());


        if (itemList.get(selectedItem).getType().equals("meal")) {
            cartItemModel.setItems(itemList.get(selectedItem).getItems());
            cartItemModel.setTitle(itemList.get(selectedItem).getTitle());
            cartItemModel.setTitleAr(itemList.get(selectedItem).getTitleAr());
        } else if (itemList.get(selectedItem).getType().equals("deal")) {
            cartItemModel.setItemsDetails(itemList.get(selectedItem).getItemsDetails());
            cartItemModel.setWithItemsDetails(itemList.get(selectedItem).getWithItemsDetails());
            cartItemModel.setTitle(itemList.get(selectedItem).getTitle());
            cartItemModel.setTitleAr(itemList.get(selectedItem).getTitleAr());
        }
        List<CartItemModel.ExtrasBean> extrasList = new ArrayList<>();
        if (!itemList.get(selectedItem).getType().equals("meal") && !itemList.get(selectedItem).getType().equals("deal")) {
            for (int i = 0; i < itemList.get(selectedItem).getExtras().size(); i++) {
                CartItemModel.ExtrasBean extra = new CartItemModel.ExtrasBean();
                extra.setId(itemList.get(selectedItem).getExtras().get(i).getId());
                extra.setChecked(itemList.get(selectedItem).getExtras().get(i).isChecked());
                extra.setPrice(itemList.get(selectedItem).getExtras().get(i).getPrice());
                extra.setTitle(itemList.get(selectedItem).getExtras().get(i).getTitle());
                extra.setType(itemList.get(selectedItem).getExtras().get(i).getType());
                extrasList.add(extra);


            }

            cartItemModel.setExtras(extrasList);
            List<CartItemModel.SizePriceBean> sizePriceList = new ArrayList<>();
            for (int i = 0; i < itemList.get(selectedItem).getSize_price().size(); i++) {
                CartItemModel.SizePriceBean sizePriceBean = new CartItemModel.SizePriceBean();
                sizePriceBean.setPrice(itemList.get(selectedItem).getSize_price().get(i).getPrice());
                sizePriceBean.setSize(itemList.get(selectedItem).getSize_price().get(i).getSize());
                sizePriceList.add(sizePriceBean);
            }
            cartItemModel.setSize_price(sizePriceList);
        }


        cartItemModel.setSold(itemList.get(selectedItem).getSold());

        if (binding.tvSize.getText().toString().equals(getString(R.string.default_size))) {
            cartItemModel.setSizeAddedToCart(AppConstant.DEFAULT_SIZE);
        } else {
            cartItemModel.setSizeAddedToCart(binding.tvSize.getText().toString());
        }
        cartItemModel.setSelectedPrice(selectedPrice);
        cartItemModel.setQtyAddedToCart(binding.tvCount.getText().toString());
        cartItemModel.setAddedToCart(true);
        float totalPrice = Float.parseFloat(cartItemModel.getSelectedPrice());

        if (!itemList.get(selectedItem).getType().equals("meal") && !itemList.get(selectedItem).getType().equals("deal")) {
            for (int i = 0; i < cartItemModel.getExtras().size(); i++) {
                if (cartItemModel.getExtras().get(i).isChecked()) {
                    totalPrice = totalPrice + Float.parseFloat(cartItemModel.getExtras().get(i).getPrice());
                }
            }
        }

        cartItemModel.setTotalPrice("" + totalPrice);


        boolean isAlreadyAdded = false;
        if (cartItems.size() > 0) {
            for (int i = 0; i < cartItems.size(); i++) {

                if (!itemList.get(selectedItem).equals("meal") && !itemList.get(selectedItem).equals("deal")) {
                    if (cartItems.get(i).getId() == cartItemModel.getId() &&
                            cartItems.get(i).getSelectedPrice().equals(cartItemModel.getSelectedPrice())
                            && cartItems.get(i).getSizeAddedToCart().equals(cartItemModel.getSizeAddedToCart())) {
                        if (new Gson().toJson(cartItems.get(i).getExtras()).equals(new Gson().toJson(cartItemModel.getExtras()))) {
                            int qty = Integer.parseInt(cartItems.get(i).getQtyAddedToCart()) +
                                    Integer.parseInt(binding.tvCount.getText().toString());
                            if (qty > Integer.parseInt(itemList.get(selectedItem).getQty())) {
                                AlertUtil.toastMsgLong(context, getString(R.string.qty_exceeds));
                            } else {
                                cartItems.get(i).setQtyAddedToCart(qty + "");
                            }
                            isAlreadyAdded = true;
                            break;
                        } else {
                            int qty = Integer.parseInt(cartItems.get(i).getQtyAddedToCart()) +
                                    Integer.parseInt(binding.tvCount.getText().toString());
                            if (qty > Integer.parseInt(itemList.get(selectedItem).getQty())) {
                                AlertUtil.toastMsgLong(context, getString(R.string.qty_exceeds));
                                isAlreadyAdded = true;
                            }
                        }
                    }
                } else {
                    if (cartItems.get(i).getId() == cartItemModel.getId()) {
                        int qty = Integer.parseInt(cartItems.get(i).getQtyAddedToCart()) +
                                Integer.parseInt(binding.tvCount.getText().toString());
                        if (qty > Integer.parseInt(itemList.get(selectedItem).getQty())) {
                            AlertUtil.toastMsgLong(context, getString(R.string.qty_exceeds));
                        } else {
                            cartItems.get(i).setQtyAddedToCart(qty + "");
                        }
                        isAlreadyAdded = true;
                        break;
                    }

                }

            }
            if (!isAlreadyAdded) {

                for (int i = 0; i < cartItems.size(); i++) {

                    if (!itemList.get(selectedItem).equals("meal") && !itemList.get(selectedItem).equals("deal")) {
                        if (cartItems.get(i).getId() == cartItemModel.getId()) {
                            int qty = Integer.parseInt(cartItems.get(i).getQtyAddedToCart()) +
                                    Integer.parseInt(binding.tvCount.getText().toString());
                            if (qty > Integer.parseInt(itemList.get(selectedItem).getQty())) {
                                AlertUtil.toastMsgLong(context, getString(R.string.qty_exceeds));
                            } else {
                                cartItems.add(0, cartItemModel);

                            }
                            isAlreadyAdded = true;
                            break;
                        }
                    }
                }
                if (!isAlreadyAdded) {
                    cartItems.add(0, cartItemModel);


                }
            }
        } else {
            cartItems.add(0, cartItemModel);

        }

        if (cartItems.size() > 0) {
            binding.tvClear.setVisibility(View.VISIBLE);
        } else {
            binding.tvClear.setVisibility(View.GONE);
        }
        cartAdapter.notifyDataSetChanged();
        SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));


        handleItemSelection();
        itemList.get(selectedItem).setSelected(false);

        resetItemList();
        calculateCartPrice();
        binding.tvEmptyCart.setVisibility(View.GONE);


    }

    //==============Method to update item in cart===============//
    private void updateItem() {

        boolean update = true;
        int qty = 0;
        for (int i = 0; i < cartItems.size(); i++) {

            if (!cartItems.get(selectedItem).equals("meal")) {
                if (cartItems.get(i).getId() == cartItems.get(selectedItem).getId()) {
                    if (i != selectedItem) {
                        qty = Integer.parseInt(cartItems.get(i).getQtyAddedToCart());
                    }

                }
            }
        }

        qty = qty + Integer.parseInt(binding.tvCount.getText().toString());

        if (qty > Integer.parseInt(cartItems.get(selectedItem).getQty())) {
            AlertUtil.toastMsgLong(context, getString(R.string.qty_exceeds));
            update = false;
        } else {
            update = true;

        }

        if (update) {
            cartItems.get(selectedItem).setQtyAddedToCart(binding.tvCount.getText().toString());
            cartItems.get(selectedItem).setSelectedPrice(selectedPrice);
            if (binding.tvSize.getText().toString().equals(getString(R.string.default_size))) {
                cartItems.get(selectedItem).setSizeAddedToCart(AppConstant.DEFAULT_SIZE);
            } else {
                cartItems.get(selectedItem).setSizeAddedToCart(binding.tvSize.getText().toString());
            }

            List<CartItemModel.ExtrasBean> extrasBeanList = new ArrayList<>();
            for (int i = 0; i < cartExtrasBeanList.size(); i++) {
                CartItemModel.ExtrasBean extra = new CartItemModel.ExtrasBean();
                extra.setId(cartExtrasBeanList.get(i).getId());
                extra.setChecked(cartExtrasBeanList.get(i).isChecked());
                extra.setPrice(cartExtrasBeanList.get(i).getPrice());
                extra.setTitle(cartExtrasBeanList.get(i).getTitle());
                extra.setType(cartExtrasBeanList.get(i).getType());
                extrasBeanList.add(extra);

            }
            cartItems.get(selectedItem).setExtras(extrasBeanList);

            float totalPrice = Float.parseFloat(cartItems.get(selectedItem).getSelectedPrice());
            for (int i = 0; i < cartItems.get(selectedItem).getExtras().size(); i++) {
                if (cartItems.get(selectedItem).getExtras().get(i).isChecked()) {
                    totalPrice = totalPrice + Float.parseFloat(cartItems.get(selectedItem).getExtras().get(i).getPrice());
                }
            }
            cartItems.get(selectedItem).setTotalPrice("" + totalPrice);
            for (int i = 0; i < cartItems.size(); i++) {
                cartItems.get(i).setSelected(true);
            }
            cartAdapter.notifyDataSetChanged();
            SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));

            handleItemSelection();


            calculateCartPrice();
        }


    }

    //===========Mehtod to select item count===========//
    private void setCountList() {

        if (Integer.parseInt(itemList.get(selectedItem).getQty()) < 0) {
            itemList.get(selectedItem).setQty("99");
            itemList.get(selectedItem).setUnlimited(true);
        }

        if (countIndex <= Integer.parseInt(itemList.get(selectedItem).getQty()) && countIndex >= 1) {
            binding.tvCount.setText("" + countIndex);
            itemList.get(selectedItem).setCount(countIndex);
            itemsAdapter.notifyDataSetChanged();
        }

        if (countIndex == 1) {
            binding.flCountDown.setSelected(true);
        } else {
            binding.flCountDown.setSelected(false);
        }

        if (countIndex == Integer.parseInt(itemList.get(selectedItem).getQty())) {
            binding.flCountUp.setSelected(true);
        } else {
            binding.flCountUp.setSelected(false);
        }
        if (Integer.parseInt(itemList.get(selectedItem).getQty()) == 1) {
            binding.flSizeUp.setSelected(true);
            binding.flSizeDown.setSelected(true);
        }

    }

    //===========Mehtod to select item count===========//
    private void setCountListForCart() {

        if (Integer.parseInt(cartItems.get(selectedItem).getQty()) < 0) {
            cartItems.get(selectedItem).setQty("99");
        }
        if (countIndex <= Integer.parseInt(cartItems.get(selectedItem).getQty()) && countIndex >= 1) {
            binding.tvCount.setText("" + countIndex);
        }

        if (countIndex == 1) {
            binding.flCountDown.setSelected(true);
        } else {
            binding.flCountDown.setSelected(false);
        }

        if (countIndex == Integer.parseInt(cartItems.get(selectedItem).getQty())) {
            binding.flCountUp.setSelected(true);
        } else {
            binding.flCountUp.setSelected(false);
        }
        if (Integer.parseInt(cartItems.get(selectedItem).getQty()) == 1) {
            binding.flSizeUp.setSelected(true);
            binding.flSizeDown.setSelected(true);
        }

    }

    //=================Method to set onClick listeners=============//
    private void setOnClickListeners() {

        binding.llCustomAmount.setOnClickListener(V -> {
            binding.llBottom.setVisibility(View.GONE);
            binding.llBottom2.setVisibility(View.VISIBLE);
        });

        binding.btnRemoveCustom.setOnClickListener(V -> {
            customAmount = 0;
            setCustomAmount(0);
            binding.llBottom.setVisibility(View.VISIBLE);
            binding.llBottom2.setVisibility(View.GONE);
        });

        binding.btnEditCustom.setOnClickListener(V -> {
            new CalculatorDialog(context, new CalculatorListener() {
                @Override
                public void onSaveClick(float val) {
                    setCustomAmount(val);
                }
            }).show();
        });

        binding.tvClear.setOnClickListener(V -> {
            AlertUtil.showAlertWindow(context, getString(R.string.clear_cart_alert), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cartItems.clear();
                    customAmount = 0;
                    cartAdapter.notifyDataSetChanged();
                    SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));

                    binding.llCheckout.setVisibility(View.GONE);
                    binding.llBottom2.setVisibility(View.GONE);
                    binding.llBottom.setVisibility(View.VISIBLE);
                    binding.llCustomAmount.setVisibility(View.GONE);
                    binding.tvClear.setVisibility(View.GONE);
                    binding.tvEmptyCart.setVisibility(View.VISIBLE);
                }
            });

        });
        binding.tvExtras.setOnClickListener(V -> {


            if (binding.flAdd.getVisibility() == View.VISIBLE) {
                if (itemList.get(selectedItem).getSold() == 0) {
                    new ChooseExtrasDialog(context, itemList.get(selectedItem).getExtras(), new DialogDismissListener() {
                        @Override
                        public void onDismiss() {
                            itemsAdapter.notifyDataSetChanged();
                        }
                    }).show();
                }
            } else {

                new ChooseExtrasCartDialog(context, cartExtrasBeanList).show();
            }

        });

        binding.imgCatBack.setOnClickListener(V -> {
            binding.rvCategories.smoothScrollBy(-100, 100);

        });

        binding.imgCatForward.setOnClickListener(V -> {

            binding.rvCategories.smoothScrollBy(100, 100);
        });

        binding.flSizeDown.setOnClickListener(V -> {
            if (sizeIndex > 0) {
                if (binding.flAdd.getVisibility() == View.VISIBLE) {
                    if (itemList.get(selectedItem).getSold() == 0) {
                        sizeIndex--;
                        setSizeList();
                    }

                } else {
                    sizeIndex--;
                    setSizeListForCart();
                }
            }
        });

        binding.flSizeUp.setOnClickListener(V -> {


            if (binding.flAdd.getVisibility() == View.VISIBLE) {
                if (itemList.get(selectedItem).getSold() == 0) {
                    if (sizeIndex < itemList.get(selectedItem).getSize_price().size() - 1) {
                        sizeIndex++;
                        setSizeList();
                    }
                }
            } else {
                if (sizeIndex < cartItems.get(selectedItem).getSize_price().size() - 1) {
                    sizeIndex++;
                    setSizeListForCart();
                }
            }

        });


        binding.tvAddItem.setOnClickListener(V -> {
            if (itemList.get(selectedItem).getSold() == 0) {
                itemList.get(selectedItem).setAddedToCart(true);
                addToCart();
                cartAdapter.notifyDataSetChanged();
                SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));

            }
        });

        binding.flCountDown.setOnClickListener(V -> {
            if (countIndex > 1) {
                if (binding.flAdd.getVisibility() == View.VISIBLE) {
                    if (itemList.get(selectedItem).getSold() == 0) {
                        countIndex--;
                        setCountList();
                    }

                } else {
                    countIndex--;
                    setCountListForCart();
                }
            }
        });

        binding.flCountUp.setOnClickListener(V -> {


            if (binding.flAdd.getVisibility() == View.VISIBLE) {
                if (itemList.get(selectedItem).getSold() == 0) {
                    if (countIndex < Integer.parseInt(itemList.get(selectedItem).getQty())) {
                        countIndex++;
                        setCountList();

                    }
                }
            } else if (binding.llUpdate.getVisibility() == View.VISIBLE) {
                if (countIndex < Integer.parseInt(cartItems.get(selectedItem).getQty())) {
                    countIndex++;
                    setCountListForCart();

                }
            }

        });


        binding.btnUpdate.setOnClickListener(V -> {
            updateItem();
        });

        binding.flCheckout.setOnClickListener(V -> {
            context.closeDrawer();
            checkoutOrder();
        });

        binding.llPickup.setOnClickListener(V -> {
            setDeliverySelector(binding.imgPickup);
        });

        binding.llDelivery.setOnClickListener(V -> {
            setDeliverySelector(binding.imgDelivery);
        });

        binding.llInstaDelivery.setOnClickListener(V -> {
            setDeliverySelector(binding.imgInstaDelivery);
        });

        binding.llSwiftDelivery.setOnClickListener(V -> {
            setDeliverySelector(binding.imgSwiftDelivery);
        });

        binding.llDinein.setOnClickListener(V -> {
            setDeliverySelector(binding.imgDineIn);
        });

        binding.llCashier.setOnClickListener(V -> {
            setDeliverySelector(binding.imgCashier);
        });
        binding.getRoot().setOnClickListener(V -> {
            context.closeDrawer();
        });

        binding.btnSoldOut.setOnClickListener(v -> {
            if (itemList.get(selectedItem).getSold() == 1) {
                if (itemList.get(selectedItem).getType().equals("meal")) {
                    if (itemList.get(selectedItem).getItemSold() == 1) {
                        AlertUtil.toastMsgLong(context, getString(R.string.meal_sold_out_error));
                    } else {
                        callChangeStockApi(selectedItem, "");
                    }
                } else if (itemList.get(selectedItem).getType().equals("deal")) {
                    if (itemList.get(selectedItem).getItemSold() == 1) {
                        AlertUtil.toastMsgLong(context, getString(R.string.deal_sold_out_error));
                    } else {
                        callChangeStockApi(selectedItem, "");
                    }
                } else {
                    if (itemList.get(selectedItem).isUnlimited()) {
                        new InStockDialog(context, "", new EditCategoryDialogListener() {
                            @Override
                            public void onUpdateClick(String quantity) {
                                callChangeStockApi(selectedItem, quantity);
                            }
                        }).show();
                    } else {
                        new InStockDialog(context, itemList.get(selectedItem).getQty(), new EditCategoryDialogListener() {
                            @Override
                            public void onUpdateClick(String quantity) {
                                callChangeStockApi(selectedItem, quantity);

                            }
                        }).show();
                    }
                }

            } else {
                callChangeStockApi(selectedItem, itemList.get(selectedItem).getQty());
            }
        });

        binding.btnRemove.setOnClickListener(V -> {
            AlertUtil.showAlertWindow(context, getString(R.string.remove_item_from_cart_alert), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeItemFromCart();
                    if (cartItems.size() > 0) {
                        binding.tvClear.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvClear.setVisibility(View.GONE);
                    }
                }
            });
        });

    }

    //=================MEhtod to go to checkout fragment and place order=========//
    private void checkoutOrder() {
        List<CartItemModel.ExtrasBean> extraList = new ArrayList<>();
        for (int i = 0; i < cartItems.size(); i++) {
            if (!cartItems.get(i).getType().equals("meal") && !cartItems.get(i).getType().equals("deal")) {
                for (int j = 0; j < cartItems.get(i).getExtras().size(); j++) {
                    if (cartItems.get(i).getExtras().get(j).isChecked()) {
                        CartItemModel.ExtrasBean extra = cartItems.get(i).getExtras().get(j);
                        extraList.add(extra);
                    }
                }

                cartItems.get(i).setExtrasAddedToCart(extraList);
            }
        }

        FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.CART_ITEMS, new Gson().toJson(cartItems));
        bundle.putFloat(AppConstant.CART_TOTAL, totalCartPrice);
        bundle.putFloat(AppConstant.CUSTOM_AMOUNT, customAmount);
        checkoutFragment.setArguments(bundle);
        ft.add(R.id.frame, checkoutFragment, CheckoutFragment.class.getSimpleName())
                .addToBackStack(OrdersFragment.class.getSimpleName())
                .commit();
    }

    //================Mehtod to menu adapter============//
    private void setMenuAdapter() {
        categoryAdapter = new MenuCategoryAdapter(context, categoryList, new MenuListener() {
            @Override
            public void onCategoryClick(int position) {
                selectedCategory = position;
                selectedItem = -1;
                if (binding.flAdd.getVisibility() == View.VISIBLE) {
                    handleItemSelection();
                }
                resetItemList();
                itemList.clear();

                if (categoryList.get(position).getId() == -1) {
                    addMealltoItemList();
                } else if (categoryList.get(position).getId() == -2) {
                    addDealltoItemList();
                } else if (categoryList.get(position).getId() == -3) {
                    addAllItemsToItemList();
                } else {
                    for (int i = 0; i < categoryList.get(position).getSubCategories().size(); i++) {
                        for (int j = categoryList.get(position).getSubCategories().get(i).getItems().size() - 1; j >= 0; j--) {

                            if (categoryList.get(position).getSubCategories().get(i).getItems().get(j).getSold() == 0) {
                                itemList.add(0, categoryList.get(position).getSubCategories().get(i).getItems().get(j));
                            } else {
                                itemList.add(categoryList.get(position).getSubCategories().get(i).getItems().get(j));
                            }

                        }
                    }
                }
                itemsAdapter.notifyDataSetChanged();

                if (itemList.size() > 0) {
                    binding.tvEmptyItems.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyItems.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onItemSelected(int position) {

            }

            @Override
            public void onStockChanged(int position) {

            }
        });

        binding.rvCategories.setAdapter(categoryAdapter);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rvCategories.setLayoutManager(mLayoutManager);
        binding.imgCatBack.setSelected(true);

        itemsAdapter = new MenuItemsAdapter(context, itemList, new MenuListener() {
            @Override
            public void onCategoryClick(int position) {

            }

            @Override
            public void onItemSelected(int position) {
                binding.llBottom.setVisibility(View.VISIBLE);
                binding.llBottom2.setVisibility(View.GONE);
                selectedItem = position;

                for (int i = 0; i < cartItems.size(); i++) {
                    cartItems.get(i).setSelected(true);
                }
                cartAdapter.notifyDataSetChanged();
                SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));

                if (position != -1) {
                    sizeIndex = 0;
                    countIndex = 1;
                    setSizeList();
                    setCountList();
                    if (!itemList.get(position).getType().equals("meal") && !itemList.get(position).getType().equals("deal")) {
                        binding.clSize.setVisibility(View.VISIBLE);
                        binding.flExtras.setVisibility(View.VISIBLE);
                        if (itemList.get(position).getExtras() != null && itemList.get(position).getExtras().size() > 0) {
                            binding.flExtras.setVisibility(View.VISIBLE);
                        } else {
                            binding.flExtras.setVisibility(View.INVISIBLE);
                        }
                        if (itemList.get(position).getSold() == 1) {
                            binding.btnSoldOut.setSelected(true);
                            // selectedPrice=itemList.get(position).getSelectedPrice();;
                        } else {
                            binding.btnSoldOut.setSelected(false);
                            //selectedPrice=itemList.get(position).getSelectedPrice();
                            callGetItemCountApi(position);
                        }

                    } else {
                        if (itemList.get(position).getSold() == 1) {
                            binding.btnSoldOut.setSelected(true);
                        } else {
                            binding.btnSoldOut.setSelected(false);
                        }
                        binding.clSize.setVisibility(View.INVISIBLE);
                        binding.flExtras.setVisibility(View.INVISIBLE);
                        selectedPrice = itemList.get(position).getSelectedPrice();
                    }
                    selectedItem = position;
                    handleItemSelection();
                    binding.flAdd.setVisibility(View.VISIBLE);
                    //binding.llSoldOut.setVisibility(View.VISIBLE);
                    binding.llUpdate.setVisibility(View.GONE);
                } else {
                    binding.btnSoldOut.setSelected(false);
                    handleItemSelection();
                }
            }

            @Override
            public void onStockChanged(int position) {
                changeItemStock(position);
            }
        });
        binding.rvItems.setAdapter(itemsAdapter);
        binding.rvItems.setLayoutManager(new GridLayoutManager(context, 3));
        cartAdapter = new CartAdapter(context, cartItems, new CartItemListener() {
            @Override
            public void onItemRemoved(int position) {
                if (binding.llUpdate.getVisibility() == View.VISIBLE && selectedItem == position) {
                    handleItemSelection();
                    for (int i = 0; i < cartItems.size(); i++) {
                        cartItems.get(i).setSelected(true);
                    }
                    cartAdapter.notifyDataSetChanged();
                    SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));
                    if (cartItems.size() > 0) {
                        binding.tvEmptyCart.setVisibility(View.GONE);
                    } else {
                        binding.tvEmptyCart.setVisibility(View.VISIBLE);
                    }
                }
                itemsAdapter.notifyDataSetChanged();
                calculateCartPrice();
            }

            @Override
            public void onItemEdit(int position) {
                binding.llBottom.setVisibility(View.VISIBLE);
                binding.llBottom2.setVisibility(View.GONE);
                for (int i = 0; i < itemList.size(); i++) {
                    itemList.get(i).setSelected(false);
                }
                itemsAdapter.notifyDataSetChanged();
                selectedItem = position;
                handleItemSelection();
                binding.flAdd.setVisibility(View.GONE);
                //binding.llSoldOut.setVisibility(View.GONE);
                binding.llUpdate.setVisibility(View.VISIBLE);
                countIndex = Integer.parseInt(cartItems.get(selectedItem).getQtyAddedToCart());
                selectedPrice = cartItems.get(selectedItem).getSelectedPrice();
                for (int i = 0; i < cartItems.size(); i++) {
                    cartItems.get(i).setSelected(true);
                }
                if (!cartItems.get(selectedItem).getType().equals("meal") && !cartItems.get(selectedItem).getType().equals("deal")) {
                    binding.clSize.setVisibility(View.VISIBLE);
                    if (cartItems.get(selectedItem).getExtras() != null && cartItems.get(selectedItem).getExtras().size() > 0) {
                        binding.flExtras.setVisibility(View.VISIBLE);
                    } else {
                        if (cartItems.get(position).getExtras() != null && cartItems.get(position).getExtras().size() > 0) {
                            binding.flExtras.setVisibility(View.VISIBLE);
                        } else {
                            binding.flExtras.setVisibility(View.INVISIBLE);
                        }
                    }
                    for (int i = 0; i < cartItems.get(selectedItem).getSize_price().size(); i++) {
                        if (cartItems.get(selectedItem).getSize_price().get(i).getSize().equals(cartItems.get(selectedItem).getSizeAddedToCart())) {
                            sizeIndex = i;
                        }
                    }
                    cartExtrasBeanList.clear();
                    for (int i = 0; i < cartItems.get(selectedItem).getExtras().size(); i++) {
                        CartItemModel.ExtrasBean extrasBean = new CartItemModel.ExtrasBean();
                        extrasBean.setType(cartItems.get(selectedItem).getExtras().get(i).getType());
                        extrasBean.setTitle(cartItems.get(selectedItem).getExtras().get(i).getTitle());
                        extrasBean.setPrice(cartItems.get(selectedItem).getExtras().get(i).getPrice());
                        extrasBean.setChecked(cartItems.get(selectedItem).getExtras().get(i).isChecked());
                        extrasBean.setId(cartItems.get(selectedItem).getExtras().get(i).getId());
                        cartExtrasBeanList.add(extrasBean);
                    }
                } else {
                    binding.clSize.setVisibility(View.INVISIBLE);
                    binding.flExtras.setVisibility(View.INVISIBLE);
                }

                cartAdapter.notifyDataSetChanged();
                SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));
                setCountListForCart();
                setSizeListForCart();

            }
        });
        binding.rvCart.setAdapter(cartAdapter);
        binding.rvCart.setLayoutManager(new LinearLayoutManager(context));

        orderListAdapter = new OrderListAdapter(context, orderList, new OkClickListener() {
            @Override
            public void onOKClicked(int position) {
                callWaiterAckApi(position);
            }
        });
        binding.rvOrders.setAdapter(orderListAdapter);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(context));

    }

    //==========Mehtod to change item availability==========//
    private void changeItemStock(int position) {
        if (itemList.get(position).getSold() == 1) {
            if (itemList.get(position).getType().equals("meal")) {
                if (itemList.get(position).getItemSold() == 1) {
                    AlertUtil.toastMsgLong(context, getString(R.string.meal_sold_out_error));
                } else {
                    callChangeStockApi(position, "");
                }
            } else if (itemList.get(position).getType().equals("deal")) {
                if (itemList.get(position).getItemSold() == 1) {
                    AlertUtil.toastMsgLong(context, getString(R.string.deal_sold_out_error));
                } else {
                    callChangeStockApi(position, "");
                }
            } else {
                if (itemList.get(position).isUnlimited()) {
                    new InStockDialog(context, "", new EditCategoryDialogListener() {
                        @Override
                        public void onUpdateClick(String quantity) {
                            callChangeStockApi(position, quantity);
                        }
                    }).show();
                } else {
                    new InStockDialog(context, itemList.get(position).getQty(), new EditCategoryDialogListener() {
                        @Override
                        public void onUpdateClick(String quantity) {
                            callChangeStockApi(position, quantity);

                        }
                    }).show();
                }
            }

        } else {
            callChangeStockApi(selectedItem, itemList.get(selectedItem).getQty());
        }
    }

    //============Method to add meal list to itemlist==========//
    private void addMealltoItemList() {
        itemList.clear();
        for (int i = mealList.size() - 1; i >= 0; i--) {
            mealList.get(i).setName(mealList.get(i).getTitle());
            mealList.get(i).setSelectedPrice(mealList.get(i).getPrice());
            mealList.get(i).setType("meal");
            if (mealList.get(i).getSold() == 0) {
                itemList.add(0, mealList.get(i));
            } else {
                itemList.add(mealList.get(i));
            }
        }
    }

    //============Method to add meal list to itemlist==========//
    private void addMeal() {
        for (int i = mealList.size() - 1; i >= 0; i--) {
            mealList.get(i).setName(mealList.get(i).getTitle());
            mealList.get(i).setSelectedPrice(mealList.get(i).getPrice());
            mealList.get(i).setType("meal");
            if (mealList.get(i).getSold() == 0) {
                itemList.add(0, mealList.get(i));
            } else {
                itemList.add(mealList.get(i));
            }
        }
    }

    //============Method to add all items to itemlist==========//
    private void addAllItemsToItemList() {
        itemList.clear();
        for (int i = 0; i < categoryList.size(); i++) {
            if (!categoryList.get(i).getName().equals("All items") && !categoryList.get(i).getName().equals("Deal")
                    && !categoryList.get(i).getName().equals("Meal")) {
                for (int j = 0; j < categoryList.get(i).getSubCategories().size(); j++) {
                    for (int k = categoryList.get(i).getSubCategories().get(j).getItems().size() - 1; k >= 0; k--) {
                        if (categoryList.get(i).getSubCategories().get(j).getItems().get(k).getSold() == 0) {
                            itemList.add(0, categoryList.get(i).getSubCategories().get(j).getItems().get(k));
                        } else {
                            itemList.add(categoryList.get(i).getSubCategories().get(j).getItems().get(k));
                        }
                    }
                }
            }
        }
        addMeal();
        addDeals();

    }

    //============Method to add meal list to itemlist==========//
    private void addDealltoItemList() {
        itemList.clear();
        for (int i = dealList.size() - 1; i >= 0; i--) {
            dealList.get(i).setName(dealList.get(i).getTitle());
            dealList.get(i).setSelectedPrice(dealList.get(i).getPrice());
            dealList.get(i).setType("deal");
            if (dealList.get(i).getSold() == 0) {
                itemList.add(0, dealList.get(i));
            } else {
                itemList.add(dealList.get(i));
            }
        }
    }

    //============Method to add meal list to itemlist==========//
    private void addDeals() {
        for (int i = dealList.size() - 1; i >= 0; i--) {
            dealList.get(i).setName(dealList.get(i).getTitle());
            dealList.get(i).setSelectedPrice(dealList.get(i).getPrice());
            dealList.get(i).setType("deal");
            if (dealList.get(i).getSold() == 0) {
                itemList.add(0, dealList.get(i));
            } else {
                itemList.add(dealList.get(i));
            }
        }
    }

    //==================MEhtod to reset data in menu items============//
    private void resetItemList() {
        for (int i = 0; i < itemList.size(); i++) {
            if (!itemList.get(i).getType().equals("meal") && !itemList.get(i).getType().equals("deal")) {
                itemList.get(i).setSelected(false);
                itemList.get(i).setSelectedPrice("");
                itemList.get(i).setTotalPrice("");
                itemList.get(i).setCount(1);
                if (itemList.get(i).getExtras() != null) {
                    for (int j = 0; j < itemList.get(i).getExtras().size(); j++) {
                        itemList.get(i).getExtras().get(j).setChecked(false);
                    }
                }
            }
        }
        itemsAdapter.notifyDataSetChanged();
    }

    //==========================Method to call soldout item api=====================//
    private void callChangeStockApi(int position, String qty) {

        if (Utilities.isNetworkAvailable(context)) {
            // Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            params.put("id", "" + itemList.get(position).getId());
            if (itemList.get(position).getSold() == 0) {
                params.put("sold", "1");
            } else {
                params.put("sold", "0");
            }

            apiRequest = new RestClient.ApiRequest(context);
            String url = "";
            if (itemList.get(position).getType().equals("meal")) {
                url = ServerConstants.CHANGE_STOCK_MEAL;
            } else if (itemList.get(position).getType().equals("deal")) {
                url = ServerConstants.CHANGE_STOCK_DEAL;
            } else {
                url = ServerConstants.CHANGE_STOCK_ITEM;

                if (itemList.get(position).getSold() == 1) {
                    if (qty.equals("")) {
                        params.put("qty", "-1");
                    } else {
                        params.put("qty", qty);
                    }
                }

            }


            apiRequest.setUrl(ServerConstants.BASE_URL + url)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (itemList.get(position).getType().equals("meal") || itemList.get(position).getType().equals("deal")) {
                                    SoldOutResponse soldOutResponse = new Gson().fromJson(response, SoldOutResponse.class);
                                    if (soldOutResponse != null) {
                                        if (soldOutResponse.getCode() == 200) {
                                            if (soldOutResponse.getResult().equals("1")) {
                                                itemList.get(position).setSold(1);
                                                itemList.add(itemList.get(position));
                                                itemList.remove(position);

                                            } else if (soldOutResponse.getResult().equals("0")) {
                                                itemList.get(position).setSold(0);
                                                itemList.add(0, itemList.get(position));
                                                itemList.remove(position + 1);

                                            }
                                            for (int i = 0; i < itemList.size(); i++) {
                                                itemList.get(i).setSelected(false);
                                            }
                                            binding.btnSoldOut.setSelected(false);
                                            itemsAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                } else {
                                    MenuResponse menuResponse = new Gson().fromJson(response, MenuResponse.class);
                                    if (menuResponse != null) {
                                        if (menuResponse.getCode() == 200) {
                                            categoryList.clear();
                                            itemList.clear();
                                            mealList.clear();
                                            dealList.clear();


                                            categoryList.addAll(menuResponse.getMerchant().getCategories());

                                            MenuResponse.MerchantBean.CategoriesBean allItemsCat = new MenuResponse.MerchantBean.CategoriesBean();
                                            allItemsCat.setId(-3);
                                            allItemsCat.setName("All items");
                                            categoryList.add(0, allItemsCat);


                                            if (menuResponse.getMerchant().getMealsDetails() != null) {
                                                if (menuResponse.getMerchant().getMealsDetails().size() > 0) {
                                                    MenuResponse.MerchantBean.CategoriesBean mealCategory = new MenuResponse.MerchantBean.CategoriesBean();
                                                    mealCategory.setId(-1);
                                                    mealCategory.setName("Meal");
                                                    categoryList.add(mealCategory);
                                                    mealList.addAll(menuResponse.getMerchant().getMealsDetails());
                                                }
                                            }

                                            if (menuResponse.getMerchant().getDeals() != null) {
                                                if (menuResponse.getMerchant().getDeals().size() > 0) {
                                                    MenuResponse.MerchantBean.CategoriesBean dealCategory = new MenuResponse.MerchantBean.CategoriesBean();
                                                    dealCategory.setId(-2);
                                                    dealCategory.setName("Deal");
                                                    categoryList.add(dealCategory);
                                                    dealList.addAll(menuResponse.getMerchant().getDeals());

                                                }
                                            }

                                            if (categoryList.size() > 0) {
                                                binding.tvEmptyItems.setVisibility(View.GONE);
                                            } else {
                                                binding.tvEmptyItems.setVisibility(View.VISIBLE);
                                            }
                                            if (categoryList.size() > selectedCategory) {
                                                categoryList.get(selectedCategory).setSelected(true);
                                            }
                                            categoryAdapter.notifyDataSetChanged();

                                            if (categoryList.get(selectedCategory).getId() == -1) {
                                                addMealltoItemList();
                                            } else if (categoryList.get(selectedCategory).getId() == -2) {
                                                addDealltoItemList();
                                            } else if (categoryList.get(selectedCategory).getId() == -3) {
                                                addAllItemsToItemList();
                                            } else {
                                                for (int i = 0; i < categoryList.get(selectedCategory).getSubCategories().size(); i++) {
                                                    for (int j = 0; j < categoryList.get(selectedCategory).getSubCategories().get(i).getItems().size(); j++) {

                                                        if (categoryList.get(selectedCategory).getSubCategories().get(i).getItems().get(j).getSold() == 0) {
                                                            itemList.add(0, categoryList.get(selectedCategory).getSubCategories().get(i).getItems().get(j));
                                                        } else {
                                                            itemList.add(categoryList.get(selectedCategory).getSubCategories().get(i).getItems().get(j));
                                                        }

                                                    }
                                                }
                                            }
                                            itemsAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
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

    //==========================Method to get menu api=====================//
    private void callGetMenuApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_MENU)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                MenuResponse menuResponse = new Gson().fromJson(response, MenuResponse.class);
                                if (menuResponse != null) {
                                    if (menuResponse.getCode() == 200) {
                                        callGetOrdersApi();
                                        categoryList.clear();
                                        itemList.clear();
                                        mealList.clear();


                                        categoryList.addAll(menuResponse.getMerchant().getCategories());
                                        if (categoryList.size() > 0) {

                                            if (itemList.size() > 0) {
                                                binding.tvEmptyItems.setVisibility(View.GONE);
                                            } else {
                                                binding.tvEmptyItems.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        MenuResponse.MerchantBean.CategoriesBean allItemsCat = new MenuResponse.MerchantBean.CategoriesBean();
                                        allItemsCat.setId(-3);
                                        allItemsCat.setName("All items");


                                        for (int i = 0; i < categoryList.size(); i++) {
                                            for (int j = 0; j < categoryList.get(i).getSubCategories().size(); j++) {
                                                for (int k = categoryList.get(i).getSubCategories().get(j).getItems().size() - 1; k >= 0; k--) {
                                                    if (categoryList.get(i).getSubCategories().get(j).getItems().get(k).getSold() == 0) {
                                                        itemList.add(0, categoryList.get(i).getSubCategories().get(j).getItems().get(k));
                                                    } else {
                                                        itemList.add(categoryList.get(i).getSubCategories().get(j).getItems().get(k));
                                                    }
                                                }
                                            }
                                        }
                                        categoryList.add(0, allItemsCat);
                                        //itemsAdapter.notifyDataSetChanged();


                                        MenuResponse.MerchantBean.CategoriesBean mealCategory = new MenuResponse.MerchantBean.CategoriesBean();
                                        mealCategory.setId(-1);
                                        mealCategory.setName("Meal");
                                        categoryList.add(mealCategory);

                                        MenuResponse.MerchantBean.CategoriesBean dealCategory = new MenuResponse.MerchantBean.CategoriesBean();
                                        dealCategory.setId(-2);
                                        dealCategory.setName("Deal");
                                        categoryList.add(dealCategory);

                                        if (menuResponse.getMerchant().getMealsDetails() != null) {
                                            if (menuResponse.getMerchant().getMealsDetails().size() > 0) {
                                                mealList.addAll(menuResponse.getMerchant().getMealsDetails());
                                            }
                                        }


                                        if (menuResponse.getMerchant().getDeals() != null) {
                                            if (menuResponse.getMerchant().getDeals().size() > 0) {
                                                dealList.addAll(menuResponse.getMerchant().getDeals());
                                            }
                                        }

                                        if (categoryList.size() > 0) {
                                            binding.tvEmptyItems.setVisibility(View.GONE);
                                        } else {
                                            binding.tvEmptyItems.setVisibility(View.VISIBLE);
                                        }
                                        if (categoryList.size() > 0) {
                                            categoryList.get(0).setSelected(true);
                                        }
                                        categoryAdapter.notifyDataSetChanged();
                                        addMeal();
                                        addDeals();

                                        if (itemList.size() > 0) {
                                            binding.tvEmptyItems.setVisibility(View.GONE);
                                        } else {
                                            binding.tvEmptyItems.setVisibility(View.VISIBLE);
                                        }
                                        itemsAdapter.notifyDataSetChanged();

                                    } else if (menuResponse.getCode() == 301) {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetMenuApi();
                                                callGetOrdersApi();
                                            }
                                        });
                                    }
                                } else {
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

    //================Method to set scroll change listener=============//
    public void setPaging() {
        binding.rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx <= 0) {
                    binding.imgCatBack.setSelected(true);
                    binding.imgCatBack.setRotation(0);
                } else {
                    binding.imgCatBack.setSelected(false);
                    binding.imgCatBack.setRotation(180);
                    Utilities.rotateViews(binding.imgCatBack, binding.imgCatForward);


                }
                if (dx > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            binding.imgCatForward.setSelected(true);
                            binding.imgCatForward.setRotation(180);
                            Utilities.rotateViews(binding.imgCatBack, binding.imgCatForward);


                        }
                    }
                } else {
                    loading = true;
                    binding.imgCatForward.setSelected(false);
                    binding.imgCatForward.setRotation(0);


                }
            }
        });
    }

    //==========Mehtod to calculate total price for cart============//
    private void calculateCartPrice() {
        totalCartPrice = 0;
        for (int position = 0; position < cartItems.size(); position++) {
            float selectedPrice = 0;
            float discountedPrice = 0;
            float totalPrice = 0;
            if (cartItems.get(position).getSelectedPrice().equals("")) {
                selectedPrice = Float.parseFloat(cartItems.get(position).getSize_price().get(0).getPrice());
            } else {
                selectedPrice = Float.parseFloat(cartItems.get(position).getSelectedPrice());
            }

            if (!cartItems.get(position).getType().equals("meal") && !cartItems.get(position).getType().equals("deal")) {
                if (cartItems.get(position).getSale_percent().equals("") && cartItems.get(position).getSale_price().equals("")) {

                } else {
                    if (!cartItems.get(position).getSale_percent().equals("")) {
                        float discount = (selectedPrice * Integer.valueOf(cartItems.get(position).getSale_percent())) / 100;
                        discountedPrice = selectedPrice - discount;
                    } else if (!cartItems.get(position).getSale_price().equals("")) {
                        float discount = Integer.valueOf(cartItems.get(position).getSale_price());
                        discountedPrice = selectedPrice - discount;
                    }
                }

                totalPrice = selectedPrice * Integer.parseInt(cartItems.get(position).getQtyAddedToCart());
                discountedPrice = discountedPrice * Integer.parseInt(cartItems.get(position).getQtyAddedToCart());


                for (int i = 0; i < cartItems.get(position).getExtras().size(); i++) {
                    if (cartItems.get(position).getExtras().get(i).isChecked()) {
                        totalPrice = totalPrice + (Float.parseFloat(cartItems.get(position).getExtras().get(i).getPrice()) * Integer.parseInt(cartItems.get(position).getQtyAddedToCart()));
                        discountedPrice = discountedPrice + (Float.parseFloat(cartItems.get(position).getExtras().get(i).getPrice()) * Integer.parseInt(cartItems.get(position).getQtyAddedToCart()));
                    }
                }
                if (!cartItems.get(position).getSale_price().equals("") || !cartItems.get(position).getSale_percent().equals("")) {
                    totalCartPrice = totalCartPrice + discountedPrice;
                } else {
                    totalCartPrice = totalCartPrice + totalPrice;

                }
            } else {
                totalCartPrice = totalCartPrice + selectedPrice * Integer.parseInt(cartItems.get(position).getQtyAddedToCart());
            }
        }


        binding.llCheckout.setVisibility(View.VISIBLE);
        totalCartPrice = (float) (Math.round(totalCartPrice * 100.0) / 100.0);
        binding.tvTotalPriceCart.setText("SR " + totalCartPrice);
        setCustomAmount(customAmount);

    }

    //==========================Method to get merchant detail api=====================//
    private void callGetItemCountApi(int position) {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("id", "" + itemList.get(position).getId());

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ITEM_COUNT)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setHeader2("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            // AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {

                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    if (genericResponse.getCode() == 200) {
                                        if (Integer.parseInt(genericResponse.getResult().getQty()) == 0) {
                                            itemList.get(position).setSold(1);
                                            itemList.get(position).setQty(genericResponse.getResult().getQty());
                                        } else if (Integer.parseInt(genericResponse.getResult().getQty()) < 0) {
                                            itemList.get(position).setSold(0);
                                            itemList.get(position).setQty("99");
                                        } else {
                                            itemList.get(position).setSold(0);
                                            itemList.get(position).setQty(genericResponse.getResult().getQty());
                                        }
                                        for (int i = 0; i < cartItems.size(); i++) {
                                            if (cartItems.get(i).getId() == itemList.get(position).getId()) {
                                                if (genericResponse.getResult().getQty().equals("-1")) {
                                                    cartItems.get(i).setQty("99");

                                                } else {
                                                    cartItems.get(i).setQty(genericResponse.getResult().getQty());

                                                }

                                            }
                                        }
                                    } else {
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                } else {
                                    AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                }


                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            // AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    //==========================Method to get merchant detail api=====================//
    public void callGetOrdersApi() {

        if (Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            String tokennew= SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, "");
            String type= SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT);
            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ORDER_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                    .setHeader2("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                try{
                                    OrderListResponse orderListResponse = new Gson().fromJson(response, OrderListResponse.class);
                                    if (orderListResponse != null) {
                                        if (orderListResponse.getCode() == 200) {
                                            orderList.clear();
                                            orderList.addAll(orderListResponse.getResult());
                                            orderListAdapter.notifyDataSetChanged();
                                            if (orderList.size() > 0) {
                                                binding.tvEmptyOrder.setVisibility(View.GONE);
                                            } else {
                                                binding.tvEmptyOrder.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                        }
                                    } else {
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }

                                }catch(Exception e){
                                    Log.e("ErrorInOrder",e.getMessage());
                                }

                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.GONE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, getString(R.string.network_error));
        }
    }

    private void filterList() {
        filteredList.clear();
        if (isDinein) {
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getDelivery().equals(AppConstant.TYPE_DINE_IN)) {
                    filteredList.add(orderList.get(i));
                }
            }
        } else if (isDelivery) {
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getDelivery().equals(AppConstant.TYPE_DELIVERY) ||
                        orderList.get(i).getDelivery().equals(AppConstant.TYPE_INSTA_DELIVERY) ||
                        orderList.get(i).getDelivery().equals(AppConstant.TYPE_SWIFT_DELIVERY)) {
                    filteredList.add(orderList.get(i));
                }
            }
        } else if (isPickup) {
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getDelivery().equals(AppConstant.TYPE_PICKUP)) {
                    filteredList.add(orderList.get(i));
                }
            }
        } else if (isCashierOrder) {
            for (int i = 0; i < orderList.size(); i++) {
                if (orderList.get(i).getDelivery().equals(AppConstant.CASHIER)) {
                    filteredList.add(orderList.get(i));
                }
            }
        }


        if (!isDelivery && !isSwiftDelivery && !isDinein && !isInstaDelivery && !isPickup && !isCashierOrder) {
            binding.rvOrders.setAdapter(orderListAdapter);
            if (orderList.size() > 0) {
                binding.tvEmptyOrder.setVisibility(View.GONE);
            } else {
                binding.tvEmptyOrder.setVisibility(View.VISIBLE);
            }
        } else {
            filteredAdapter = new OrderListAdapter(context, filteredList, new OkClickListener() {
                @Override
                public void onOKClicked(int position) {
                    callWaiterAckApi(position);
                }
            });
            binding.rvOrders.setAdapter(filteredAdapter);
            if (filteredList.size() > 0) {
                binding.tvEmptyOrder.setVisibility(View.GONE);
            } else {
                binding.tvEmptyOrder.setVisibility(View.VISIBLE);
            }
        }

        orderListAdapter.notifyDataSetChanged();
    }

    //=============Mehtod to handle bottom layout for items==============//
    private void handleItemSelection() {
        if (selectedItem == -1) {
            binding.tvSize.setText("_");
            binding.tvCount.setText("0");
            binding.flSizeUp.setClickable(false);
            binding.flSizeDown.setClickable(false);
            binding.flCountUp.setClickable(false);
            binding.flCountDown.setClickable(false);
            binding.tvAddItem.setClickable(false);
            binding.btnSoldOut.setClickable(false);
            binding.tvExtras.setClickable(false);

        } else {

            binding.flSizeUp.setClickable(true);
            binding.flSizeDown.setClickable(true);
            binding.flCountUp.setClickable(true);
            binding.flCountDown.setClickable(true);
            binding.tvAddItem.setClickable(true);
            binding.btnSoldOut.setClickable(true);
            binding.tvExtras.setClickable(true);


        }
    }

    private void removeItemFromCart() {
        if (binding.llUpdate.getVisibility() == View.VISIBLE && selectedItem == selectedItem) {
            cartItems.remove(selectedItem);
            SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));

            selectedItem = -1;
            handleItemSelection();

            for (int i = 0; i < cartItems.size(); i++) {
                cartItems.get(i).setSelected(true);
            }
            cartAdapter.notifyDataSetChanged();
            SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));


            if (cartItems.size() > 0) {
                binding.tvEmptyCart.setVisibility(View.GONE);
            } else {
                binding.tvEmptyCart.setVisibility(View.VISIBLE);
                customAmount = 0;
            }
        }
        for (int i = 0; i < cartItems.size(); i++) {
            cartItems.get(i).setSelected(true);
        }
        cartAdapter.notifyDataSetChanged();
        SharedPrefManager.getInstance(context).saveString(AppConstant.CART_HISTORY, new Gson().toJson(cartItems));
        calculateCartPrice();
        binding.flAdd.setVisibility(View.VISIBLE);
        // binding.llSoldOut.setVisibility(View.VISIBLE);
        binding.llUpdate.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        orderListAdapter.notifyDataSetChanged();
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter(AppConstant.ORDER_UPDATE));
        // start automatic printer
        SharedPrefManager sharedPrefManager=SharedPrefManager.getInstance(context);
        if (sharedPrefManager.getInt(AppConstant.PRINTER_AUTO_ON, 0) == 1) {
            SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
            List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
            if (tempPrintList.size() > 0) {
                autoPrint();
            }
        }
    }

    private void autoPrint() {
        // this is for automatic print from notification
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!context.isFinishing()) {
                    try {
                        SharedPrefManager tempPrefenceManager = SharedPrefManager.getInstance(context);
                        List<OrderListResponse.ResultBean> tempPrintList = loadSharedPreferencesLogList(tempPrefenceManager);
                        if (tempPrintList.size() > 0) {
                            for (int i = 0; i < tempPrintList.size(); i++) {
                              /*  AutomaticOrderDetailDialog orderDetailDialog = new AutomaticOrderDetailDialog(context, tempPrintList.get(i), i);
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

    private void callWaiterAckApi(int position) {
        if (Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            if (filteredList.size() > 0) {
                params.put("oid", "" + filteredList.get(position).getId());
            } else {
                params.put("oid", "" + orderList.get(position).getId());

            }
            params.put("device_id", SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN, ""));


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.ACK_WAITER_CALL)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        if (genericResponse.getCode() == 200) {

                                            if (filteredList.size() > 0) {
                                                filteredList.get(position).setWaiterCall(0);
                                                filteredAdapter.notifyDataSetChanged();
                                            } else {
                                                orderList.get(position).setWaiterCall(0);
                                                orderListAdapter.notifyDataSetChanged();
                                            }

                                        } else if (genericResponse.getCode() == 301) {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    AlertUtil.showAlertWindow(context, context.getString(R.string.still_want_to_proceed), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });
                                                }
                                            });
                                        }

                                    } else {
                                        binding.progressBar.setVisibility(View.GONE);
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.GONE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        } else {
            AlertUtil.toastMsg(context, context.getString(R.string.network_error));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }



}
