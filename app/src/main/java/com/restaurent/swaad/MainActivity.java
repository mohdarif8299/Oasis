package com.restaurent.swaad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView later_log;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private boolean withAPICalled = false;
    private LinearLayout googleSignin;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }
        later_log = findViewById(R.id.later_log);
        googleSignin = findViewById(R.id.google_login);
        googleSignin.setOnClickListener(view->{
            signIn();
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
       later_log.setOnClickListener(view->  startActivity(new Intent(getApplicationContext(),HomeActivity.class)));
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(withAPICalled==false )  mCallbackManager.onActivityResult(requestCode, resultCode, data);
       else {
           if (requestCode == RC_SIGN_IN) {
               Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
               try {
                   GoogleSignInAccount account = task.getResult(ApiException.class);
                   firebaseAuthWithGoogle(account);
               } catch (ApiException e) {
                   e.printStackTrace();
               }
           }
      }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,task->{
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserAccount userAccount = new UserAccount(user.getEmail(),user.getPhoneNumber(),user.getDisplayName());
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user.getUid()).push();
                        databaseReference.setValue(userAccount);
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task1->{
                    if (task1.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (Objects.equals(FirebaseAuth.getInstance().getUid(),user.getUid()))
                        {
                        }
                        else {
                            UserAccount userAccount = new UserAccount(user.getEmail(),user.getPhoneNumber(),user.getDisplayName());
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user.getUid());
                            databaseReference.setValue(userAccount);
                        }
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();
                }
                     else {
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
