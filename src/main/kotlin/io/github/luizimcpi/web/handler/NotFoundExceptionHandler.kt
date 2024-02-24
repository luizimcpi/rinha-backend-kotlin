package io.github.luizimcpi.web.handler

import io.github.luizimcpi.exception.NotFoundException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [NotFoundException::class, ExceptionHandler::class])
class NotFoundExceptionHandler :
    ExceptionHandler<NotFoundException?, HttpResponse<ErrorMessageResponse>> {
    override fun handle(request: HttpRequest<*>?, exception: NotFoundException?): HttpResponse<ErrorMessageResponse> {
        if (exception != null) {
            return HttpResponse.notFound(ErrorMessageResponse(exception.message!!))
        }
        return HttpResponse.notFound()
    }
}