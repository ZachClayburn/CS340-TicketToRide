package com.tickettoride.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tickettoride.R;
import com.tickettoride.facadeProxies.SessionFacadeProxy;
import com.tickettoride.facadeProxies.UserFacadeProxy;

import com.tickettoride.models.Password;
import com.tickettoride.models.Username;

public class LoginActivity extends MyBaseActivity {
    private Username user = new Username("");
    private Password pass = new Password("");
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private Context context;
    private ProgressBar loadingSpinner;
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
        context = this;
        loadingSpinner=(ProgressBar)findViewById(R.id.login_progressBar);
        loadingSpinner.setVisibility(View.GONE);
        
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
        login = findViewById(R.id.sign_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SessionFacadeProxy.SINGLETON.create(user, pass);
                    loadingSpinner.setVisibility(View.VISIBLE);
                } catch (Throwable t) { }
            }
        });
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("onClick", "Caught in Register Click Event");
                try {
                    UserFacadeProxy.SINGLETON.create(user, pass);
                    loadingSpinner.setVisibility(View.VISIBLE);
                } catch (Throwable t) { }

            }
        });

    }
    
    @Override
    public void onResume(){
        super.onResume();
        loadingSpinner.setVisibility(View.GONE);
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

    @Override
    public void moveToJoin() {
        Intent intent = new Intent(LoginActivity.this, JoinGameActivity.class);
        startActivity(intent);
    }
    public Runnable loginErrorRunnable = new Runnable() {
        @Override
        public void run() {
            loadingSpinner.setVisibility(View.GONE);
            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show();
        }
    };

    public Runnable createErrorRunnable = new Runnable() {
        @Override
        public void run() {
            loadingSpinner.setVisibility(View.GONE);
            Toast.makeText(context, R.string.register_error, Toast.LENGTH_SHORT).show();
        }
    };

    public void loginError() {
        runOnUiThread(loginErrorRunnable);
    }

    public void createError() {
        runOnUiThread(createErrorRunnable);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
