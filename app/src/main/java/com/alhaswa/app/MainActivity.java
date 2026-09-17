package com.alhaswa.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private MapFile yemenMap;
    private MapFile worldMap;

    private TileCache yemenCache;
    private TileCache worldCache;

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
        layout.setPadding(
                dp(20),
                dp(30),
                dp(20),
                dp(20)
        );

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
        subtitleParams.setMargins(
                0,
                dp(8),
                0,
                dp(25)
        );

        layout.addView(subtitle, subtitleParams);

        TextView mapButton =
                button("🗺️  الخريطة", 20);

        TextView tideButton =
                button("🌊  المد والجزر", 20);

        TextView moonButton =
                button("🌙  مراحل القمر", 20);

        TextView compassButton =
                button("🧭  البوصلة", 20);

        layout.addView(mapButton, buttonParams());
        layout.addView(tideButton, buttonParams());
        layout.addView(moonButton, buttonParams());
        layout.addView(compassButton, buttonParams());

        mapButton.setOnClickListener(
                v -> showMap()
        );

        tideButton.setOnClickListener(
                v -> showMessage("قسم المد والجزر")
        );

        moonButton.setOnClickListener(
                v -> showMessage(
                        "اعرف مرحلة القمر الحالية ومراحلها القادمة"
                )
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
        authorParams.setMargins(
                0,
                dp(8),
                0,
                0
        );

        layout.addView(author, authorParams);

        TextView location = textView(
                "الحسوة اليمن",
                14,
                Color.rgb(20, 70, 110),
                Gravity.CENTER
        );

        LinearLayout.LayoutParams locationParams = fullWidth();
        locationParams.setMargins(
                0,
                dp(5),
                0,
                0
        );

        layout.addView(location, locationParams);

        TextView version = textView(
                "الإصدار 1.0",
                12,
                Color.GRAY,
                Gravity.CENTER
        );

        LinearLayout.LayoutParams versionParams = fullWidth();
        versionParams.setMargins(
                0,
                dp(5),
                0,
                0
        );

        layout.addView(version, versionParams);

        setContentView(layout);
    }

    private void showMap() {

        closeMapResources();

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
                fullWidth()
        );

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

        back.setOnClickListener(
                v -> {
                    closeMapResources();
                    showHome();
                }
        );

        try {

            /*
             * تحميل ملف اليمن
             */
            File yemenFile =
                    getAssetFile("Yemen.map");

            /*
             * تحميل ملف العالم
             */
            File worldFile =
                    getAssetFile("world.map");

            if (!yemenFile.exists()
                    || yemenFile.length() == 0) {

                throw new Exception(
                        "Yemen.map غير موجود"
                );
            }

            if (!worldFile.exists()
                    || worldFile.length() == 0) {

                throw new Exception(
                        "world.map غير موجود"
                );
            }

            /*
             * فتح الخرائط
             */
            yemenMap =
                    new MapFile(yemenFile);

            worldMap =
                    new MapFile(worldFile);

            int tileSize =
                    mapView
                            .getModel()
                            .displayModel
                            .getTileSize();

            /*
             * Cache لخريطة العالم
             */
            worldCache =
                    AndroidUtil.createTileCache(
                            this,
                            "world-map-cache",
                            tileSize,
                            1.5f,
                            1.5f
                    );

            /*
             * Cache لخريطة اليمن
             */
            yemenCache =
                    AndroidUtil.createTileCache(
                            this,
                            "yemen-map-cache",
                            tileSize,
                            1.5f,
                            1.5f
                    );

            /*
             * Render Theme
             */
            XmlRenderTheme renderTheme =
                    new AssetsRenderTheme(
                            getAssets(),
                            "rendertheme/",
                            "osmarender.xml",
                            null
                    );

            /*
             * طبقة العالم
             */
            TileRendererLayer worldLayer =
                    new TileRendererLayer(
                            worldCache,
                            worldMap,
                            mapView
                                    .getModel()
                                    .mapViewPosition,
                            false,
                            true,
                            false,
                            AndroidGraphicFactory.INSTANCE
                    );

            worldLayer.setXmlRenderTheme(
                    renderTheme
            );

            /*
             * طبقة اليمن
             */
            TileRendererLayer yemenLayer =
                    new TileRendererLayer(
                            yemenCache,
                            yemenMap,
                            mapView
                                    .getModel()
                                    .mapViewPosition,
                            false,
                            true,
                            false,
                            AndroidGraphicFactory.INSTANCE
                    );

            yemenLayer.setXmlRenderTheme(
                    renderTheme
            );

            /*
             * العالم أولاً
             */
            mapView
                    .getLayerManager()
                    .getLayers()
                    .add(worldLayer);

            /*
             * اليمن فوق العالم
             */
            mapView
                    .getLayerManager()
                    .getLayers()
                    .add(yemenLayer);

            /*
             * نبدأ من منطقة اليمن
             */
            mapView.setCenter(
                    yemenMap
                            .boundingBox()
                            .getCenterPoint()
            );

            /*
             * مستوى تكبير مناسب لليمن
             */
            mapView.setZoomLevel(
                    (byte) 7
            );

            mapView.invalidate();

            long yemenSize =
                    yemenFile.length()
                            / 1024
                            / 1024;

            long worldSize =
                    worldFile.length()
                            / 1024
                            / 1024;

            Toast.makeText(
                    this,
                    "تم تحميل الخريطتين\n"
                            + "اليمن: "
                            + yemenSize
                            + " MB\n"
                            + "العالم: "
                            + worldSize
                            + " MB",
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            String message =
                    e.getMessage();

            if (message == null) {
                message =
                        "خطأ غير معروف";
            }

            Toast.makeText(
                    this,
                    "خطأ في الخريطة:\n"
                            + message,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private File getAssetFile(
            String assetName
    ) throws Exception {

        File file =
                new File(
                        getFilesDir(),
                        assetName
                );

        /*
         * إذا كانت النسخة موجودة
         * نستخدمها مباشرة.
         */
        if (file.exists()
                && file.length() > 0) {

            return file;
        }

        InputStream input = null;
        FileOutputStream output = null;

        try {

            input =
                    getAssets().open(
                            assetName
                    );

            output =
                    new FileOutputStream(
                            file
                    );

            byte[] buffer =
                    new byte[1024 * 1024];

            int length;

            while (
                    (length =
                            input.read(buffer)) != -1
            ) {

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

        if (!file.exists()
                || file.length() == 0) {

            throw new Exception(
                    assetName
                            + " فارغ أو لم يتم نسخه"
            );
        }

        return file;
    }

    private TextView button(
            String text,
            int size
    ) {

        TextView view =
                textView(
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

    private LinearLayout.LayoutParams
    buttonParams() {

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

    private LinearLayout.LayoutParams
    fullWidth() {

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

        TextView view =
                new TextView(this);

        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(gravity);

        return view;
    }

    private void showMessage(
            String message
    ) {

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

        if (yemenCache != null) {

            try {
                yemenCache.destroy();
            } catch (Exception ignored) {
            }

            yemenCache = null;
        }

        if (worldCache != null) {

            try {
                worldCache.destroy();
            } catch (Exception ignored) {
            }

            worldCache = null;
        }

        if (yemenMap != null) {

            try {
                yemenMap.close();
            } catch (Exception ignored) {
            }

            yemenMap = null;
        }

        if (worldMap != null) {

            try {
                worldMap.close();
            } catch (Exception ignored) {
            }

            worldMap = null;
        }
    }

    @Override
    protected void onDestroy() {

        closeMapResources();

        AndroidGraphicFactory
                .clearResourceMemoryCache();

        super.onDestroy();
    }
}
