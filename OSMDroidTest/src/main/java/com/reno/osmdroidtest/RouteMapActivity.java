package com.reno.osmdroidtest;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;

public class RouteMapActivity extends ActionBarActivity {


    private static final String TAG = RouteMapActivity.class.getName();

    // Most of this is useless
    private XYTileSource MBTILESRENDER = new XYTileSource(
            "mbtiles",
            ResourceProxy.string.offline_mode,
            0, 10,  // zoom min/max <- should be taken from metadata if available
            256, ".png", "http://i.dont.care.org/");

    private MapView mOsmv;
    private ResourceProxy mResourceProxy;
    private MapTileProviderArray mProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapBeginConfig();
        loadUI();
        mapEndConfig();

    }

    private void mapBeginConfig(){

        String packageDir = "/com.reno.osmdroidtest";
        mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(this);

        File f = new File(Environment.getExternalStorageDirectory() + packageDir, "brasil.mbtiles");

        IArchiveFile[] files = { MBTilesFileArchive.getDatabaseFileArchive(f) };
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, MBTILESRENDER, files);

        mProvider = new MapTileProviderArray(MBTILESRENDER, null,
                new MapTileModuleProviderBase[]{ moduleProvider }
        );

        this.mOsmv = new MapView(this, 256, mResourceProxy, mProvider);

        this.mOsmv.setBackgroundColor(Color.rgb(181,208,208));
    }

    private void mapEndConfig(){
        this.mOsmv.setBuiltInZoomControls(true);
        this.mOsmv.setMultiTouchControls(true);

        mOsmv.getController().setZoom(4);
        mOsmv.setMinZoomLevel(4);
        mOsmv.setMaxZoomLevel(8);
        double lon = -49.636;
        double lat = -15.008;

        IGeoPoint point = new GeoPoint(lat, lon); // lat lon and not inverse
        mOsmv.getController().setCenter(point);
    }

    private void loadUI() {
        this.setContentView(R.layout.activity_route_map);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.maplayout);

        this.mOsmv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        rl.addView(this.mOsmv);
    }

}
