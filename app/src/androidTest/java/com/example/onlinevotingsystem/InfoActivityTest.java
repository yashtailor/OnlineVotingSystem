package com.example.onlinevotingsystem;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class InfoActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    public LoginActivity loginActivity;
    @Before
    public void setUp() throws Exception {
        loginActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLogin(){
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                EditText email = loginActivity.findViewById(R.id.email);
                EditText pass = loginActivity.findViewById(R.id.password);
                email.setText("yashtailor2000@gmail.com");
                pass.setText("24122000");
                Button loginBtn = loginActivity.findViewById(R.id.login);
                loginBtn.performClick();
                assertTrue(loginActivity.isCurUserLoggedIn());
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }
}