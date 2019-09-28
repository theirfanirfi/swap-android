package swap.irfanullah.com.swap.Libraries;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class VolleyLib {
    public static final String BASE_URL = "http://"+RetroLib.IP+"/swap/public/api/";
    //public static final String BASE_URL = "http://www.thelekkihub.com/swap/public/api/";
    public static final Boolean NOT_AUTH_LOG_IN = true;
    public static final String NOT_AUTH_MESSAGE = "You are not logged in. Please log in and try again.";
    public static final String IS_AUTHENTICATED = "isAuthenticated";


    public static void postRequest(final Context context, String URL, final Map<String,String> params, final VolleyListener volleyListener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    volleyListener.onRecieve(object);
                } catch (JSONException e) {
                    volleyListener.onException(e.toString());
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyListener.onError(error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(stringRequest);
    }


    public static void getRequest(final Context context, String URL, final VolleyListener volleyListener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL+URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    volleyListener.onRecieve(object);
                } catch (JSONException e) {
                    volleyListener.onException(e.toString());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyListener.onError(error.toString());
                    }
                }
        ){


        };

        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(stringRequest);
    }

    public static void getAuthRequest(final Context context, String URL, final VolleyAuthListener volleyAuthListener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL+URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean(IS_AUTHENTICATED))
                    {
                        volleyAuthListener.onRecieve(object);
                    }
                    else
                    {
                        volleyAuthListener.onNotAuth(NOT_AUTH_MESSAGE,NOT_AUTH_LOG_IN);
                    }

                } catch (JSONException e) {
                    volleyAuthListener.onException(e.toString());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyAuthListener.onError(error.toString());
                    }
                }
        ){


        };

        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(stringRequest);
    }

    public static void jsonRequest(final Context context, String URL, Map<String, String> params, final VolleyAuthListener volleyAuthListener)
    {
        RequestQueue rq = Volley.newRequestQueue(context);

        JSONObject object = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    volleyAuthListener.onRecieve(response);
                } catch (JSONException e) {
                   volleyAuthListener.onException(e.toString());
                   volleyAuthListener.onNotAuth("hello",true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            volleyAuthListener.onError(error.toString());
            }
        }
        );

        rq.getCache().clear();
        rq.add(jsonObjectRequest);
    }

    public static String encode(String data) throws UnsupportedEncodingException {
        byte[] encoded = Base64.encode(data.getBytes("UTF-8"),Base64.DEFAULT);
       String str = new String(encoded,"UTF-8");
       return str;
    }

    public interface VolleyListener {
       // void onRecieve(String data);
        void onRecieve(JSONObject object) throws JSONException;
        void onException(String exception);
        void onError(String volleyError);
    }

    public interface VolleyAuthListener
    {
        void onRecieve(JSONObject object) throws JSONException;
        void onNotAuth(String message,Boolean auth);
        void onException(String exception);
        void onError(String volleyError);
    }
}
