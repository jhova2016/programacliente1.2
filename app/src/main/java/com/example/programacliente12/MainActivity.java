package com.example.programacliente12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    EditText IP;
    EditText Usuario;
    EditText Contraseña;
    TextView Alerta;
    Button Aceptar;
    String Usuariotxt="invitado2",Contraseñatxt="12345678",Iptxt="192.168.1.1";
    //String Usuariotxt,Contraseñatxt,Iptxt;
    FTPClient mFTPClient = null;
    boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IP=findViewById(R.id.IPEdt);
        Usuario=findViewById(R.id.emailEdt);
        Contraseña=findViewById(R.id.passwordEdt);
        Aceptar=findViewById(R.id.loginButton);
        Alerta=findViewById(R.id.Alerta);
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuariotxt=Usuario.getText().toString();
                Contraseñatxt=Contraseña.getText().toString();
                Iptxt=IP.getText().toString();

                new TestAsync2().execute();

                //startActivity(new Intent(getBaseContext(), Main2Activity.class));

            }
        });

    }


    class TestAsync2 extends AsyncTask<Void, Integer, String> {

        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            try {


                mFTPClient = new FTPClient();
                // connecting to the host
                mFTPClient.connect(Iptxt, 21);

                // now check the reply code, if positive mean connection success
                if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                    // login using username & password
                    status = mFTPClient.login(Usuariotxt, Contraseñatxt);
                    String filename = "3354371.jpg";
                    OutputStream os = new FileOutputStream(filename);
                    mFTPClient.retrieveFile(filename, os);
                    mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(status)
            {
                Alerta.setVisibility(Alerta.INVISIBLE);
                 Intent intent=new Intent(getBaseContext(), Main2Activity.class);
                intent.putExtra("usuario", Usuariotxt);
                intent.putExtra("contraseña", Contraseñatxt);
                intent.putExtra("ip", Iptxt);

                startActivity(intent);
            }
            else
            {

                Alerta.setVisibility(Alerta.VISIBLE);

            }



        }
    }

}
