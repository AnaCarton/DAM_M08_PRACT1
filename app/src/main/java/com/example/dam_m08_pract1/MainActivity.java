package com.example.dam_m08_pract1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    //Se declaran las variables
    EditText editTextNumberDecimal;
    Spinner spinnerOrigen, spinnerDestino;
    TextView textViewOrig, textViewDestino, textViewValorIntroducido, textViewConvertido, textViewResultadoConv;
    Button buttonConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicialización de las variables a partir de los elementos xml con findViewById por los identificadores
        editTextNumberDecimal = findViewById(R.id.valorIntroducido);

        textViewOrig = findViewById(R.id.textViewOrige);
        textViewDestino = findViewById(R.id.textViewDestino);
        textViewValorIntroducido = findViewById(R.id.textViewValorIntroducido);
        textViewConvertido = findViewById(R.id.buttonConvert);
        textViewResultadoConv = findViewById(R.id.convertedValues);

        spinnerOrigen = findViewById(R.id.spinnerOrigen);
        spinnerDestino = findViewById(R.id.spinnerDestino);

        buttonConvert = findViewById(R.id.buttonConvert);


        //Los spinner son desplegables con los siguientes elementos:
        // 1. la lista de valores posibles (distintas unidades métricas) que tiene el desplegable --> Array de String

        String[] unidades = {"", "Kilómetro cuadrado", "Metro cuadrado", "Milla cuadrada",
                "Yarda cuadrada", "Pie cuadrado", "Pulgada cuadrada", "Hectárea", "Acre"};
        
        //2. un adaptador predefinido (elemento intermedio entre los datos y el spinner)
        ArrayAdapter<String> adaptador = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, unidades);


        // Se rellenan los spinner con el array de unidades

        spinnerOrigen.setAdapter(adaptador);

        spinnerDestino.setAdapter(adaptador);

        //Una vez definidas e inicializadas las variables, se pueden hacer acciones



        //Se usa el km^2 como unidad única a partir del cual realizar la conversión de unidades
        //Array de conversión a unidad única(km^2):

        Double[] indiceConversionDesdeKm2 = {0.0, 1.0, 0.000001, 2.58998811, (1/1195990.0), (1/10763910.41670972), (1/1550003100.0062), 0.01, 0.00404686};



        // Se guardan en el array las conversiones de las unidades definidades en el array 'unidades'
        //según su posición con respecto a km cuadrados:


        //[1] --> convertir de km^2 a km^2, [2] --> convertir de km^2 a m^2
        //[3]--> convertir de km^2 a milla^2, [4] --> convertir de km^2 a yarda^2
        //[5]--> convertir de km^2 a pie^2, [6] --> convertir de km^2 a pulgada^2
        //[7]--> convertir de km^2 a hectárea, [8] --> convertir de km^2 a acre



        //Cuando se hace clic sobre el botón de convertir, se genera otro evento
        //y se ejecuta el siguiente código
        buttonConvert.setOnClickListener(new View.OnClickListener() {

            //La forma más dinámica de poder llamar a un botón/generar una acción sobre el botón
            @Override
            public void onClick(View view) {

                if ((spinnerOrigen.getSelectedItemPosition() == 0) || (spinnerDestino.getSelectedItemPosition() == 0)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setMessage("Necesita seleccionar las unidades de origen y de destino para realizar la conversión.").show();

                    //Para impedir que los valores de los desplegables estén vacíos seleccionamos por defecto un valor en cada spinner
                    spinnerOrigen.setSelection(1);
                    spinnerDestino.setSelection(2);

                }
                if (editTextNumberDecimal.getText().toString().isEmpty()) {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog2.setMessage("Necesita introducir un valor para realizar la conversión.").show();

                    //Se muestra un error en el textView donde se introduce el valor a convertir
                    //editTextNumberDecimal.setError("Necesita introducir un valor para realizar la conversión.");

                    //Si no introducen un valor en el campo, se podría poner el valor por defecto a 1
                    //editTextNumberDecimal.setText("1");

                }

                //Se intenta parsear el valor introducido a double porque tiene que ser un valor numérico
                try {
                    String valorIntroducido = editTextNumberDecimal.getText().toString();

                    double valorIntroducidoDecimal = Double.parseDouble(valorIntroducido);

                    // Se crean dos variables para guardar las posiciones de los items seleccionados en los spinners de origen y de destino
                    int spinnerOrigenItemPosition = spinnerOrigen.getSelectedItemPosition();
                    int spinnerDestinoItemPosition = spinnerDestino.getSelectedItemPosition();

                    // Se corrobora que el valor (double) sea superior a 0 (no se pueden meter valores negativos)

                    if (valorIntroducidoDecimal > 0) {
                        //Al comprobar que sea positivo el valor, se pasa a km2 (la unidad "estándar")
                        // a partir del cual se calcula el resto de unidades métricas

                        Double resultadoConversion = valorIntroducidoDecimal * ( indiceConversionDesdeKm2[spinnerOrigenItemPosition]/indiceConversionDesdeKm2[spinnerDestinoItemPosition]);

                        // Se pide que el resultado de la conversión solo tenga dos decimales
                        BigDecimal resultadoConversionBigDecimal = new BigDecimal(resultadoConversion).setScale(2, RoundingMode.HALF_UP);
                        resultadoConversion = resultadoConversionBigDecimal.doubleValue();


                        //Cuando se obtiene el resultado de la conversión, se muestra en el textViewResultadoConv
                        //indicando también las unidades métricas

                        textViewResultadoConv.setText("Resultado Conversión:" +"\n" + resultadoConversion + " " + unidades[spinnerDestinoItemPosition] + " " );

                    } else
                    //Si el valor introducido es negativo
                    {
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                        alertDialog2.setMessage("El valor introducido no puede ser negativo.").show();
                    }

                } catch (Exception ex1) {
                    //Si no se puede parsear a double es que no han introducido un valor numérico
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog2.setMessage("Necesita introducir un valor numérico válido para realizar la conversión.").show();

                }


            }


    });

    }
}