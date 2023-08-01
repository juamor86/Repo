package es.juntadeandalucia.msspa.saludandalucia.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import android.provider.DocumentsContract
import com.google.gson.Gson
import timber.log.Timber
import java.io.*


class FileUtils {
    companion object {
        fun writeToFile(data: Any, fileName: String, context: Context) {
            try {
                val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
                outputStreamWriter.write(Gson().toJson(data))
                outputStreamWriter.close()
            } catch (e: IOException) {
                Timber.e("writeToFile: %s", e.message)
            }
        }

        fun readFromFile(context: Context, fileName: String): String? {
            try {
                val inputStream: InputStream? = context.openFileInput(fileName)
                if (inputStream != null) {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var receiveString: String? = ""
                    val stringBuilder = StringBuilder()
                    while (bufferedReader.readLine().also { receiveString = it } != null) {
                        stringBuilder.append("\n").append(receiveString)
                    }
                    inputStream.close()
                    return stringBuilder.toString()
                }
            } catch (e: FileNotFoundException) {
                Timber.e( "readFromFile - File not found: %s", e.toString())
                return null
            } catch (e: IOException) {
                Timber.e("readFromFile - Can not read file: $e")
                return null
            }

            return null
        }

        fun deleteFile(file: File): () -> Unit = {
            try {
                if (file.exists()) {
                    Timber.i("File to delete: ${file.name}")
                    val result = file.delete()
                    if (result) {
                        Timber.i("Deleted file succeeded.")
                    } else {
                        Timber.i("Deletion file failed.")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        fun extractUriDownload(context: Context, downloadId: Long): String? {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val downloadLocalUri =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex) && downloadLocalUri != null) {
                    cursor.close()
                    return downloadLocalUri
                } else if (DownloadManager.STATUS_FAILED == cursor.getInt(columnIndex)) {
                    cursor.close()
                    return null
                }
                return null
            } else {
                return null
            }
        }

        fun deleteTempFile(context: Context) {
            val files = context.cacheDir.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.name.contains(Consts.TEMP_FILE)) {
                        file.delete()
                    }
                }
            }
        }

        fun createTemporalFile(context: Context, uri: Uri): File? {
            val id = DocumentsContract.getDocumentId(uri)
            if (id != null) {
                val file = File(
                    context.cacheDir,
                    Utils.getFileName(context, uri) ?: (Consts.TEMP_FILE +
                            context.contentResolver.getType(uri)!!
                                .split("/")[1])
                )
                if (file.exists()) {
                    deleteTempFile(context)
                }
                try {
                    context.contentResolver.openInputStream(uri)
                        .use { inputStream ->
                            FileOutputStream(file).use { output ->
                                val buffer = ByteArray(4 * 1024)
                                var read: Int
                                if (inputStream != null) {
                                    while (inputStream.read(buffer).also { read = it } != -1) {
                                        output.write(buffer, 0, read)
                                    }
                                }
                                output.flush()
                                return file
                            }
                        }
                } catch (ex: IOException) {
                    return null
                }
            } else {
                return null
            }
        }

        fun openDownloadedLocalUri(context: Context, downloadLocalUri: String) {
            val path = Uri.parse(downloadLocalUri).path
            path?.let { pathToFile ->
                val file = File(pathToFile)
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.setDataAndType(
                    FileProvider.getUriForFile(
                        context,
                        context.applicationContext.packageName + ".provider",
                        file
                    ), "application/pdf"
                )
                browserIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                try {
                    context.startActivity(browserIntent)
                } catch (e: Exception) {
                    Timber.e("Error opening file: ${e.message}")
                }
            }
        }
    }
}