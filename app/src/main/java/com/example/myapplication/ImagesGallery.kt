package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class ImagesGallery {
    companion object {
        fun listOfImages(context: Context): ArrayList<String> {
            val listOfAllImages:ArrayList<String> = arrayListOf();
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            val projection: Array<String?> = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            val orderBy: String = MediaStore.Video.Media.DATE_TAKEN
            val cursor = context.contentResolver.query(uri,projection, null, null, orderBy+"DESC");
            if (cursor != null) {
                val column_index_data:Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                while(cursor.moveToNext()) {
                    val absolutePathOfImage = cursor.getString(column_index_data)
                    listOfAllImages.add(absolutePathOfImage)
                }
            }
            return listOfAllImages
        }

    }
}