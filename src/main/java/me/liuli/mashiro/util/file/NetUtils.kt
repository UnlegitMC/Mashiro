package me.liuli.mashiro.util.file

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files

object NetUtils {
    fun downloadFile(url: URL, file: File) {
        val httpurlconnection = url.openConnection() as HttpURLConnection
        httpurlconnection.requestMethod = "GET"

        if (file.exists()) {
            file.delete()
        }

        Files.copy(httpurlconnection.inputStream, file.toPath())
    }
}