package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

        // 如果用户已经登录，直接跳转到 User 界面
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToUserScreen(currentUser);
            return;
        }

        setupGoogleSignIn();

        findViewById(R.id.btnGoogleLogin).setOnClickListener(v -> startGoogleSignIn());
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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Log.d(TAG, "登录成功，账户: " + account.getEmail());
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                                navigateToUserScreen(user);
                            } else {
                                Log.e(TAG, "Firebase 登录失败", task.getException());
                                Toast.makeText(this, "Firebase 登录失败，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google 登录失败", e);
            Toast.makeText(this, "Google 登录失败，请重试", Toast.LENGTH_SHORT).show();
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
