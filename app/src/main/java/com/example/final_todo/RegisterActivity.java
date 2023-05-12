package com.example.final_todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.final_todo.model.User;
import com.example.final_todo.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    UserViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new UserViewModel(getApplication());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Button btnLogin = findViewById(R.id.register_activity_login_btn);
        Button btnRegister = findViewById(R.id.register_activity_register_btn);

        EditText txtUserName = findViewById(R.id.register_activity_username);
        EditText txtPassword = findViewById(R.id.register_activity_password);
        EditText txtConfirmPassword = findViewById(R.id.register_activity_confirm_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = txtUserName.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirm = txtConfirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this, "Username Cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Password Cannot be Empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirm)) {
                    Toast.makeText(RegisterActivity.this, "Entered passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                viewModel.getUserByName(username).observe(RegisterActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user != null) {
                            Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            User newUser = new User();
                            newUser.setUserName(username);
                            newUser.setPassword(password);
                            viewModel.saveUser(newUser);
                            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor spEditor = sharedPreferences.edit();
                            spEditor.putString("loginToken", "Loggedin");
                            spEditor.commit();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}