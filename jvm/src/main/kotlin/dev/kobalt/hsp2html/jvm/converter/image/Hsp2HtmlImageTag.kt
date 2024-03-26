/*
 * dev.kobalt.hsp2html
 * Copyright (C) 2022 Tom.K
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.kobalt.hsp2html.jvm.converter.image

import dev.kobalt.hsp2html.jvm.entity.HspImageEntity
import dev.kobalt.hsp2html.jvm.extension.encodeToBase64String
import dev.kobalt.hsp2html.jvm.extension.imageDimensions
import dev.kobalt.hsp2html.jvm.extension.requireIsLocatedIn
import dev.kobalt.hsp2html.jvm.gif.GifConverter
import kotlinx.css.CssBuilder
import kotlinx.html.*
import java.io.ByteArrayOutputStream
import java.io.File

@HtmlTagMarker
fun HtmlBlockTag.hsp2HtmlImage(resourcePath: String, pageObject: HspImageEntity, gifConverter: GifConverter) {

    val imagesFolder = File("$resourcePath/images")
    val gifsFolder = imagesFolder.resolve("gifs")
    val shapesFolder = imagesFolder.resolve("shapes")
    val staticsFolder = imagesFolder.resolve("static")
    val bgsFolder = imagesFolder.resolve("bgs")
    val wordartsFolder = imagesFolder.resolve("wordart")

    var data: ByteArray? = null

    // Check gifs folder first.
    val gifFolder = gifsFolder.resolve(pageObject.imageFilename).requireIsLocatedIn(gifsFolder)
    if (gifFolder.exists() && gifFolder.isDirectory) {
        // Apply offset. If it's on anim move over, update offset value.
        val offset = pageObject.offset.toInt().let { if (it > 0) it + 1 else 1 }.toString()
        // if (pageObject.animMoveOver == "-2") offset =
        // (offset.toInt() + 1).toString()
        var gifFile =
            gifFolder.resolve("frame-$offset.png").takeIf { it.exists() } ?: gifFolder.resolve("frame-$offset.png.png")
        if (!gifFile.exists()) {
            val padOffset = offset.padStart(2, '0')
            gifFile = gifFolder.resolve("frame-$padOffset.png").takeIf { it.exists() }
                ?: gifFolder.resolve("frame-$padOffset.png.png")
        }

        if (gifFile.exists()) {
            data = gifFile.readBytes()
        }
        if (pageObject.animMoveOver == "0") {
            val fileList = gifFolder.listFiles()
            val frames = fileList?.filter { it.extension == "png" }.orEmpty()
            val speed = fileList?.find { it.extension == "speed" }
                ?.nameWithoutExtension?.toIntOrNull() ?: 5 // Default for those without .speed file.
            // Manually measured, may be inaccurate.
            /*val actualSpeed = when (speed) {
                1 -> 100
                2 -> 60
                3 -> 35
                4 -> 25
                5 -> 20
                6 -> 17
                7 -> 14
                8 -> 12
                9 -> 11
                10 -> 10
                11 -> 9
                12 -> 8
                13 -> 7
                14 -> 5
                60 -> 1
                else -> 20
            }*/
            // Based on manual measurements, graph with these values gives a linear function y = 100 / x. Values mostly match with this function.
            val x = speed
            val y = if (x > 0) 100 / x else 1
            data = ByteArrayOutputStream().use { outputStream ->
                gifConverter.generate(outputStream, frames.map { Pair(it, y) })
                outputStream.toByteArray()
            }
        }
    }
    val shapeFile = shapesFolder.resolve(pageObject.imageFilename.plus(".png"))
    if (shapeFile.exists()) {
        shapeFile.requireIsLocatedIn(shapesFolder)
        data = shapeFile.readBytes()
    }
    val staticFile = staticsFolder.resolve(pageObject.imageFilename.plus(".png"))
    if (staticFile.exists()) {
        staticFile.requireIsLocatedIn(staticsFolder)
        data = staticFile.readBytes()
    }
    val bgFile = bgsFolder.resolve(pageObject.imageFilename.plus(".png"))
    if (bgFile.exists()) {
        bgFile.requireIsLocatedIn(bgsFolder)
        data = bgFile.readBytes()
    }
    val wordartFolder = wordartsFolder.resolve(pageObject.imageFilename)
    if (wordartFolder.exists() && wordartFolder.isDirectory) {
        val name = when (pageObject.offset.toInt()) {
            0 -> "0"
            1 -> "1"
            2 -> "2"
            3 -> "3"
            4 -> "4"
            5 -> "5"
            6 -> "6"
            7 -> "7"
            8 -> "8"
            9 -> "9"
            10 -> "a"
            11 -> "b"
            12 -> "c"
            13 -> "d"
            14 -> "e"
            15 -> "f"
            16 -> "g"
            17 -> "h"
            18 -> "i"
            19 -> "j"
            20 -> "k"
            21 -> "l"
            22 -> "m"
            23 -> "n"
            24 -> "o"
            25 -> "p"
            26 -> "q"
            27 -> "r"
            28 -> "s"
            29 -> "t"
            30 -> "u"
            31 -> "v"
            32 -> "w"
            33 -> "x"
            34 -> "y"
            35 -> "z"
            36 -> "zz0exclam"
            37 -> "zz1quest"
            38 -> "zz2apost"
            39 -> "zz3colon"
            40 -> "zz4quote"
            else -> null
        }
        val file = wordartFolder.resolve("$name.png")
        data = file.readBytes()
        file.requireIsLocatedIn(wordartFolder)
    }
    data?.let { imageFile ->
        val dimensions = imageFile.imageDimensions()
        div("hspImage${pageObject.uid}") {
            style = CssBuilder().apply {
                put("position", "absolute")
                put("left", "${pageObject.positionX - (dimensions.first / 2)}px")
                put("top", "${pageObject.positionY - (dimensions.second / 2)}px")
                put("width", "${dimensions.first}px")
                put("height", "${dimensions.second}px")
            }.toString().replace("\n", "")
            img("hspImage${pageObject.uid}") {
                src = imageFile.inputStream().use { it.readBytes().encodeToBase64String() }
                    .let { "data:image/gif;base64,$it" }
            }
        }
    }
}

/*
@HtmlTagMarker
fun HtmlBlockTag.hsp12HtmlImage(resourcePath: String, pageObject: HspImageEntity, gifConverter: GifConverter) {

    val imagesFolder = File("$resourcePath/image")
    val gifsFolder = imagesFolder.resolve("gifs")
    val shapesFolder = imagesFolder.resolve("shapes")
    val staticsFolder = imagesFolder.resolve("static")
    val bgsFolder = imagesFolder.resolve("bgs")
    val wordartsFolder = imagesFolder.resolve("wordart")

    var file: File? = null
    var tempFile: File? = null

    // Check gifs folder first.
    val gifFolder = gifsFolder.resolve(pageObject.imageFilename).requireIsLocatedIn(gifsFolder)
    if (gifFolder.exists() && gifFolder.isDirectory) {
        // Apply offset. If it's on anim move over, update offset value.
        val offset = pageObject.offset.toInt().let { if (it > 0) it + 1 else 1 }.toString()
        // if (pageObject.animMoveOver == "-2") offset =
        // (offset.toInt() + 1).toString()
        var gifFile =
            gifFolder.resolve("frame-$offset.png").takeIf { it.exists() } ?: gifFolder.resolve("frame-$offset.png.png")
        if (!gifFile.exists()) {
            val padOffset = offset.padStart(2, '0')
            gifFile = gifFolder.resolve("frame-$padOffset.png").takeIf { it.exists() }
                ?: gifFolder.resolve("frame-$padOffset.png.png")
        }

        if (gifFile.exists()) {
            file = gifFile
        }
        if (pageObject.animMoveOver == "0") {
            fun createTempFile(folder: File): File {
                return File.createTempFile("gen", ".gif", folder)
            }

            val tempFolder = File("temp").also { it.mkdir() }
            tempFile = createTempFile(tempFolder)
            val fileList = gifFolder.listFiles()
            val frames = fileList?.filter { it.extension == "png" }.orEmpty()
            val speed = fileList?.find { it.extension == "speed" }
                ?.nameWithoutExtension?.toIntOrNull() ?: 5 // Default for those without .speed file.
            // Manually measured, may be inaccurate.
            /*val actualSpeed = when (speed) {
                1 -> 100
                2 -> 60
                3 -> 35
                4 -> 25
                5 -> 20
                6 -> 17
                7 -> 14
                8 -> 12
                9 -> 11
                10 -> 10
                11 -> 9
                12 -> 8
                13 -> 7
                14 -> 5
                60 -> 1
                else -> 20
            }*/
            // Based on manual measurements, graph with these values gives a linear function y = 100 / x. Values mostly match with this function.
            val x = speed
            val y = if (x > 0) 100 / x else 1
            gifConverter.generate(tempFile, frames.map { Pair(it, y) })
            file = tempFile
        }
    }
    val shapeFile = shapesFolder.resolve(pageObject.imageFilename.plus(".png"))
    if (shapeFile.exists()) {
        shapeFile.requireIsLocatedIn(shapesFolder)
        file = shapeFile
    }
    val staticFile = staticsFolder.resolve(pageObject.imageFilename.plus(".png"))
    if (staticFile.exists()) {
        staticFile.requireIsLocatedIn(staticsFolder)
        file = staticFile
    }
    val bgFile = bgsFolder.resolve(pageObject.imageFilename.plus(".png"))
    if (bgFile.exists()) {
        bgFile.requireIsLocatedIn(bgsFolder)
        file = bgFile
    }
    val wordartFolder = wordartsFolder.resolve(pageObject.imageFilename)
    if (wordartFolder.exists() && wordartFolder.isDirectory) {
        val name = when (pageObject.offset.toInt()) {
            0 -> "0"
            1 -> "1"
            2 -> "2"
            3 -> "3"
            4 -> "4"
            5 -> "5"
            6 -> "6"
            7 -> "7"
            8 -> "8"
            9 -> "9"
            10 -> "a"
            11 -> "b"
            12 -> "c"
            13 -> "d"
            14 -> "e"
            15 -> "f"
            16 -> "g"
            17 -> "h"
            18 -> "i"
            19 -> "j"
            20 -> "k"
            21 -> "l"
            22 -> "m"
            23 -> "n"
            24 -> "o"
            25 -> "p"
            26 -> "q"
            27 -> "r"
            28 -> "s"
            29 -> "t"
            30 -> "u"
            31 -> "v"
            32 -> "w"
            33 -> "x"
            34 -> "y"
            35 -> "z"
            36 -> "zz0exclam"
            37 -> "zz1quest"
            38 -> "zz2apost"
            39 -> "zz3colon"
            40 -> "zz4quote"
            else -> null
        }
        file = wordartFolder.resolve("$name.png")
        file.requireIsLocatedIn(wordartFolder)
    }
    file?.takeIf { it.exists() }?.let { imageFile ->
        val dimensions = imageFile.imageDimensions()
        div("hspImage${pageObject.uid}") {
            style = CssBuilder().apply {
                put("position", "absolute")
                put("left", "${pageObject.positionX - (dimensions.first / 2)}px")
                put("top", "${pageObject.positionY - (dimensions.second / 2)}px")
                put("width", "${dimensions.first}px")
                put("height", "${dimensions.second}px")
            }.toString().replace("\n", "")
            img("hspImage${pageObject.uid}") {
                src = imageFile.inputStream().use { it.readBytes().encodeToBase64String() }
                    .let { "data:image/gif;base64,$it" }
            }
        }
    }
    tempFile?.delete()
}
*/