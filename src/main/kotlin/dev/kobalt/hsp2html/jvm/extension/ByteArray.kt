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

package dev.kobalt.hsp2html.jvm.extension

import java.io.ByteArrayInputStream
import java.util.*
import javax.imageio.ImageIO

internal fun ByteArray.encodeToBase64String(): String = Base64.getEncoder().withoutPadding().encodeToString(this)

internal fun ByteArray.imageDimensions(): Pair<Int, Int> {
    ByteArrayInputStream(this).use {
        ImageIO.createImageInputStream(it).use { imageInputStream ->
            val readers = ImageIO.getImageReaders(imageInputStream)
            readers.forEach { reader ->
                reader.input = imageInputStream
                val width = reader.getWidth(reader.minIndex)
                val height = reader.getHeight(reader.minIndex)
                reader.dispose()
                return Pair(width, height)
            }
        }
    }
    return Pair(0, 0)
}
