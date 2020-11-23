package com.anton25360.psgtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_row.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val videoId = intent.getStringExtra("videoId") //data from intent

        fetchData(videoId)
    }


    private fun fetchData(videoId: String?) {

        Toast.makeText(this, "getting data...", Toast.LENGTH_SHORT).show()

        val url = "https://www.googleapis.com/youtube/v3/videos?key=AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k&id=$videoId&part=snippet,contentDetails"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val data: String? = response.body?.string() //response as a string
                val dataFromApi = JSONObject(data)["items"].toString() //convert to json object
                val mItems = JSONArray(dataFromApi)[0].toString() //gets the data of a single video in array format
                val mSnippet = JSONObject(mItems)["snippet"].toString()
                val mThumbnails = JSONObject(mSnippet)["thumbnails"].toString()
                val mDefault = JSONObject(mThumbnails)["maxres"].toString()
                val mContentDetails = JSONObject(mItems)["contentDetails"].toString()

                //values
                val title = JSONObject(mSnippet)["title"].toString()
                val datePublished = JSONObject(mSnippet)["publishedAt"].toString()
                val description = JSONObject(mSnippet)["description"].toString()
                val url = JSONObject(mDefault)["url"].toString()
                val duration = JSONObject(mContentDetails)["duration"].toString()
//                detail_textView. text = "$title + $datePublished + $description + $url + $duration"

                runOnUiThread {
                    // Stuff that updates the UI
                    detail_title.text = title
                    Picasso.get().load(url).into(detail_thumbnail)


                }






                //loop through json array and print all titles:
//                for (i in 0 until JSONArray(dataFromApi).length()) {
//                    val videoArray = JSONArray(dataFromApi)[i].toString() //gets the data of a single video in array format
//                    val mSnippet = JSONObject(videoArray)["snippet"].toString()
//                    val mThumbnails = JSONObject(mSnippet)["thumbnails"].toString()
//                    val mDefault = JSONObject(mThumbnails)["maxres"].toString()
//                    val mResourceId = JSONObject(mSnippet)["resourceId"].toString()
//
//                    val title = JSONObject(mSnippet)["title"].toString()
//                    val url = JSONObject(mDefault)["url"].toString()
//                    val videoId = JSONObject(mResourceId)["videoId"].toString()
//
//                    val videoData: ArrayList<String> = ArrayList() //stores title, thumbnail url, and video ID of a single video
//                    videoData.add(title)
//                    videoData.add(url)
//                    videoData.add(videoId)
//
//                    Log.d(TAG, "onResponse: $title + $url + $videoId = $videoData")
//                    dataArray.add(videoData)
//
//                }
//
//                runOnUiThread {
//                    // Stuff that updates the UI
//                    main_RV.adapter = MainAdapter(dataArray, this@MainActivity) //apply adapter, put array in the ()
//
//                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: $e")
            }
        })

    }

}