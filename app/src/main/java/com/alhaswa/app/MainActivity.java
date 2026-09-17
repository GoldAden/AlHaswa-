package com.alhaswa.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {

    private MapView mapView;
    private MapFile mapFileReader;
    private TileCache tileCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        showHome();
    }

    private void showHome() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(dp(20), dp(30), dp(20), dp(20));

        TextView title = textView(
                "الحسوة ماجلان",
                30,
                Color.rgb(20, 70, 110),
                Gravity.CENTER
        );

        layout.addView(title, fullWidth());

        TextView subtitle = textView(
                "دليلك للملاحة واستكشاف البر والبحر",
                16,
                Color.DKGRAY,
                Gravity.CENTER
        );

        LinearLayout.LayoutParams subtitleParams = fullWidth();
        subtitleParams.setMargins(0, dp(8), 0, dp(25));
        layout.addView(subtitle, subtitleParams);

        TextView mapButton = button("🗺️  الخريطة", 20);
        TextView tideButton = button("🌊  المد والجزر", 20);
        TextView moonButton = button("🌙  مراحل القمر", 20);
        TextView compassButton = button("🧭  البوصلة", 20);

        layout.addView(mapButton, buttonParams());
        layout.addView(tideButton, buttonParams());
        layout.addView(moonButton, buttonParams());
        layout.addView(compassButton, buttonParams());

        mapButton.setOnClickListener(v -> showMap());

        tideButton.setOnClickListener(
                v -> showMessage("قسم المد والجزر")
        );

        moonButton.setOnClickListener(
                v -> showMessage("اعرف مرحلة القمر الحالية ومراحله القادمة")
        );

        compassButton.setOnClickListener(
                v -> showMessage("قسم البوصلة")
        );

        TextView spacer = new TextView(this);

        layout.addView(
                spacer,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        1
                )
        );

        TextView offline = textView(
                "● التطبيق صمم ليعمل بدون إنترنت",
                14,
                Color.DKGRAY,
                Gravity.CENTER
        );

        layout.addView(offline, fullWidth());

        TextView author = textView(
                "تم تصميم هذا التطبيق من قبل أصيل صادق",
                14,
                Color.GRAY,
                Gravity.CENTER
        );

        LinearLayout.LayoutParams authorParams = fullWidth();
        authorParams.setMargins(0, dp(8), 0, 0);
        layout.addView(author, authorParams);

        TextView location = textView(
                "الحسوة اليمن",
                14,
                Color.rgb(20, 70, 110),
                Gravity.CENTER
        );

        LinearLayout.LayoutParams locationParams = fullWidth();
        locationParams.setMargins(0, dp(5), 0, 0);
        layout.addView(location, locationParams);

        TextView version = textView(
                "الإصدار 1.0",
                12,
                Color.GRAY,
                Gravity.CENTER
        );

        LinearLayout.LayoutParams versionParams = fullWidth();
        versionParams.setMargins(0, dp(5), 0, 0);
        layout.addView(version, versionParams);

        setContentView(layout);
    }

    private void showMap() {

        closeMapResources();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView back = textView(
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

        layout.addView(back, fullWidth());

        mapView = new MapView(this);

        mapView.setClickable(true);
        mapView.setFocusable(true);

        layout.addView(
                mapView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        setContentView(layout);

        back.setOnClickListener(v -> {
            closeMapResources();
            showHome();
        });

        try {

            File mapFile = getMapFile();

            if (!mapFile.exists() || mapFile.length() == 0) {
                throw new Exception("Yemen.map غير موجود أو فارغ");
            }

            mapFileReader = new MapFile(mapFile);

            MapDataStore mapDataStore = mapFileReader;

            BoundingBox bounds = mapDataStore.boundingBox();

            if (bounds == null) {
                throw new Exception("تعذر قراءة حدود الخريطة");
            }

            LatLong center = bounds.getCenterPoint();

            int tileSize = mapView
                    .getModel()
                    .displayModel
                    .getTileSize();

            tileCache = AndroidUtil.createTileCache(
                    this,
                    "yemen-map-cache",
                    tileSize,
                    1.5f,
                    1.5f
            );

            TileRendererLayer renderer =
                    new TileRendererLayer(
                            tileCache,
                            mapDataStore,
                            mapView
                                    .getModel()
                                    .mapViewPosition,
                            false,
                            true,
                            false,
                            AndroidGraphicFactory.INSTANCE
                    );

            XmlRenderTheme renderTheme =
                    new AssetsRenderTheme(
                            getAssets(),
                            "rendertheme/",
                            "osmarender.xml",
                            null
                    );

            renderer.setXmlRenderTheme(renderTheme);

            mapView
                    .getLayerManager()
                    .getLayers()
                    .add(renderer);

            mapView.setCenter(center);

            /*
             * نبدأ بتكبير منخفض حتى تظهر الخريطة
             * حتى لو كانت Yemen.map تغطي مساحة كبيرة.
             */
            mapView.setZoomLevel((byte) 7);

            mapView.invalidate();

            String info =
                    "تم تحميل الخريطة\n"
                    + "الحجم: "
                    + (mapFile.length() / 1024 / 1024)
                    + " MB";

            Toast.makeText(
                    this,
                    info,
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            String message = e.getMessage();

            if (message == null) {
                message = "خطأ غير معروف";
            }

            Toast.makeText(
                    this,
                    "خطأ في الخريطة:\n" + message,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private File getMapFile() throws Exception {

        File mapFile = new File(
                getFilesDir(),
                "Yemen.map"
        );

        if (mapFile.exists() && mapFile.length() > 0) {
            return mapFile;
        }

        InputStream input = null;
        FileOutputStream output = null;

        try {

            input = getAssets().open("Yemen.map");

            output = new FileOutputStream(mapFile);

            byte[] buffer = new byte[1024 * 1024];

            int length;

            while ((length = input.read(buffer)) != -1) {

                output.write(
                        buffer,
                        0,
                        length
                );
            }

            output.flush();

        } finally {

            if (output != null) {
                try {
                    output.close();
                } catch (Exception ignored) {
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (Exception ignored) {
                }
            }
        }

        if (!mapFile.exists() || mapFile.length() == 0) {
            throw new Exception(
                    "فشل نسخ Yemen.map من assets"
            );
        }

        return mapFile;
    }

    private TextView button(
            String text,
            int size
    ) {

        TextView view = textView(
                text,
                size,
                Color.WHITE,
                Gravity.CENTER
        );

        view.setBackgroundColor(
                Color.rgb(20, 90, 135)
        );

        view.setPadding(
                dp(10),
                dp(18),
                dp(10),
                dp(18)
        );

        return view;
    }

    private LinearLayout.LayoutParams buttonParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                dp(7),
                0,
                dp(7)
        );

        return params;
    }

    private LinearLayout.LayoutParams fullWidth() {

        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private TextView textView(
            String text,
            int size,
            int color,
            int gravity
    ) {

        TextView view = new TextView(this);

        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(gravity);

        return view;
    }

    private void showMessage(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }

    private void closeMapResources() {

        if (mapView != null) {
            try {
                mapView.destroyAll();
            } catch (Exception ignored) {
            }
            mapView = null;
        }

        if (tileCache != null) {
            try {
                tileCache.destroy();
            } catch (Exception ignored) {
            }
            tileCache = null;
        }

        if (mapFileReader != null) {
            try {
                mapFileReader.close();
            } catch (Exception ignored) {
            }
            mapFileReader = null;
        }
    }

    @Override
    protected void onDestroy() {

        closeMapResources();

        AndroidGraphicFactory.clearResourceMemoryCache();

        super.onDestroy();
    }
}
