package com.alhaswa.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {

    private LinearLayout mainLayout;

    private MapView mapView;

    private TileCache worldTileCache;
    private TileCache yemenTileCache;

    private MapFile worldMap;
    private MapFile yemenMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        showHome();
    }

    private int dp(float value) {
        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
                        + 0.5f
        );
    }

    private TextView textView(
            String text,
            float size,
            int color,
            int gravity
    ) {
        TextView tv = new TextView(this);

        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(color);
        tv.setGravity(gravity);

        return tv;
    }

    private GradientDrawable background(
            int color,
            float radius
    ) {
        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));

        return drawable;
    }

    private void showHome() {

        ScrollView scrollView =
                new ScrollView(this);

        scrollView.setFillViewport(true);

        mainLayout =
                new LinearLayout(this);

        mainLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        mainLayout.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        mainLayout.setPadding(
                0,
                dp(24),
                0,
                dp(18)
        );

        mainLayout.setBackgroundColor(
                Color.rgb(245, 247, 250)
        );

        scrollView.addView(mainLayout);

        TextView title =
                textView(
                        "الحسوة ماجلان",
                        30,
                        Color.rgb(20, 70, 110),
                        Gravity.CENTER
                );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        mainLayout.addView(
                title,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        TextView subtitle =
                textView(
                        "دليلك للملاحة واستكشاف البر والبحر",
                        16,
                        Color.rgb(90, 90, 90),
                        Gravity.CENTER
                );

        subtitle.setPadding(
                dp(10),
                dp(7),
                dp(10),
                dp(18)
        );

        mainLayout.addView(subtitle);

        TextView section =
                textView(
                        "أدوات الملاحة",
                        21,
                        Color.rgb(30, 30, 30),
                        Gravity.RIGHT
                );

        section.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        section.setPadding(
                dp(18),
                dp(8),
                dp(18),
                dp(8)
        );

        mainLayout.addView(
                section,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        addNavigationCard(
                "🗺️",
                "الخريطة",
                "استعرض الخريطة وتابع موقعك"
        );

        addNavigationCard(
                "🌊",
                "المد والجزر",
                "تابع حالة المد والجزر ومواعيدها"
        );

        addNavigationCard(
                "🌙",
                "مراحل القمر",
                "اعرف مرحلة القمر الحالية ومراحلة القادمة"
        );

        addNavigationCard(
                "🧭",
                "البوصلة",
                "اعرف اتجاهك الحالي بسهولة"
        );

        TextView offline =
                textView(
                        "● التطبيق صمم ليعمل بدون إنترنت",
                        14,
                        Color.rgb(80, 80, 80),
                        Gravity.CENTER
                );

        offline.setPadding(
                dp(10),
                dp(22),
                dp(10),
                dp(5)
        );

        mainLayout.addView(offline);

        TextView author =
                textView(
                        "تم تصميم هذا التطبيق من قبل أصيل صادق",
                        14,
                        Color.rgb(80, 80, 80),
                        Gravity.CENTER
                );

        mainLayout.addView(author);

        TextView version =
                textView(
                        "الإصدار 1.0",
                        13,
                        Color.rgb(120, 120, 120),
                        Gravity.CENTER
                );

        version.setPadding(
                0,
                dp(5),
                0,
                0
        );

        mainLayout.addView(version);

        setContentView(scrollView);
    }

    private void addNavigationCard(
            String icon,
            String title,
            String description
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                dp(18),
                dp(16),
                dp(18),
                dp(16)
        );

        card.setBackground(
                background(
                        Color.WHITE,
                        18
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                dp(16),
                dp(7),
                dp(16),
                dp(7)
        );

        card.setLayoutParams(params);

        TextView iconView =
                textView(
                        icon,
                        30,
                        Color.DKGRAY,
                        Gravity.CENTER
                );

        card.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(55)
                )
        );

        LinearLayout textLayout =
                new LinearLayout(this);

        textLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        textLayout.setGravity(
                Gravity.CENTER_VERTICAL |
                        Gravity.RIGHT
        );

        textLayout.setPadding(
                dp(12),
                0,
                0,
                0
        );

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        textLayout.setLayoutParams(textParams);

        TextView titleView =
                textView(
                        title,
                        20,
                        Color.rgb(30, 30, 30),
                        Gravity.RIGHT
                );

        titleView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        TextView descriptionView =
                textView(
                        description,
                        14,
                        Color.rgb(100, 100, 100),
                        Gravity.RIGHT
                );

        descriptionView.setPadding(
                0,
                dp(5),
                0,
                0
        );

        textLayout.addView(titleView);
        textLayout.addView(descriptionView);

        card.addView(textLayout);

        card.setOnClickListener(v -> {

            if (title.equals("الخريطة")) {

                showMap();

            } else {

                Toast.makeText(
                        MainActivity.this,
                        title,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        mainLayout.addView(card);
    }

    private File copyAssetToInternal(
            String assetName,
            String outputName
    ) throws Exception {

        File outputFile =
                new File(
                        getFilesDir(),
                        outputName
                );

        if (outputFile.exists()
                && outputFile.length() > 0) {

            return outputFile;
        }

        InputStream input =
                getAssets().open(assetName);

        FileOutputStream output =
                new FileOutputStream(
                        outputFile
                );

        byte[] buffer =
                new byte[1024 * 1024];

        int length;

        while (
                (length = input.read(buffer)) != -1
        ) {

            output.write(
                    buffer,
                    0,
                    length
            );
        }

        output.flush();
        output.close();
        input.close();

        return outputFile;
    }

    private File getYemenFile() throws Exception {

        return copyAssetToInternal(
                "Yemen.map",
                "Yemen_v2.map"
        );
    }

    private File getWorldFile() throws Exception {

        return copyAssetToInternal(
                "world.map",
                "world_v2.map"
        );
    }

    /*
     * ثيم خريطة اليمن
     */
    private XmlRenderTheme getYemenRenderTheme() {

        return new AssetsRenderTheme(
                getAssets(),
                "rendertheme/",
                "osmarender.xml",
                null
        );
    }

    /*
     * ثيم العالم
     *
     * هذا الثيم لا يرسم البحر ولا يلون الخريطة.
     * يرسم فقط حدود الدول وأسماء الدول
     * إذا كانت موجودة داخل world.map.
     */
    private XmlRenderTheme getWorldRenderTheme() {

        return new AssetsRenderTheme(
                getAssets(),
                "rendertheme/",
                "world.xml",
                null
        );
    }

    private void showMap() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        TextView back =
                textView(
                        "← العودة",
                        18,
                        Color.WHITE,
                        Gravity.CENTER
                );

        back.setBackgroundColor(
                Color.rgb(20, 70, 110)
        );

        back.setPadding(
                0,
                dp(14),
                0,
                dp(14)
        );

        layout.addView(
                back,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP
