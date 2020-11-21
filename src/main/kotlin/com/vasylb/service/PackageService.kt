package com.vasylb.service

import com.vasylb.util.withResources
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import se.jabberwocky.ccrypt.CCrypt
import java.io.BufferedOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object PackageService {

    fun createPackage(destination: File, fileNames: List<String>) {
            val packageName = Paths.get("./temp.pkg");
            this.packFiles(packageName, fileNames)
            this.encryptArchive(packageName, destination)
    }

    private fun encryptArchive(packageName: Path, destination: File) {
        val original = packageName.toFile()
        CCrypt("r0ckrobo#23456").encrypt(original, destination)
        original.delete()
    }

    private fun packFiles(packageName: Path, fileNames: List<String>) {
        withResources {
            val newOutputStream = Files.newOutputStream(packageName)
            val bufferedOutputStream = BufferedOutputStream(newOutputStream)
            val gzipCompressorOutputStream = GzipCompressorOutputStream(bufferedOutputStream)
            val tarArchiveOutputStream = TarArchiveOutputStream(gzipCompressorOutputStream)

            fileNames
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
}