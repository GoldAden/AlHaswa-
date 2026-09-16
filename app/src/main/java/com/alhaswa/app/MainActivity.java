package com.alhaswa.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Space;
public class MainActivity extends Activity {

    int dark = Color.rgb(18, 35, 48);
    int blue = Color.rgb(25, 118, 160);
    int white = Color.WHITE;
    int light = Color.rgb(245, 248, 250);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // الشاشة الرئيسية
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(24, 35, 24, 24);
        root.setBackgroundColor(light);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // العنوان
        TextView title = new TextView(this);
        title.setText("الحسوة ماجلان");
        title.setTextSize(30);
        title.setTextColor(dark);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        root.addView(title, new LinearLayout.LayoutParams(
                -1, 60
        ));

        // الوصف
        TextView subtitle = new TextView(this);
        subtitle.setText("ملاحتك واستكشافك بدون إنترنت");
        subtitle.setTextSize(16);
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setGravity(Gravity.CENTER);

        root.addView(subtitle, new LinearLayout.LayoutParams(
                -1, 50
        ));

        // مساحة
        Space spacer = new Space(this);
        root.addView(spacer, new LinearLayout.LayoutParams(
                1, 25
        ));

        // الصف الأول
        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setGravity(Gravity.CENTER);
        row1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView map = createCard("🗺", "الخريطة");
        TextView tide = createCard("🌊", "المد والجزر");

        row1.addView(map, cardParams());
        row1.addView(tide, cardParams());

        root.addView(row1);

        // مسافة
        Space space2 = new Space(this);
        root.addView(space2, new LinearLayout.LayoutParams(
                1, 18
        ));

        // الصف الثاني
        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setGravity(Gravity.CENTER);
        row2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView moon = createCard("🌙", "مراحل القمر");
        TextView compass = createCard("🧭", "البوصلة");

        row2.addView(moon, cardParams());
        row2.addView(compass, cardParams());

        root.addView(row2);

        // أحداث الأزرار
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

        // أسفل الشاشة
        Space bottomSpace = new Space(this);
        root.addView(bottomSpace, new LinearLayout.LayoutParams(
                1, 0, 1
        ));

        TextView footer = new TextView(this);
        footer.setText("الحسوة ماجلان • إصدار 1.0");
        footer.setTextSize(13);
        footer.setTextColor(Color.GRAY);
        footer.setGravity(Gravity.CENTER);

        root.addView(footer, new LinearLayout.LayoutParams(
                -1, 40
        ));

        setContentView(root);
    }

    private TextView createCard(String icon, String text) {

        TextView card = new TextView(this);

        card.setText(icon + "\n\n" + text);
        card.setTextSize(19);
        card.setTextColor(dark);
        card.setGravity(Gravity.CENTER);
        card.setTypeface(null, android.graphics.Typeface.BOLD);

        GradientDrawable background = new GradientDrawable();
        background.setColor(white);
        background.setCornerRadius(28);
        background.setStroke(2, Color.rgb(225, 232, 236));

        card.setBackground(background);
        card.setElevation(7);
        card.setPadding(10, 15, 10, 15);

        return card;
    }

    private LinearLayout.LayoutParams cardParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        190,
                        1
                );

        params.setMargins(8, 0, 8, 0);

        return params;
    }
}
