package com.icashier.app.printer;

import android.content.Context;
import android.content.DialogInterface;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.Epos2CallbackCode;
import com.icashier.app.R;
import com.icashier.app.helper.AlertUtil;

public class ShowMsg {
    public static void showException(Exception e, String method, Context context) {
        show(getEposExceptionText(((Epos2Exception) e).getErrorStatus(),context), context);
    }

    public static void showResult(int code, String errMsg, Context context) {
        show(getCodeText(code,context), context);
    }

    public static void showMsg(String msg, Context context) {
        show(msg, context);
    }

    private static void show(String msg, Context context) {
        AlertUtil.showToastShort(context,msg);
    }

    private static String getEposExceptionText(int state,Context context) {
        String return_text = "";
        switch (state) {
            case    Epos2Exception.ERR_PARAM:
                return_text = context.getString(R.string.error_generic);
                break;
            case    Epos2Exception.ERR_CONNECT:
                return_text = context.getString(R.string.err_con_printer);
                break;
            case    Epos2Exception.ERR_TIMEOUT:
                return_text = context.getString(R.string.err_timeout_printer);
                break;
            case    Epos2Exception.ERR_MEMORY:
                return_text = context.getString(R.string.err_memory_printer);
                break;
            case    Epos2Exception.ERR_ILLEGAL:
                return_text = context.getString(R.string.err_invalid_cmd_printer);
                break;
            case    Epos2Exception.ERR_PROCESSING:
                return_text = context.getString(R.string.err_processing_printer);
                break;
            case    Epos2Exception.ERR_NOT_FOUND:
                return_text = context.getString(R.string.err_not_found_printer);
                break;
            case    Epos2Exception.ERR_IN_USE:
                return_text = context.getString(R.string.err_in_use_printer);
                break;
            case    Epos2Exception.ERR_TYPE_INVALID:
                return_text = context.getString(R.string.err_invalid_type_printer);
                break;
            case    Epos2Exception.ERR_DISCONNECT:
                return_text = context.getString(R.string.err_disconnected_printer);
                break;
            case    Epos2Exception.ERR_ALREADY_OPENED:
                return_text = context.getString(R.string.err_already_open_printer);
                break;
            case    Epos2Exception.ERR_ALREADY_USED:
                return_text = context.getString(R.string.err_already_used_printer);
                break;
            case    Epos2Exception.ERR_BOX_COUNT_OVER:
                return_text = context.getString(R.string.err_box_count_printer);
                break;
            case    Epos2Exception.ERR_BOX_CLIENT_OVER:
                return_text = context.getString(R.string.err_box_client_printer);
                break;
            case    Epos2Exception.ERR_UNSUPPORTED:
                return_text = context.getString(R.string.err_unsupproted_printer);
                break;
            case    Epos2Exception.ERR_FAILURE:
                return_text = context.getString(R.string.error_generic);
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }

    private static String getCodeText(int state,Context context) {
        String return_text = "";
        switch (state) {
            case Epos2CallbackCode.CODE_SUCCESS:
                return_text = context.getString(R.string.reciept_print_success);
                break;
            case Epos2CallbackCode.CODE_PRINTING:
                return_text = context.getString(R.string.status_printing);
                break;
            case Epos2CallbackCode.CODE_ERR_AUTORECOVER:
                return_text = context.getString(R.string.err_auto_recover_printer);
                break;
            case Epos2CallbackCode.CODE_ERR_COVER_OPEN:
                return_text = context.getString(R.string.err_cover_open_printer);
                break;
            case Epos2CallbackCode.CODE_ERR_CUTTER:
                return_text = context.getString(R.string.err_cutter_printer);
                break;
            case Epos2CallbackCode.CODE_ERR_MECHANICAL:
                return_text = context.getString(R.string.err_mechanical_printer);
                break;
            case Epos2CallbackCode.CODE_ERR_EMPTY:
                return_text = context.getString(R.string.err_empty_printer);
                break;
            case Epos2CallbackCode.CODE_ERR_UNRECOVERABLE:
                return_text = context.getString(R.string.err_unrecoverable_printer);
                break;
            case Epos2CallbackCode.CODE_ERR_FAILURE:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_NOT_FOUND:
                return_text = context.getString(R.string.err_not_found);
                break;
            case Epos2CallbackCode.CODE_ERR_SYSTEM:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_PORT:
                return_text = context.getString(R.string.err_port);
                break;
            case Epos2CallbackCode.CODE_ERR_TIMEOUT:
                return_text = context.getString(R.string.err_timeout);
                break;
            case Epos2CallbackCode.CODE_ERR_JOB_NOT_FOUND:
                return_text = context.getString(R.string.err_job);
                break;
            case Epos2CallbackCode.CODE_ERR_SPOOLER:
                return_text = context.getString(R.string.err_spooler);
                break;
            case Epos2CallbackCode.CODE_ERR_BATTERY_LOW:
                return_text = context.getString(R.string.err_battery_low);
                break;
            case Epos2CallbackCode.CODE_ERR_TOO_MANY_REQUESTS:
                return_text = context.getString(R.string.err_many_reqs);
                break;
            case Epos2CallbackCode.CODE_ERR_REQUEST_ENTITY_TOO_LARGE:
                return_text = context.getString(R.string.err_large_request);
                break;
            case Epos2CallbackCode.CODE_CANCELED:
                return_text = context.getString(R.string.err_cancelled);
                break;
            case Epos2CallbackCode.CODE_ERR_NO_MICR_DATA:
                return_text = context.getString(R.string.err_no_data);
                break;
            case Epos2CallbackCode.CODE_ERR_ILLEGAL_LENGTH:
                return_text = context.getString(R.string.err_invalid_length);
                break;
            case Epos2CallbackCode.CODE_ERR_NO_MAGNETIC_DATA:
                return_text = context.getString(R.string.err_magnetic_data);
                break;
            case Epos2CallbackCode.CODE_ERR_RECOGNITION:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_READ:
                return_text = context.getString(R.string.err_reading_data);
                break;
            case Epos2CallbackCode.CODE_ERR_NOISE_DETECTED:
                return_text = context.getString(R.string.err_noise_detected);
                break;
            case Epos2CallbackCode.CODE_ERR_PAPER_JAM:
                return_text = context.getString(R.string.err_paper_jam);
                break;
            case Epos2CallbackCode.CODE_ERR_PAPER_PULLED_OUT:
                return_text = context.getString(R.string.err_paper_pulled);
                break;
            case Epos2CallbackCode.CODE_ERR_CANCEL_FAILED:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_PAPER_TYPE:
                return_text = context.getString(R.string.err_paper_type);
                break;
            case Epos2CallbackCode.CODE_ERR_WAIT_INSERTION:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_ILLEGAL:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_INSERTED:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_WAIT_REMOVAL:
                return_text = context.getString(R.string.error_generic);
                break;
            case Epos2CallbackCode.CODE_ERR_DEVICE_BUSY:
                return_text = context.getString(R.string.err_device_busy);
                break;
            case Epos2CallbackCode.CODE_ERR_IN_USE:
                return_text = context.getString(R.string.err_already_used_printer);
                break;
            case Epos2CallbackCode.CODE_ERR_CONNECT:
                return_text = context.getString(R.string.err_printer_connection);
                break;
            case Epos2CallbackCode.CODE_ERR_DISCONNECT:
                return_text = context.getString(R.string.err_diconnecting);
                break;
            case Epos2CallbackCode.CODE_ERR_MEMORY:
                return_text = context.getString(R.string.err_outofmemory);
                break;
            case Epos2CallbackCode.CODE_ERR_PROCESSING:
                return_text = context.getString(R.string.err_procession_req);
                break;
            case Epos2CallbackCode.CODE_ERR_PARAM:
                return_text = context.getString(R.string.error_generic);
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }
}
