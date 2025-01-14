package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Account extends AppCompatActivity {

    private static final String TAG = "GoogleSignIn";
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        firebaseAuth = FirebaseAuth.getInstance();
        Button backToMainButton = findViewById(R.id.btn_back);
        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToUserScreen(currentUser);
            return;
        }

        setupGoogleSignIn();

        Button emailLoginButton = findViewById(R.id.btnEmailLogin);
        emailLoginButton.setOnClickListener(v -> startEmailLogin());

        findViewById(R.id.btnGoogleLogin).setOnClickListener(v -> startGoogleSignIn());

        Button newAccountButton = findViewById(R.id.btnNewAccount);
        newAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(Account.this, NewAccount.class);
            startActivity(intent);
        });
        Button btnForgotPassword = findViewById(R.id.btnPassword);
        btnForgotPassword.setOnClickListener(v -> {
            String email = "user@example.com";
            resetPassword(email);
        });


    }


    private void startEmailLogin() {
        Toast.makeText(this, "メールログインが選択されました", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Account.this, EmailLogin.class);
        startActivity(intent);
    }





    @Override


    public void onBackPressed() {

        super.onBackPressed();

        Intent intent = new Intent(Account.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }
    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void startGoogleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Account.this, "パスワードリセットのメールが送信されました。", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Account.this, "エラーが発生しました。再試行してください", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "登録成功 " + account.getEmail());
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Toast.makeText(this, "登録成功", Toast.LENGTH_SHORT).show();
                                navigateToUserScreen(user);
                            } else {
                                Log.e(TAG, "Firebase 登录失败", task.getException());
                                Toast.makeText(this, "Firebase 登录失败，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google 登録できませんでした", e);
            Toast.makeText(this, "Google 登録失敗もう一度登録してください", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToUserScreen(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Account.this, User.class);
            intent.putExtra("USER_NAME", user.getDisplayName());
            intent.putExtra("USER_EMAIL", user.getEmail());
            startActivity(intent);
            finish();
        }
    }

}
