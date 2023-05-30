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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    protected void listProducts(){
        String url = "http://10.10.62.17:3300/";
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
                                String descripcion = response.getJSONObject(i).getString("descripcion") ;
                                String marca = response.getJSONObject(i).getString("marca");
                                origenDatos.add(descripcion+"::"+marca);
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