package com.anton25360.psgtest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


val TAG = "xxx"

@Suppress("NAME_SHADOWING", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k
        fetchData()
    }

    private fun fetchData() {

        Toast.makeText(this, "getting data...", Toast.LENGTH_SHORT).show()
        val url = "https://www.googleapis.com/youtube/v3/playlistItems?key=AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k&playlistId=UU_A--fhX5gea0i4UtpD99Gg&part=snippet&maxResults=5"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val data: String? = response.body?.string() //response as a string
                val dataFromApi = JSONObject(data)["items"].toString() //convert to json object

                val dataArray = emptyArray<Any>() //empty array to put data from loop in

                //loop through json array and print all titles:
                for (i in 0 until JSONArray(dataFromApi).length()) {
                    val videoData = JSONArray(dataFromApi)[i].toString() //gets the data of a single video
                    val mSnippet = JSONObject(videoData)["snippet"].toString()
                    val mThumbnails = JSONObject(mSnippet)["thumbnails"].toString()
                    val mDefault = JSONObject(mThumbnails)["default"].toString()
                    val mResourceId = JSONObject(mSnippet)["resourceId"].toString()

                    val title = JSONObject(mSnippet)["title"]
                    val url = JSONObject(mDefault)["url"].toString()
                    val videoId = JSONObject(mResourceId)["videoId"].toString()

                    Log.d(TAG, "onResponse: $title + $url + $videoId")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: $e")
            }
        })

    }
}