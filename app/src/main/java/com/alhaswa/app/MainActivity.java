package com.alhaswa.app;  
  
import android.Manifest;  
import android.annotation.SuppressLint;  
import android.app.Activity;  
import android.content.pm.PackageManager;  
import android.graphics.Color;  
import android.graphics.Typeface;  
import android.graphics.drawable.GradientDrawable;  
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.os.Bundle;  
import android.view.Gravity;  
import android.widget.LinearLayout;  
import android.widget.ScrollView;  
import android.widget.TextView;  
import android.widget.Toast;  
  
import org.mapsforge.core.graphics.Paint;  
import org.mapsforge.core.graphics.Style;  
import org.mapsforge.core.model.LatLong;  
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;  
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;  
import org.mapsforge.map.android.util.AndroidUtil;  
import org.mapsforge.map.android.view.MapView;  
import org.mapsforge.map.layer.cache.TileCache;  
import org.mapsforge.map.layer.overlay.FixedPixelCircle;  
import org.mapsforge.map.layer.renderer.TileRendererLayer;  
import org.mapsforge.map.reader.MapFile;  
import org.mapsforge.map.rendertheme.XmlRenderTheme;  
  
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
  
public class MainActivity extends Activity {  
  
    /* =============================================  
     *  ثوابت  
     * ============================================= */  
    private static final int REQ_LOCATION = 1001;  
    private static final LatLong YEMEN_CENTER = new LatLong(15.5527, 48.5164);  
    private static final byte INIT_ZOOM = 6;  
  
    /* =============================================  
     *  عناصر الشاشة الرئيسية  
     * ============================================= */  
    private LinearLayout mainLayout;  
  
    /* =============================================  
     *  عناصر الخريطة  
     * ============================================= */  
    private MapView mapView;  
    private TileCache tileCache;  
    private MapFile mapFile;  
    private LocationManager locationManager;  
    private FixedPixelCircle locationDot;  
    private Paint dotFill;  
    private Paint dotStroke;  
    private boolean centeredOnGps = false;  
  
    /* =============================================  
     *  onCreate  
     * ============================================= */  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        AndroidGraphicFactory.createInstance(getApplication());  
        showHome();  
    }  
  
    /* =============================================  
     *  أدوات مساعدة للواجهة  
     * ============================================= */  
  
    private int dp(float value) {  
        return (int) (value  
                * getResources().getDisplayMetrics().density  
                + 0.5f);  
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
        GradientDrawable d = new GradientDrawable();  
        d.setColor(color);  
        d.setCornerRadius(dp(radius));  
        return d;  
    }  
  
    /* =============================================  
     *  الشاشة الرئيسية  
     * ============================================= */  
  
    private void showHome() {  
  
        ScrollView scroll = new ScrollView(this);  
        scroll.setFillViewport(true);  
  
        mainLayout = new LinearLayout(this);  
        mainLayout.setOrientation(LinearLayout.VERTICAL);  
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);  
        mainLayout.setPadding(0, dp(24), 0, dp(18));  
        mainLayout.setBackgroundColor(  
                Color.rgb(245, 247, 250)  
        );  
  
        scroll.addView(mainLayout);  
  
        /* --- العنوان --- */  
        TextView title = textView(  
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
  
        /* --- الوصف --- */  
        TextView subtitle = textView(  
                "دليلك للملاحة واستكشاف البر والبحر",  
                16,  
                Color.rgb(90, 90, 90),  
                Gravity.CENTER  
        );  
        subtitle.setPadding(  
                dp(10), dp(7),  
                dp(10), dp(18)  
        );  
        mainLayout.addView(subtitle);  
  
        /* --- عنوان القسم --- */  
        TextView section = textView(  
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
                dp(18), dp(8),  
                dp(18), dp(8)  
        );  
        mainLayout.addView(  
                section,  
                new LinearLayout.LayoutParams(  
                        LinearLayout.LayoutParams.MATCH_PARENT,  
                        LinearLayout.LayoutParams.WRAP_CONTENT  
                )  
        );  
  
        /* --- البطاقات --- */  
        addCard(  
                "🗺️",  
                "الخريطة",  
                "استعرض الخريطة وتابع موقعك"  
        );  
        addCard(  
                "🌊",  
                "المد والجزر",  
                "تابع حالة المد والجزر ومواعيدها"  
        );  
        addCard(  
                "🌙",  
                "مراحل القمر",  
                "اعرف مرحلة القمر الحالية والقادمة"  
        );  
        addCard(  
                "🧭",  
                "البوصلة",  
                "اعرف اتجاهك الحالي بسهولة"  
        );  
  
        /* --- ملاحظة أوفلاين --- */  
        TextView offline = textView(  
                "● التطبيق صمم ليعمل بدون إنترنت",  
                14,  
                Color.rgb(80, 80, 80),  
                Gravity.CENTER  
        );  
        offline.setPadding(  
                dp(10), dp(22),  
                dp(10), dp(5)  
        );  
        mainLayout.addView(offline);  
  
        /* --- المصمم --- */  
        TextView author = textView(  
                "تم تصميم هذا التطبيق من قبل أصيل صادق",  
                14,  
                Color.rgb(80, 80, 80),  
                Gravity.CENTER  
        );  
        mainLayout.addView(author);  
  
        /* --- الإصدار --- */  
        TextView ver = textView(  
                "الإصدار 1.0",  
                13,  
                Color.rgb(120, 120, 120),  
                Gravity.CENTER  
        );  
        ver.setPadding(0, dp(5), 0, 0);  
        mainLayout.addView(ver);  
  
        setContentView(scroll);  
    }  
  
    private void addCard(  
            String icon,  
            String title,  
            String description  
    ) {  
        LinearLayout card = new LinearLayout(this);  
        card.setOrientation(LinearLayout.HORIZONTAL);  
        card.setGravity(Gravity.CENTER_VERTICAL);  
        card.setPadding(  
                dp(18), dp(16),  
                dp(18), dp(16)  
        );  
        card.setBackground(  
                background(Color.WHITE, 18)  
        );  
  
        LinearLayout.LayoutParams params =  
                new LinearLayout.LayoutParams(  
                        LinearLayout.LayoutParams.MATCH_PARENT,  
                        LinearLayout.LayoutParams.WRAP_CONTENT  
                );  
        params.setMargins(  
                dp(16), dp(7),  
                dp(16), dp(7)  
        );  
        card.setLayoutParams(params);  
  
        /* أيقونة */  
        TextView iconView = textView(  
                icon, 30,  
                Color.DKGRAY,  
                Gravity.CENTER  
        );  
        card.addView(  
                iconView,  
                new LinearLayout.LayoutParams(  
                        dp(55), dp(55)  
                )  
        );  
  
        /* عمود النص */  
        LinearLayout textCol =  
                new LinearLayout(this);  
        textCol.setOrientation(  
                LinearLayout.VERTICAL  
        );  
        textCol.setGravity(  
                Gravity.CENTER_VERTICAL  
                        | Gravity.RIGHT  
        );  
        textCol.setPadding(dp(12), 0, 0, 0);  
  
        LinearLayout.LayoutParams tp =  
                new LinearLayout.LayoutParams(  
                        0,  
                        LinearLayout.LayoutParams.WRAP_CONTENT,  
                        1  
                );  
        textCol.setLayoutParams(tp);  
  
        TextView titleView = textView(  
                title, 20,  
                Color.rgb(30, 30, 30),  
                Gravity.RIGHT  
        );  
        titleView.setTypeface(  
                Typeface.DEFAULT,  
                Typeface.BOLD  
        );  
  
        TextView descView = textView(  
                description, 14,  
                Color.rgb(100, 100, 100),  
                Gravity.RIGHT  
        );  
        descView.setPadding(  
                0, dp(5), 0, 0  
        );  
  
        textCol.addView(titleView);  
        textCol.addView(descView);  
        card.addView(textCol);  
  
        /* --- عند الضغط --- */  
        card.setOnClickListener(v -> {  
            if (title.equals("الخريطة")) {  
                openMap();  
            } else {  
                Toast.makeText(  
                        this,  
                        title,  
                        Toast.LENGTH_SHORT  
                ).show();  
            }  
        });  
  
        mainLayout.addView(card);  
    }  
  
    /* =============================================  
     *  نسخ ملف الخريطة من Assets  
     * ============================================= */  
  
    /**  
     * ينسخ ملف الخريطة من مجلد assets  
     * إلى التخزين الداخلي للتطبيق.  
     *  
     * شرط الحجم > 50 ك.ب. ضروري جدًا:  
     * يمنع نسخ مؤشر Git LFS (133 بايت)  
     * الذي يبدو كملف صالح لكنه ليس خريطة.  
     */  
    private File copyMapAsset(  
            String assetName,  
            String localName  
    ) throws Exception {  
  
        File out = new File(  
                getFilesDir(),  
                localName  
        );  
  
        /*  
         * إذا الملف موجود وحجمه > 50 ك.ب.  
         * نعتبره خريطة حقيقية ونتخطى النسخ.  
         * هذا يسرع فتح الخريطة في المرات التالية.  
         */  
        if (out.exists()  
                && out.length() > 50000) {  
            return out;  
        }  
  
        try (  
                InputStream is =  
                        getAssets().open(assetName);  
                FileOutputStream fos =  
                        new FileOutputStream(out)  
        ) {  
            byte[] buf =  
                    new byte[1024 * 1024];  
            int n;  
            while ((n = is.read(buf)) != -1) {  
                fos.write(buf, 0, n);  
            }  
        }  
  
        return out;  
    }  
  
    /* =============================================  
     *  RenderTheme  
     * ============================================= */  
  
    private XmlRenderTheme alhaswaTheme() {  
        return new AssetsRenderTheme(  
                getAssets(),  
                "rendertheme/",  
                "alhaswa.xml",  
                null  
        );  
    }  
  
    /* =============================================  
     *  فتح الخريطة  
     * ============================================= */  
  
    private void openMap() {  
  
        centeredOnGps = false;  
  
        LinearLayout layout =  
                new LinearLayout(this);  
        layout.setOrientation(  
                LinearLayout.VERTICAL  
        );  
  
        /* --- زر العودة --- */  
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
                0, dp(14),  
                0, dp(14)  
        );  
        layout.addView(  
                back,  
                new LinearLayout.LayoutParams(  
                        LinearLayout.LayoutParams.MATCH_PARENT,  
                        LinearLayout.LayoutParams.WRAP_CONTENT  
                )  
        );  
  
        /* --- الخريطة --- */  
        mapView = new MapView(this);  
        mapView.setClickable(true);  
        mapView.setFocusable(true);  
        layout.addView(  
                mapView,  
                new LinearLayout.LayoutParams(  
                        LinearLayout.LayoutParams.MATCH_PARENT,  
                        0, 1  
                )  
        );  
  
        /* --- زر الموقع --- */  
        TextView locBtn = textView(  
                "📍 موقعي",  
                14,  
                Color.WHITE,  
                Gravity.CENTER  
        );  
        locBtn.setBackgroundColor(  
                Color.rgb(66, 133, 244)  
        );  
        locBtn.setPadding(  
                dp(14), dp(10),  
                dp(14), dp(10)  
        );  
  
        LinearLayout.LayoutParams locLp =  
                new LinearLayout.LayoutParams(  
                        LinearLayout.LayoutParams.WRAP_CONTENT,  
                        LinearLayout.LayoutParams.WRAP_CONTENT  
                );  
        locLp.gravity = Gravity.END;  
  
        LinearLayout locRow =  
                new LinearLayout(this);  
        locRow.setGravity(  
                Gravity.END | Gravity.BOTTOM  
        );  
        locRow.setPadding(  
                0, 0, dp(16), dp(16)  
        );  
        locRow.addView(locBtn, locLp);  
  
        layout.addView(  
                locRow,  
                new LinearLayout.LayoutParams(  
                        LinearLayout.LayoutParams.MATCH_PARENT,  
                        LinearLayout.LayoutParams.WRAP_CONTENT  
                )  
        );  
  
        setContentView(layout);  
  
        /* --- أحداث الأزرار --- */  
        back.setOnClickListener(v -> {  
            closeMap();  
            showHome();  
        });  
  
        locBtn.setOnClickListener(v -> {  
            askForLocation();  
        });  
  
        /* --- تحميل الخريطة --- */  
        loadMap();  
    }  
  
    private void loadMap() {  
  
        try {  
  
            /*  
             * نسخ AlHaswa.map من assets  
             * إلى التخزين الداخلي  
             */  
            File mapData = copyMapAsset(  
                    "AlHaswa.map",  
                    "AlHaswa.map"  
            );  
  
            mapFile = new MapFile(mapData);  
  
            int tileSize =  
                    mapView  
                            .getModel()  
                            .displayModel  
                            .getTileSize();  
  
            tileCache =  
                    AndroidUtil.createTileCache(  
                            this,  
                            "alhaswa-cache",  
                            tileSize,  
                            2f,  
                            2f  
                    );  
  
            /*  
             * طبقة العرض  
             * ----------  
             * renderLabels = true  → أسماء المدن والدول  
             * isTransparent = false → خلفية غير شفافة  
             */  
            TileRendererLayer layer =  
                    new TileRendererLayer(  
                            tileCache,  
                            mapFile,  
                            mapView  
                                    .getModel()  
                                    .mapViewPosition,  
                            false,  
                            true,  
                            false,  
                            AndroidGraphicFactory.INSTANCE  
                    );  
  
            layer.setXmlRenderTheme(  
                    alhaswaTheme()  
            );  
  
            mapView  
                    .getLayerManager()  
                    .getLayers()  
                    .add(layer);  
  
            /* =============================  
             *  دائرة موقع المستخدم (GPS)  
             * ============================= */  
  
            dotFill =  
                    AndroidGraphicFactory  
                            .INSTANCE  
                            .createPaint();  
            dotFill.setColor(  
                    Color.rgb(66, 133, 244)  
            );  
            dotFill.setStyle(Style.FILL);  
  
            dotStroke =  
                    AndroidGraphicFactory  
                            .INSTANCE  
                            .createPaint();  
            dotStroke.setColor(Color.WHITE);  
            dotStroke.setStyle(Style.STROKE);  
            dotStroke.setStrokeWidth(3);  
  
            locationDot = new FixedPixelCircle(  
                    YEMEN_CENTER,  
                    8,  
                    dotFill,  
                    dotStroke  
            );  
  
            locationDot.setVisible(false);  
  
            mapView  
                    .getLayerManager()  
                    .getLayers()  
                    .add(locationDot);  
  
            /* --- المركز والتكبير --- */  
            mapView.setCenter(  
                    YEMEN_CENTER  
            );  
            mapView.setZoomLevel(  
                    INIT_ZOOM  
            );  
            mapView.invalidate();  
  
            /* --- بدء GPS --- */  
            locationManager =  
                    (LocationManager)  
                            getSystemService(  
                                    LOCATION_SERVICE  
                            );  
            askForLocation();  
  
            long mb =  
                    mapData.length()  
                            / 1024  
                            / 1024;  
  
            Toast.makeText(  
                    this,  
                    "تم تحميل الخريطة ("  
                            + mb + " MB)",  
                    Toast.LENGTH_LONG  
            ).show();  
  
        } catch (Exception e) {  
  
            Toast.makeText(  
                    this,  
                    "خطأ في تحميل الخريطة:\n"  
                            + e.getClass()  
                                    .getSimpleName()  
                            + "\n"  
                            + e.getMessage(),  
                    Toast.LENGTH_LONG  
            ).show();  
        }  
    }  
  
    /* =============================================  
     *  GPS — صلاحيات + استماع  
     * ============================================= */  
  
    private void askForLocation() {  
  
        if (checkSelfPermission(  
                Manifest.permission  
                        .ACCESS_FINE_LOCATION  
        ) != PackageManager  
                .PERMISSION_GRANTED) {  
  
            requestPermissions(  
                    new String[]{  
                            Manifest.permission  
                                    .ACCESS_FINE_LOCATION,  
                            Manifest.permission  
                                    .ACCESS_COARSE_LOCATION  
                    },  
                    REQ_LOCATION  
            );  
            return;  
        }  
  
        startGps();  
    }  
  
    @Override  
    public void onRequestPermissionsResult(  
            int code,  
            String[] perms,  
            int[] results  
    ) {  
        super.onRequestPermissionsResult(  
                code, perms, results  
        );  
  
        if (code == REQ_LOCATION) {  
  
            if (results.length > 0  
                    && results[0]  
                    == PackageManager  
                    .PERMISSION_GRANTED) {  
                startGps();  
            } else {  
                Toast.makeText(  
                        this,  
                        "صلاحية الموقع مرفوضة",  
                        Toast.LENGTH_LONG  
                ).show();  
            }  
        }  
    }  
  
    @SuppressLint("MissingPermission")  
    private void startGps() {  
  
        if (locationManager == null) return;  
  
        if (checkSelfPermission(  
                Manifest.permission  
                        .ACCESS_FINE_LOCATION  
        ) != PackageManager  
                .PERMISSION_GRANTED) return;  
  
        try {  
  
            locationManager.requestLocationUpdates(  
                    LocationManager.GPS_PROVIDER,  
                    1000,  
                    5,  
                    gpsListener  
            );  
  
            locationManager.requestLocationUpdates(  
                    LocationManager.NETWORK_PROVIDER,  
                    2000,  
                    10,  
                    gpsListener  
            );  
  
        } catch (Exception ignored) {  
        }  
    }  
  
    private void stopGps() {  
  
        if (locationManager != null) {  
            try {  
                locationManager.removeUpdates(  
                        gpsListener  
                );  
            } catch (Exception ignored) {  
            }  
        }  
    }  
  
    /**  
     * يستمع إلى تحديثات الموقع من GPS أو الشبكة.  
     *  
     * عندما يصل إحداث جديد:  
     * 1. ينقل دائرة الموقع إلى الإحداث الجديد  
     * 2. يُظهر الدائرة على الخريطة  
     * 3. في أول مرة فقط: يُوسّط الخريطة على المستخدم  
     */  
    private final LocationListener gpsListener =  
            new LocationListener() {  
  
                @Override  
                public void onLocationChanged(  
                        Location loc  
                ) {  
                    LatLong pos =  
                            new LatLong(  
                                    loc.getLatitude(),  
                                    loc.getLongitude()  
                            );  
  
                    if (mapView != null  
                            && locationDot != null) {  
  
                        /*  
                         * نزيل الدائرة القديمة  
                         * ونضيف واحدة جديدة في الموقع  
                         * المحدّث (لأن Circle لا يملك  
                         * setLatLong في بعض إصدارات  
                         * Mapsforge)  
                         */  
                        mapView  
                                .getLayerManager()  
                                .getLayers()  
                                .remove(locationDot);  
  
                        locationDot =  
                                new FixedPixelCircle(  
                                        pos,  
                                        8,  
                                        dotFill,  
                                        dotStroke  
                                );  
  
                        mapView  
                                .getLayerManager()  
                                .getLayers()  
                                .add(locationDot);  
  
                        mapView.invalidate();  
                    }  
  
                    /*  
                     * أول تحديث GPS:  
                     * نُوسّط الخريطة على موقع المستخدم  
                     * ونكبّر إلى مستوى 14  
                     */  
                    if (!centeredOnGps  
                            && mapView != null) {  
  
                        mapView.setCenter(pos);  
                        mapView.setZoomLevel(  
                                (byte) 14  
                        );  
                        centeredOnGps = true;  
                    }  
                }  
  
                @Override  
                public void onStatusChanged(  
                        String p,  
                        int s,  
                        Bundle e  
                ) {  
                }  
  
                @Override  
                public void onProviderEnabled(  
                        String p  
                ) {  
                }  
  
                @Override  
                public void onProviderDisabled(  
                        String p  
                ) {  
                }  
            };  
  
    /* =============================================  
     *  تنظيف الموارد  
     * ============================================= */  
  
    private void closeMap() {  
  
        stopGps();  
  
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
  
        if (mapFile != null) {  
            try {  
                mapFile.close();  
            } catch (Exception ignored) {  
            }  
            mapFile = null;  
        }  
  
        locationManager = null;  
        locationDot = null;  
        dotFill = null;  
        dotStroke = null;  
        centeredOnGps = false;  
    }  
  
    /* =============================================  
     *  دورة حياة Activity  
     * ============================================= */  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        if (mapView != null) startGps();  
    }  
  
    @Override  
    protected void onPause() {  
        super.onPause();  
        stopGps();  
    }  
  
    @Override  
    protected void onDestroy() {  
        closeMap();  
        AndroidGraphicFactory  
                .clearResourceMemoryCache();  
        super.onDestroy();  
    }  
}  
