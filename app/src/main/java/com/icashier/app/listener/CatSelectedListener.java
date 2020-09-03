package com.icashier.app.listener;

import com.icashier.app.model.PredefinedCategoriesResponse;

public interface CatSelectedListener {
   void  onCatSelected(PredefinedCategoriesResponse.ResultBean cat);
   void onCatSelected(String cat);
}
