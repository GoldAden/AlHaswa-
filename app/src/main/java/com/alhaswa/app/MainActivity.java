package com.alhaswa.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // ألوان الواجهة
    int navy = Color.rgb(8, 38, 58);
    int blue = Color.rgb(12, 104, 145);
    int cyan = Color.rgb(33, 177, 201);
    int white = Color.WHITE;
    int textDark = Color.rgb(20, 45, 60);
    int gray = Color.rgb(100, 120, 130);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // الخلفية الرئيسية
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        root.setBackgroundColor(Color.rgb(241, 247, 249));
        root.setPadding(18, 18, 18, 18);

        // =========================
        // الرأس
        // =========================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(15, 18, 15, 18);

        GradientDrawable headerBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[] {
                        Color.rgb(5, 54, 82),
                        Color.rgb(10, 121, 151)
                }
        );

        headerBg.setCornerRadius(35);
        header.setBackground(headerBg);
        header.setElevation(8);

        TextView title = new TextView(this);
        title.setText("الحسوة ماجلان");
        title.setTextColor(white);
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setIncludeFontPadding(true);

        header.addView(title, new LinearLayout.LayoutParams(
                -1, 55
        ));

        TextView subtitle = new TextView(this);
        subtitle.setText("دليلك البحري والاستكشافي");
        subtitle.setTextColor(Color.rgb(220, 245, 250));
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setIncludeFontPadding(true);

        header.addView(subtitle, new LinearLayout.LayoutParams(
                -1, 38
        ));

        root.addView(header, new LinearLayout.LayoutParams(
                -1, 125
        ));

        // =========================
        // عنوان القسم
        // =========================

        TextView sectionTitle = new TextView(this);
        sectionTitle.setText("ماذا تريد أن تستكشف؟");
        sectionTitle.setTextColor(textDark);
        sectionTitle.setTextSize(19);
        sectionTitle.setTypeface(null, Typeface.BOLD);
        sectionTitle.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        sectionTitle.setPadding(5, 8, 5, 0);
        sectionTitle.setIncludeFontPadding(true);

        root.addView(sectionTitle, new LinearLayout.LayoutParams(
                -1, 55
        ));

        // =========================
        // الصف الأول
        // =========================

        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView map = createCard(
                "◉",
                "الخريطة",
                "موقعك والملاحة",
                Color.rgb(14, 113, 153)
        );

        TextView tide = createCard(
                "≈",
                "المد والجزر",
                "حالة البحر",
                Color.rgb(0, 145, 160)
        );

        row1.addView(map, cardParams());
        row1.addView(tide, cardParams());

        root.addView(row1, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        // =========================
        // مسافة
        // =========================

        View gap = new View(this);
        root.addView(gap, new LinearLayout.LayoutParams(
                1, 12
        ));

        // =========================
        // الصف الثاني
        // =========================

        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView moon = createCard(
                "☾",
                "مراحل القمر",
                "معلومات القمر",
                Color.rgb(77, 83, 145)
        );

        TextView compass = createCard(
                "✥",
                "البوصلة",
                "اتجاهك الحالي",
                Color.rgb(20, 125, 105)
        );

        row2.addView(moon, cardParams());
        row2.addView(compass, cardParams());

        root.addView(row2, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        // =========================
        // معلومات أسفل الشاشة
        // =========================

        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER);
        bottom.setOrientation(LinearLayout.VERTICAL);

        TextView offline = new TextView(this);
        offline.setText("●  يعمل بدون إنترنت");
        offline.setTextColor(Color.rgb(25, 125, 105));
        offline.setTextSize(13);
        offline.setGravity(Gravity.CENTER);
        offline.setTypeface(null, Typeface.BOLD);

        bottom.addView(offline, new LinearLayout.LayoutParams(
                -1, 30
        ));

        TextView version = new TextView(this);
        version.setText("الحسوة ماجلان  •  الإصدار 1.0");
        version.setTextColor(gray);
        version.setTextSize(11);
        version.setGravity(Gravity.CENTER);

        bottom.addView(version, new LinearLayout.LayoutParams(
                -1, 25
        ));

        root.addView(bottom, new LinearLayout.LayoutParams(
                -1, 65
        ));

        // =========================
        // الضغط على البطاقات
        // =========================

        map.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "الخريطة",
                        Toast.LENGTH_SHORT
                ).show()
        );

        tide.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "المد والجزر",
                        Toast.LENGTH_SHORT
                ).show()
        );

        moon.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "مراحل القمر",
                        Toast.LENGTH_SHORT
                ).show()
        );

        compass.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "البوصلة",
                        Toast.LENGTH_SHORT
                ).show()
        );

        setContentView(root);
    }

    // إنشاء بطاقة
    private TextView createCard(
            String icon,
            String title,
            String description,
            int iconColor) {

        TextView card = new TextView(this);

        card.setText(
                icon + "\n" +
                title + "\n" +
                description
        );

        card.setTextSize(16);
        card.setTextColor(textDark);
        card.setGravity(Gravity.CENTER);
        card.setTypeface(null, Typeface.BOLD);
        card.setIncludeFontPadding(true);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(white);
        bg.setCornerRadius(30);
        bg.setStroke(2, Color.rgb(220, 232, 236));

        card.setBackground(bg);
        card.setElevation(6);
        card.setPadding(8, 12, 8, 12);

        return card;
    }

    // حجم البطاقات
    private LinearLayout.LayoutParams cardParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                );

        params.setMargins(6, 0, 6, 0);

        return params;
    }
}
