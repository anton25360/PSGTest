package com.anton25360.psgtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k
        fetchData()
    }

    private fun fetchData() {

        Toast.makeText(this, "getting data...", Toast.LENGTH_SHORT).show()
        val url = "https://www.googleapis.com/youtube/v3/playlistItems?key=AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k&playlistId=UU_A--fhX5gea0i4UtpD99Gg&part=snippet&maxResults=50"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val data: String? = response.body?.string() //response as a string
                val jsonArray = JSONArray(data) //convert to json array

                Log.d("TAG", "onResponse: $data")

                //loop through original json array:
//                for (i in 0 until jsonArray.length()) {
//                    val item = jsonArray.getJSONObject(i)
//
//                    if (item.get("available") == "yes") { //if item is available
//                        availableItems.add(item.get("item_name")) //add it to the array
//                    }
//                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println(e)
            }
        })

    }
}