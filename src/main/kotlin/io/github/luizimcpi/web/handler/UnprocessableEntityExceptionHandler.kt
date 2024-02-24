package io.github.luizimcpi.web.handler

import io.github.luizimcpi.exception.UnprocessableEnityException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.UNPROCESSABLE_ENTITY
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [UnprocessableEnityException::class, ExceptionHandler::class])
class UnprocessableEntityExceptionHandler :
    ExceptionHandler<UnprocessableEnityException?, HttpResponse<Any>> {
    override fun handle(request: HttpRequest<*>?, exception: UnprocessableEnityException?): HttpResponse<Any> {
        if (exception != null) {
            return HttpResponse.status<Any?>(UNPROCESSABLE_ENTITY).body(ErrorMessageResponse(exception.message!!))
        }
        return HttpResponse.status<Any?>(UNPROCESSABLE_ENTITY)
    }
}