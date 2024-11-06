package com.example.fbuddybluetooth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper();
        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());
    }
    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new Thread(() -> {
            boolean success = dbHelper.login(email, password);
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
