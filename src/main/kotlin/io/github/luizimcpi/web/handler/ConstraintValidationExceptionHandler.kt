package io.github.luizimcpi.web.handler

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.UNPROCESSABLE_ENTITY
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.validation.exceptions.ConstraintExceptionHandler
import jakarta.inject.Singleton
import javax.validation.ConstraintViolationException

@Produces
@Singleton
@Requires(classes = [ConstraintValidationExceptionHandler::class, ExceptionHandler::class])
@Replaces(ConstraintExceptionHandler::class)
class ConstraintValidationExceptionHandler :
    ExceptionHandler<ConstraintViolationException?, HttpResponse<ErrorMessageResponse>> {
    override fun handle(request: HttpRequest<*>?, exception: ConstraintViolationException?): HttpResponse<ErrorMessageResponse> {
        if (exception != null) {
            return HttpResponse.status<Any?>(UNPROCESSABLE_ENTITY).body(ErrorMessageResponse(exception.message!!))
        }
        return HttpResponse.status(UNPROCESSABLE_ENTITY)
    }
}