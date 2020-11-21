package com.vasylb.controller

import com.vasylb.service.SoundRecorder
import com.vasylb.service.FileService
import com.vasylb.service.PackageService
import com.vasylb.util.getFileName
import com.vasylb.util.withoutExt
import javafx.beans.property.SimpleStringProperty
import javafx.stage.FileChooser
import tornadofx.*
import java.nio.file.Files
import java.nio.file.Path

class MainController : Controller() {

    val observableAudioFileNames = initFileNames()

    fun openFolder() {
        initFileNames()
        val dir = chooseDirectory("Select folder with audio files")
        runAsync {
            FileService.getFilesFromFolder(dir)
        } ui { filename ->
            filename.forEach { this.updateInputs(it) }
        }
    }

    fun openFile(key: String) {
        val file =
            chooseFile(
                "Select .wav file",
                arrayOf(FileChooser.ExtensionFilter("Wav files(*.wav)", "*.wav")),
                null,
                FileChooserMode.Single
            )

        file.map { it.absolutePath }.forEach {
            this.updateInputs(key, it)
        }

    }

    fun createPkg() {
        val destination =
            chooseFile(
                "Select where to save",
                arrayOf(FileChooser.ExtensionFilter("Pkg Files(*.pkg", "*.pkg")),
                null,
                FileChooserMode.Save,
            )

        if (destination.isNotEmpty()) {
            runAsync {
                PackageService.createPackage(destination[0], observableAudioFileNames.values.map { it.get() })
            } ui {
                information("Packaging done", "Package was created at $destination")
            }
        }
    }

    //TODO refactor this
    fun handleRecording(key: String, isRecording: Boolean) {
        runAsync {
            if (isRecording) {
                SoundRecorder.finish()
                updateInputs("./data/$key.wav")
            } else {
                SoundRecorder.start(key)
            }
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
        val filePath = javaClass.classLoader?.getResource("voice_names.csv")?.toURI()

        return Files.readAllLines(Path.of(filePath))
            .map { it.split(",")[0].withoutExt() }
            .associateWith { SimpleStringProperty("") }
            .toMutableMap()

    }
}
