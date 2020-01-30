/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 * Copyright 2013-2014 Ludwig M Brinckmann
 * Copyright 2015-2019 devemux86
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.samples.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.layers.MyLocationOverlay;
import org.mapsforge.map.android.rotation.RotateView;
import org.mapsforge.map.layer.overlay.Circle;
import org.mapsforge.map.layer.overlay.Marker;


import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;



import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.FixedPixelCircle;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.samples.android.dummy.DummyContent;

/**
 * MapViewer that shows current position. In the data directory of the Samples
 * project is the file berlin.gpx that can be loaded in the Android Monitor to
 * simulate location data in the center of Berlin.
 */

import android.provider.Settings.Secure;


public class LocationOverlayMapViewer extends DownloadLayerViewer implements LocationListener {



    //RotationGestureDetector mRotationDetector;

    private MotionEvent mLastMultitouchEvent = null;

    Location old_location ;
    LocationOverlayMapViewer l = this;
    private static Paint getPaint(int color, int strokeWidth, Style style) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        return paint;
    }

    private LocationManager locationManager;
    private MyLocationOverlay myLocationOverlay;
    protected LatLong latLong1 = new LatLong(41.0243006, 29.016278);
    protected LatLong latLong2 = new LatLong(41.0244006, 29.017278);
    protected LatLong latLong3 = new LatLong(41.0246006, 29.016278);

    private List<LatLong> liste = null;



    private static final Paint GREEN = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.GREEN), 0,
            Style.FILL);
    private static final Paint RED = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.RED), 0,
            Style.FILL);
    private static final Paint BLACK = Utils.createPaint(
            AndroidGraphicFactory.INSTANCE.createColor(Color.BLACK), 0,
            Style.FILL);

    private int i;




    @Override
    protected void createLayers() {
        super.createLayers();

        // marker to show at the location
        Bitmap bitmap = new AndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_indicator_current_position));
        Marker marker = new Marker(null, bitmap, 0, 0);

        // circle to show the location accuracy (optional)
        Circle circle = new Circle(null, 0,
                getPaint(AndroidGraphicFactory.INSTANCE.createColor(48, 0, 0, 255), 0, Style.FILL),
                getPaint(AndroidGraphicFactory.INSTANCE.createColor(160, 0, 0, 255), 2, Style.STROKE));

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        liste = new ArrayList<LatLong>();

        db.collection("toilet-geo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("locationoverlay", document.getId() + " => " + document.getData());

                                //addItem(new DummyContent.DummyItem(Integer.toString(a),document.get("userid").toString()+Integer.toString(a), new LatLong(Double.parseDouble(document.get("geo.latitude").toString()), Double.parseDouble(document.get("geo.longitude").toString())), "This is the famous Brandenburger Tor"));
                                LatLong aaa = new LatLong(Double.parseDouble(document.get("geo.latitude").toString()), Double.parseDouble(document.get("geo.longitude").toString()));
                                liste.add(aaa);
                                Log.d("locationoverlaysss", document.get("geo.latitude").toString() + document.get("geo.longitude").toString());
                                //  Toast.makeText(this.mapView, "document gets success", Toast.LENGTH_SHORT).show();

                                ccc();

                            }
                        } else {
                            Log.d("ccc4555566666", "Error getting documents: ", task.getException());
                            //Toast.makeText(this.mapView, "document gets error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




        //Marker marker1 = Utils.createTappableMarker(this,
        //        R.drawable.marker_green, latLong1);
        //Marker marker2 = Utils.createTappableMarker(this,
        //        R.drawable.marker_green, latLong2);
        //Marker marker3 = Utils.createTappableMarker(this,
        //        R.drawable.marker_green, latLong3);
        // create the overlay
        this.myLocationOverlay = new MyLocationOverlay(marker, circle);
        this.mapView.getLayerManager().getLayers().add(this.myLocationOverlay);
        //this.mapView.getLayerManager().getLayers().add(marker1);
        //this.mapView.getLayerManager().getLayers().add(marker2);
        //this.mapView.getLayerManager().getLayers().add(marker3);


    }


    private void ccc(){
        Log.d("ssss", Integer.toString(liste.size()));

        Marker marker1;
        for (int counter = 0; counter < liste.size(); counter++) {
            Log.d("ssss", "Error getting documents: ");

            marker1 = Utils.createTappableMarker(this,
                    R.drawable.marker_green, liste.get(counter));
            this.mapView.getLayerManager().getLayers().add(marker1);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        //mRotationDetector = new RotationGestureDetector(this);


        Button centerButton = (Button) findViewById(R.id.centerButton);
        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.mapView.setCenter(new LatLong(old_location.getLatitude(), old_location.getLongitude()));
                l.mapView.setZoomLevel((byte) 17);
            }
        });


        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Access a Cloud Firestore instance from your Activity
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Create a new user with a first and last name
                Map<String, Object> loc = new HashMap<>();
                loc.put("geo", old_location);
                Long tsLong = System.currentTimeMillis()/1000;
                loc.put("time", tsLong);
                loc.put("userid", "cabbar");


// Add a new document with a generated ID
                db.collection("toilet-geo")
                        .add(loc)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("SUCC", "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(LocationOverlayMapViewer.this, "Point added successufly", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LocationOverlayMapViewer.this, "Point couln't added", Toast.LENGTH_SHORT).show();
                                Log.w("ERR", "Error adding document", e);
                            }
                        });
            }
        });

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }


    public boolean onLongPress(LatLong tapLatLong, Point thisXY,
                               Point tapXY) {
        Marker marker1 = Utils.createTappableMarker(this,
                R.drawable.marker_green, tapLatLong);

        this.mapView.getLayerManager().getLayers().add(marker1);
        return true;
    }

    /*protected void onLongPress(final LatLong position) {
        float circleSize = 20 * this.mapView.getModel().displayModel.getScaleFactor();


        Marker marker1 = Utils.createTappableMarker(this,
                R.drawable.marker_green, position);

        this.mapView.getLayerManager().getLayers().add(marker1);

        i += 1;

        FixedPixelCircle tappableCircle = new FixedPixelCircle(position,
                circleSize, GREEN, null) {

            int count = i;

            @Override
            public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas
                    canvas, Point topLeftPoint) {
                super.draw(boundingBox, zoomLevel, canvas, topLeftPoint);

                long mapSize = MercatorProjection.getMapSize(zoomLevel, this.displayModel.getTileSize());

                int pixelX = (int) (MercatorProjection.longitudeToPixelX(position.longitude, mapSize) - topLeftPoint.x);
                int pixelY = (int) (MercatorProjection.latitudeToPixelY(position.latitude, mapSize) - topLeftPoint.y);
                String text = Integer.toString(count);
                canvas.drawText(text, pixelX - BLACK.getTextWidth(text) / 2, pixelY + BLACK.getTextHeight(text) / 2, BLACK);
            }

            @Override
            public boolean onLongPress(LatLong geoPoint, Point viewPosition,
                                       Point tapPoint) {
                if (this.contains(viewPosition, tapPoint)) {
                    Log.d("SUCC", "uzun basıldııııııııııııııııııı ");











                    //LongPressAction.this.mapView.getLayerManager()
                    //        .getLayers().remove(this);
                    //LongPressAction.this.mapView.getLayerManager()
                    //        .redrawLayers();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onTap(LatLong geoPoint, Point viewPosition,
                                 Point tapPoint) {
                if (this.contains(viewPosition, tapPoint)) {
                    //toggleColor();
                    this.requestRedraw();
                    return true;
                }
                return false;
            }

            private void toggleColor() {
                if (this.getPaintFill().equals(LongPressAction.GREEN)) {
                    this.setPaintFill(LongPressAction.RED);
                } else {
                    this.setPaintFill(LongPressAction.GREEN);
                }
            }
        };
        this.mapView.getLayerManager().getLayers().add(tappableCircle);
        tappableCircle.requestRedraw();

    } */


    //@Override
    //public boolean onTouchEvent(MotionEvent event){
        //this.getMapView().setRotation(10);
    //    return super.onTouchEvent(event);
    //}
/*
    @Override
    public void OnRotation(RotationGestureDetector rotationDetector) {
        float angle = rotationDetector.getAngle();
        Toast.makeText(this,
                "getresource " + angle,
                Toast.LENGTH_SHORT).show();
        //RotateView rotateView = (RotateView) findViewById(R.id.mapView);
        //rotateView.setHeading(rotateView.getHeading() - 45f);
        //rotateView.postInvalidate();

    }
*/


    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableAvailableProviders();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        enableAvailableProviders();
    }

    @Override
    public void onStop() {
        this.locationManager.removeUpdates(this);

        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.myLocationOverlay.setPosition(location.getLatitude(), location.getLongitude(), location.getAccuracy());
        // Follow location
        this.mapView.setCenter(new LatLong(location.getLatitude(), location.getLongitude()));
        this.mapView.setZoomLevel((byte) 17);
        old_location = location;

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void enableAvailableProviders() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                return;
            }
        }

        this.locationManager.removeUpdates(this);

        for (String provider : this.locationManager.getProviders(true)) {
            if (LocationManager.GPS_PROVIDER.equals(provider)
                    || LocationManager.NETWORK_PROVIDER.equals(provider)) {
                this.locationManager.requestLocationUpdates(provider, 0, 20, this);
            }
        }
    }





}