package com.markjmind.jwtools.net;

import android.util.Log;

import com.markjmind.jwtools.log.Loger;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by codemasta on 2015-09-14.
 */
public class OkWeb{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private HashMap<String, String> param;
    private HashMap<String, String> header;
    private String host;
    private String uri;
    private String paramString;
    protected boolean debug = false;
    private Call call;


    public OkWeb(String host) {
        client = new OkHttpClient();
        header = new HashMap<>();
        param = new HashMap<>();
        uri = "";
        paramString = "";
        this.setHost(host);
    }

    public OkWeb setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public String getHost() {
        return host;
    }

    protected <ResultType extends ResultAdapter> ResultType getResult(Response response, Class<ResultType> type) throws IOException {
        try {
            Constructor<ResultType> constructor = type.getConstructor();
            ResultType obj = constructor.newInstance();
            obj.response = response;
            obj.bodyString = response.body().string();
            return obj;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(type.getName()+" class does not implement Constructor(Response)", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(type.getName()+" class does not implement Constructor InvocationTargetException", e);
        } catch (InstantiationException e) {
            throw new RuntimeException(type.getName()+" class can not make instance Constructor(Response)", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(type.getName()+" class illegalAccess Constructor(Response)", e);
        }

    }

    public <ResultType extends ResultAdapter>ResultType get(Class<ResultType> resultType) throws IOException, WebException {
        HttpUrl.Builder builder = HttpUrl.parse(new URL(new URL(host), uri).toString()+ paramString).newBuilder();
        String[] keys = getParamKeys();
        for (String key : keys) {
            builder.addEncodedQueryParameter(key, param.get(key));
        }
        HttpUrl httpUrl = builder.build();

        Request.Builder reqestBuilder = new Request.Builder().url(httpUrl);
        String[] headerKeys = getHeaderKeys();
        for (String key : headerKeys) {
            reqestBuilder.addHeader(key, header.get(key));
        }
        Request request = reqestBuilder.build();
        debugRequest("GET", paramString);

        call = client.newCall(request);
        Response response = call.execute();
        ResultType result = getResult(response, resultType);
        debugResponse(result.getBodyString(), response);
        unexpectedCode(response);
        return result;
    }

    public <ResultType extends ResultAdapter>ResultType form(Class<ResultType> resultType) throws WebException, IOException {

        Request.Builder reqestBuilder = new Request.Builder()
                .url(new URL(new URL(host), uri).toString()+ paramString)
                .cacheControl(new CacheControl.Builder().noCache().build());

        FormEncodingBuilder body = new FormEncodingBuilder();
        String[] keys = getParamKeys();
        for (String key : keys) {
            body.add(key, param.get(key));
        }

        String[] headerKeys = getHeaderKeys();
        for (String key : headerKeys) {
            reqestBuilder.addHeader(key, header.get(key));
        }

        Request request = reqestBuilder
                .post(body.build())
                .build();
        debugRequest("FORM", paramString);

        call = client.newCall(request);
        Response response = call.execute();
        ResultType result = getResult(response, resultType);
        debugResponse(result.getBodyString(), response);
        unexpectedCode(response);
        return result;
    }



    public <ResultType extends ResultAdapter>ResultType post(String text, Class<ResultType> resultType) throws IOException, WebException {

        Request.Builder reqestBuilder = new Request.Builder();
        String[] headerKeys = getHeaderKeys();
        for (String key : headerKeys) {
            reqestBuilder.addHeader(key, header.get(key));
        }

        RequestBody body = RequestBody.create(JSON, text);
        Request request = reqestBuilder.url(new URL(new URL(host), uri).toString() + paramString)
                .post(body)
                .build();
        debugRequest("POST", text);

        call = client.newCall(request);
        Response response = call.execute();
        ResultType result = getResult(response, resultType);
        debugResponse(result.getBodyString(), response);
        unexpectedCode(response);

        return result;
    }


    public String[] getHeaderKeys() {
        if (header.size() == 0) {
            return new String[0];
        }
        String keys[] = header.keySet().toArray(new String[0]);
        if (keys == null) {
            return new String[0];
        }
        return keys;
    }

    public static String sha1(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return "0x" + hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected void debugRequest(String method, String text) throws MalformedURLException {
        if (debug) {
            Log.i(this.getClass().getSimpleName(), " ");
            Log.w(this.getClass().getSimpleName(), "△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△△");
            Log.d(this.getClass().getSimpleName(), "★ API Call : " + Loger.callLibrary(this.getClass())+" ★");
            Log.i(this.getClass().getSimpleName(), "○ [Request] " + method + " : " + new URL(new URL(host), uri).toString());
            String[] headerKeys = getHeaderKeys();
            if (headerKeys.length > 0) {
                Log.i(this.getClass().getSimpleName(), "- Header");
                for (String key : headerKeys) {
                    Log.d(this.getClass().getSimpleName(), "" + key + ":" + header.get(key));
                }
            }

            if (text != null && !"".equals(text)) {
                Log.i(this.getClass().getSimpleName(), "- Text");
                Log.d(this.getClass().getSimpleName(), text);
            }

            String[] paramKeys = getParamKeys();
            if (paramKeys.length > 0) {
                Log.i(this.getClass().getSimpleName(), "- Parameter");
                for (int i = paramKeys.length-1; i>=0; i--) {
                    String key = paramKeys[i];
                    Log.d(this.getClass().getSimpleName(), key + ":" + param.get(key));
                }
            }
        }

    }

    protected void debugResponse(String bodyResult, Response response) {
        if (debug) {
            Log.i(this.getClass().getSimpleName(), "● [Response] Code : " + response.code());
            Log.i(this.getClass().getSimpleName(), "- body");
            Log.d(this.getClass().getSimpleName(), bodyResult.replaceAll("\\r", ""));
            Log.i(this.getClass().getSimpleName(), " ");
        }
    }

    private void unexpectedCode(Response response) throws WebException {
        if (!response.isSuccessful()) {
            Log.e(this.getClass().getSimpleName(), "Server Response Error Unexpected code:" + response.code());
            Log.e(this.getClass().getSimpleName(), response.message());
            throw new WebException("Unexpected code " + response);
        }
    }

    public OkWeb setHost(String host) {
        this.host = host;
        return this;
    }

    public OkWeb addUri(String uri){
        if(uri!=null) {
            this.uri = uri;
        }
        return this;
    }

    public OkWeb clearHeader() {
        header.clear();
        return this;
    }

    public OkWeb addHeader(String key, String value) {
        header.put(key, value);
        return this;
    }

    public OkWeb removeHeader(String key){
        header.remove(key);
        return this;
    }


    public OkWeb clearParam() {
        param.clear();
        return this;
    }

    public OkWeb addParam(String key, String value) {
        param.put(key, value);
        return this;
    }

    public OkWeb removeParam(String key){
        param.remove(key);
        return this;
    }

    public String[] getParamKeys() {
        if (param.size() == 0) {
            return new String[0];
        }
        String keys[] = param.keySet().toArray(new String[0]);
        if (keys == null) {
            return new String[0];
        }
        return keys;
    }

    public OkWeb addParamString(String paramString){
        if (paramString == null) {
            paramString = "";
        } else {
            paramString = "/?" + paramString;
        }
        this.paramString = paramString;
        return this;
    }

    public OkWeb ignoreVerify() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // SSL 컨텍스트
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // SSL 소켓 생성
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            client.setSslSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return this;
    }

    public void cancel(){
        if(call!=null && !call.isCanceled()){
            call.cancel();
        }
    }

    public OkHttpClient getOkHttpClient(){
        return this.client;
    }
}

