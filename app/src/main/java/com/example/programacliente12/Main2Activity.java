package com.example.programacliente12;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    TextView texto;
    TextView texto2;
    TextView textoCarpeta;
    ImageButton btn1;
    ImageButton btn2;
    RecyclerView lista;

    AlertDialog DialogFiltros;

    FTPClient mFTPClient = null;
    String[] fileList = {""};
    String aux3;
    private ArrayList<elemento> names;
    ArrayAdapter<String> adapter;
    adapter adap;
    String nombrearchivo;
    String nombrecarpeta="";
    String Usuariotxt,Contraseñatxt,Iptxt;
    boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        revisaPermisosAlmacenamiento();
        texto = findViewById(R.id.Usuario);
        texto2 = findViewById(R.id.Servidor);
        btn1 = findViewById(R.id.bo);
        btn2 = findViewById(R.id.bo2);
        lista = findViewById(R.id.lista);
        textoCarpeta=findViewById(R.id.carpeta);
        lista.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        names = new ArrayList<elemento>();

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        adap =new adapter(names);
        adap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(names.get(lista.getChildAdapterPosition(v)).getTipo()=="File")
                {
                    nombrearchivo= names.get(lista.getChildAdapterPosition(v)).getNombre();
                    DialogFiltro();

                    DialogFiltros.show();

                }
                else
                {
                    nombrecarpeta=nombrecarpeta+"/"+ names.get(lista.getChildAdapterPosition(v)).getNombre();
                    textoCarpeta.setText(nombrecarpeta);

                    names.clear();
                    new TestAsync6().execute();
                    new TestAsync().execute();
                }

            }
        });


       Usuariotxt= getIntent().getExtras().getString("usuario");
        Contraseñatxt= getIntent().getExtras().getString("contraseña");
        Iptxt= getIntent().getExtras().getString("ip");
        texto.setText(Usuariotxt);
        texto2.setText(Iptxt);
        new TestAsync2().execute();
        new TestAsync().execute();

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names.clear();
                nombrecarpeta="";
                textoCarpeta.setText(nombrecarpeta);
                new TestAsync2().execute();
                new TestAsync().execute();

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);

            }
        });



    }

    private void DialogFiltro()
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view =View.inflate(this,R.layout.dialog,null);
        TextView texto=view.findViewById(R.id.texto);
        Button  cancelar=view.findViewById(R.id.cancelar);

        Button  descargar=view.findViewById(R.id.descargar);

        texto.setText( nombrearchivo);

        //builder.setCancelable(false);
        builder.setView(view);
        DialogFiltros=builder.create();

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestAsync5().execute();
                DialogFiltros.dismiss();

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFiltros.dismiss();

            }
        });

    }

    private void revisaPermisosAlmacenamiento() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionChec2k = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }
    }


    class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            try {

                FTPFile[] ftpFiles = mFTPClient.listFiles("");
                int length = ftpFiles.length;
                fileList = new String[length];
                for (int i = 0; i < length; i++) {
                    String name = ftpFiles[i].getName();

                    aux3=aux3+"\n\n"+name;
                    boolean isFile = ftpFiles[i].isFile();

                    if (isFile) {
                        fileList[i] = "File :: " + name;
                        names.add(new elemento(name,"File"));

                    } else {
                        fileList[i] = "Directory :: " + name;
                        names.add(new elemento(name,"Directory"));

                    }



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

            Log.d(TAG + " onPostExecute", "" + result);
            adap.notifyDataSetChanged();
            lista.setAdapter(adap);

        }
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





        }
    }
    class TestAsync6 extends AsyncTask<Void, Integer, String> {

        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            try {


                mFTPClient.changeWorkingDirectory(nombrecarpeta);;


                // now check the reply code, if positive mean connection success
                if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                    // login using username & password
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




        }
    }


    class Hilo2 extends Thread {
        FileOutputStream fos = null;

        public void run(String nombre) {
            try {


                String filename = nombre;


                fos = new FileOutputStream(filename);


// Fetch file from server


                mFTPClient.retrieveFile("/" + filename, fos);

            } catch (IOException e) {


                e.printStackTrace();

            } finally {


                try {


                    if (fos != null) {


                        fos.close();


                    }


                    mFTPClient.disconnect();


                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }
    }

    class TestAsync4 extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            try {

                mFTPClient.enterLocalPassiveMode();
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);

                String data = "/sdcard/gato.jpg";
                FileInputStream in = new FileInputStream(new File(data));
                boolean result = mFTPClient.storeFile("gato.jpg", in);
                in.close();
                if (result) Log.v("upload result", "succeeded");
                mFTPClient.logout();


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

            Log.d(TAG + " onPostExecute", "" + result);
            adap.notifyDataSetChanged();
            lista.setAdapter(adap);

        }
    }

    class TestAsync5 extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute","On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");
            try {

                String remoteFile1 = nombrearchivo;
                String ExternalStorageDirectory = Environment.getExternalStorageDirectory() + File.separator;
                File downloadFile1 = new File(ExternalStorageDirectory+nombrearchivo );
                OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success =mFTPClient.retrieveFile(remoteFile1, outputStream1);
                outputStream1.close();

                if (success) {
                    System.out.println("File #1 has been downloaded successfully.");
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
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Se descargo el archivo  "+nombrearchivo, Toast.LENGTH_SHORT);

            toast1.show();


        }
    }

}
