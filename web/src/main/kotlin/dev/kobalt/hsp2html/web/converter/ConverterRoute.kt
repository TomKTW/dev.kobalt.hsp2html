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

import dev.kobalt.hsp2html.web.html.HtmlStringContent
import dev.kobalt.hsp2html.web.inputstream.InputStreamSizeLimitReachedException
import dev.kobalt.hsp2html.web.inputstream.LimitedSizeInputStream
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.TimeoutCancellationException

fun Route.converterRoute() {
    get {
        call.respondText(application.converter.getIndexPageContent(), ContentType.Text.Html)
    }
    post {
        runCatching {
            val parts = call.receiveMultipart().readAllParts()
            val part = parts.find { it.name == "input" } as? PartData.FileItem ?: throw Exception()
            val data = LimitedSizeInputStream(part.streamProvider(), 500 * 1024).readBytes().decodeToString()
            val conversion = application.converter.submit(data)
            call.respond(HtmlStringContent(HttpStatusCode.OK, conversion))
        }.getOrElse {
            call.respondText(
                text = application.converter.getFailurePageContent().replace(
                    "\$cause\$", when (it) {
                        is TimeoutCancellationException -> "The process took too long to convert as the server is most likely overloaded. You could try again later or download converter locally, which might be preferred if this is not functional."
                        is InputStreamSizeLimitReachedException -> "Submitted content is bigger than size limit (500 kB)."
                        else -> "Conversion was not successful."
                    }
                ),
                contentType = ContentType.Text.Html,
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}

