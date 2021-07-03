package me.liuli.mashiro.gui.ultralight.page

import me.liuli.mashiro.Mashiro
import java.io.File
import java.nio.file.StandardWatchEventKinds

class Page(val name: String) {
    private val pageFolder = File(Mashiro.ultralightManager.dataDir, name)
    private val htmlFile = File(pageFolder, "index.html")

    val viewableFile: String
        get() = htmlFile.toURI().toString()

    val exist: Boolean
        get() = htmlFile.exists()

    private val watcher by lazy {
        val path = pageFolder.toPath()
        val watchService = path.fileSystem.newWatchService()
        path.register(watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        )
        watchService
    }

    fun hasUpdate(): Boolean {
        val watchKey = watcher.poll()
        val shouldUpdate = watchKey?.pollEvents()?.isNotEmpty() == true
        watchKey?.reset()
        return shouldUpdate
    }

    fun close() {
        watcher.close()
    }
}