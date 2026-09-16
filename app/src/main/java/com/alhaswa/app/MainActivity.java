package com.alhaswa.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    int dark = Color.rgb(18, 48, 65);
    int white = Color.WHITE;
    int background = Color.rgb(244, 248, 250);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(background);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 20, 18, 25);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        scrollView.addView(root);

        // =========================
        // رأس التطبيق
        // =========================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(20, 22, 20, 22);

        GradientDrawable headerBackground = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(7, 55, 78),
                        Color.rgb(9, 125, 151)
                }
        );

        headerBackground.setCornerRadius(32);
        header.setBackground(headerBackground);
        header.setElevation(8);

        TextView title = new TextView(this);
        title.setText("الحسوة ماجلان");
        title.setTextSize(26);
        title.setTextColor(white);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setIncludeFontPadding(true);

        header.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("دليلك للملاحة واستكشاف البر والبحر");
        subtitle.setTextSize(14);
        subtitle.setTextColor(Color.rgb(225, 245, 248));
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 8, 0, 0);
        subtitle.setIncludeFontPadding(true);

        header.addView(subtitle);

        root.addView(header);

        addSpace(root, 18);

        // =========================
        // عنوان القسم
        // =========================

        TextView section = new TextView(this);
        section.setText("أدوات الملاحة");
        section.setTextSize(20);
        section.setTextColor(dark);
        section.setTypeface(null, Typeface.BOLD);
        section.setGravity(Gravity.RIGHT);
        section.setIncludeFontPadding(true);
        section.setPadding(4, 5, 4, 12);

        root.addView(section);

        // =========================
        // البطاقات
        // =========================

        LinearLayout map = createCard(
                "🗺️",
                "الخريطة",
                "استعرض الخريطة وتابع موقعك"
        );

        root.addView(map);

        addSpace(root, 12);

        LinearLayout tide = createCard(
                "🌊",
                "المد والجزر",
                "تابع حالة المد والجزر ومواعيدها"
        );

        root.addView(tide);

        addSpace(root, 12);

        LinearLayout moon = createCard(
                "🌙",
                "مراحل القمر",
                "اعرف مرحلة القمر الحالية ومراحلة القادمة"
        );

        root.addView(moon);

        addSpace(root, 12);

        LinearLayout compass = createCard(
                "🧭",
                "البوصلة",
                "اعرف اتجاهك الحالي بسهولة"
        );

        root.addView(compass);

        // =========================
        // مساحة قبل الأسفل
        // =========================

        View largeSpace = new View(this);

        root.addView(
                largeSpace,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        1
                )
        );

        // =========================
        // أسفل الشاشة
        // =========================

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.VERTICAL);
        bottom.setGravity(Gravity.CENTER);

        TextView status = new TextView(this);
        status.setText("● التطبيق صمم ليعمل بدون إنترنت");
        status.setTextSize(14);
        status.setTextColor(Color.rgb(25, 120, 95));
        status.setTypeface(null, Typeface.BOLD);
        status.setGravity(Gravity.CENTER);
        status.setIncludeFontPadding(true);

        bottom.addView(status);

        TextView version = new TextView(this);
        version.setText("الإصدار 1.0");
        version.setTextSize(12);
        version.setTextColor(Color.rgb(120, 135, 145));
        version.setGravity(Gravity.CENTER);
        version.setPadding(0, 6, 0, 5);
        version.setIncludeFontPadding(true);

        bottom.addView(version);

        root.addView(bottom);

        // =========================
        // الضغط على البطاقات
        // =========================

        map.setOnClickListener(v ->
                Toast.makeText(this, "الخريطة", Toast.LENGTH_SHORT).show()
        );

        tide.setOnClickListener(v ->
                Toast.makeText(this, "المد والجزر", Toast.LENGTH_SHORT).show()
        );

        moon.setOnClickListener(v ->
                Toast.makeText(this, "مراحل القمر", Toast.LENGTH_SHORT).show()
        );

        compass.setOnClickListener(v ->
                Toast.makeText(this, "البوصلة", Toast.LENGTH_SHORT).show()
        );

        setContentView(scrollView);
    }

    // =========================
    // إنشاء بطاقة
    // =========================

    private LinearLayout createCard(
            String icon,
            String title,
            String description) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(20, 22, 20, 22);
        card.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        GradientDrawable backgroundCard = new GradientDrawable();
        backgroundCard.setColor(white);
        backgroundCard.setCornerRadius(28);
        backgroundCard.setStroke(
                2,
                Color.rgb(222, 232, 236)
        );

        card.setBackground(backgroundCard);
        card.setElevation(5);

        // الأيقونة
        TextView iconText = new TextView(this);
        iconText.setText(icon);
        iconText.setTextSize(30);
        iconText.setGravity(Gravity.CENTER);
        iconText.setIncludeFontPadding(true);

        card.addView(iconText);

        // اسم الوظيفة - أكبر
        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(21);
        titleText.setTextColor(dark);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setGravity(Gravity.CENTER);
        titleText.setIncludeFontPadding(true);
        titleText.setPadding(0, 5, 0, 3);

        card.addView(titleText);

        // الوصف - أصغر
        TextView descriptionText = new TextView(this);
        descriptionText.setText(description);
        descriptionText.setTextSize(14);
        descriptionText.setTextColor(
                Color.rgb(105, 120, 130)
        );
        descriptionText.setGravity(Gravity.CENTER);
        descriptionText.setIncludeFontPadding(true);

        card.addView(descriptionText);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        card.setLayoutParams(params);

        return card;
    }

    // =========================
    // مسافة
    // =========================

    private void addSpace(
            LinearLayout layout,
            int height) {

        View space = new View(this);

        layout.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        height
                )
        );
    }
}
