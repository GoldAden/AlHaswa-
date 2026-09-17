package com.alhaswa.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(
                getApplication()
        );

        showHome();
    }

    private void showHome() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        layout.setPadding(
                dp(20),
                dp(30),
                dp(20),
                dp(20)
        );

        TextView title =
                textView(
                        "الحسوة ماجلان",
                        30,
                        Color.rgb(20, 70, 110),
                        Gravity.CENTER
                );

        layout.addView(
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
                        Color.DKGRAY,
                        Gravity.CENTER
                );

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        subtitleParams.setMargins(
                0,
                dp(8),
                0,
                dp(25)
        );

        layout.addView(
                subtitle,
                subtitleParams
        );

        TextView mapButton =
                button(
                        "🗺️  الخريطة",
                        20
                );

        TextView tideButton =
                button(
                        "🌊  المد والجزر",
                        20
                );

        TextView moonButton =
                button(
                        "🌙  مراحل القمر",
                        20
                );

        TextView compassButton =
                button(
                        "🧭  البوصلة",
                        20
                );

        layout.addView(
                mapButton,
                buttonParams()
        );

        layout.addView(
                tideButton,
                buttonParams()
        );

        layout.addView(
                moonButton,
                buttonParams()
        );

        layout.addView(
                compassButton,
                buttonParams()
        );

        mapButton.setOnClickListener(
                v -> showMap()
        );

        tideButton.setOnClickListener(
                v -> showMessage(
                        "قسم المد والجزر"
                )
        );

        moonButton.setOnClickListener(
                v -> showMessage(
                        "اعرف مرحلة القمر الحالية ومراحلة القادمة"
                )
        );

        compassButton.setOnClickListener(
                v -> showMessage(
                        "قسم البوصلة"
                )
        );

        TextView spacer =
                new TextView(this);

        layout.addView(
                spacer,
                new LinearLayout.LayoutParams(
                        1,
                        0,
                        1
                )
        );

        TextView offline =
                textView(
                        "● التطبيق صمم ليعمل بدون إنترنت",
                        14,
                        Color.DKGRAY,
                        Gravity.CENTER
                );

        layout.addView(
                offline,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        TextView author =
                textView(
                        "تم تصميم هذا التطبيق من قبل أصيل صادق",
                        14,
                        Color.GRAY,
                        Gravity.CENTER
                );

        LinearLayout.LayoutParams authorParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        authorParams.setMargins(
                0,
                dp(8),
                0,
                0
        );

        layout.addView(
                author,
                authorParams
        );

        TextView location =
                textView(
                        "الحسوة اليمن",
                        14,
                        Color.rgb(20, 70, 110),
                        Gravity.CENTER
                );

        LinearLayout.LayoutParams locationParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        locationParams.setMargins(
                0,
                dp(5),
                0,
                0
        );

        layout.addView(
                location,
                locationParams
        );

        TextView version =
                textView(
                        "الإصدار 1.0",
                        12,
                        Color.GRAY,
                        Gravity.CENTER
                );

        LinearLayout.LayoutParams versionParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        versionParams.setMargins(
                0,
                dp(5),
                0,
                0
        );

        layout.addView(
                version,
                versionParams
        );

        setContentView(layout);
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
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        final MapView mapView =
                new MapView(this);

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
                v -> showHome()
        );

        try {

            /*
             * تحميل Yemen.map من assets
             */
            File mapFile =
                    getMapFile();

            long fileSize =
                    mapFile.length();

            /*
             * فتح الخريطة
             */
            MapFile mapFileReader =
                    new MapFile(mapFile);

            MapDataStore mapDataStore =
                    mapFileReader;

            /*
             * قراءة حدود الخريطة الحقيقية
             */
            BoundingBox bounds =
                    mapDataStore.boundingBox();

            /*
             * حساب مركز الخريطة تلقائياً
             */
            LatLong center =
                    bounds.getCenterPoint();

            /*
             * حجم البلاطة
             */
            int tileSize =
                    mapView
                            .getModel()
                            .displayModel
                            .getTileSize();

            /*
             * Tile Cache جديد للتجربة
             */
            TileCache tileCache =
                    AndroidUtil.createTileCache(
                            this,
                            "yemen-debug-cache",
                            tileSize,
                            2f,
                            2f
                    );

            /*
             * طبقة الخريطة
             */
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

            renderer.setXmlRenderTheme(
                    renderTheme
            );

            /*
             * إضافة طبقة الخريطة
             */
            mapView
                    .getLayerManager()
                    .getLayers()
                    .add(renderer);

            /*
             * وضع الخريطة في مركزها الحقيقي
             */
            mapView.setCenter(
                    center
            );

            /*
             * مستوى تكبير مناسب للتشخيص
             */
            mapView.setZoomLevel(
                    (byte) 12
            );

            mapView.invalidate();

            /*
             * معلومات تشخيصية
             */
            String info =
                    "حجم Yemen.map: "
                            + (fileSize / 1024 / 1024)
                            + " MB\n"
                            + "الشمال: "
                            + bounds.maxLatitude
                            + "\n"
                            + "الجنوب: "
                            + bounds.minLatitude
                            + "\n"
                            + "الشرق: "
                            + bounds.maxLongitude
                            + "\n"
                            + "الغرب: "
                            + bounds.minLongitude;

            Toast.makeText(
                    this,
                    info,
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "خطأ حقيقي في الخريطة:\n"
                            + e.getClass().getSimpleName()
                            + "\n"
                            + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private File getMapFile()
            throws Exception {

        /*
         * نستخدم اسم جديد حتى لا نقرأ
         * نسخة قديمة من Yemen.map
         */
        File mapFile =
                new File(
                        getFilesDir(),
                        "Yemen_debug.map"
                );

        /*
         * إذا كانت النسخة القديمة موجودة
         * نحذفها أولاً
         */
        if (mapFile.exists()) {
            mapFile.delete();
        }

        /*
         * فتح Yemen.map من assets
         */
        InputStream input =
                getAssets().open(
                        "Yemen.map"
                );

        /*
         * نسخ الخريطة إلى التخزين الداخلي
         */
        FileOutputStream output =
                new FileOutputStream(
                        mapFile
                );

        byte[] buffer =
                new byte[1024 * 1024];

        int length;

        while (
                (length = input.read(buffer)) > 0
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

        return mapFile;
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

    private int dp(
            int value
    ) {

        return (int)
                (
                        value
                                * getResources()
                                        .getDisplayMetrics()
                                        .density
                );
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        AndroidGraphicFactory.clearResourceMemoryCache();
    }
}

هذا الكود لا يحتاج إلى تغيير "build.gradle".

بعد الحفظ شغّل GitHub Actions وابنِ التطبيق وثبّته.

المهم: عند فتح الخريطة ستظهر رسالة فيها:

- حجم "Yemen.map"
- الشمال
- الجنوب
- الشرق
- الغرب

أرسل لي صورة الرسالة أو اكتب الأرقام التي تظهر فيها. من هذه الأرقام سنحدد مباشرة لماذا الخريطة بيضاء، بدل أن نغيّر الكود عشوائيًا.
