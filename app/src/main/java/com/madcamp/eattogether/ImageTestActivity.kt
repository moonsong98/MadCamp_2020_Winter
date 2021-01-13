package com.madcamp.eattogether

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ImageTestActivity : AppCompatActivity() {
    lateinit var recyclerView:RecyclerView
    lateinit var sendButton: Button
    lateinit var getButton: Button
    lateinit var imageView : ImageView
    var images = ArrayList<String>()
    companion object {
        private val REQUEST_READ_STORACE: Int = 101
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_test)
        recyclerView = findViewById(R.id.recycler_view)
        sendButton = findViewById(R.id.send)
        getButton = findViewById(R.id.get)
        imageView = findViewById(R.id.image_view)


        sendButton.setOnClickListener {
            val imageFile = File(images[0])
            val reqFile = imageFile.asRequestBody("image/${imageFile.extension}".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, reqFile)
            val apiInterface = APIClient.getClient().create(APIInterface::class.java)
            apiInterface.uploadImage(imagePart).enqueue(object: Callback<ResponseBody>{
                override fun onResponse( call: Call<ResponseBody>,
                                         response: Response<ResponseBody>
                ) {
                    Toast.makeText(this@ImageTestActivity, response.body()!!.toString(), LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@ImageTestActivity, "Failed to send Image", LENGTH_SHORT).show()
                }
            })
        }

        getButton.setOnClickListener {
            val apiInterface = APIClient.getClient().create(APIInterface::class.java)
            apiInterface.getImage("IMG_20210113_050221.jpg").enqueue(object: Callback<ResponseBody>{
                override fun onResponse( call: Call<ResponseBody>,
                                         response: Response<ResponseBody>
                ) {
                    Toast.makeText(this@ImageTestActivity, "Succeeded to get Image", LENGTH_SHORT).show()
                    Glide.with(this@ImageTestActivity).load("http://192.249.18.238:8080/post/IMG_20210113_050221.jpg")
                        .into(imageView)

                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@ImageTestActivity, "Failed to get Image", LENGTH_SHORT).show()
                }
            })

        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_STORACE)
        } else{
            loadImages();
        }
    }

    override fun onStart() {
        super.onStart()
        loadImages()
    }

    private fun loadImages() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        val orderBy: String = MediaStore.Video.Media.DATE_TAKEN
        val cursor = this.contentResolver.query(uri,projection, null, null, "$orderBy DESC");
        images = ArrayList()
        cursor?.use{
            val column_index_data:Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while(cursor.moveToNext()) {
                val absolutePathOfImage = cursor.getString(column_index_data)
                images.add(absolutePathOfImage)
            }
        }
        recyclerView.adapter = Adapter(this, images)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_READ_STORACE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                loadImages()
        }
    }

    class Adapter(private val context: Context, private val images:List<String>): RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val image:String = images[position]
            Glide.with(context).load(image).into(holder.image)
        }

        override fun getItemCount(): Int = images.size;

        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.image);
        }
    }
}