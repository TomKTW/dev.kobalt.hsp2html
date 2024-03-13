/*
 * dev.kobalt.hsp2html
 * Copyright (C) 2024 Tom.K
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

package dev.kobalt.hsp2html.web.converter

import io.ktor.server.application.*
import io.ktor.util.*
import kotlin.io.path.Path
import kotlin.io.path.exists

/** Plugin that provides an instance of converter repository. */
val ConverterPlugin = createApplicationPlugin(
    name = ConverterConfiguration.NAME,
    createConfiguration = ::ConverterConfiguration
) {
    application.attributes.put(
        AttributeKey(ConverterConfiguration.NAME),
        ConverterRepository(
            jarPath = pluginConfig.jarPath?.let { Path(it) }?.takeIf { it.exists() }!!,
            resourcePath = pluginConfig.resourcePath?.let { Path(it) }?.takeIf { it.exists() }!!,
            fontPath = pluginConfig.fontPath?.let { Path(it) }?.takeIf { it.exists() }!!,
        )
    )
}
