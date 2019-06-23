package swap.irfanullah.com.swap.Libraries;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swap.irfanullah.com.swap.APIs.ApiService;


public class RetroLib {
    public static final String BASE_URL = "http://169.254.28.88/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }
        else
        {
            return retrofit;
        }
    }

    public static ApiService geApiService()
    {
        return getRetrofit().create(ApiService.class);
    }

    public static void toastHere(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

}
