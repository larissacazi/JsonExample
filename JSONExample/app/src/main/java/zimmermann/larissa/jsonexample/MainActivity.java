package zimmermann.larissa.jsonexample;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button botaoEnviar;
    private EditText unidadeEntrada, unidadeSaida, valorEntrada;
    private TextView saidaValorConverter, saidaUnidadeConverter, saidaUnidadeConvertida, saidaValorConvertido;
    ServerResponse resposta = new ServerResponse();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unidadeEntrada = (EditText) findViewById(R.id.edittext_unidade_entrada);
        unidadeSaida = (EditText) findViewById(R.id.edittext_unidade_saida);
        valorEntrada = (EditText) findViewById(R.id.edittext_valor);
        saidaValorConverter = (TextView) findViewById(R.id.textview_valorconverter);
        saidaUnidadeConverter = (TextView) findViewById(R.id.textview_unidadeconverter);
        saidaUnidadeConvertida = (TextView) findViewById(R.id.textview_unidadeconvertida);
        saidaValorConvertido = (TextView) findViewById(R.id.textview_valorconvertido);
        botaoEnviar = (Button) findViewById(R.id.button_enviar);

        listenersButtons();
    }

    /**
     * Chama os listeners para os botões
     */
    public void listenersButtons() {

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = new ProgressDialog(MainActivity.this);
                progress.setTitle("Enviando...");
                progress.show();


                //pega os valores dos edittextos
                String unidadeE = unidadeEntrada.getText().toString();
                String unidadeS = unidadeSaida.getText().toString();
                String valor = valorEntrada.getText().toString();

                //chama o retrofit para fazer a requisição no webservice
                retrofitConverter(unidadeE, valor, unidadeS);

            }
        });
    }

    public void setaValores(){

        saidaUnidadeConverter.setText(unidadeEntrada.getText().toString());
        saidaValorConverter.setText(valorEntrada.getText().toString());
        saidaUnidadeConvertida.setText(unidadeSaida.getText().toString());
        saidaValorConvertido.setText(resposta.getResult());

    }


    public void retrofitConverter(String unidadeEnt, String valorEnt, String unidadeSai) {

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<ServerResponse> call = service.converterUnidade(unidadeEnt, valorEnt, unidadeSai);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.isSuccessful()) {

                    ServerResponse respostaServidor = response.body();

                    //verifica aqui se o corpo da resposta não é nulo
                    if (respostaServidor != null) {

                        if(respostaServidor.isValid()) {

                            resposta.setFrom_type(respostaServidor.getFrom_type());
                            resposta.setFrom_value(respostaServidor.getFrom_value());
                            resposta.setResult(respostaServidor.getResult());
                            resposta.setTo_type(respostaServidor.getTo_type());
                            resposta.setValid(respostaServidor.isValid());

                            progress.dismiss();
                            setaValores(); //Coloca todos os valores

                        } else{

                            Toast.makeText(getApplicationContext(),"Insira unidade e valores válidos", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(getApplicationContext(),"Resposta nula do servidor", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    // segura os erros de requisição
                    ResponseBody errorBody = response.errorBody();
                }

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
