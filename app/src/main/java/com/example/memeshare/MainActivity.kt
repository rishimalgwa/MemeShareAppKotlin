package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentMemeUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }
    private fun loadmeme(){
        findViewById<ProgressBar>(R.id.progress_circular).visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,url, null,
            {
                    response ->
               currentMemeUrl = response.getString("url")
                Glide.with(this).load(currentMemeUrl).listener(
                    object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                            findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            findViewById<ProgressBar>(R.id.progress_circular).visibility = View.GONE
                            return false
                        }

                    }
                ).into(findViewById(R.id.memeImageView))
                              },
            {  Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()}
        )
       MySingleton.getInstance(this).addToRequestQueue(jsonRequest)
    }
    fun nextMemeOnClick(view: View) {
    loadmeme()
    }
    fun shareMemeOnClick(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this cool meme $currentMemeUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using")
        startActivity(chooser)
    }
}