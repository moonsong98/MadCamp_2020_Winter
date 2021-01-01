package com.example.myapplication

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class GalleryAdapter(private val context: Context, private val images: List<String>, private val photoListener: PhotoListener):
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image:String = images[position]
        Glide.with(context).load(image).into(holder.image)
        holder.itemView.setOnClickListener { photoListener.onPhotoClick(image) }
    }

    override fun getItemCount(): Int = images.size;

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image);
    }
    interface PhotoListener {
        fun onPhotoClick(path: String)
    }
}