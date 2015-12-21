package com.markjmind.jwtools.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by markj on 2015-12-04.
 */
public class Result extends ResultAdapter{

    public Result(){

    }

    public JSONObject getJSON(String... deps_jsonKey) throws JSONException {
        if(deps_jsonKey==null || deps_jsonKey.length==0){
            return new JSONObject(getBodyString());
        }else{
            JSONObject json = new JSONObject(getBodyString());
            for(String key:deps_jsonKey){
                json = json.getJSONObject(key);
            }
            return json;
        }
    }

    public JSONArray getJSONArray(String... deps_jsonKey) throws JSONException {
        if(deps_jsonKey==null || deps_jsonKey.length==0){
            return new JSONArray(getBodyString());
        }else{
            JSONObject json = new JSONObject(getBodyString());
            for(int i=0;i<deps_jsonKey.length-1;i++){
                json = json.getJSONObject(deps_jsonKey[i]);
            }
            return json.getJSONArray(deps_jsonKey[deps_jsonKey.length-1]);
        }
    }

   public <Dto>Dto fromJson(Class<Dto> dtoClass, String... deps) throws JSONException {
        Gson gson = getCustomGson();
        Dto result = (Dto) gson.fromJson(getJSON(deps).toString(), dtoClass);
        return result;
    }

    public <Dto>Dto fromJson(Class<Dto> dtoClass, JSONObject json){
        Gson gson = getCustomGson();
        Dto result = (Dto) gson.fromJson(json.toString(), dtoClass);
        return result;
    }

    public <Dto>Dto fromJson(TypeToken<?> typeToken, String... deps) throws JSONException {
        Gson gson = getCustomGson();
        Dto result = (Dto) gson.fromJson(getJSON(deps).toString(), typeToken.getType());
        return result;
    }

    public Gson getCustomGson(){
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        return builder.create();
    }

}
