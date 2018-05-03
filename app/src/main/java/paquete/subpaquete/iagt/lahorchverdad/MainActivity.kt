package paquete.subpaquete.iagt.lahorchverdad

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.iagt.lahorchverdad.R
import paqueteCrear.example.iagt.lahorchverdad.activityCrear

import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class MainActivity : AppCompatActivity() {

    var listado: RecyclerView? =null
    var confesionGrupo=ArrayList<confesion>()
    var comunicadorUI:Handler= Handler()
    var rela:RelativeLayout?=null

    var selectTodos:String="select * from confesiones where aprobadaConfesion=1 order by idConfesion DESC"
    var selectPicante:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Picante' order by idConfesion DESC"
    var selectAmor:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Amor' order by idConfesion DESC"
    var selectSalud:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Salud' order by idConfesion DESC"
    var selectAmistad:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Amistad' order by idConfesion DESC"
    var selectFamilia:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Familia' order by idConfesion DESC"
    var selectTrabajo:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Trabajo' order by idConfesion DESC"
    var selectEstudio:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Estudios' order by idConfesion DESC"
    var selectDinero:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Dinero' order by idConfesion DESC"
    var selectOtros:String="select * from confesiones where aprobadaConfesion=1 AND categoria='Otros' order by idConfesion DESC"

    var seleccionado:String=selectTodos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        AsyncTaskBase().execute(selectTodos)


        listado=findViewById(R.id.listado)
        listado!!.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rela=findViewById(R.id.rela)


        fab.setOnClickListener { view ->
            var ventana:Intent= Intent(this, activityCrear::class.java)
            startActivity(ventana)
        }
        actu.setOnClickListener {

            listado!!.visibility=View.GONE
            rela!!.visibility=View.VISIBLE

            confesionGrupo=ArrayList<confesion>()
              AsyncTaskBase().execute(seleccionado)
            actu.hide()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        listado!!.visibility=View.GONE
        rela!!.visibility=View.VISIBLE

        var titulo:String=item.title.toString()
        confesionGrupo=ArrayList<confesion>()

        setTitle("LaHorchVerdad - "+titulo)
        if (titulo.equals("Todos")){
            AsyncTaskBase().execute(selectTodos)
            seleccionado=selectTodos
        }else if(titulo.equals("Picante")) {
            AsyncTaskBase().execute(selectPicante)
            seleccionado=selectPicante
        }else if(titulo.equals("Amor")) {
            AsyncTaskBase().execute(selectAmor)
            seleccionado=selectAmor
        }else if(titulo.equals("Trabajo")) {
            AsyncTaskBase().execute(selectTrabajo)
            seleccionado=selectTrabajo
        }else if(titulo.equals("Amistad")) {
            AsyncTaskBase().execute(selectAmistad)
            seleccionado=selectAmistad
        }else if(titulo.equals("Familia")) {
            AsyncTaskBase().execute(selectFamilia)
            seleccionado=selectFamilia
        }else if(titulo.equals("Dinero")) {
            AsyncTaskBase().execute(selectDinero)
            seleccionado=selectDinero
        }else if(titulo.equals("Otros")) {
            AsyncTaskBase().execute(selectOtros)
            seleccionado=selectOtros
        }else if(titulo.equals("Estudios")) {
            AsyncTaskBase().execute(selectEstudio)
            seleccionado=selectEstudio
        }else if(titulo.equals("Salud")) {
            AsyncTaskBase().execute(selectSalud)
            seleccionado=selectSalud
        }


        return true
    }


    inner class AsyncTaskBase: AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {

            try{

               val conn:Connection

                Class.forName("com.mysql.jdbc.Driver").newInstance()
                conn=DriverManager.getConnection("jdbc:mysql://sql2.freesqldatabase.com:3306/sql2233658", "sql2233658", "uK4*dD2%")


                var st:Statement=conn.createStatement()
                var rs:ResultSet=st.executeQuery(params[0])

                while (rs.next()){
                    val id:Int=rs.getInt(1)
                    val genero:String=rs.getString(2)
                    val confesion:String=rs.getString(3)
                    val categoria:String=rs.getString(4)
                    val fecha:String=rs.getString(5)
                    val votosIzq:Int=rs.getInt(6)
                    val votosDer:Int=rs.getInt(7)

                    confesionGrupo.add(confesion(id,genero,confesion,fecha,categoria,votosIzq,votosDer))
                }

                comunicadorUI.post(Runnable {
                    var adaptamiento=adaptadorCustomizado(confesionGrupo)
                    listado!!.adapter=adaptamiento
                    listado!!.visibility=View.VISIBLE
                    rela!!.visibility=View.GONE
                    actu.show()

                })

            }catch (e:Exception ){
                Log.e("Base datos conexion","Erro de : "+e.message)
            }
            return ""
        }
    }

}



data class confesion(val id:Int,val genero: String, val confesion: String,val fecha:String, val categoria:String,val votosIzq:Int,val votosDer:Int)








