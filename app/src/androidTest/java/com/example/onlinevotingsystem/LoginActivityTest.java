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

import java.util.concurrent.TimeUnit;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    public LoginActivity loginActivity;
    @Before
    public void setUp() throws Exception {
        loginActivity = mActivityTestRule.getActivity();
        loginActivity.logout();
    }

    @Test
    public void testLogin() throws InterruptedException {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                EditText email = loginActivity.findViewById(R.id.emailLogin);
                EditText pass = loginActivity.findViewById(R.id.password);
                email.setText("yash2000@gmail.com");
                pass.setText("241220");
                Button loginBtn = loginActivity.findViewById(R.id.login);
                loginBtn.performClick();
                assertFalse(loginActivity.isCurUserLoggedIn());
                loginActivity = null;
            }
        });
    }

    @Test
    public void testLogin2() throws InterruptedException {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                loginActivity = mActivityTestRule.getActivity();
                EditText email = loginActivity.findViewById(R.id.emailLogin);
                EditText pass = loginActivity.findViewById(R.id.password);
                email.setText("yash2000@gmail.com");
                pass.setText("2412");
                Button loginBtn = loginActivity.findViewById(R.id.login);
                loginBtn.performClick();
                assertFalse(loginActivity.isCurUserLoggedIn());
                loginActivity = null;
            }
        });
    }

    @Test
    public void testLogin3(){
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                loginActivity = mActivityTestRule.getActivity();
                EditText email = loginActivity.findViewById(R.id.emailLogin);
                EditText pass = loginActivity.findViewById(R.id.password);
                email.setText("yashtailor2000@gmail.com");
                pass.setText("24122000");
                Button loginBtn = loginActivity.findViewById(R.id.login);
                loginBtn.performClick();
                waitForSomeTime();
            }
        });
    }

    public void waitForSomeTime(){
        for(int i=0;i<10000000;i++){
            if(i==10000000){
                assertTrue(loginActivity.isCurUserLoggedIn());
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }
}