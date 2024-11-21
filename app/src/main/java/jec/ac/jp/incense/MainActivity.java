package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        // 设置主布局
        setContentView(R.layout.activity_main);

        // 处理系统栏的内边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 按钮对应的文本信息数组
        int[] buttonIds = {
                R.id.imgbutton1, R.id.imgbutton2, R.id.imgbutton3,
                R.id.imgbutton4, R.id.imgbutton5, R.id.imgbutton6,
                R.id.imgbutton7, R.id.imgbutton8, R.id.imgbutton9
        };

        String[] texts = {
                "◆大江戸香　ブランドコンセプト◆\n" +
                        "「雅」と「俗」の文化交流が作りあげた、遊びと洒落の江戸文化。\n" +
                        "四季を楽しみ、えにしを尊び、人々がいきいきと暮らしていた江戸の町。\n" +
                        "江戸の匂い立つような暮らしぶりを、ここ江戸は東京の香司が香りで表現。\n" +
                        "ひとすじのけむりが、粋でモダンな江戸の世界へとご招待。\n" +
                        "さあさあ、江戸の香りの世界へようこそ。\n" +
                        "\n" +
                        "◆大江戸香　伽羅姫　香りの特徴◆\n" +
                        "江戸の憧れ。粋な女性の嗜み。深く味わいのある伽羅をイメージした香り。\n" +
                        "公家から大名などの富裕層へも広がりをみせた「香道」。\n" +
                        "庶民の間でも香木への想いは強く、歌舞伎や浄瑠璃の演目に艶やかに登場。\n" +
                        "さらに「伽羅の油」という生薬で香り付けした鬢付油も大ヒット。\n" +
                        "「伽羅」という言葉は”すてきなもの”を形容する表現にも使われました。\n" +
                        "そんな江戸の人々にとって憧れの的だった「伽羅」をイメージした香りです。\n" +
                        "\n" +
                        "パッケージの裏には浮世絵が描かれています。\n" +
                        "また、江戸の石畳を模した香立てもついています。\n" +
                        "江戸で位の高い人たちに人気だったといわれる錫製の香立ては、水洗いも可能で、さびにくく長くお使いいただけます。英語による説明も表記されておりますので、海外へのお土産にもおすすめです。",
                "按钮2对应的文本信息", "按钮3对应的文本信息", "按钮4对应的文本信息",
                "按钮5对应的文本信息", "按钮6对应的文本信息", "按钮7对应的文本信息",
                "按钮8对应的文本信息", "按钮9对应的文本信息"
        };

        int[] imageResIds = {
                R.drawable.senkou1, R.drawable.senkou2, R.drawable.senkou3,
                R.drawable.senkou4, R.drawable.senkou5, R.drawable.senkou6,
                R.drawable.senkou7, R.drawable.senkou8, R.drawable.senkou9
        };

        for (int i = 0; i < buttonIds.length; i++) {
            ImageButton button = findViewById(buttonIds[i]);
            String text = texts[i];
            int imageResId = imageResIds[i]; // 动态获取对应的图片资源 ID
            button.setOnClickListener(v -> navigateToMinute(text, imageResId));
        }

    }

    /**
     * 跳转到 MinuteActivity 的方法
     *
     * @param text      传递的文本内容
     * @param imageResId 图片资源 ID
     */
    private void navigateToMinute(String text, int imageResId) {
        Intent intent = new Intent(this, MinuteActivity.class);
        intent.putExtra("EXTRA_TEXT", text);
        intent.putExtra("EXTRA_IMAGE", imageResId);
        startActivity(intent);
    }
}
