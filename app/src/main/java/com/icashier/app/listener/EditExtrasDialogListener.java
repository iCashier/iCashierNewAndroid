package com.icashier.app.listener;

import com.icashier.app.model.ExtrasListResponse;

public interface EditExtrasDialogListener {
    void onUpdateClick(ExtrasListResponse.ResultBean updatedExtra);
}
