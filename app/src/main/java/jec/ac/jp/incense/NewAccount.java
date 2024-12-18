package jec.ac.jp.incense;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class NewAccount extends AppCompatActivity {

    private static final String TAG = "NewAccount";
    private FirebaseAuth firebaseAuth;
    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);


        firebaseAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.etEmail);
        edtPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(NewAccount.this, "メールとパスワードを入力してください", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, password);
            }
        });
    }


    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {


                        Toast.makeText(NewAccount.this, "登録成功", Toast.LENGTH_SHORT).show();

                        navigateToUserScreen();
                    } else {

                        Toast.makeText(NewAccount.this, "登録に失敗しました。再試行してください", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Registration failed", task.getException());
                    }
                });
    }

    // 跳转到用户界面
    private void navigateToUserScreen() {


        finish();
    }
}
