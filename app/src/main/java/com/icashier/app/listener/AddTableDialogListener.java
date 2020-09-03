package com.icashier.app.listener;

import com.icashier.app.model.TableListResponse;

import java.util.List;

public interface AddTableDialogListener {
    void onDialogDismissed(List<TableListResponse.ResultBean> list);
}
