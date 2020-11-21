package com.vasylb.service

import java.io.File

object FileService {

    fun getFilesFromFolder(dir: File?): List<String>  {
       return if (dir == null) emptyList()
              else File(dir.absolutePath).walk()
                .filter { it.isFile }
                .filter { it.canonicalPath.endsWith(".wav") }
                .map { it.absolutePath }
                .toList()
        }
}