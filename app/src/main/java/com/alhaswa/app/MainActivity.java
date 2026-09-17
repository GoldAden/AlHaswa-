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

    private final int dark = Color.rgb(13, 48, 67);
    private final int blue = Color.rgb(8, 111, 143);
    private final int background = Color.rgb(242, 247, 249);
    private final int white = Color.WHITE;

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
        root.setPadding(16, 18, 16, 24);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        scrollView.addView(root);

        // =========================
        // HEADER
        // =========================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(20, 28, 20, 28);

        GradientDrawable headerBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(5, 48, 70),
                        Color.rgb(7, 123, 151)
                }
        );

        headerBg.setCornerRadius(36);
        header.setBackground(headerBg);
        header.setElevation(10);

        TextView title = new TextView(this);
        title.setText("الحسوة ماجلان");
        title.setTextSize(29);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setIncludeFontPadding(true);

        header.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("دليلك للملاحة واستكشاف البر والبحر");
        subtitle.setTextSize(14);
        subtitle.setTextColor(Color.rgb(225, 246, 249));
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 9, 0, 0);
        subtitle.setIncludeFontPadding(true);

        header.addView(subtitle);

        root.addView(header);

        addSpace(root, 20);

        // =========================
        // SECTION TITLE
        // =========================

        TextView section = new TextView(this);
        section.setText("أدوات الملاحة");
        section.setTextSize(21);
        section.setTextColor(dark);
        section.setTypeface(null, Typeface.BOLD);
        section.setGravity(Gravity.RIGHT);
        section.setPadding(6, 4, 6, 12);

        root.addView(section);

        // =========================
        // MAP CARD
        // =========================

        LinearLayout map = createCard(
                "🗺️",
                "الخريطة",
                "استعرض الخريطة وتابع موقعك",
                true
        );

        root.addView(map);

        addSpace(root, 12);

        // =========================
        // TIDE CARD
        // =========================

        LinearLayout tide = createCard(
                "🌊",
                "المد والجزر",
                "تابع حالة المد والجزر ومواعيدها",
                false
        );

        root.addView(tide);

        addSpace(root, 12);

        // =========================
        // MOON CARD
        // =========================

        LinearLayout moon = createCard(
                "🌙",
                "مراحل القمر",
                "اعرف مرحلة القمر الحالية ومراحلة القادمة",
                false
        );

        root.addView(moon);

        addSpace(root, 12);

        // =========================
        // COMPASS CARD
        // =========================

        LinearLayout compass = createCard(
                "🧭",
                "البوصلة",
                "اعرف اتجاهك الحالي بسهولة",
                false
        );

        root.addView(compass);

        addSpace(root, 25);

        // =========================
        // FOOTER
        // =========================

        LinearLayout footer = new LinearLayout(this);
        footer.setOrientation(LinearLayout.VERTICAL);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(10, 8, 10, 8);

        TextView status = new TextView(this);
        status.setText("التطبيق صمم ليعمل بدون إنترنت");
        status.setTextSize(14);
        status.setTextColor(Color.rgb(25, 120, 95));
        status.setTypeface(null, Typeface.BOLD);
        status.setGravity(Gravity.CENTER);

        footer.addView(status);

        TextView credit = new TextView(this);
        credit.setText("تم تصميم هذا التطبيق من قبل أصيل صادق");
        credit.setTextSize(12);
        credit.setTextColor(Color.rgb(100, 115, 125));
        credit.setGravity(Gravity.CENTER);
        credit.setPadding(0, 5, 0, 2);

        footer.addView(credit);

        TextView version = new TextView(this);
        version.setText("الإصدار 1.0");
        version.setTextSize(12);
        version.setTextColor(Color.rgb(120, 135, 145));
        version.setGravity(Gravity.CENTER);
        version.setPadding(0, 6, 0, 4);

        footer.addView(version);

        root.addView(footer);

        // =========================
        // CLICK EVENTS
        // =========================

        map.setOnClickListener(v -> showMap());

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

    // =========================================================
    // MAP SCREEN
    // =========================================================

    private void showMap() {

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.rgb(238, 243, 245));

        // =========================
        // MAP TOP BAR
        // =========================

        LinearLayout topBar = new LinearLayout(this);
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setGravity(Gravity.CENTER_VERTICAL);
        topBar.setPadding(10, 10, 10, 10);
        topBar.setBackgroundColor(Color.rgb(5, 48, 70));
        topBar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView title = new TextView(this);
        title.setText("🗺️ خريطة اليمن");
        title.setTextSize(19);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        topBar.addView(title, titleParams);

        TextView back = new TextView(this);
        back.setText("رجوع");
        back.setTextSize(15);
        back.setTextColor(Color.WHITE);
        back.setGravity(Gravity.CENTER);
        back.setPadding(22, 10, 22, 10);

        GradientDrawable backBg = new GradientDrawable();
        backBg.setColor(Color.rgb(8, 111, 143));
        backBg.setCornerRadius(22);

        back.setBackground(backBg);

        back.setOnClickListener(v -> {

            if (mapView != null) {
                mapView.destroyAll();
                mapView = null;
            }

            showHome();
        });

        topBar.addView(back);

        container.addView(topBar);

        // =========================
        // MAP VIEW
        // =========================

        mapView = new MapView(this);

        mapView.setClickable(true);
        mapView.setFocusable(true);

        container.addView(
                mapView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        // =========================
        // MAP CONTROLS
        // =========================

        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        controls.setGravity(Gravity.CENTER);
        controls.setPadding(12, 10, 12, 12);
        controls.setBackgroundColor(Color.WHITE);

        TextView zoomOut = createMapButton("−");

        TextView zoomIn = createMapButton("+");

        zoomOut.setOnClickListener(v -> {

            byte zoom = mapView
                    .getModel()
                    .mapViewPosition
                    .getZoomLevel();

            if (zoom > 1) {
                mapView.setZoomLevel((byte) (zoom - 1));
            }
        });

        zoomIn.setOnClickListener(v -> {

            byte zoom = mapView
                    .getModel()
                    .mapViewPosition
                    .getZoomLevel();

            if (zoom < 20) {
                mapView.setZoomLevel((byte) (zoom + 1));
            }
        });

        controls.addView(
                zoomOut,
                new LinearLayout.LayoutParams(
                        70,
                        58
                )
        );

        addHorizontalSpace(controls, 12);

        controls.addView(
                zoomIn,
                new LinearLayout.LayoutParams(
                        70,
                        58
                )
        );

        container.addView(controls);

        setContentView(container);

        // =========================
        // LOAD MAP
        // =========================

        try {

            File mapFile = getMapFile();

            if (!mapFile.exists()) {
                throw new Exception(
                        "الملف غير موجود بعد النسخ"
                );
            }

            if (mapFile.length() == 0) {
                throw new Exception(
                        "حجم Yemen.map يساوي صفر"
                );
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
                    e.getClass().getName();

            if (e.getMessage() != null &&
                    !e.getMessage().isEmpty()) {

                error += "\n\n" + e.getMessage();
            }

            TextView errorView =
                    new TextView(this);

            errorView.setText(
                    "خطأ في تحميل خريطة اليمن\n\n" +
                    error
            );

            errorView.setTextSize(15);
            errorView.setTextColor(Color.RED);
            errorView.setGravity(Gravity.CENTER);
            errorView.setPadding(
                    20,
                    20,
                    20,
                    20
            );

            errorView.setTextIsSelectable(true);

            container.addView(
                    errorView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // MAP BUTTON
    // =========================================================

    private TextView createMapButton(String text) {

        TextView button = new TextView(this);

        button.setText(text);
        button.setTextSize(32);
        button.setTextColor(dark);
        button.setTypeface(null, Typeface.BOLD);
        button.setGravity(Gravity.CENTER);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setCornerRadius(24);
        bg.setStroke(
                2,
                Color.rgb(210, 224, 230)
        );

        button.setBackground(bg);
        button.setElevation(5);

        return button;
    }

    // =========================================================
    // MAP FILE
    // =========================================================

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

        byte[] buffer =
                new byte[8192];

        int length;

        while ((length = input.read(buffer)) != -1) {

            output.write(
                    buffer,
                    0,
                    length
            );
        }

        output.flush();
        output.close();
        input.close();

        return mapFile;
    }

    // =========================================================
    // HOME CARD
    // =========================================================

    private LinearLayout createCard(
            String icon,
            String title,
            String description,
            boolean mainCard) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                18,
                mainCard ? 23 : 20,
                18,
                mainCard ? 23 : 20
        );

        card.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        GradientDrawable bg =
                new GradientDrawable();

        if (mainCard) {

            bg.setColor(
                    Color.rgb(231, 246, 250)
            );

            bg.setStroke(
                    2,
                    Color.rgb(8, 135, 160)
            );

        } else {

            bg.setColor(Color.WHITE);

            bg.setStroke(
                    2,
                    Color.rgb(222, 232, 236)
            );
        }

        bg.setCornerRadius(30);

        card.setBackground(bg);
        card.setElevation(mainCard ? 8 : 5);

        // ICON

        TextView iconView =
                new TextView(this);

        iconView.setText(icon);
        iconView.setTextSize(
                mainCard ? 36 : 32
        );

        iconView.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        mainCard ? 65 : 60,
                        mainCard ? 65 : 60
                );

        card.addView(
                iconView,
                iconParams
        );

        addHorizontalSpace(card, 15);

        // TEXT AREA

        LinearLayout textArea =
                new LinearLayout(this);

        textArea.setOrientation(
                LinearLayout.VERTICAL
        );

        textArea.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView titleView =
                new TextView(this);

        titleView.setText(title);
        titleView.setTextSize(
                mainCard ? 21 : 19
        );

        titleView.setTextColor(dark);
        titleView.setTypeface(
                null,
                Typeface.BOLD
        );

        titleView.setGravity(
                Gravity.RIGHT
        );

        textArea.addView(titleView);

        TextView descriptionView =
                new TextView(this);

        descriptionView.setText(description);
        descriptionView.setTextSize(13);
        descriptionView.setTextColor(
                Color.rgb(105, 120, 130)
        );

        descriptionView.setGravity(
                Gravity.RIGHT
        );

        descriptionView.setPadding(
                0,
                4,
                0,
                0
        );

        textArea.addView(
                descriptionView
        );

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        card.addView(
                textArea,
                textParams
        );

        return card;
    }

    // =========================================================
    // SPACING
    // =========================================================

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

    private void addHorizontalSpace(
            LinearLayout layout,
            int width) {

        View space = new View(this);

        layout.addView(
                space,
                new LinearLayout.LayoutParams(
                        width,
                        1
                )
        );
    }

    // =========================================================
    // BACK BUTTON
    // =========================================================

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
