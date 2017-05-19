package com.rawrstudio.fademac.views;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rawrstudio.fademac.R;

import java.io.File;
import java.util.Calendar;

public class RegistroJugador extends AppCompatActivity {
    private ImageView ivTomarFoto, avatarJudador;
    private Uri uri;
    private File file;
    private final int REQUEST_PERMISSION_CODE= 1;
    private Spinner spCategorias, spEntidades, spGenero;
    private String[] itemsCategoria, itemsGenero, itemsEntidad;
    private RadioGroup rgExperiencia;
    private RadioButton rbVeterano, rbNovato;
    private LinearLayout llProcedencia;
    private TextView etFechaNac, etEdad, tvCurp;
    private int anno, mes, dia, edad, contAnnos, contMes, contDias;
    private String armandoCurp ="", fechaNacCurp;
    private char c1, c2, c3, c4, cH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_jugador);

        int permissiionCheck = ContextCompat.checkSelfPermission(RegistroJugador.this, android.Manifest.permission.CAMERA);
        if(permissiionCheck == PackageManager.PERMISSION_DENIED){
            RequestRuntimePermission();
        }

        iniGuiRegistro();
        iniListenersRegistro();
        armandoCurp = c1 +"a"+ c2+"e" + c3+"r" + c4+"l" + "fecha";
    }

    public void iniGuiRegistro(){
        avatarJudador = (ImageView)findViewById(R.id.avatar_jugador);
        ivTomarFoto = (ImageView)findViewById(R.id.iv_tomar_foto);
        llProcedencia = (LinearLayout)findViewById(R.id.contenedor_procedencia);
        etFechaNac = (TextView)findViewById(R.id.et_fecha_nacimiento_datos_generales);
        etEdad = (TextView)findViewById(R.id.et_edad_jugador);
        tvCurp = (TextView)findViewById(R.id.tv_curp_jugador);
        rgExperiencia = (RadioGroup)findViewById(R.id.rg_experiencia);
        rbVeterano = (RadioButton)findViewById(R.id.rb_experiencia_veterano);
        rbNovato = (RadioButton)findViewById(R.id.rb_experiencia_novato);
        rbVeterano.setChecked(true);
        spCategorias = (Spinner)findViewById(R.id.sp_categorias);
        spGenero = (Spinner)findViewById(R.id.sp_genero);
        spEntidades = (Spinner)findViewById(R.id.sp_entidades);
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
                Intent CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(Environment.getExternalStorageDirectory(),
                        "file"+String.valueOf(System.currentTimeMillis())+".jpg");
                uri = Uri.fromFile(file);
                CamIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                CamIntent.putExtra("return-data",true);
                startActivityForResult(CamIntent,0);

            }
        });

        etFechaNac.setOnClickListener(new View.OnClickListener() {
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
                            etFechaNac.setText(i + " / 0" + (i1 +1)+ " / 0" + i2);
                        } else if(i1<10){
                            etFechaNac.setText(i + " / 0" + (i1 +1)+ " / " + i2);
                        } else if(i2<10){
                            etFechaNac.setText(i + " / " + (i1 +1)+ " / 0" + i2);
                        } else{
                        etFechaNac.setText(i + " / " + (i1 +1)+ " - /" + i2);
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
                visivilidadExperiencia();
                tvCurp.setText(armandoCurp);
            }
        });


    }

    public void RequestRuntimePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(RegistroJugador.this, android.Manifest.permission.CAMERA))
            Toast.makeText(this,"CAMERA permission allows us to access CAMERA app", Toast.LENGTH_SHORT).show();
        else
        {
            ActivityCompat.requestPermissions(RegistroJugador.this,new String[]{android.Manifest.permission.CAMERA},REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK)
            CropImage();
        else if(requestCode == 2)
        {
            if(data != null)
            {
                uri = data.getData();
                CropImage();
            }
        }
        else if (requestCode == 1)
        {
            if(data != null)
            {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
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

    private void visivilidadExperiencia() {
        if(rbNovato.isChecked()){
            llProcedencia.setVisibility(View.VISIBLE);

        } else{
            llProcedencia.setVisibility(View.GONE);
        }
    }


    }
