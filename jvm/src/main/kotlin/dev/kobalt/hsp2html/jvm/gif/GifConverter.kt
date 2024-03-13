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

package dev.kobalt.hsp2html.jvm.gif

import org.w3c.dom.Node
import java.io.File
import java.io.OutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageTypeSpecifier
import javax.imageio.ImageWriter
import javax.imageio.metadata.IIOMetadataNode
import javax.imageio.stream.ImageOutputStream

/**
 * Reference: https://stackoverflow.com/a/42514975
 * Legacy reference: https://community.oracle.com/thread/1264385
 * @author Andrew Thompson, Maxideon, GeoffTitmus
 */
class GifConverter {

    fun generate(output: OutputStream, images: List<Pair<File, Int>>): OutputStream {
        val imageWriter = ImageIO.getImageWritersByFormatName("gif").next()
        ImageIO.createImageOutputStream(output).use { outputStream ->
            imageWriter.writeSequence(outputStream) {
                images.sortedBy { it.first.name }.forEachIndexed { index, data ->
                    val image = ImageIO.read(data.first)
                    val delay = data.second.toString()
                    val writerParams = imageWriter.defaultWriteParam
                    val metadata = imageWriter.getDefaultImageMetadata(ImageTypeSpecifier(image), writerParams)
                    val metadataFormatName = metadata.nativeMetadataFormatName
                    if (metadataFormatName != "javax_imageio_gif_image_1.0") throw Exception("Invalid metadata format: $metadataFormatName")
                    metadata.setFromTree(metadataFormatName, metadata.getAsTree(metadataFormatName).also {
                        (it.findChild("GraphicControlExtension") as? IIOMetadataNode)?.apply {
                            setAttribute("userDelay", "FALSE")
                            setAttribute("disposalMethod", "restoreToBackgroundColor")
                            setAttribute("delayTime", delay)
                        }
                        if (index == 0) it.appendChild(IIOMetadataNode("ApplicationExtensions").apply {
                            appendChild(IIOMetadataNode("ApplicationExtension").apply {
                                setAttribute("applicationID", "NETSCAPE")
                                setAttribute("authenticationCode", "2.0")
                                userObject = byteArrayOf(0x1, 0x0, 0x0)
                            })
                        })
                    })
                    imageWriter.writeToSequence(IIOImage(image, null, metadata), null)
                }
            }
        }
        return output
    }

    private fun Node.findChild(name: String): Node? {
        val nodes = childNodes
        for (position in 0..nodes.length) {
            val node = nodes.item(position)
            if (node.nodeName == name) return node
        }
        return null
    }

    private fun ImageWriter.writeSequence(outputStream: ImageOutputStream, block: () -> Unit) {
        output = outputStream
        prepareWriteSequence(null)
        block()
        endWriteSequence()
    }

}