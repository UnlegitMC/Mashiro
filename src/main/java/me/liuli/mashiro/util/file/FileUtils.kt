package me.liuli.mashiro.util.file

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

object FileUtils {
    /**
     * @author CCBlueX
     */
    fun extractZip(zipFile: File, folder: File) {
        val zipStream = FileInputStream(zipFile)
        if (!folder.exists()) {
            folder.mkdir()
        }

        ZipInputStream(zipStream).use { zipInputStream ->
            var zipEntry = zipInputStream.nextEntry

            while (zipEntry != null) {
                if (zipEntry.isDirectory) {
                    zipEntry = zipInputStream.nextEntry
                    continue
                }

                val newFile = File(folder, zipEntry.name)
                File(newFile.parent).mkdirs()

                FileOutputStream(newFile).use {
                    zipInputStream.copyTo(it)
                }
                zipEntry = zipInputStream.nextEntry
            }

            zipInputStream.closeEntry()
        }
    }
}