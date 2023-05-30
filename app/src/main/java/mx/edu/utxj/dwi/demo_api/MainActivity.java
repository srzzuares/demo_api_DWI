package mx.edu.utxj.dwi.demo_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private Button btnSave, btnSerach,btnDelete,btnUpdate;
    private EditText etCodigoBarras,etDescripcion,etMarca,etprecioCompra,etprecioVenta,etExistencias;
    private ListView lvProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bottons for actions
        btnSave.findViewById(R.id.btnSave);
        btnSerach.findViewById(R.id.btnSearch);
        btnDelete.findViewById(R.id.btnDelete);
        btnUpdate.findViewById(R.id.btnUpdate);
        //Select in text from ActivityMain
        etCodigoBarras.findViewById(R.id.etCodigoBarras);
        etDescripcion.findViewById(R.id.etDescripcion);
        etMarca.findViewById(R.id.etMarca);
        etprecioCompra.findViewById(R.id.etprecioCompra);
        etprecioVenta.findViewById(R.id.etprecioVenta);
        etExistencias.findViewById(R.id.etExistencias);
        //List Products
        lvProducts.findViewById(R.id.lvProducts);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}