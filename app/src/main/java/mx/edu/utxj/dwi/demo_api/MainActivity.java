package mx.edu.utxj.dwi.demo_api;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String url = "http://10.10.62.17:3300/";
    private Button btnSave, btnDelete, btnUpdate, btnSearch;
    private EditText etCodigoBarras,etDescripcion,etMarca,etprecioCompra,etprecioVenta,etExistencias;
    private ListView lvProducts;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private ArrayList<String> origenDatos= new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSave=findViewById(R.id.btnSave);
        btnSearch=findViewById(R.id.btnSearch);
        btnDelete=findViewById(R.id.btnDelete);
        btnUpdate=findViewById(R.id.btnUpdate);
        etCodigoBarras=findViewById(R.id.etCodigoBarras);
        etDescripcion=findViewById(R.id.etDescripcion);
        etMarca=findViewById(R.id.etMarca);
        etprecioCompra=findViewById(R.id.etprecioCompra);
        etprecioVenta=findViewById(R.id.etprecioVenta);
        etExistencias=findViewById(R.id.etExistencias);
        requestQueue= Volley.newRequestQueue(this);
        lvProducts=findViewById(R.id.lvProducts);
        listProducts();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest peticion = new JsonObjectRequest(
                        Request.Method.GET,
                        url + etCodigoBarras.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status"))
                                    Toast.makeText(MainActivity.this, "PRODUCTO NO ENCONTRADO", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        etDescripcion.setText(response.getString("descripcion"));
                                        etMarca.setText(response.getString("marca"));
                                        etprecioCompra.setText(String.valueOf(response.getInt("preciocompra")));
                                        etprecioVenta.setText(String.valueOf(response.getInt("precioventa")));
                                        etExistencias.setText(String.valueOf(response.getInt("existencias")));
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue.add(peticion);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject insertarDAtos = new JSONObject();
                try {
                    insertarDAtos.put("codigobarras",etCodigoBarras.getText().toString());
                    insertarDAtos.put("descripcion",etDescripcion.getText().toString());
                    insertarDAtos.put("marca",etMarca.getText().toString());
                    insertarDAtos.put("preciocompra",Integer.parseInt(etprecioCompra.getText().toString()));
                    insertarDAtos.put("precioventa",Integer.parseInt(etprecioVenta.getText().toString()));
                    insertarDAtos.put("existencias",Integer.parseInt(etExistencias.getText().toString()));
                }catch (JSONException e){
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url+"insert/",
                        insertarDAtos,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Producto Insertado"))
                                        Toast.makeText(MainActivity.this,"Producto insertado con éxito", Toast.LENGTH_SHORT).show();
                                    listProducts();
                                }catch (JSONException e){
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                requestQueue.add(jsonObjectRequest);
            }
        });
    }

    protected void listProducts(){

        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        for(int i = 0;i < response.length(); i++){
                            try {
                                String codigobarras = response.getJSONObject(i).getString("codigobarras");
                                String descripcion = response.getJSONObject(i).getString("descripcion") ;
                                String marca = response.getJSONObject(i).getString("marca");
                                origenDatos.add(codigobarras+">>"+descripcion+"::"+marca);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, origenDatos);
                        lvProducts.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}