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

public class RegisterActivityTest {
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    public RegisterActivity RegisterActivity;
    @Before
    public void setUp() throws Exception {
        RegisterActivity = mActivityTestRule.getActivity();
        RegisterActivity.logout();
    }

    @Test
    public void testRegister() throws InterruptedException {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                EditText email = RegisterActivity.findViewById(R.id.email);
                EditText pass = RegisterActivity.findViewById(R.id.passwd);
                email.setText("yash2000@gmail.com");
                pass.setText("24122000");
                Button registerBtn = RegisterActivity.findViewById(R.id.btnregister);
                registerBtn.performClick();
                assertFalse(RegisterActivity.isCurUserLoggedIn());
                RegisterActivity = null;
            }
        });
    }

    @Test
    public void testRegister2() throws InterruptedException {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RegisterActivity = mActivityTestRule.getActivity();
                EditText email = RegisterActivity.findViewById(R.id.email);
                EditText pass = RegisterActivity.findViewById(R.id.passwd);
                email.setText("yash2009@gmail.com");
                pass.setText("2412");
                Button registerBtn = RegisterActivity.findViewById(R.id.btnregister);
                registerBtn.performClick();
                assertFalse(RegisterActivity.isCurUserLoggedIn());
                RegisterActivity = null;
            }
        });
    }

    @Test
    public void testRegister3(){
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                RegisterActivity = mActivityTestRule.getActivity();
                EditText email = RegisterActivity.findViewById(R.id.email);
                EditText pass = RegisterActivity.findViewById(R.id.passwd);
                email.setText("yashtailor2009@gmail.com");
                pass.setText("24122000");
                Button registerBtn = RegisterActivity.findViewById(R.id.btnregister);
                registerBtn.performClick();
                waitForSomeTime();
            }
        });
    }

    public void waitForSomeTime(){
        for(int i=0;i<10000000;i++){
            if(i==10000000){
                assertTrue(RegisterActivity.isCurUserLoggedIn());
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        RegisterActivity = null;
    }
}