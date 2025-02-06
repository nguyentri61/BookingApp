package vn.iostar.signupsignindemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iostar.signupsignindemo.API.AuthAPI;
import vn.iostar.signupsignindemo.API.RetrofitClient;
import vn.iostar.signupsignindemo.Model.User;

public class SignUpActivity extends AppCompatActivity {

    //ánh xạ
    TextView textEmail = findViewById(R.id.textEmail);
    TextView passWord = findViewById(R.id.textPass);
    TextView passWordAgain = findViewById(R.id.textPassAgain);
    Button btnSignUp = findViewById(R.id.btnSignUp);
    private AuthAPI authAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Retrofit
        authAPI = RetrofitClient.getClient("http://localhost:8080") // Thay bằng URL API thật
                .create(AuthAPI.class);
        TextView textViewSignIn = findViewById(R.id.textViewSignIn);
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        //Nút đăng ký
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textEmail.getText().toString();
                String pass = passWord.getText().toString();
                if(!pass.equals(passWordAgain.getText().toString()))
                {
                    Toast.makeText(SignUpActivity.this,"Mật khẩu nhập không khớp nhau",Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(email, pass);
                }
            }
        });

    }

    private void registerUser(String email, String pass) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(pass);

        authAPI.register(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công: " + response.body(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Lỗi " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}