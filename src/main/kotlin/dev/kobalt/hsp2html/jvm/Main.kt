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

package dev.kobalt.hsp2html.jvm

import dev.kobalt.hsp2html.jvm.converter.Hsp2HtmlConverter
import dev.kobalt.hsp2html.jvm.extension.ifLet
import dev.kobalt.hsp2html.jvm.parser.Hsp2HtmlParser
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.BufferedInputStream
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("hsp2html")
    val resourcePath by parser.option(ArgType.String, "resourcePath", null, null)
    val fontPath by parser.option(ArgType.String, "fontPath", null, null)
    val pagePath by parser.option(ArgType.String, "pagePath", null, null)
    parser.parse(args)
    ifLet(resourcePath, fontPath) { resources, fonts ->
        val converter = Hsp2HtmlConverter(resources, fonts, Hsp2HtmlParser())
        ((pagePath?.let { File(it).readText() }) ?: run {
            BufferedInputStream(System.`in`).use {
                if (it.available() > 0) it.readBytes().decodeToString() else null
            }
        })?.let {
            print(converter.convert(it))
        }
    }
}
