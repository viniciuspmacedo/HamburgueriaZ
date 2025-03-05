package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText mEditNome;
    private CheckBox mCheckBoxBacon;
    private CheckBox mCheckBoxQueijo;
    private CheckBox mCheckBoxOnionRings;
    private Button mButtonSomar;
    private Button mButtonSubtrair;
    private Button mButtonFazerPedido;
    private TextView mTextQuantidade;
    private TextView mTextResumoPedido;

    private int quantidade = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mEditNome = findViewById(R.id.editTextNome);
        mCheckBoxBacon = findViewById(R.id.checkBoxBacon);
        mCheckBoxQueijo = findViewById(R.id.checkBoxQueijo);
        mCheckBoxOnionRings = findViewById(R.id.checkBoxOnionRings);
        mButtonSomar = findViewById(R.id.btnSomar);
        mButtonSubtrair = findViewById(R.id.btnSubtrair);
        mButtonFazerPedido = findViewById(R.id.btnFazerPedido);
        mTextQuantidade = findViewById(R.id.textViewQuantidade);
        mTextResumoPedido = findViewById(R.id.textViewResumoPedido);

        mButtonSomar.setOnClickListener(aoSomar);
        mButtonSubtrair.setOnClickListener(aoSubtrair);

        mCheckBoxBacon.setOnCheckedChangeListener(aoSelecionar);
        mCheckBoxQueijo.setOnCheckedChangeListener(aoSelecionar);
        mCheckBoxOnionRings.setOnCheckedChangeListener(aoSelecionar);

        mButtonFazerPedido.setOnClickListener(aoFazerPedido);

        mostraQuantidade();
    }

    private void somar() {
        quantidade++;
    }

    private void subtrair() {
        if (quantidade > 0) {
            quantidade--;
        }
    }

    private void mostraQuantidade() {
        mTextQuantidade.setText(String.valueOf(quantidade));
    }

    public View.OnClickListener aoSomar = view -> {
        somar();
        mostraQuantidade();
        mostraResumo();
    };

    public View.OnClickListener aoSubtrair = view -> {
        subtrair();
        mostraQuantidade();
        mostraResumo();
    };

    public View.OnClickListener aoFazerPedido = view -> {
        enviaEmail();
    };

    private void mostraResumo() {
        if (!mEditNome.getText().toString().isEmpty()) {
            String nome = mEditNome.getText().toString();
            String temBacon = mCheckBoxBacon.isChecked() ? "Sim" : "Não";
            String temQueijo = mCheckBoxQueijo.isChecked() ? "Sim" : "Não";
            String temOnionsRings = mCheckBoxOnionRings.isChecked() ? "Sim" : "Não";
            float total = (20F + (mCheckBoxBacon.isChecked() ? 2f : 0f) +
                    (mCheckBoxQueijo.isChecked() ? 2f : 0f) + (mCheckBoxOnionRings.isChecked() ? 3f : 0f))
                    * quantidade;
            mTextResumoPedido.setText(String.format("%s\nTem Bacon? %s\nTem Queijo? %s\n" +
                            "Tem Onion Rings? %s\nQuantidade: %d\nPreço final R$ %.2f", nome,
                    temBacon, temQueijo, temOnionsRings, quantidade, total));
        } else {
            Toast.makeText(this, "Por favor, digite seu nome", Toast.LENGTH_SHORT).show();
        }

    }

    private void enviaEmail() {
        if (!mEditNome.getText().toString().isEmpty()) {
            String email = "hamburgueriaz@email.com";
            String subject = "Pedido de " + mEditNome.getText().toString();
            String body = mTextResumoPedido.getText().toString();

            // Codifica o assunto e o corpo para a URL
            String uriText = "mailto:" + email +
                    "?subject=" + Uri.encode(subject) +
                    "&body=" + Uri.encode(body);

            Uri uri = Uri.parse(uriText);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(uri);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, ("Pedido de "
                    + mEditNome.getText().toString()));
            emailIntent.putExtra(Intent.EXTRA_TEXT, mTextResumoPedido.getText().toString());

            try {
                startActivity(Intent.createChooser(emailIntent, "Enviando email..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "Não há cliente de email instalado",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Por favor preencha seu nome", Toast.LENGTH_SHORT).show();
        }
    }

    public CompoundButton.OnCheckedChangeListener aoSelecionar = (buttonView, isChecked) -> {
        mostraResumo();
    };
}