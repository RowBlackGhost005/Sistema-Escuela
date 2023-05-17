package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.receptor.Receptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn: Button = findViewById(R.id.btn_cola)



        btn.setOnClickListener {
            MiTareaAsync2().execute()
        }

        val boton: Button = findViewById(R.id.send_button)
        boton.setOnClickListener{
            MiTareaAsync().execute()
        }
    }
    inner class MiTareaAsync : AsyncTask<Void, Void, String>() {
        val mensaje: EditText = findViewById(R.id.message_edit_text)
        val et: EditText = findViewById(R.id.mensaje_cola)
        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactory.getDefault() as SSLSocketFactory, TrustAllCerts())
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
                .build()
            val request = Request.Builder()
                .url("http://192.168.0.153:8080/RESTNotificaciones/webresources/generic/hola/"+mensaje.text.toString())
                .build()
            val response = client.newCall(request).execute()
            return response.body?.string() ?: ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null) {
                // Procesar la respuesta recibida
                Log.d("RESPUESTA API", result)
                et.setText(result)

            } else {
                // Manejar la respuesta de error
                Log.e("ERROR API", "No se pudo obtener la respuesta")
            }
        }

        private inner class TrustAllCerts : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

    }
    inner class MiTareaAsync2 : AsyncTask<Void, Void, String>() {
        val cola: EditText = findViewById(R.id.name_cola)
        val et: EditText = findViewById(R.id.mensaje_cola)
        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactory.getDefault() as SSLSocketFactory, TrustAllCerts())
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
                .build()
            val request = Request.Builder()
                .url("http://192.168.0.153:8080/RESTNotificaciones/webresources/generic/recibir/"+cola.text.toString())
                .build()
            val response = client.newCall(request).execute()
            return response.body?.string() ?: ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null) {
                // Procesar la respuesta recibida
                Log.d("RESPUESTA API", result)
            } else {
                // Manejar la respuesta de error
                Log.e("ERROR API", "No se pudo obtener la respuesta")
            }
        }

        private inner class TrustAllCerts : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

    }
}