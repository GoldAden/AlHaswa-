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

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {

    int dark = Color.rgb(18, 48, 65);
    int white = Color.WHITE;
    int background = Color.rgb(244, 248, 250);

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        showHome();
    }

    private void showHome() {
        mapView = null;

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(background);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 20, 18, 25);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        scrollView.addView(root);

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

        TextView section = new TextView(this);
        section.setText("أدوات الملاحة");
        section.setTextSize(20);
        section.setTextColor(dark);
        section.setTypeface(null, Typeface.BOLD);
        section.setGravity(Gravity.RIGHT);
        section.setIncludeFontPadding(true);
        section.setPadding(4, 5, 4, 12);
        root.addView(section);

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

        View largeSpace = new View(this);
        root.addView(
                largeSpace,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        1
                )
        );

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.VERTICAL);
        bottom.setGravity(Gravity.CENTER);

        TextView status = new TextView(this);
        status.setText("التطبيق صمم ليعمل بدون إنترنت");
        status.setTextSize(14);
        status.setTextColor(Color.rgb(25, 120, 95));
        status.setTypeface(null, Typeface.BOLD);
        status.setGravity(Gravity.CENTER);
        status.setIncludeFontPadding(true);
        bottom.addView(status);

        TextView credit = new TextView(this);
        credit.setText("تم تصميم هذا التطبيق من قبل أصيل صادق");
        credit.setTextSize(12);
        credit.setTextColor(Color.rgb(100, 115, 125));
        credit.setGravity(Gravity.CENTER);
        credit.setPadding(0, 4, 0, 2);
        credit.setIncludeFontPadding(true);
        bottom.addView(credit);

        TextView version = new TextView(this);
        version.setText("الإصدار 1.0");
        version.setTextSize(12);
        version.setTextColor(Color.rgb(120, 135, 145));
        version.setGravity(Gravity.CENTER);
        version.setPadding(0, 6, 0, 5);
        version.setIncludeFontPadding(true);
        bottom.addView(version);

        root.addView(bottom);

        map.setOnClickListener(v -> showMap());

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

    private void showMap() {

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.WHITE);

        LinearLayout topBar = new LinearLayout(this);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        topBar.setPadding(12, 10, 12, 10);
        topBar.setBackgroundColor(Color.rgb(7, 55, 78));
        topBar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView mapTitle = new TextView(this);
        mapTitle.setText("🗺️ خريطة اليمن");
        mapTitle.setTextSize(19);
        mapTitle.setTextColor(Color.WHITE);
        mapTitle.setTypeface(null, Typeface.BOLD);
        mapTitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
        topBar.addView(mapTitle, titleParams);

        TextView back = new TextView(this);
        back.setText("رجوع");
        back.setTextSize(15);
        back.setTextColor(Color.WHITE);
        back.setGravity(Gravity.CENTER);
        back.setPadding(20, 10, 20, 10);

        back.setOnClickListener(v -> {
            if (mapView != null) {
                mapView.destroyAll();
                mapView = null;
            }
            showHome();
        });

        topBar.addView(back);
        container.addView(topBar);

        mapView = new MapView(this);

        container.addView(
                mapView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        setContentView(container);

        try {
            File mapFile = getMapFile();

            if (!mapFile.exists()) {
                throw new Exception("الملف غير موجود بعد النسخ");
            }

            if (mapFile.length() == 0) {
                throw new Exception("حجم Yemen.map يساوي صفر");
            }

            MapDataStore mapDataStore =
                    new MapFile(mapFile);

            int tileSize = 256;

            TileCache tileCache =
                    AndroidUtil.createTileCache(
                            this,
                            "yemen-map-cache",
                            tileSize,
                            1f,
                            1f
                    );

            TileRendererLayer tileRendererLayer =
                    new TileRendererLayer(
                            tileCache,
                            mapDataStore,
                            mapView.getModel().mapViewPosition,
                            false,
                            true,
                            false,
                            AndroidGraphicFactory.INSTANCE
                    );

            mapView.getLayerManager()
                    .getLayers()
                    .add(tileRendererLayer);

            mapView.setCenter(
                    new LatLong(
                            15.5527,
                            48.5164
                    )
            );

            mapView.setZoomLevel((byte) 6);

            Toast.makeText(
                    this,
                    "تم تحميل خريطة اليمن بدون إنترنت",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            String error =
                    e.getClass().getSimpleName();

            if (e.getMessage() != null &&
                    !e.getMessage().isEmpty()) {

                error += ": " + e.getMessage();
            }

            Toast.makeText(
                    this,
                    "خطأ الخريطة: " + error,
                    Toast.LENGTH_LONG
            ).show();

            e.printStackTrace();
        }
    }

    private File getMapFile() throws Exception {

        File mapFile =
                new File(
                        getFilesDir(),
                        "Yemen.map"
                );

        if (mapFile.exists() &&
                mapFile.length() > 0) {

            return mapFile;
        }

        InputStream input =
                getAssets().open("Yemen.map");

        FileOutputStream output =
                new FileOutputStream(mapFile);

        byte[] buffer = new byte[8192];

        int length;

        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();

        return mapFile;
    }

    private LinearLayout createCard(
            String icon,
            String title,
            String description) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(Gravity.CENTER);

        card.setPadding(
                20,
                22,
                20,
                22
        );

        card.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        GradientDrawable backgroundCard =
                new GradientDrawable();

        backgroundCard.setColor(white);

        backgroundCard.setCornerRadius(28);

        backgroundCard.setStroke(
                2,
                Color.rgb(222, 232, 236)
        );

        card.setBackground(
                backgroundCard
        );

        card.setElevation(5);

        TextView iconText =
                new TextView(this);

        iconText.setText(icon);

        iconText.setTextSize(30);

        iconText.setGravity(
                Gravity.CENTER
        );

        iconText.setIncludeFontPadding(
                true
        );

        card.addView(iconText);

        TextView titleText =
                new TextView(this);

        titleText.setText(title);

        titleText.setTextSize(21);

        titleText.setTextColor(dark);

        titleText.setTypeface(
                null,
                Typeface.BOLD
        );

        titleText.setGravity(
                Gravity.CENTER
        );

        titleText.setIncludeFontPadding(
                true
        );

        titleText.setPadding(
                0,
                5,
                0,
                3
        );

        card.addView(titleText);

        TextView descriptionText =
                new TextView(this);

        descriptionText.setText(
                description
        );

        descriptionText.setTextSize(14);

        descriptionText.setTextColor(
                Color.rgb(
                        105,
                        120,
                        130
                )
        );

        descriptionText.setGravity(
                Gravity.CENTER
        );

        descriptionText.setIncludeFontPadding(
                true
        );

        card.addView(
                descriptionText
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        card.setLayoutParams(params);

        return card;
    }

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

    @Override
    public void onBackPressed() {

        if (mapView != null) {

            mapView.destroyAll();

            mapView = null;

            showHome();

        } else {

            super.onBackPressed();
        }
    }
}
