package vn.iostar.signupsignindemo.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.iostar.signupsignindemo.Model.ForgetRequest;
import vn.iostar.signupsignindemo.Model.OTPRequest;
import vn.iostar.signupsignindemo.Model.User;

public interface AuthAPI {
    @POST("/api/auth/register")
    Call<String> register(@Body User user);

    @POST("/api/auth/login")
    Call<String> login(@Body User user);

    @POST("/api/auth/forget")
    Call<String> forget(@Body ForgetRequest request);

    @POST("/api/auth/active")
    Call<String> activate(@Body OTPRequest request);
}
