package com.rawrstudio.fademac.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rawrstudio.fademac.R;
import com.rawrstudio.fademac.models.Jugador;

import java.io.File;
import java.net.URI;
import java.util.Calendar;

public class RegistroJugador extends AppCompatActivity implements TextWatcher{
    private ImageView ivTomarFoto, avatarJudador;
    private Uri uri, uriFoto, subirFotoPerfil;
    private File file;
    private final int REQUEST_PERMISSION_CODE_CAMERA= 1, REQUEST_PERMISSION_CODE_ALMACENAMIENTO= 2;
    private Spinner spCategorias, spEntidades, spGenero;
    private String[] itemsCategoria, itemsGenero, itemsEntidad;
    private RadioGroup rgExperiencia;
    private RadioButton rbVeterano, rbNovato;
    private LinearLayout llProcedencia;
    private TextView tvCurp, tvEdad, tvFechaNac;
    private int anno, mes, dia;//, permissionCheck, contAnnos, contMes, contDias;
    private String procedencia, noVet, genero, armandoCurp ="", categoria, fechaNacCurp, siglasEntidad="";
    private EditText etEquipoActual, etEquipoProcedencia, etHomoclave, etPosicion, etPeso, etEstatura, etJerseyJugador, etFechaNac, etPrimerNombre, etSegundoNombre, etApellidoPaterno, etApellidoMaterno;
    private char cg, c14, c15, c16;
    private Button btnGuardar;
    private char consonantes[] = {'b','B','c','C','d','D','f','F','g','G','h','H','j','J','k','K','l','L','m','M','n','N','ñ','Ñ','p','P','q','Q','r','R','s','S','t','T','v','V','w','W','x','X','y','Y','z','Z'};
    private DatabaseReference mRef, jugadorRef;
    private boolean sinExperiencia = true, permisoCamara = true, permisoStorage = true;
    private StorageReference mStorageReference, mFotoParaCredencial;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_jugador);
        mRef = FirebaseDatabase.getInstance().getReference();
        jugadorRef = mRef.child("registros").child("jugadores");
        mStorageReference = FirebaseStorage.getInstance().getReference();

        iniGuiRegistro();
        iniListenersRegistro();
        comprobarPermisos();
    }

    public void iniGuiRegistro(){
        avatarJudador = (ImageView)findViewById(R.id.avatar_jugador);
        ivTomarFoto = (ImageView)findViewById(R.id.iv_tomar_foto);
        etEquipoActual = (EditText)findViewById(R.id.et_equipo_jugador);
        etEquipoProcedencia = (EditText)findViewById(R.id.et_equipo_procedencia);
        etJerseyJugador = (EditText)findViewById(R.id.et_jersey_jugador);
        etPosicion = (EditText)findViewById(R.id.et_posicion_jugador);
        etPeso = (EditText)findViewById(R.id.et_peso_jugador);
        etEstatura = (EditText)findViewById(R.id.et_altura_jugador);
        llProcedencia = (LinearLayout)findViewById(R.id.contenedor_procedencia);
        tvFechaNac = (TextView) findViewById(R.id.tv_fecha_nacimiento_datos_generales);
        tvEdad = (TextView)findViewById(R.id.tv_edad_jugador);
        tvCurp = (TextView)findViewById(R.id.tv_curp_jugador);
        etHomoclave = (EditText)findViewById(R.id.et_homoclave_jugador);
        rgExperiencia = (RadioGroup)findViewById(R.id.rg_experiencia);
        rbVeterano = (RadioButton)findViewById(R.id.rb_experiencia_veterano);
        rbNovato = (RadioButton)findViewById(R.id.rb_experiencia_novato);
        //rbVeterano.setChecked(true);
        spCategorias = (Spinner)findViewById(R.id.sp_categorias);
        spGenero = (Spinner)findViewById(R.id.sp_genero);
        spEntidades = (Spinner)findViewById(R.id.sp_entidades);
        etPrimerNombre = (EditText) findViewById(R.id.et_primer_nombre);
        etSegundoNombre = (EditText) findViewById(R.id.et_segundo_nombre);
        etApellidoPaterno = (EditText)findViewById(R.id.et_apellido_paterno);
        etApellidoMaterno = (EditText)findViewById(R.id.et_apellido_materno);
        btnGuardar = (Button)findViewById(R.id.btn_guardar_registro);

        tvFechaNac.addTextChangedListener(this);
        etPrimerNombre.addTextChangedListener(this);
        etApellidoPaterno.addTextChangedListener(this);
        etApellidoMaterno.addTextChangedListener(this);

        itemsCategoria = getResources().getStringArray(R.array.categorias_fademac);
        itemsGenero = getResources().getStringArray(R.array.genero);
        itemsEntidad = getResources().getStringArray(R.array.entidad_federativa);

        ArrayAdapter<String> adapadorSpinnerCategoria = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, itemsCategoria);
        adapadorSpinnerCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategorias.setAdapter(adapadorSpinnerCategoria);

        ArrayAdapter<String> adapadorSpinnerGenero = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, itemsGenero);
        adapadorSpinnerGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapadorSpinnerGenero);

        ArrayAdapter<String> adapadorSpinnerEntidad = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, itemsEntidad);
        adapadorSpinnerEntidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEntidades.setAdapter(adapadorSpinnerEntidad);

    }

    public void iniListenersRegistro(){
        ivTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permisoCamara == true && permisoStorage == true) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                Intent CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(Environment.getExternalStorageDirectory(), File.separator + "/fademac/registros/" +String.valueOf(System.currentTimeMillis())+".jpg");
                uri = Uri.fromFile(file);
                CamIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                CamIntent.putExtra("return-data",true);
                startActivityForResult(CamIntent,0);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Intent CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file = new File(Environment.getExternalStorageDirectory(), File.separator + "/fademac/registros/" +String.valueOf(System.currentTimeMillis())+".jpg");
                    String authorities = getApplicationContext().getPackageName() + ".fileProvider";
                    uri = FileProvider.getUriForFile(RegistroJugador.this, authorities, file);
                    CamIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    CamIntent.putExtra("return-data",true);
                    startActivityForResult(CamIntent,0);
                }

                } else {
                    alertDialogBasico();
                }


            }
        });

        tvFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar fechaSistema = Calendar.getInstance();
                anno = fechaSistema.get(Calendar.YEAR);
                mes = fechaSistema.get(Calendar.MONTH);
                dia = fechaSistema.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroJugador.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if(i1<10 && i2 <10){
                            tvFechaNac.setText(i + "/0" + (i1 +1)+ "/0" + i2);
                        } else if(i1<10){
                            tvFechaNac.setText(i + "/0" + (i1 +1)+ "/" + i2);
                        } else if(i2<10){
                            tvFechaNac.setText(i + "/" + (i1 +1)+ "/0" + i2);
                        } else{
                        tvFechaNac.setText(i + "/" + (i1 +1)+ "/" + i2);
                        }
                        /*String edadS = (anno -i).t;
                        etEdad.setText((anno-i));*/
                    }

                }, anno, mes, dia);
                datePickerDialog.show();
            }

        });

        rgExperiencia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(rbNovato.isChecked()){
                        llProcedencia.setVisibility(View.VISIBLE);
                    }
            }
        });

        rbNovato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noVet = getResources().getText(R.string.novato).toString();
                if(etEquipoProcedencia.getText().toString().equals("")){
                    procedencia = getResources().getText(R.string.novato).toString();
                } else {
                    procedencia = etEquipoProcedencia.getText().toString();
                }
            }
        });

        rbVeterano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinExperiencia = false;
                llProcedencia.setVisibility(View.GONE);
                noVet = getResources().getText(R.string.veterano).toString();
                procedencia = etEquipoActual.getText().toString();
            }
        });

        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = itemsCategoria[position].toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    cg = 'H';
                    genero = itemsGenero[position].toString();

                } else if (position == 2){
                    cg = 'M';
                    genero = itemsGenero[position].toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spEntidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        siglasEntidad = "AS";
                        break;
                    case 2:
                        siglasEntidad = "BC";
                        break;
                    case 3:
                        siglasEntidad = "BS";
                        break;
                    case 4:
                        siglasEntidad = "CC";
                        break;
                    case 5:
                        siglasEntidad = "DF";
                        break;
                    case 6:
                        siglasEntidad = "CL";
                        break;
                    case 7:
                        siglasEntidad = "CM";
                        break;
                    case 8:
                        siglasEntidad = "CS";
                        break;
                    case 9:
                        siglasEntidad = "CH";
                        break;
                    case 10:
                        siglasEntidad = "DG";
                        break;
                    case 11:
                        siglasEntidad = "GT";
                        break;
                    case 12:
                        siglasEntidad = "GR";
                        break;
                    case 13:
                        siglasEntidad = "HG";
                        break;
                    case 14:
                        siglasEntidad = "JC";
                        break;
                    case 15:
                        siglasEntidad = "MC";
                        break;
                    case 16:
                        siglasEntidad = "MN";
                        break;
                    case 17:
                        siglasEntidad = "MS";
                        break;
                    case 18:
                        siglasEntidad = "NT";
                        break;
                    case 19:
                        siglasEntidad = "NL";
                        break;
                    case 20:
                        siglasEntidad = "OC";
                        break;
                    case 21:
                        siglasEntidad = "PL";
                        break;
                    case 22:
                        siglasEntidad = "QT";
                        break;
                    case 23:
                        siglasEntidad = "QR";
                        break;
                    case 24:
                        siglasEntidad = "SP";
                        break;
                    case 25:
                        siglasEntidad = "SL";
                        break;
                    case 26:
                        siglasEntidad = "SR";
                        break;
                    case 27:
                        siglasEntidad = "TC";
                        break;
                    case 28:
                        siglasEntidad = "TS";
                        break;
                    case 29:
                        siglasEntidad = "TL";
                        break;
                    case 30:
                        siglasEntidad = "VZ";
                        break;
                    case 31:
                        siglasEntidad = "YN";
                        break;
                    case 32:
                        siglasEntidad = "ZS";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String primerNombre = etPrimerNombre.getText().toString();
                String segundoNombre = etSegundoNombre.getText().toString();
                String apellidoPaterno = etApellidoPaterno.getText().toString();
                String apelldioMaterno = etApellidoMaterno.getText().toString();
                String fechaDeNacimiento = tvFechaNac.getText().toString();
                String equipoActual = etEquipoActual.getText().toString();
                String curpCompleto = tvCurp.getText().toString() + etHomoclave.getText().toString();
                String posicion = etPosicion.getText().toString();
                String edad = tvEdad.getText().toString();
                String peso = etPeso.getText().toString();
                String estatura = etEstatura.getText().toString();
                String jersey = etJerseyJugador.getText().toString();
                Jugador nuevoJugador = new Jugador(primerNombre, segundoNombre, apellidoPaterno, apelldioMaterno, fechaDeNacimiento,
                        genero, equipoActual, procedencia, noVet, curpCompleto, posicion, edad, peso, estatura, jersey, categoria);
                jugadorRef.push().setValue(nuevoJugador);

                mFotoParaCredencial = mStorageReference.child("fotos_resgistros").child("fademac").child(file.toString());
                mFotoParaCredencial.putFile(uri);

                Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistroJugador.this, MainActivity.class));

            }
        });

    }

    public void RequestRuntimePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(RegistroJugador.this, android.Manifest.permission.CAMERA)) {
            Toast.makeText(this, "CAMERA permission allows us to access CAMERA app", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(RegistroJugador.this,new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0 && resultCode == RESULT_OK) {
            CropImage();
        } else if (requestCode == 1) {
            if(data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                avatarJudador.setBackground(null);
                avatarJudador.setImageBitmap(bitmap);
            }
        }
    }

    private void CropImage() {

        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 5);
            CropIntent.putExtra("aspectY", 6);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);
        } catch (ActivityNotFoundException ex) {

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        armandoCurp ="";
    }

    @Override
    public void afterTextChanged(Editable s) {
        int edadTemporalNum = 0;
        char c2;
        boolean primerVocal = false, primerConsonante = false, boolc14 = false, boolc15 = false, boolc16 = false;

        if(etApellidoPaterno.length()>0) {
            String subPrimerChar = etApellidoPaterno.getText().toString().substring(0, 1);
            if (subPrimerChar.equals("a") || subPrimerChar.equals("A") || subPrimerChar.equals("á") || subPrimerChar.equals("Á")) {
                subPrimerChar = "A";
            } else if (subPrimerChar.equals("e") || subPrimerChar.equals("E") || subPrimerChar.equals("é") || subPrimerChar.equals("É")) {
                subPrimerChar = "E";
            } else if (subPrimerChar.equals("i") || subPrimerChar.equals("I") || subPrimerChar.equals("í") || subPrimerChar.equals("Í")) {
                subPrimerChar = "I";
            } else if (subPrimerChar.equals("o") || subPrimerChar.equals("O") || subPrimerChar.equals("ó") || subPrimerChar.equals("Ó")) {
                subPrimerChar = "O";
            } else if (subPrimerChar.equals("u") || subPrimerChar.equals("U") || subPrimerChar.equals("ú") || subPrimerChar.equals("Ú") || subPrimerChar.equals("ü") || subPrimerChar.equals("Ü")){
                subPrimerChar = "U";
            }
            armandoCurp += subPrimerChar;

            for (int i = 1; i < etApellidoPaterno.length() && primerVocal == false; i++) {
                c2 = etApellidoPaterno.getText().toString().charAt(i);
                if (c2 == 'a' || c2 == 'A' || c2 == 'á' || c2 == 'Á') {
                    c2 = 'A';
                    armandoCurp += c2;
                    primerVocal = true;
                } else if (c2 == 'e' || c2 == 'E' || c2 == 'é' || c2 == 'É') {
                    c2 = 'E';
                    armandoCurp += c2;
                    primerVocal = true;
                } else if (c2 == 'i' || c2 == 'I' || c2 == 'í' || c2 == 'Í') {
                    c2 = 'I';
                    armandoCurp += c2;
                    primerVocal = true;
                } else if (c2 == 'o' || c2 == 'O' || c2 == 'ó' || c2 == 'Ó') {
                    c2 = 'O';
                    armandoCurp += c2;
                    primerVocal = true;
                } else if (c2 == 'u' || c2 == 'U' || c2 == 'ú' || c2 == 'Ú' || c2 == 'ü' || c2 == 'Ü') {
                    c2 = 'U';
                    armandoCurp += c2;
                    primerVocal = true;
                }
            }

            for (int i=1; i <etApellidoPaterno.length() && boolc14 == false; i++){
                for (int j=0; j < consonantes.length; j++ ){
                    c14 = etApellidoPaterno.getText().toString().charAt(i);
                    if(c14 == consonantes[j]){
                        boolc14 = true;
                    }

                }
            }
        }

        if(etApellidoMaterno.length()>0){
            String subTercerChar = etApellidoMaterno.getText().toString().substring(0,1);
            if (subTercerChar.equals("a") || subTercerChar.equals("A") || subTercerChar.equals("á") || subTercerChar.equals("Á")) {
                subTercerChar = "A";
            } else if (subTercerChar.equals("e") || subTercerChar.equals("E") || subTercerChar.equals("é") || subTercerChar.equals("É")) {
                subTercerChar = "E";
            } else if (subTercerChar.equals("i") || subTercerChar.equals("I") || subTercerChar.equals("í") || subTercerChar.equals("Í")) {
                subTercerChar = "I";
            } else if (subTercerChar.equals("o") || subTercerChar.equals("O") || subTercerChar.equals("ó") || subTercerChar.equals("Ó")) {
                subTercerChar = "O";
            } else if (subTercerChar.equals("u") || subTercerChar.equals("U") || subTercerChar.equals("ú") || subTercerChar.equals("Ú") || subTercerChar.equals("ü") || subTercerChar.equals("Ü")) {
                subTercerChar = "U";
            }
            armandoCurp += subTercerChar;

            for (int i=1; i <etApellidoMaterno.length() && boolc15 == false; i++){
                for (int j=0; j < consonantes.length; j++ ){
                    c15 = etApellidoMaterno.getText().toString().charAt(i);
                    if(c15 == consonantes[j]){
                        boolc15 = true;
                    }

                }
            }
        }

        if(etPrimerNombre.length()>0){
            String subPrimerNombre = etPrimerNombre.getText().toString().substring(0,1);
            if (subPrimerNombre.equals("a") || subPrimerNombre.equals("A") || subPrimerNombre.equals("á") || subPrimerNombre.equals("Á")) {
                subPrimerNombre = "A";
            } else if (subPrimerNombre.equals("e") || subPrimerNombre.equals("E") || subPrimerNombre.equals("é") || subPrimerNombre.equals("É")) {
                subPrimerNombre = "E";
            } else if (subPrimerNombre.equals("i") || subPrimerNombre.equals("I") || subPrimerNombre.equals("í") || subPrimerNombre.equals("Í")) {
                subPrimerNombre = "I";
            } else if (subPrimerNombre.equals("o") || subPrimerNombre.equals("O") || subPrimerNombre.equals("ó") || subPrimerNombre.equals("Ó")) {
                subPrimerNombre = "O";
            } else if (subPrimerNombre.equals("u") || subPrimerNombre.equals("U") || subPrimerNombre.equals("ú") || subPrimerNombre.equals("Ú")) {
                subPrimerNombre = "U";
            }
            armandoCurp += subPrimerNombre;

            for (int i=1; i <etPrimerNombre.length() && boolc16 == false; i++){
                for (int j=0; j < consonantes.length; j++ ){
                    c16 = etPrimerNombre.getText().toString().charAt(i);
                    if(c16 == consonantes[j]){
                        boolc16 = true;
                    }

                }
            }

        }

        if (tvFechaNac.getText().toString().length()>0) {
            String annoTemporal = tvFechaNac.getText().toString().substring(0,4);
            Calendar fechaSistema = Calendar.getInstance();
            anno = fechaSistema.get(Calendar.YEAR);
            int noAnnoTemporal = Integer.parseInt(annoTemporal);
            edadTemporalNum = anno - noAnnoTemporal;

            String annoCortoTemporal = tvFechaNac.getText().toString().substring(2,4);
            String mesTemporal = tvFechaNac.getText().toString().substring(5,7);
            String diaTemporal = tvFechaNac.getText().toString().substring(8,10);
            armandoCurp += annoCortoTemporal + mesTemporal + diaTemporal;

        }


        armandoCurp += cg;
        armandoCurp += siglasEntidad;
        armandoCurp += c14;
        armandoCurp += c15;
        armandoCurp += c16;
        tvEdad.setText(String.valueOf(edadTemporalNum));
        tvCurp.setText(armandoCurp.toUpperCase());


    }

    public void comprobarPermisos(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //startActivity(intentLLamada);
            //Toast.makeText(this, "1 Permiso Concedido", Toast.LENGTH_SHORT).show();

        } else {
            alertDialogBasico();
            //explicarUsoPermiso();
            //solicitarPermisoHacerLlamada();
        }
    }

    private void explicarUsoPermiso() {
        //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //Toast.makeText(this, "2.1 Explicamos razonadamente porque necesitamos el permiso", Toast.LENGTH_SHORT).show();
            //Explicarle al usuario porque necesitas el permiso (Opcional)
            alertDialogBasico();
        }
    }

    private void solicitarPermiso() {


        //Pedimos el permiso o los permisos con un cuadro de dialogo del sistema
        /*ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_PERMISSION_CODE_CAMERA);*/
        int PERMISSIONS_ALL = 2;
        String permissions[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_ALL);
        //Toast.makeText(this, "2.2 Pedimos el permiso con un cuadro de dialogo del sistema", Toast.LENGTH_SHORT).show();

    }

    public void alertDialogBasico() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Camara y almacenamiento");
        builder.setMessage("Se requiere permiso a la camara para el registro de los jugadores que sera guardado en la carpeta 'fademac/registros/'");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                solicitarPermiso();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permisoCamara = true;
            Toast.makeText(this, "Permiso a camara concedido", Toast.LENGTH_SHORT).show();
        } else {
            permisoCamara = false;
            Toast.makeText(this, "Permiso a camara denegado", Toast.LENGTH_SHORT).show();
        }


        if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            permisoStorage = true;
            Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show();
        } else {
            permisoStorage = false;
            Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
        }
        /*
        switch (requestCode){
            case REQUEST_PERMISSION_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permiso a camara concedido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso a camara denegado", Toast.LENGTH_SHORT).show();
                }
                //break;
            case REQUEST_PERMISSION_CODE_ALMACENAMIENTO:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
                }
                break;
        }*/
    }
}