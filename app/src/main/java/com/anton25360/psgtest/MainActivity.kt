package com.anton25360.psgtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

val dataArray: ArrayList<ArrayList<String>> = ArrayList() //empty array to put data from loop in


@Suppress("NAME_SHADOWING", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_RV.layoutManager = LinearLayoutManager(this) //set layout manager
        fetchData()
    }


    private fun fetchData() { //gets data from yt api

        val url = "https://www.googleapis.com/youtube/v3/playlistItems?key=AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k&playlistId=UU_A--fhX5gea0i4UtpD99Gg&part=snippet&maxResults=10"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val data: String? = response.body?.string() //response as a string
                val dataFromApi = JSONObject(data)["items"].toString() //convert to json object

                //loop through json array and print all titles:
                for (i in 0 until JSONArray(dataFromApi).length()) {
                    val videoArray = JSONArray(dataFromApi)[i].toString() //gets the data of a single video in array format
                    val mSnippet = JSONObject(videoArray)["snippet"].toString()
                    val mThumbnails = JSONObject(mSnippet)["thumbnails"].toString()
                    val mDefault = JSONObject(mThumbnails)["maxres"].toString()
                    val mResourceId = JSONObject(mSnippet)["resourceId"].toString()

                    val title = JSONObject(mSnippet)["title"].toString()
                    val url = JSONObject(mDefault)["url"].toString()
                    val videoId = JSONObject(mResourceId)["videoId"].toString()

                    val videoData: ArrayList<String> = ArrayList() //stores title, thumbnail url, and video ID of a single video (for the adapter)
                    videoData.add(title)
                    videoData.add(url)
                    videoData.add(videoId)
                    dataArray.add(videoData)

                }

                runOnUiThread {
                    // Stuff that updates the UI
                    main_RV.adapter = MainAdapter(dataArray, this@MainActivity) //apply adapter

                }

            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })

    }

    fun onItemClick(position: Int) {
        val clickedItem = dataArray[position]
        openDetailActivity(clickedItem) //opens the detail activity, with the associated videoId
    }

    private fun openDetailActivity(item: java.util.ArrayList<String>) { //opens the detail activity, with the associated videoId
        val intent = Intent(this, DetailActivity::class.java)
        val videoId = item[2]
        intent.putExtra("videoId",videoId)
        startActivity(intent)

    }
}

