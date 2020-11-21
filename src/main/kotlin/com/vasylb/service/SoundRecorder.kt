package com.vasylb.service

import java.io.File
import java.io.IOException
import javax.sound.sampled.*

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
object SoundRecorder {
    // format of audio file
    private val fileType = AudioFileFormat.Type.WAVE

    // the line from which audio data is captured
    lateinit var line: TargetDataLine;

    /**
     * Defines an audio format
     */
    private val audioFormat: AudioFormat
        get() {
            val sampleRate = 44100f
            val sampleSizeInBits = 16
            val channels = 1
            val signed = true
            val bigEndian = true

            return AudioFormat(
                sampleRate, sampleSizeInBits,
                channels, signed, bigEndian
            )
        }

    /**
     * Captures the sound and record into a WAV file
     */
    fun start(fileName: String) {
        try {
            val format = audioFormat
            val info = DataLine.Info(TargetDataLine::class.java, format)
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                println("Line not supported")
            }
            line = AudioSystem.getLine(info) as TargetDataLine
            line.open(format)
            line.start() // start capturing
            println("Start capturing...")
            val ais = AudioInputStream(line)
            println("Start recording...")

            // start recording
            val wavFile = File("./data/$fileName.wav")
            if (!wavFile.parentFile.exists()) {
                wavFile.parentFile.mkdirs()
            }
            if (!wavFile.exists()) {
                wavFile.createNewFile()
            }

            AudioSystem.write(ais, fileType, wavFile)
        } catch (ex: LineUnavailableException) {
            ex.printStackTrace()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    fun finish() {
        line.stop()
        line.close()
        println("Finished")
    }
}