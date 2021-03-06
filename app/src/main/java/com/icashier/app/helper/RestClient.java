package com.icashier.app.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.icashier.app.constants.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class RestClient {

    /*
    Constants
     */
    public static final String BASE_URL = "";
    public static final String TAG_REQUEST = "Api Request ";
    public static final String TAG_RESPONSE = "Api Response ";
    public static String INTERNET_CONNECTION_ERROR = "Please check your internet connection";
    public static String SLOW_INTERNET_CONNECTION_ERROR = "You seems to be on slow network. Switch to faster network for better experience";
    public static String API_FAILED_ERROR = "Something went wrong. Please try again";
    public static String METHOD_MISSING_ERROR = "Request method missing";
    public static String API_CONNECT_ERROR = "Response :- " + "Error: Unable to connect to server.";
    public static String API_TIMEOUT_ERROR = "Response :- " + "Error: Connection timed out.";
    public static String API_TIMEOUT_RESPONSE = "Connection timed out. Please try again";
    public static String API_CONNECT_RESPONSE = "Unable to connect to server. Please try again";

    public static class ApiRequest {
        private Handler notifyHandler;
        private Runnable notifyRunnable;
        private Context context;
        private OkHttpClient client;
        private Request request;
        private Call apiCall;

        /*
        Configs
         */
        public int readTimeOut = 30;
        public int writeTimeOut = 30;
        public int connectTimeOut = 20;
        public boolean logging = true;
        public boolean caching = false;

        public static final String METHOD_GET = "GET";
        public static final String METHOD_POST = "POST";
        public static final String METHOD_MULTIPART = "MULTIPART";

        /*
        Variables
         */
        private String url;
        private String tag = "API";
        private String headerValue = "";
        private String headerKey = "Authorization";
        private String headerValue2 = "";
        private String headerKey2 = "type";
        private String headerValue3 = "";
        private String headerKey3 = "cashiertoken";
        private String requestMethod = "get";
        private JSONObject params;
        private HashMap<String, File> keyValueFileParams;
        private ArrayList<File> fileParams;
        private String fileParamsKey;
        private HashMap<String, String> headers;


        /*
        Callbacks
         */
        private ResponseListener responseListener;
        private ErrorListener errorListener;
        private ProgressListener progressListener;


        public ApiRequest(Context context) {
            this.context = context;
            client = new OkHttpClient.Builder()
                    .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(readTimeOut, TimeUnit.SECONDS)
                    .readTimeout(writeTimeOut, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

        }

        public ApiRequest setUrl(String apiUrl) {
            url = apiUrl;
            return this;
        }

        public ApiRequest setMethod(String value) {
            requestMethod = value;
            return this;
        }

        public ApiRequest setConnectTimeout(int timeout) {
            connectTimeOut = timeout;
            return this;
        }

        public ApiRequest setReadTimeout(int timeout) {
            readTimeOut = timeout;
            return this;
        }

        public ApiRequest setWriteTimeout(int timeout) {
            writeTimeOut = timeout;
            return this;
        }

        public ApiRequest setHeader(String key, String value) {
            headerKey = key;
            headerValue = value;
            return this;
        }

        public ApiRequest setHeader2(String key, String value) {
            headerKey2 = key;
            headerValue2 = value;
            return this;
        }

        public ApiRequest setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public ApiRequest setHeader3(String key, String value) {
            headerKey2 = key;
            headerValue2 = value;
            return this;
        }

        public ApiRequest setLogging(boolean value) {
            logging = value;
            return this;
        }

        public ApiRequest setCachingEnabled(boolean value) {
            caching = value;
            return this;
        }

        public ApiRequest setTag(String value) {
            tag = value;
            return this;
        }

        public ApiRequest setParams(final HashMap<String, String> postParams) {
            if (postParams != null)
                params = new JSONObject(postParams);
           // Log.e("API_REQ",params.toString());
            return this;
        }

        public ApiRequest setParamsRow(final String postParamsRows) {
            if (postParamsRows != null) {
                try {
                    params = new JSONObject(postParamsRows);
                    Log.e("API_REQ",params.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return this;
        }

        public ApiRequest setFileParams(final HashMap<String, File> params) {
            keyValueFileParams = params;
            return this;
        }

        public ApiRequest setFileParams(final String key, final ArrayList<File> params) {
            fileParamsKey = key;
            fileParams = params;
            return this;
        }


        public ApiRequest setResponseListener(ResponseListener listener) {
            responseListener = listener;
            return this;
        }

        public ApiRequest setErrorListener(ErrorListener listener) {
            errorListener = listener;
            return this;
        }

        public ApiRequest setProgressListener(ProgressListener listener) {
            progressListener = listener;
            return this;
        }

        public ApiRequest cancelRequest() {
            if (apiCall != null /*&& !apiCall.isExecuted()*/)
                apiCall.cancel();
            return this;
        }


        public ApiRequest execute() {
            if (isOnline(context)) {

                notifyHandler = new Handler();
                notifyRunnable = new Runnable() {
                    @Override
                    public void run() {
                        AlertUtil.showToastLong(context, SLOW_INTERNET_CONNECTION_ERROR);
                    }
                };
                notifyHandler.postDelayed(notifyRunnable, 10000);

                if (requestMethod.equalsIgnoreCase("get")) {
                    if (url != null && url.length() > 0) {
                        Log.i(TAG_REQUEST, url.toString() != null ? url.toString() : "");
//                        request = new Request.Builder()
//                                .header(headerKey, headerValue)
//                                .url(url)
//                                .build();
                        Request.Builder req = new Request.Builder();


                        HashMap<String, String> headersToken = new HashMap<>();
                        headersToken.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
                        headersToken.put("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT));
                        headersToken.put("cashiertoken", SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN, ""));
                        headersToken.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));
                        if (headersToken != null) {
                            for (Map.Entry<String, String> entry : headersToken.entrySet()) {
                                if (entry.getValue() != null && entry.getValue().length() > 0) {
                                    req.addHeader(entry.getKey(), entry.getValue());
                                }
                            }
                            req.url(url);
                        } else {
                            req.header(headerKey, headerValue)
                                    .url(url);

                        }
                        request = req.build();
                    }
                } else if (requestMethod.equalsIgnoreCase("post")) {
                    if (params != null) {
                        Log.i(TAG_REQUEST, params.toString() != null ? params.toString() : "");
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, params.toString());
                        Request.Builder req = new Request.Builder();


                        HashMap<String, String> headersToken = new HashMap<>();
                        headersToken.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
                        headersToken.put("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT));
                        headersToken.put("cashiertoken", SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN, ""));
                        headersToken.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));

                        if (headersToken != null) {
                            for (Map.Entry<String, String> entry : headersToken.entrySet()) {
                                if (entry.getValue() != null) {
                                    req.addHeader(entry.getKey(), entry.getValue());
                                }
                            }
                            req.url(url)
                                    .post(body);
                        } else {
                            req.header(headerKey, headerValue)
                                    .url(url)
                                    .post(body);

                        }
                        request = req.build();
                    }
                } else if (requestMethod.equalsIgnoreCase("multipart")) {
                    MultipartBody.Builder dataBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    if (params != null) {
                        Log.i(TAG_REQUEST, params.toString() != null ? params.toString() : "");
                        try {
                            Iterator<String> iterator = params.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                dataBuilder.addFormDataPart(key, params.getString(key));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (errorListener != null)
                                errorListener.onError(tag, API_FAILED_ERROR);
                        }
                    }

                    if (fileParams != null && fileParams.size() > 0) {
                        for (File file : fileParams) {
                            if (file != null) {
                                String fileName = file.getName();
                                MediaType MEDIA_TYPE_PNG = MediaType.parse(URLConnection.guessContentTypeFromName(fileName));
                                dataBuilder.addFormDataPart(fileParamsKey, fileName, RequestBody.create(MEDIA_TYPE_PNG, file));
                            }
                        }
                    }

                    if (keyValueFileParams != null && keyValueFileParams.size() > 0) {
                        for (Map.Entry<String, File> entry : keyValueFileParams.entrySet()) {
                            if (entry.getValue() != null && entry.getValue().toString().length() > 0) {
                                String fileName = entry.getValue().getName();
                                MediaType MEDIA_TYPE_PNG = MediaType.parse(URLConnection.guessContentTypeFromName(fileName));
                                dataBuilder.addFormDataPart(entry.getKey(), fileName, RequestBody.create(MEDIA_TYPE_PNG, entry.getValue()));
                            }
                        }
                    }

                    if (dataBuilder != null) {
                        MultipartBody requestBody = dataBuilder.build();
                        CountingRequestBody fileRequestBody = new CountingRequestBody(requestBody, new Listener() {
                            @Override
                            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                                Handler mainHandler = new Handler(context.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (progressListener != null)
                                            progressListener.onProgress((int) ((bytesWritten * 100) / contentLength));
                                    }
                                };
                                mainHandler.post(myRunnable);
                            }
                        });

                        HashMap<String, String> headersToken = new HashMap<>();
                        headersToken.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
                        headersToken.put("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT));
                        headersToken.put("cashiertoken", SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN, ""));
                        headersToken.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));

                        Request.Builder req = new Request.Builder();
                        if (headersToken != null) {
                            for (Map.Entry<String, String> entry : headersToken.entrySet()) {
                                if (entry.getValue() != null) {
                                    req.addHeader(entry.getKey(), entry.getValue());
                                }
                            }
                            req.url(url)
                                    .post(fileRequestBody);
                        } else {
                            req.header(headerKey, headerValue)
                                    .url(url)
                                    .post(fileRequestBody);

                        }
                        request = req.build();
                    }

                } else {
                    if (errorListener != null)
                        errorListener.onError(tag, METHOD_MISSING_ERROR);
                }

                apiCall = client.newCall(request);
                apiCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        notifyHandler.removeCallbacks(notifyRunnable);
                        //call.cancel();
                        if (!apiCall.isCanceled()) {
                            Handler mainHandler = new Handler(context.getMainLooper());
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    String errMsg = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : API_FAILED_ERROR;
                                    if (logging)
                                        Log.i(TAG_RESPONSE, errMsg);

                                    if (errorListener != null)
                                        errorListener.onError(tag, API_FAILED_ERROR);
                                }
                            };
                            mainHandler.post(myRunnable);
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        notifyHandler.removeCallbacks(notifyRunnable);
                        if (!apiCall.isCanceled()) {
                            final String responseStr = response.body().string();
                            Handler mainHandler = new Handler(context.getMainLooper());
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (caching) {
                                        cacheResponse(context, tag, responseStr);
                                    }

                                    if (logging)
                                        Log.i(TAG_RESPONSE, responseStr != null ? responseStr : "");

                                    if (responseListener != null)
                                        responseListener.onResponse(tag, responseStr);
                                }
                            };
                            mainHandler.post(myRunnable);
                        }

                    }
                });
            } else {
                if (caching) {
                    String responseStr = getCacheResponse(context, tag);
                    if (responseStr != null && responseStr.length() > 0) {
                        if (responseListener != null)
                            responseListener.onResponse(tag, responseStr);
                    } else {
                        if (errorListener != null)
                            errorListener.onError(tag, INTERNET_CONNECTION_ERROR);
                    }
                } else {
                    if (errorListener != null)
                        errorListener.onError(tag, INTERNET_CONNECTION_ERROR);
                }
            }

            return this;
        }
        public ApiRequest executeStc( HashMap<String,String> headers) {
            if (isOnline(context)) {

                notifyHandler = new Handler();
                notifyRunnable = new Runnable() {
                    @Override
                    public void run() {
                        AlertUtil.showToastLong(context, SLOW_INTERNET_CONNECTION_ERROR);
                    }
                };
                notifyHandler.postDelayed(notifyRunnable, 10000);

                 if (requestMethod.equalsIgnoreCase("post")) {
                    if (params != null) {
                        Log.i(TAG_REQUEST, params.toString() != null ? params.toString() : "");
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, params.toString());
                        Request.Builder req = new Request.Builder();

                        if (headers != null) {
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                if (entry.getValue() != null) {
                                    req.addHeader(entry.getKey(), entry.getValue());
                                }
                            }
                            req.url(url)
                                    .post(body);
                        } else {
                            req.header(headerKey, headerValue)
                                    .url(url)
                                    .post(body);

                        }
                        request = req.build();
                    }
                } else if (requestMethod.equalsIgnoreCase("multipart")) {
                    MultipartBody.Builder dataBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    if (params != null) {
                        Log.i(TAG_REQUEST, params.toString() != null ? params.toString() : "");
                        try {
                            Iterator<String> iterator = params.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                dataBuilder.addFormDataPart(key, params.getString(key));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (errorListener != null)
                                errorListener.onError(tag, API_FAILED_ERROR);
                        }
                    }

                    if (fileParams != null && fileParams.size() > 0) {
                        for (File file : fileParams) {
                            if (file != null) {
                                String fileName = file.getName();
                                MediaType MEDIA_TYPE_PNG = MediaType.parse(URLConnection.guessContentTypeFromName(fileName));
                                dataBuilder.addFormDataPart(fileParamsKey, fileName, RequestBody.create(MEDIA_TYPE_PNG, file));
                            }
                        }
                    }

                    if (keyValueFileParams != null && keyValueFileParams.size() > 0) {
                        for (Map.Entry<String, File> entry : keyValueFileParams.entrySet()) {
                            if (entry.getValue() != null && entry.getValue().toString().length() > 0) {
                                String fileName = entry.getValue().getName();
                                MediaType MEDIA_TYPE_PNG = MediaType.parse(URLConnection.guessContentTypeFromName(fileName));
                                dataBuilder.addFormDataPart(entry.getKey(), fileName, RequestBody.create(MEDIA_TYPE_PNG, entry.getValue()));
                            }
                        }
                    }

                    if (dataBuilder != null) {
                        MultipartBody requestBody = dataBuilder.build();
                        CountingRequestBody fileRequestBody = new CountingRequestBody(requestBody, new Listener() {
                            @Override
                            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                                Handler mainHandler = new Handler(context.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (progressListener != null)
                                            progressListener.onProgress((int) ((bytesWritten * 100) / contentLength));
                                    }
                                };
                                mainHandler.post(myRunnable);
                            }
                        });

                        HashMap<String, String> headersToken = new HashMap<>();
                        headersToken.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""));
                        headersToken.put("type", SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT));
                        headersToken.put("cashiertoken", SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN, ""));
                        headersToken.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));

                        Request.Builder req = new Request.Builder();
                        if (headersToken != null) {
                            for (Map.Entry<String, String> entry : headersToken.entrySet()) {
                                if (entry.getValue() != null) {
                                    req.addHeader(entry.getKey(), entry.getValue());
                                }
                            }
                            req.url(url)
                                    .post(fileRequestBody);
                        } else {
                            req.header(headerKey, headerValue)
                                    .url(url)
                                    .post(fileRequestBody);

                        }
                        request = req.build();
                    }

                } else {
                    if (errorListener != null)
                        errorListener.onError(tag, METHOD_MISSING_ERROR);
                }

                apiCall = client.newCall(request);
                apiCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        notifyHandler.removeCallbacks(notifyRunnable);
                        //call.cancel();
                        if (!apiCall.isCanceled()) {
                            Handler mainHandler = new Handler(context.getMainLooper());
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    String errMsg = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : API_FAILED_ERROR;
                                    if (logging)
                                        Log.i(TAG_RESPONSE, errMsg);

                                    if (errorListener != null)
                                        errorListener.onError(tag, API_FAILED_ERROR);
                                }
                            };
                            mainHandler.post(myRunnable);
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        notifyHandler.removeCallbacks(notifyRunnable);
                        if (!apiCall.isCanceled()) {
                            final String responseStr = response.body().string();
                            Handler mainHandler = new Handler(context.getMainLooper());
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (caching) {
                                        cacheResponse(context, tag, responseStr);
                                    }

                                    if (logging)
                                        Log.i(TAG_RESPONSE, responseStr != null ? responseStr : "");

                                    if (responseListener != null)
                                        responseListener.onResponse(tag, responseStr);
                                }
                            };
                            mainHandler.post(myRunnable);
                        }

                    }
                });
            } else {
                if (caching) {
                    String responseStr = getCacheResponse(context, tag);
                    if (responseStr != null && responseStr.length() > 0) {
                        if (responseListener != null)
                            responseListener.onResponse(tag, responseStr);
                    } else {
                        if (errorListener != null)
                            errorListener.onError(tag, INTERNET_CONNECTION_ERROR);
                    }
                } else {
                    if (errorListener != null)
                        errorListener.onError(tag, INTERNET_CONNECTION_ERROR);
                }
            }

            return this;
        }
    }

    public static class CountingRequestBody extends RequestBody {

        protected RequestBody delegate;
        protected Listener listener;

        protected CountingSink countingSink;

        public CountingRequestBody(RequestBody delegate, Listener listener) {
            this.delegate = delegate;
            this.listener = listener;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            try {
                return delegate.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {

            countingSink = new CountingSink(sink);
            BufferedSink bufferedSink = Okio.buffer(countingSink);

            delegate.writeTo(bufferedSink);

            bufferedSink.flush();
        }

        protected final class CountingSink extends ForwardingSink {

            private long bytesWritten = 0;

            public CountingSink(Sink delegate) {
                super(delegate);
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                bytesWritten += byteCount;
                listener.onRequestProgress(bytesWritten, contentLength());
            }

        }


    }

    interface Listener {
        public void onRequestProgress(long bytesWritten, long contentLength);
    }


    public static class FileRequestBody extends RequestBody {
        private final RequestBody mRequestBody;
        private final ProgressCallback progressListener;


        FileRequestBody(RequestBody requestBody, ProgressCallback progressListener) {
            this.mRequestBody = requestBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return mRequestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return mRequestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (progressListener == null) {
                mRequestBody.writeTo(sink);
                return;
            }
            ProgressOutputStream progressOutputStream = new ProgressOutputStream(sink.outputStream(), progressListener, contentLength());
            BufferedSink progressSink = Okio.buffer(Okio.sink(progressOutputStream));
            mRequestBody.writeTo(progressSink);
            progressSink.flush();
        }

    }


    public static class ProgressOutputStream extends OutputStream {
        private final OutputStream stream;
        private final ProgressCallback listener;

        private long total;
        private long totalWritten;

        ProgressOutputStream(OutputStream stream, ProgressCallback listener, long total) {
            this.stream = stream;
            this.listener = listener;
            this.total = total;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            this.stream.write(b, off, len);
            if (this.total < 0) {
                this.listener.onProgressChanged(-1, -1, -1);
                return;
            }
            if (len < b.length) {
                this.totalWritten += len;
            } else {
                this.totalWritten += b.length;
            }
            this.listener.onProgressChanged(this.totalWritten, this.total, (this.totalWritten * 1.0F) / this.total);
        }

        @Override
        public void write(int b) throws IOException {
            this.stream.write(b);
            if (this.total < 0) {
                this.listener.onProgressChanged(-1, -1, -1);
                return;
            }
            this.totalWritten++;
            this.listener.onProgressChanged(this.totalWritten, this.total, (this.totalWritten * 1.0F) / this.total);
        }

        @Override
        public void close() throws IOException {
            if (this.stream != null) {
                this.stream.close();
            }
        }

        @Override
        public void flush() throws IOException {
            if (this.stream != null) {
                this.stream.flush();
            }
        }
    }


    public static void cacheResponse(Context context, String tag, String responseStr) {
        File file = new File(context.getFilesDir(), tag + ".txt");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(responseStr);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCacheResponse(Context context, String tag) {
        File file = new File(context.getFilesDir(), tag + ".txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String read;
            StringBuilder builder = new StringBuilder("");
            while ((read = bufferedReader.readLine()) != null) {
                builder.append(read);
            }
            bufferedReader.close();
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isOnline(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    interface ProgressCallback {
        void onProgressChanged(long numBytes, long totalBytes, float percent);
    }

    public interface ResponseListener {
        void onResponse(String tag, String response);
    }

    public interface ProgressListener {
        void onProgress(int progress);
    }

    public interface ErrorListener {
        void onError(String tag, String errorMsg);
    }

}