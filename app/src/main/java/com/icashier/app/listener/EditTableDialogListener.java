package com.icashier.app.listener;

import com.icashier.app.model.TableListResponse;

public interface EditTableDialogListener {

    void onUpdateClick(TableListResponse.ResultBean tableData);
}
