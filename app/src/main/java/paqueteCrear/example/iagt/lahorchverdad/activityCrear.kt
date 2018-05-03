package paqueteCrear.example.iagt.lahorchverdad

import android.os.AsyncTask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.*
import com.example.iagt.lahorchverdad.R

import java.sql.Connection
import java.sql.DriverManager

import java.sql.Statement
import java.text.SimpleDateFormat

import java.util.*

class activityCrear : AppCompatActivity() {


    var spinnerGen: Spinner? = null
    var spinnerCla: Spinner? = null
    var confe:EditText?=null
    var btn:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        spinnerGen=findViewById(R.id.spiGenero)
        spinnerCla=findViewById(R.id.spiCategorias)
        confe=findViewById(R.id.txtconfe)
        btn=findViewById(R.id.btnNuevo)
        btn!!.setOnClickListener {
            click()
        }

        val list_of_generos = arrayOf("Mujer", "Hombre", "Otros")
        val list_of_clasificacion = arrayOf("Picante", "Salud", "Amistad","Amor","Familia","Trabajo","Estudios","Dinero","Otros")

        val adaptadorGenero = ArrayAdapter(this, android.R.layout.preference_category, list_of_generos)
        adaptadorGenero.setDropDownViewResource(android.R.layout.preference_category)
        spinnerGen!!.setAdapter(adaptadorGenero)

        val adaptadorClasi = ArrayAdapter(this, android.R.layout.preference_category, list_of_clasificacion)
        adaptadorClasi.setDropDownViewResource(android.R.layout.preference_category)
        spinnerCla!!.setAdapter(adaptadorClasi)

    }
    fun click() {
        val genero:String=spinnerGen!!.selectedItem.toString()
        val clasificacion:String=spinnerCla!!.selectedItem.toString()
        val fecha = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
        val secreto:String=confe!!.text.toString()

        AsyncTaskBase().execute("insert into confesiones(genero,confesion,categoria,fecha,votosIzquierda,votosDerecha,aprobadaConfesion) " +
                "values ('"+genero+"','"+secreto+"','"+clasificacion+"','"+fecha+"',"+0+","+0+","+0+")")
        onBackPressed()
    }
    inner class AsyncTaskBase: AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {
            try{
                val conn: Connection

                Class.forName("com.mysql.jdbc.Driver").newInstance()
                conn= DriverManager.getConnection("jdbc:mysql://sql2.freesqldatabase.com:3306/sql2233658", "sql2233658", "uK4*dD2%")


                var st: Statement =conn.createStatement()

                st.executeUpdate(params[0])


            }catch (e:Exception ){
                Log.e("Base datos conexion","Erro de : "+e.message)
            }
            return ""
        }
    }
}
