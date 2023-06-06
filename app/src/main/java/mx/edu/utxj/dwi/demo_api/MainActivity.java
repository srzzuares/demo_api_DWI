package mx.edu.utxj.dwi.demo_api;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button btnSave;
    private Button btnSearch;
    private Button btnDelete;
    private Button btnUpdate;
    private EditText etCodigoBarras,etDescripcion,etMarca,etprecioCompra,etprecioVenta,etExistencias;
    private ListView lvProducts;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private ArrayList<String> origenDatos= new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String url = "http://10.10.62.17:3300/";
    @SuppressLint("MissingInflatedId")
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

        // --------------------------------------------------------------- buscar
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

// --------------------------------------------------------------- guardar
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject producto= new JSONObject();
                try {
                    producto.put("codigobarras",etCodigoBarras.getText().toString());
                    producto.put("descripcion",etDescripcion.getText().toString());
                    producto.put("marca",etMarca.getText().toString());
                    producto.put("preciocompra",etprecioCompra.getText().toString());
                    producto.put("precioventa",etprecioVenta.getText().toString());
                    producto.put("existencias",etExistencias.getText().toString());
                }catch (JSONException e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                        Request.Method.POST,
                        url + "insert/" ,
                        producto,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Producto insertado"))
                                        Toast.makeText(MainActivity.this, "¡Producto insertado con exito", Toast.LENGTH_SHORT).show();
                                    etCodigoBarras.setText("");
                                    etDescripcion.setText("");
                                    etMarca.setText("");
                                    etprecioCompra.setText("");
                                    etprecioVenta.setText("");
                                    etExistencias.setText("");
                                    listProducts();
                                } catch (JSONException e){
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);
            }});


// --------------------------------------------------------------- actualizar
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject updatedProduct = new JSONObject();
                try {
                    updatedProduct.put("codigobarras", etCodigoBarras.getText().toString());
                    updatedProduct.put("descripcion", etDescripcion.getText().toString());
                    updatedProduct.put("marca", etMarca.getText().toString());
                    updatedProduct.put("preciocompra", etprecioCompra.getText().toString());
                    updatedProduct.put("precioventa", etprecioVenta.getText().toString());
                    updatedProduct.put("existencias", etExistencias.getText().toString());
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                JsonObjectRequest updateRequest = new JsonObjectRequest(
                        Request.Method.PUT,
                        url + "actualizar/" + etCodigoBarras.getText().toString(),
                        updatedProduct,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Producto actualizado")) Toast.makeText(MainActivity.this, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                    etCodigoBarras.setText("");
                                    etDescripcion.setText("");
                                    etMarca.setText("");
                                    etprecioCompra.setText("");
                                    etprecioVenta.setText("");
                                    etExistencias.setText("");
                                    listProducts();
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                requestQueue.add(updateRequest);
            }
        });


// --------------------------------------------------------------- eliminarsa
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigoBarras = etCodigoBarras.getText().toString();

                JsonObjectRequest deleteRequest = new JsonObjectRequest(
                        Request.Method.DELETE,
                        url + "borrar/" + codigoBarras,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Producto eliminado")) {
                                        Toast.makeText(MainActivity.this, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                        listProducts();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                etCodigoBarras.setText("");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue.add(deleteRequest);

            }
        });listProducts();









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
                                String codigobarras =response.getJSONObject(i).getString("codigobarras");
                                String descripcion = response.getJSONObject(i).getString("descripcion") ;
                                String marca = response.getJSONObject(i).getString("marca");
                                origenDatos.add(codigobarras +"::" +descripcion+"::"+marca);
                            } catch (JSONException e) {
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