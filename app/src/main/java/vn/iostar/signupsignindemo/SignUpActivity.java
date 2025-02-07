package vn.iostar.signupsignindemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iostar.signupsignindemo.API.AuthAPI;
import vn.iostar.signupsignindemo.API.RetrofitClient;
import vn.iostar.signupsignindemo.Model.ApiResponse;
import vn.iostar.signupsignindemo.Model.User;

public class SignUpActivity extends AppCompatActivity {

    private AuthAPI authAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Ánh xạ các view
        EditText textEmail = findViewById(R.id.textEmail);
        EditText passWord = findViewById(R.id.textPass);
        EditText passWordAgain = findViewById(R.id.textPassAgain);
        MaterialButton btnSignUp = findViewById(R.id.btnSignUp);
        TextView textViewSignIn = findViewById(R.id.textViewSignIn);

        // Thiết lập xử lý sự kiện click vào "Đăng nhập" để chuyển qua màn hình SignIn
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        // Khởi tạo Retrofit để gọi API
            authAPI = RetrofitClient.getClient("http://10.0.2.2:8080") // Thay bằng URL API thật
                    .create(AuthAPI.class);

        // Xử lý sự kiện khi người dùng nhấn nút đăng ký
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textEmail.getText().toString();
                String pass = passWord.getText().toString();
                String passAgain = passWordAgain.getText().toString();



                // Kiểm tra mật khẩu có khớp hay không
                if (!pass.equals(passAgain)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu nhập không khớp nhau", Toast.LENGTH_SHORT).show();
                } else {
                    // Gọi hàm đăng ký người dùng
                    registerUser(email, pass);
                }
            }
        });

        // Ánh xạ padding cho hệ thống thanh điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser(String email, String pass) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(pass);

        authAPI.register(user).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    try {
                        // Đọc lỗi từ errorBody()
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Response error: " + errorBody);
                        Toast.makeText(SignUpActivity.this, errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Lỗi " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Lỗi", t.getMessage());
            }
        });
    }
}
