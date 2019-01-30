package com.tickettoride.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tickettoride.R;

import modelAttributes.Password;
import modelAttributes.Username;

public class LoginActivity extends AppCompatActivity {
    private Username user = new Username();
    private Password pass = new Password();
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = findViewById(R.id.username_title);
        password = findViewById(R.id.password_title);
        login = findViewById(R.id.sign_in);
        register = findViewById(R.id.register);
        login.setEnabled(false);
        register.setEnabled(false);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //login button listener
                user.setUsername(s.toString());
                //checks if user entered fields
                setEnabled();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //password field listener
                pass.setPassword(s.toString());
                //checks if user filled fields
                setEnabled();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        login = (Button) findViewById(R.id.sign_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement web socket for login when ready
                /*Intent intent = new Intent(LoginActivity.this, JoinGameActivity.class);
                startActivity(intent);*/


            }
        });
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement web socket for register when ready
                /*Intent intent = new Intent(LoginActivity.this, JoinGameActivity.class);
                startActivity(intent);*/
            }
        });

    }
    public void setEnabled() {
        //if username and password fields have characters, login and register buttons are enabled
        if (!pass.getPassword().equals("") && !user.getUsername().equals("")) {
            login.setEnabled(true);
            register.setEnabled(true);
        }
        else {
            login.setEnabled(false);
            register.setEnabled(false);
        }
    }
}
