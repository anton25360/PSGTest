package com.anton25360.psgtest

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*


val commentsArray: ArrayList<ArrayList<String>> = ArrayList() //empty array to put data from loop in

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING")
class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val videoId = intent.getStringExtra("videoId") //data from intent
        comments_RV.layoutManager = LinearLayoutManager(this) //set layout manager

        fetchData(videoId)
        fetchComments(videoId)
    }

    private fun fetchComments(videoId: String?) {
        val url ="https://www.googleapis.com/youtube/v3/commentThreads?key=AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k&part=snippet&videoId=$videoId"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val data: String? = response.body?.string() //response as a string
                val dataFromApi = JSONObject(data)["items"].toString() //convert to json object

                //loop through json array and print all titles:
                for (i in 0 until JSONArray(dataFromApi).length()) {
                    val mItems = JSONArray(dataFromApi)[i].toString() //gets the data of a single comment in array format
                    val mSnippet = JSONObject(mItems)["snippet"].toString()
                    val mTopLevelComment = JSONObject(mSnippet)["topLevelComment"].toString()
                    val mCommentSnippet = JSONObject(mTopLevelComment)["snippet"].toString()

                    val text = JSONObject(mCommentSnippet)["textOriginal"].toString()
                    val author = JSONObject(mCommentSnippet)["authorDisplayName"].toString()

                    val commentData: ArrayList<String> = ArrayList() //stores title, thumbnail url, and video ID of a single video for the adapter
                    commentData.add(text)
                    commentData.add(author)
                    commentsArray.add(commentData)
                }

                runOnUiThread {
                    // Stuff that updates the UI
                    comments_RV.adapter = CommentsAdapter(commentsArray) //apply adapter
                }

            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }


    private fun fetchData(videoId: String?) {

        val url = "https://www.googleapis.com/youtube/v3/videos?key=AIzaSyDY9AyHAa43UnviLtl0GaynGUmEyAvlr5k&id=$videoId&part=snippet,contentDetails"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            @SuppressLint("SimpleDateFormat")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                val data: String? = response.body?.string() //response as a string
                val dataFromApi = JSONObject(data)["items"].toString() //convert to json object
                val mItems =
                    JSONArray(dataFromApi)[0].toString() //gets the data of a single video in array format
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

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val date: Date = format.parse(datePublished)


                val d = Duration.parse(duration)
                val seconds = d[ChronoUnit.SECONDS]

                val numOfMinutes = seconds / 60
                val numOfSeconds = seconds - (numOfMinutes*60)
                val durationString = "$numOfMinutes minutes, $numOfSeconds seconds"

                runOnUiThread {
                    // updates video title, thumbnail, date published, duration, and description
                    detail_title.text = title
                    Picasso.get().load(url).into(detail_thumbnail)
                    detail_datePublished.text = date.toString()
                    detail_videoDuration.text = durationString
                    detail_description.text = description
                }
            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }
}