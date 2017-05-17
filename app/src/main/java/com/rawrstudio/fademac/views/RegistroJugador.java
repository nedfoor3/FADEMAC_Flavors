package com.rawrstudio.fademac.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.rawrstudio.fademac.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RegistroJugador extends AppCompatActivity {

    private ImageView ivFotoRegistro, ivTomarFoto;
    private EditText etFechaDeNacimiento, etEdadAnnos;
    private DatePickerDialog etFechaNacDialog;
    private SimpleDateFormat dateFormat;
    private int anno, mes, dia, edadAnoos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_jugador);
        iniGuiRegistro();
        iniListenersRegistro();
    }

    public void iniGuiRegistro(){
        ivFotoRegistro = (ImageView) findViewById(R.id.iv_foto_registro);
        ivTomarFoto = (ImageView)findViewById(R.id.iv_tomar_foto);
        etFechaDeNacimiento = (EditText)findViewById(R.id.et_fecha_nacimiento_datos_generales);
        etFechaDeNacimiento.setInputType(InputType.TYPE_NULL);
        etFechaDeNacimiento.requestFocus();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    }

    public void iniListenersRegistro(){
        ivTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);

            }
        });


        etFechaDeNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar fechaSistema = Calendar.getInstance();
                anno = fechaSistema.get(Calendar.YEAR);
                mes = fechaSistema.get(Calendar.MONTH);
                dia = fechaSistema.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroJugador.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etFechaDeNacimiento.setText(i + "-" + (i1 +1)+ "-" + i2);
                    }

                }, anno, mes, dia);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bitmap mBitmap = (Bitmap) data.getExtras().get("data");
            ivFotoRegistro.setImageBitmap(mBitmap);

        } catch (NullPointerException e) {

        }

    }
}
