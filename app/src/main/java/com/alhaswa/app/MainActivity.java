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
    private TileCache tileCache;
    private MapFile worldMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        showWorldMap();
    }

    private void showWorldMap() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(this);

        title.setText("اختبار خريطة العالم");
        title.setTextColor(Color.WHITE);
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);
        title.setBackgroundColor(Color.rgb(20, 70, 110));
        title.setPadding(0, dp(14), 0, dp(14));

        layout.addView(
                title,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        mapView = new MapView(this);

        layout.addView(
                mapView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        setContentView(layout);

        try {

            File worldFile = getWorldFile();

            if (!worldFile.exists()) {
                throw new Exception("world.map غير موجود");
            }

            if (worldFile.length() == 0) {
                throw new Exception("world.map فارغ");
            }

            worldMap = new MapFile(worldFile);

            int tileSize =
                    mapView
                            .getModel()
                            .displayModel
                            .getTileSize();

            tileCache =
                    AndroidUtil.createTileCache(
                            this,
                            "world-test-cache",
                            tileSize,
                            2f,
                            2f
                    );

            TileRendererLayer layer =
                    new TileRendererLayer(
                            tileCache,
                            worldMap,
                            mapView
                                    .getModel()
                                    .mapViewPosition,
                            false,
                            true,
                            false,
                            AndroidGraphicFactory.INSTANCE
                    );

            XmlRenderTheme theme =
                    new AssetsRenderTheme(
                            getAssets(),
                            "rendertheme/",
                            "osmarender.xml",
                            null
                    );

            layer.setXmlRenderTheme(theme);

            mapView
                    .getLayerManager()
                    .getLayers()
                    .add(layer);

            /*
             * نبدأ بتصغير كبير حتى نرى
             * هل العالم موجود أصلًا.
             */
            mapView.setCenter(
                    worldMap
                            .boundingBox()
                            .getCenterPoint()
            );

            mapView.setZoomLevel((byte) 2);

            mapView.invalidate();

            long size =
                    worldFile.length()
                            / 1024
                            / 1024;

            Toast.makeText(
                    this,
                    "تم فتح world.map\nالحجم: "
                            + size
                            + " MB",
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "خطأ:\n"
                            + e.getClass().getSimpleName()
                            + "\n"
                            + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private File getWorldFile() throws Exception {

        File file =
                new File(
                        getFilesDir(),
                        "world.map"
                );

        if (file.exists()
                && file.length() > 0) {

            return file;
        }

        InputStream input = null;
        FileOutputStream output = null;

        try {

            input =
                    getAssets().open(
                            "world.map"
                    );

            output =
                    new FileOutputStream(file);

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

        } finally {

            if (output != null) {
                output.close();
            }

            if (input != null) {
                input.close();
            }
        }

        return file;
    }

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }

    @Override
    protected void onDestroy() {

        if (mapView != null) {
            try {
                mapView.destroyAll();
            } catch (Exception ignored) {
            }
        }

        if (tileCache != null) {
            try {
                tileCache.destroy();
            } catch (Exception ignored) {
            }
        }

        if (worldMap != null) {
            try {
                worldMap.close();
            } catch (Exception ignored) {
            }
        }

        AndroidGraphicFactory
                .clearResourceMemoryCache();

        super.onDestroy();
    }
}
