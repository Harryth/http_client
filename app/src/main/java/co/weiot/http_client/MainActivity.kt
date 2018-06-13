package co.weiot.http_client

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URLEncoder
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.Display


class MainActivity : AppCompatActivity() {

    private val EXTRA_MESSAGE = "co.weiot.http_client.MESSAGE"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.textView)

        // Initialize queue
        val queue = Volley.newRequestQueue(this)

        ////////////////////////////////////////////////////////////////////////////////////////////
        //HTTP GET request

//        val url = "https://restserver-206003.appspot.com/user"

//        val stringRequest = StringRequest(Request.Method.GET, url,
//                Response.Listener<String> { response ->
//                    android.util.Log.i("HTTP", "Response: $response")
//
//                    val jsonResp = JSONObject(response)
//                    val jsonArray = jsonResp.getJSONArray("user")
//                    val jsonUser = jsonArray.getJSONObject(0)
//                    textView.text = "ok: ${jsonResp.getString("ok")}" + "\n" +
//                            "lastname: ${jsonUser.getString("lastname")}"
//                }, Response.ErrorListener { error ->
//            android.util.Log.e("HTTP", error.toString())
//        })

        //HTTP GET request
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //HTTP POST request for sign up

        val signupUri = "https://restserver-206003.appspot.com/signup"
        //val testUri = "https://restserver-206003.appspot.com/test"

        // Get data from GUI fields
        val firstname = findViewById<EditText>(R.id.editFirstName)
        val lastname = findViewById<EditText>(R.id.editLastName)
        val username = findViewById<EditText>(R.id.editUsername)
        val email = findViewById<EditText>(R.id.editMail)
        val pass = findViewById<EditText>(R.id.editPass)

        // Creates the request
        val jsonPOSTRequest = object: JsonObjectRequest(Request.Method.POST, signupUri, null,
                Response.Listener<JSONObject> { res -> // Prints response in console

                    textView.text = "Ok"
                    Log.d("HTTP", res.toString())
                },
                Response.ErrorListener { err -> // Prints error in console

                    val statusCode = err.networkResponse.statusCode // Prints error status code
                    val errMsg = String(err.networkResponse.data) // Prints error message

                    textView.text = "Fail"
                    Log.e("HTTP","$statusCode -> $errMsg")
        }){

            // Set Content-Type to x-www-form-urlencoded
            override fun getBodyContentType(): String {

                return "application/x-www-form-urlencoded; charset=$paramsEncoding"
            }

            // Set token to auth platform in the request header
            override fun getHeaders(): MutableMap<String, String> {

                val params = HashMap<String, String>()

                params.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzaWdudXAiLCJpc3MiOiJBbmRyb2lkIEFwcCIsImlhdCI6MTUxNjIzOTAyMn0.tjedUnTGBHqTD2II4OzErDTC-0pUPy1dHca8K58qqr8")

                return params
            }

            // Set body with encoded params
            override fun getBody(): ByteArray {

                val body = URLEncoder.encode("firstname", paramsEncoding) + "=" +
                        URLEncoder.encode("${firstname.text}", paramsEncoding) + "&" +
                        URLEncoder.encode("lastname", paramsEncoding) + "=" +
                        URLEncoder.encode("${lastname.text}", paramsEncoding) + "&" +
                        URLEncoder.encode("username", paramsEncoding) + "=" +
                        URLEncoder.encode("${username.text}", paramsEncoding) + "&" +
                        URLEncoder.encode("email", paramsEncoding) + "=" +
                        URLEncoder.encode("${email.text}", paramsEncoding) + "&" +
                        URLEncoder.encode("password", paramsEncoding) + "=" +
                        URLEncoder.encode("${pass.text}", paramsEncoding)


                return body.toByteArray()
            }

        }

        //HTTP POST request for sign up
        ////////////////////////////////////////////////////////////////////////////////////////////
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            queue.add(jsonPOSTRequest) // Add request to queue

            val intent = Intent(this, LoginActivity::class.java)
            // val editText = findViewById<View>(R.id.editText) as EditText
            val message = "Hola Mundo!"
            intent.putExtra(EXTRA_MESSAGE, message)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
