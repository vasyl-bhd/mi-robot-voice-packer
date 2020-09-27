package com.vasylb.controller

import com.vasylb.withResources
import javafx.beans.property.SimpleStringProperty
import javafx.stage.FileChooser
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import se.jabberwocky.ccrypt.CCrypt
import tornadofx.Controller
import tornadofx.FileChooserMode
import tornadofx.chooseDirectory
import tornadofx.chooseFile
import java.io.BufferedOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class MainController : Controller() {

    val observableAudioFileNames: MutableMap<String, SimpleStringProperty>

    init {
        observableAudioFileNames = initFileNames()
    }

    fun openFolder() {
        val dir = chooseDirectory("Select audio folder")
        dir?.let {
            File(dir.absolutePath).walk()
                .filter { it.isFile }
                .filter { it.canonicalPath.endsWith(".wav") }
                .map { it.absolutePath }
                .forEach { this.updateInputs(it) }
        }
    }

    fun openFile() {
        val file =
            chooseFile(
                "Select .wav file",
                arrayOf(FileChooser.ExtensionFilter("Wav files(*.wav)", "*.wav"))
            )

        file.map { it.absolutePath }.forEach { this.updateInputs(it) }

    }

    fun createPkg() {
        val destination =
            chooseFile(
                "Select where to save",
                arrayOf(FileChooser.ExtensionFilter("Pkg Files(*.pkg", "*.pkg")),
                null,
                FileChooserMode.Save
            )

        if (destination.isNotEmpty()) {
            val packageName = Paths.get("./temp.pkg");
            this.packFiles(packageName)
            this.encryptArchive(packageName, destination[0])
        }
    }

    private fun encryptArchive(packageName: Path, destination: File) {
        val original = packageName.toFile()
        CCrypt("r0ckrobo#23456").encrypt(original, destination)
        original.delete()
    }

    private fun packFiles(packageName: Path) {
        withResources {
            val newOutputStream = Files.newOutputStream(packageName)
            val bufferedOutputStream = BufferedOutputStream(newOutputStream)
            val gzipCompressorOutputStream = GzipCompressorOutputStream(bufferedOutputStream)
            val tarArchiveOutputStream = TarArchiveOutputStream(gzipCompressorOutputStream)

            observableAudioFileNames
                .values
                .map { it.get() }
                .filter { it.isNotBlank() }
                .map { Paths.get(it) }
                .forEach { path ->
                    tarArchiveOutputStream.putArchiveEntry(TarArchiveEntry(path.toFile(), path.fileName.toString()))
                    Files.copy(path, tarArchiveOutputStream)
                    tarArchiveOutputStream.closeArchiveEntry()
                }
            tarArchiveOutputStream.finish()
            tarArchiveOutputStream.close()
        }
    }

    private fun updateInputs(value: String) {
        updateInputs(getFileName(value), value)
    }

    private fun updateInputs(key: String, value: String) {
        val simpleStringProperty = observableAudioFileNames[key]
        simpleStringProperty?.set(value)
    }

    private fun initFileNames(): MutableMap<String, SimpleStringProperty> {
        val filePath = this::class.java.classLoader?.getResource("voice_names.csv")?.toURI()

        return Files.readAllLines(Path.of(filePath))
            .map { it.split(",")[0].withoutExt() }
            .associateWith { SimpleStringProperty("") }
            .toMutableMap()

    }

    private fun String.withoutExt(): String {
        return this.substring(0, this.length - 4)
    }

    private fun getFileName(path: String): String {
        return path.substring(path.lastIndexOf("/") + 1, path.length - 4)
    }

}