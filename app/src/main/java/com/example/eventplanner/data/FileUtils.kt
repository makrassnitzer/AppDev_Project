package com.example.eventplanner.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileUtils(private val context: Context) {

    fun savePdfFile(uri: Uri, fileName: String): String? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, fileName)

        return try {
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (inputStream?.read(buffer).also { read = it ?: -1 } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}