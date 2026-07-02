package com.uteq.software.googlemaps;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private static final String BASE_URL = "http://35.153.103.86/turismo10022025/";

    GoogleMap mapa;
    Double lat, lng;
    float radio;
    Circle circulo = null;
    Slider sliderRadio;
    EditText txtLatitud, txtLongitud;
    Spinner spinnerCategoria, spinnerSubcategoria;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    RequestQueue requestQueue;
    ArrayList<String> idsCategoria = new ArrayList<>();
    ArrayList<String> idsSubcategoria = new ArrayList<>();
    String idCategoria = "";
    String idSubcategoria = "";
    boolean cargandoCategoria = false;
    boolean cargandoSubcategoria = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lat = -1.0123899038933037;
        lng = -79.46922232877745;
        radio = 1;

        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        sliderRadio = findViewById(R.id.sliderRadio);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerSubcategoria = findViewById(R.id.spinnerSubcategoria);

        requestQueue = Volley.newRequestQueue(this);

        sliderRadio.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                radio = slider.getValue();
                updateInterfaz();
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
            }
        });

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cargandoCategoria) {
                    return;
                }
                idCategoria = idsCategoria.get(position);
                cargarSubcategorias();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSubcategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cargandoSubcategoria) {
                    return;
                }
                idSubcategoria = idsSubcategoria.get(position);
                if (mapa != null) {
                    updateInterfaz();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cargarCategorias();
        cargarSubcategorias();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);

        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));

        mapa.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mapa.getCameraPosition().target;
                lat = center.latitude;
                lng = center.longitude;
                updateInterfaz();
            }
        });

        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        updateInterfaz();
    }

    private void cargarCategorias() {
        StringRequest request = new StringRequest(Request.Method.GET,
                BASE_URL + "categoria/getlistadoCB",
                result -> {
                    try {
                        ArrayList<String> nombres = new ArrayList<>();
                        idsCategoria.clear();
                        nombres.add(getString(R.string.todas));
                        idsCategoria.add("");
                        JSONArray lista = new JSONArray(result);
                        for (int i = 0; i < lista.length(); i++) {
                            JSONObject item = lista.getJSONObject(i);
                            nombres.add(item.getString("descripcion"));
                            idsCategoria.add(item.getString("id"));
                        }
                        cargandoCategoria = true;
                        spinnerCategoria.setAdapter(new ArrayAdapter<>(
                                MainActivity2.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                nombres));
                        cargandoCategoria = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                });
        requestQueue.add(request);
    }

    private void cargarSubcategorias() {
        idSubcategoria = "";
        if (idCategoria.isEmpty()) {
            ArrayList<String> nombres = new ArrayList<>();
            idsSubcategoria.clear();
            nombres.add(getString(R.string.todas));
            idsSubcategoria.add("");
            cargandoSubcategoria = true;
            spinnerSubcategoria.setAdapter(new ArrayAdapter<>(
                    MainActivity2.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    nombres));
            cargandoSubcategoria = false;
            if (mapa != null) {
                updateInterfaz();
            }
            return;
        }

        StringRequest request = new StringRequest(Request.Method.GET,
                BASE_URL + "subcategoria/getlistadoCB/" + idCategoria,
                result -> {
                    try {
                        ArrayList<String> nombres = new ArrayList<>();
                        idsSubcategoria.clear();
                        nombres.add(getString(R.string.todas));
                        idsSubcategoria.add("");
                        JSONArray lista = new JSONArray(result);
                        for (int i = 0; i < lista.length(); i++) {
                            JSONObject item = lista.getJSONObject(i);
                            nombres.add(item.getString("descripcion"));
                            idsSubcategoria.add(item.getString("id"));
                        }
                        cargandoSubcategoria = true;
                        spinnerSubcategoria.setAdapter(new ArrayAdapter<>(
                                MainActivity2.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                nombres));
                        cargandoSubcategoria = false;
                        if (mapa != null) {
                            updateInterfaz();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                });
        requestQueue.add(request);
    }

    private void updateInterfaz() {
        txtLatitud.setText(String.format("%.4f", lat));
        txtLongitud.setText(String.format("%.4f", lng));
        if (circulo != null) {
            circulo.remove();
            circulo = null;
        }
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(radio * 100)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 150, 50, 50));
        circulo = mapa.addCircle(circleOptions);

        String url = BASE_URL + "lugar_turistico/json_getlistadoMapa?lat=" + lat
                + "&lng=" + lng + "&radio=" + radio;
        if (!idCategoria.isEmpty()) {
            url += "&idc=" + idCategoria;
        }
        if (!idSubcategoria.isEmpty()) {
            url += "&idsc=" + idSubcategoria;
        }

        StringRequest request = new StringRequest(Request.Method.GET, url,
                this::processFinish,
                error -> {
                });
        requestQueue.add(request);
    }

    private boolean dentroDelCirculo(double markerLat, double markerLng) {
        float[] distancia = new float[1];
        Location.distanceBetween(lat, lng, markerLat, markerLng, distancia);
        return distancia[0] <= radio * 100;
    }

    private void processFinish(String result) {
        try {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();

            JSONObject JSONobj = new JSONObject(result);
            JSONArray jsonLista = JSONobj.getJSONArray("data");
            for (int i = 0; i < jsonLista.length(); i++) {
                JSONObject lugar = jsonLista.getJSONObject(i);
                double markerLat = Double.parseDouble(lugar.getString("lat"));
                double markerLng = Double.parseDouble(lugar.getString("lng"));
                if (!dentroDelCirculo(markerLat, markerLng)) {
                    continue;
                }
                markers.add(mapa.addMarker(
                        new MarkerOptions().position(
                                new LatLng(markerLat, markerLng)
                        ).title(lugar.getString("nombre"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
