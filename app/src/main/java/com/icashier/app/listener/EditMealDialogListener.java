package com.icashier.app.listener;

import com.icashier.app.model.MealListResponse;

import java.util.List;

public interface EditMealDialogListener {
    void onUpdate(List<MealListResponse.ResultBean> list);
}
