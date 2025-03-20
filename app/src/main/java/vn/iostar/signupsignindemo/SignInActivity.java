package vn.iostar.signupsignindemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iostar.signupsignindemo.API.AuthAPI;
import vn.iostar.signupsignindemo.API.RetrofitClient;
import vn.iostar.signupsignindemo.Model.ApiResponse;
import vn.iostar.signupsignindemo.Model.User;

public class SignInActivity extends AppCompatActivity {

    private AuthAPI authAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        //Ánh xạ
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPass = findViewById(R.id.txtPass);
        MaterialButton btnLogin = findViewById(R.id.btnSignIn);


        // Khởi tạo Retrofit để gọi API
        authAPI = RetrofitClient.getClient("http://10.0.2.2:8080/").create(AuthAPI.class);


        //Thực thi khi bấm nút đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String pass = txtPass.getText().toString().trim();
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    login(email, pass);
                }
            }
        });

        TextView textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void login(String email, String pass) {
        User user = new User();
        user.setPassword(pass);
        user.setEmail(email);

        authAPI.login(user).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignInActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    try {
                        // Đọc lỗi từ errorBody()
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Response error: " + errorBody);
                        Toast.makeText(SignInActivity.this, errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error reading error body", e);
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "Lỗi " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Lỗi", t.getMessage());
            }
        });
    }
}