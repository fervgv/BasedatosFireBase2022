package cl.isisur.basedatosfirebase2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cl.isisur.basedatosfirebase2022.Clases.Libro;


public class MainActivity extends AppCompatActivity {
        private List<Libro> ListLibro = new ArrayList<Libro>(); //se crea una lista llamada listlibro
        private List<String> ListLibroNombre = new ArrayList();
        ArrayAdapter<Libro> arrayAdapterLibro; //y un array adapter llamado libro
        ArrayAdapter<String> arrayAdapterString;

        //se crean las conexiones para el edittext, button y listview
        EditText eTNombre,eTEditorial;
        Button bTBoton, btEliminar;
        ListView lvListadoLibros;

    //conexion con la base de datos/////////////////////////////////////
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //////////////////////////////////////////////////////////////////

    @Override //tipica conexion entre xml y java
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTNombre=findViewById(R.id.eTNombre);
        eTEditorial=findViewById(R.id.eTEditorial);
        bTBoton=findViewById(R.id.bTAgregar);
        btEliminar=findViewById(R.id.btEliminar);
        lvListadoLibros=findViewById(R.id.lvListadoLibros);
        inicializarFireBase(); //inicializar la base de datos
        listarDatos(); //inicializar la lista de datos

        bTBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Libro libro = new Libro(); //se crea un objeto que se llama libro
                //libro.setIdAutor("11111"); aqui se agregan elementos libro
                libro.setIdAutor(UUID.randomUUID().toString()); //aqui genera un numero random, puede ser manual o
                //                                                depende de la manera en que este contruida la bd
                libro.setNombre(eTNombre.getText().toString()); //nombre del texto
                libro.setEstado(eTEditorial.getText().toString()); //y nombre del estado
                //asi es como se agrega un libro a la base de datos
                databaseReference.child("Libro").child(libro.getIdAutor()).setValue(libro);
                ////////////////     la tabla "Libro"^^     aqui se crea un hijo^^     aqui se^^
                //                                          de la tabla               agrega el libro


            }
        });


    }
    private void listarDatos() {
        databaseReference.child("Libro").addValueEventListener(new ValueEventListener() {
            //       preguntamos por el libro^^
            @Override
            /*obtenemos todo el listado de la tabla libros*/
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpiamos la lista de libros
             ListLibro.clear();
             for (DataSnapshot objs : snapshot.getChildren()){ //mientras no sea el ultimo hijo
                 //vamos a agregar los libros
                 Libro li =objs.getValue(Libro.class);
                 //y lo vamos a agregar a la lista libros
                 ListLibro.add(li);
                 ListLibroNombre.add(""+li.getNombre()+" "+li.getEstado());
                 //tenemos el array adapter de lisbros y los apunta a ((la lista de libros d))el xml, para que aparezcan en pantalla
                 arrayAdapterString =new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,ListLibroNombre);
                 //luego se asigna la lista a el array
                 lvListadoLibros.setAdapter(arrayAdapterString);
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //conectar a la BD y luego asignar la base de datos
    private void inicializarFireBase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase =FirebaseDatabase.getInstance(); //aqui se conecta
        databaseReference =firebaseDatabase.getReference(); //y aqui se asigna
    }
}