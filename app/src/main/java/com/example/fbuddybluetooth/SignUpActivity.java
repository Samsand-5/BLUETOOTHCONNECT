package com.example.fbuddybluetooth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DatabaseHelper();
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> signUpUser());
    }
    private void signUpUser() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new Thread(() -> {
            boolean success = dbHelper.signUp(username, email, password);
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sign-Up Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
