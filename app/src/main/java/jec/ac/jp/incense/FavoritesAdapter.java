package jec.ac.jp.incense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<FavoriteItem> favoriteItems;
    private Context context;

    // 构造器接收上下文和数据列表
    public FavoritesAdapter(Context context, List<FavoriteItem> favoriteItems) {
        this.context = context;
        this.favoriteItems = favoriteItems;
    }

    // 创建视图持有者
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 为每个列表项加载布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    // 绑定数据到视图
    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteItem item = favoriteItems.get(position);
        holder.favoriteImage.setImageResource(item.getImageResId());  // 设置图片
        holder.favoriteTitle.setText(item.getTitle());  // 设置标题

        // 处理按钮点击事件，示例为收藏按钮
        holder.favoriteButton.setOnClickListener(v -> {
            // 在这里可以添加你的收藏或取消收藏操作
            // 比如改变按钮图标，更新数据库等
        });
    }

    // 返回列表项数量
    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    // 视图持有者类，用来缓存视图项的子视图
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ImageView favoriteImage;
        TextView favoriteTitle;
        ImageButton favoriteButton;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            favoriteImage = itemView.findViewById(R.id.favoriteImage);
            favoriteTitle = itemView.findViewById(R.id.favoriteTitle);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
}

