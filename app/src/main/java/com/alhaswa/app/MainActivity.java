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

    private int dark = Color.rgb(18, 48, 65);
    private int white = Color.WHITE;
    private int background = Color.rgb(244, 248, 250);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =========================
        // ScrollView
        // =========================

        ScrollView scrollView = new ScrollView(this);

        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(background);

        // =========================
        // الحاوية الرئيسية
        // =========================

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 20, 18, 25);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        scrollView.addView(root);

        // =========================
        // الرأس
        // =========================

        LinearLayout header = new LinearLayout(this);

        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(20, 22, 20, 22);

        GradientDrawable headerBackground = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[] {
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

        // =========================
        // المسافة
        // =========================

        addSpace(root, 18);

        // =========================
        // عنوان
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
        // الخريطة
        // =========================

        TextView map = createCard(
                "🗺️",
                "الخريطة",
                "استعرض الخريطة وتابع موقعك"
        );

        root.addView(map);

        addSpace(root, 12);

        // =========================
        // المد والجزر
        // =========================

        TextView tide = createCard(
                "🌊",
                "المد والجزر",
                "تابع حالة المد والجزر ومواعيدها"
        );

        root.addView(tide);

        addSpace(root, 12);

        // =========================
        // القمر
        // =========================

        TextView moon = createCard(
                "🌙",
                "مراحل القمر",
                "اعرف مرحلة القمر الحالية ومراحلة القادمة"
        );

        root.addView(moon);

        addSpace(root, 12);

        // =========================
        // البوصلة
        // =========================

        TextView compass = createCard(
                "🧭",
                "البوصلة",
                "اعرف اتجاهك الحالي بسهولة"
        );

        root.addView(compass);

        addSpace(root, 20);

        // =========================
        // حالة التطبيق
        // =========================

        LinearLayout status = new LinearLayout(this);

        status.setOrientation(LinearLayout.VERTICAL);
        status.setGravity(Gravity.CENTER);
        status.setPadding(15, 15, 15, 15);

        GradientDrawable statusBackground = new GradientDrawable();

        statusBackground.setColor(Color.rgb(232, 246, 242));
        statusBackground.setCornerRadius(24);

        status.setBackground(statusBackground);

        TextView statusText = new TextView(this);

        statusText.setText("●  التطبيق يعمل بدون إنترنت");
        statusText.setTextSize(14);
        statusText.setTextColor(Color.rgb(25, 120, 95));
        statusText.setTypeface(null, Typeface.BOLD);
        statusText.setGravity(Gravity.CENTER);
        statusText.setIncludeFontPadding(true);

        status.addView(statusText);

        root.addView(status);

        addSpace(root, 18);

        // =========================
        // أسفل الشاشة
        // =========================

        TextView footer = new TextView(this);

        footer.setText("الحسوة ماجلان\nالإصدار 1.0");
        footer.setTextSize(12);
        footer.setTextColor(Color.rgb(120, 135, 145));
        footer.setGravity(Gravity.CENTER);
        footer.setIncludeFontPadding(true);
        footer.setPadding(0, 5, 0, 10);

        root.addView(footer);

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

        setContentView(scrollView);
    }

    // ==================================================
    // إنشاء بطاقة مرنة
    // ==================================================

    private TextView createCard(
            String icon,
            String title,
            String description) {

        TextView card = new TextView(this);

        card.setText(
                icon + "\n" +
                title + "\n" +
                description
        );

        card.setTextSize(17);
        card.setTextColor(dark);

        card.setGravity(Gravity.CENTER);
        card.setTypeface(null, Typeface.BOLD);

        /*
         * مهم جدًا:
         * لا يوجد ارتفاع ثابت هنا.
         * البطاقة تأخذ الارتفاع الذي تحتاجه.
         */
        card.setPadding(20, 22, 20, 22);

        card.setIncludeFontPadding(true);

        GradientDrawable background = new GradientDrawable();

        background.setColor(white);
        background.setCornerRadius(28);
        background.setStroke(
                2,
                Color.rgb(222, 232, 236)
        );

        card.setBackground(background);

        card.setElevation(5);

        /*
         * العرض كامل والارتفاع تلقائي
         */
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        card.setLayoutParams(params);

        return card;
    }

    // ==================================================
    // مسافة
    // ==================================================

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
