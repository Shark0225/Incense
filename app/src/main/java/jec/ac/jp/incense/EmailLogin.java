package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmailLogin extends AppCompatActivity {

    private static final String TAG = "EmailLogin";
    private FirebaseAuth firebaseAuth;
    private EditText edtEmail, edtPassword;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(EmailLogin.this, "メールとパスワード入力してください", Toast.LENGTH_SHORT).show();
            } else {
                loginWithEmail(email, password);
            }
        });
    }
        private void loginWithEmail (String email, String password){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(EmailLogin.this, "登录成功", Toast.LENGTH_SHORT).show();
                                navigateToUserScreen(user);
                            }
                        } else {

                            Toast.makeText(EmailLogin.this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Login failed", task.getException());
                        }
                    });
        }
    private void saveUserToFirestore(FirebaseUser user) {
        if (user != null) {

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", user.getDisplayName());
            userData.put("email", user.getEmail());
            userData.put("uid", user.getUid());

            firestore.collection("users")
                    .document(user.getUid())
                    .set(userData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "User data saved to Firestore"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error saving user data", e));
        }
    }


        private void navigateToUserScreen(FirebaseUser user) {
            if (user != null) {
                Intent intent = new Intent(EmailLogin.this, User.class);
                intent.putExtra("USER_NAME", user.getDisplayName());
                intent.putExtra("USER_EMAIL", user.getEmail());
                startActivity(intent);
                finish();
            }
        }
    }
