/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
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
package org.mapsforge.samples.android.dummy;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class DummyContent {
    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String content;
        public final String id;
        public final LatLong location;
        public final String text;

        public DummyItem(String id, String content, LatLong location,
                         String text) {
            this.id = id;
            this.content = content;
            this.location = location;
            this.text = text;
        }

        @Override
        public String toString() {
            return this.content;
        }

    }

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    static {


        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("toilet-geo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int a = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ccc5555666666", document.getId() + " => " + document.getData());

                                addItem(new DummyItem(Integer.toString(a),document.get("userid").toString()+Integer.toString(a), new LatLong(Double.parseDouble(document.get("geo.latitude").toString()), Double.parseDouble(document.get("geo.longitude").toString())), "This is the famous Brandenburger Tor"));
                                Log.d("asssss", document.get("geo.latitude").toString() + document.get("geo.longitude").toString());
                                a += 1;
                                //  Toast.makeText(this.mapView, "document gets success", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.d("ccc4555566666", "Error getting documents: ", task.getException());
                            //Toast.makeText(this.mapView, "document gets error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }



    protected DummyContent() {
        // no-op




    }
}
