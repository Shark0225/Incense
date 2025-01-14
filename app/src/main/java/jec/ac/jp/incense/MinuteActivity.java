package jec.ac.jp.incense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MinuteActivity extends AppCompatActivity {

    private FirebaseFirestore db; // Firestore 實例
    private boolean isFavorite = false; // 收藏狀態
    private String userId = "user_12345"; // 假設的用戶 ID
    private String productId = "product_56789"; // 假設的商品 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minute);

        // 初始化 Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // 初始化元件
        ImageButton favoriteButton = findViewById(R.id.favoriteButton);
        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);
        ScrollView scrollView = findViewById(R.id.scrollView);
        Button openLinkButton = findViewById(R.id.openLinkButton);
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnApp = findViewById(R.id.btn_app);
        ImageButton btnUser = findViewById(R.id.btn_user);

        // 獲取傳遞的數據
        String text = getIntent().getStringExtra("EXTRA_TEXT");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_image);
        String url = getIntent().getStringExtra("EXTRA_URL");

        // 設置文本和圖片
        textView.setText(text);
        imageView.setImageResource(imageResId);

        // 初始化收藏狀態
        checkFavoriteStatus(favoriteButton);

        // 收藏按鈕點擊事件
        favoriteButton.setOnClickListener(v -> toggleFavoriteStatus(favoriteButton));

        // 滾動事件：滾到底部時顯示按鈕
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (!scrollView.canScrollVertically(1)) {
                openLinkButton.setVisibility(View.VISIBLE);
            }
        });

        // 打開鏈接按鈕點擊事件
        if (url != null && !url.isEmpty()) {
            openLinkButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }

        // 底部導航按鈕事件
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        btnApp.setOnClickListener(v -> {
            // App 按鈕功能
        });
        btnUser.setOnClickListener(v -> {
            // User 按鈕功能
        });
    }

    /**
     * 檢查收藏狀態
     */
    private void checkFavoriteStatus(ImageButton favoriteButton) {
        db.collection("favorites")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("product_id", productId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    isFavorite = !queryDocumentSnapshots.isEmpty(); // 如果有結果，說明已收藏
                    updateFavoriteButton(favoriteButton);
                });
    }

    /**
     * 切換收藏狀態
     */
    private void toggleFavoriteStatus(ImageButton favoriteButton) {
        if (isFavorite) {
            // 如果已收藏，從 Firestore 中刪除
            db.collection("favorites")
                    .whereEqualTo("user_id", userId)
                    .whereEqualTo("product_id", productId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
                        }
                        isFavorite = false;
                        updateFavoriteButton(favoriteButton);
                    });
        } else {
            // 如果未收藏，添加到 Firestore
            db.collection("favorites").add(new Favorite(userId, productId))
                    .addOnSuccessListener(documentReference -> {
                        isFavorite = true;
                        updateFavoriteButton(favoriteButton);
                    });
        }
    }

    /**
     * 更新收藏按鈕圖標
     */
    private void updateFavoriteButton(ImageButton favoriteButton) {
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    /**
     * 收藏數據類
     */
    public static class Favorite {
        private String user_id;
        private String product_id;

        public Favorite(String user_id, String product_id) {
            this.user_id = user_id;
            this.product_id = product_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getProduct_id() {
            return product_id;
        }
    }
}
