package vn.iostar.signupsignindemo.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) // Đổi thành URL server của bạn
                    .addConverterFactory(GsonConverterFactory.create()) // Chuyển đổi JSON tự động
                    .build();
        }
        return retrofit;
    }
}
