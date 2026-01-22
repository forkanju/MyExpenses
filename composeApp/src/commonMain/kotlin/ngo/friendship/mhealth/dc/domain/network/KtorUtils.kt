package ngo.friendship.mhealth.dc.domain.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import io.ktor.serialization.JsonConvertException
import io.ktor.util.StringValuesBuilder
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap
import ngo.friendship.mhealth.dc.utils.tryGet

/**
 * name : Yamin Mahdi
 * date : 18/07/24 9:50 AM
 * email: yamin_khan@mail.com
 *
 * @ : uttara, dhaka
 */

/**
 * Converts a serializable object [T] to a ktor `Parameters` object.
 *
 * @param T The type of the object to convert. Must be serializable.
 * @return A ktor `Parameters` object containing the key-value pairs from the object.
 */
inline fun <reified T> T.toKtorParameters() = parameters {
    Properties.encodeToMap(this@toKtorParameters).toList().forEach {
        println(it)
        append(it.first, it.second)
    }
}

/**
 * Appends a key-value pair to a ktor `Parameters` object.
 *
 * @param name The name of the key-value pair.
 * @param value The value of the key-value pair.
 */
fun StringValuesBuilder.append(name: String, value: Any?): Unit =
    value?.let { this.append(name, it.toString()) } ?: Unit


/**
 * Converts a ktor `Parameters` object to a `ParametersBuilder` object.
 */
fun Parameters.toParametersBuilder() = ParametersBuilder().apply {
    appendAll(this@toParametersBuilder)
}

/**
 * Processes a POST request to the specified [url] with the given [body] using the Ktor HTTP client.
 *
 * @param R The type of the expected response body. Must be serializable.
 * @param url The URL to send the POST request to.
 * @param body The serialized request body.
 * @param request The ktor `HttpRequestBuilder` to configure the request.
 * @return A [R] object representing the result of the request.
 */
suspend inline fun <reified R> HttpClient.processPostRequest(
    url: String,
    parameters: Parameters = Parameters.Empty,
    body: Any,
    crossinline request: HttpRequestBuilder.() -> Unit = {}
): R {
    return tryHttpCall {
        if (!connectionListener.isConnected) error("No internet connection")
        val response = post(url) {
            contentType(ContentType.Application.Json)
            this.url.parameters.appendAll(parameters)
            setBody(body)
            request()
        }
        response.body()
    }
}

/**
 * Processes a POST request to the specified [url] with the given [formParameters] using the Ktor HTTP client.
 *
 * @param R The type of the expected response body. Must be serializable.
 * @param url The URL to send the POST request to.
 * @param formParameters The ktor parameters to include in the request.
 * @param request The ktor 'HttpRequestBuilder' to configure the request.
 * @return A [R] object representing the result of the request.
 */
suspend inline fun <reified R> HttpClient.processFormRequest(
    url: String,
    formParameters: Parameters = Parameters.Empty,
    crossinline request: HttpRequestBuilder.() -> Unit = {}
): R {
    return tryHttpCall {
        if (!connectionListener.isConnected) error("No internet connection")
        val response = submitForm(url, formParameters) {
            request()
        }
        response.body()
    }
}

/**
 * Processes a POST request to the specified [url] with the given [map] using the Ktor HTTP client.
 *
 * @param R The type of the expected response body. Must be serializable.
 * @param url The URL to send the POST request to.
 * @param request The `MutableMap` to include in the request.
 * @return A [R] object representing the result of the request.
 */
suspend inline fun <reified R> HttpClient.processSimplePostRequest(
    url: String,
    parameters: Parameters = Parameters.Empty,
    crossinline request: MutableMap<String, Any>.(HttpRequestBuilder) -> Unit
): R {
    return tryHttpCall {
        if (!connectionListener.isConnected) error("No internet connection")
        val response = post(url) {
            contentType(ContentType.Application.Json)
            this.url.parameters.appendAll(parameters)
            val body = mutableMapOf<String, Any>()
            request(body, this)
            setBody(body)
        }
        response.body()
    }
}

/**
 * Processes a GET request to the specified [url] with the given [parameters] using the Ktor HTTP client.
 *
 * @param R The type of the expected response body. Must be serializable.
 * @param url The URL to send the POST request to.
 * @param request The ktor parameters to include as @return.
 * @return A [R] object representing the result of the request.
 */
suspend inline fun <reified R> HttpClient.processGetRequest(
    url: String,
    crossinline request: HttpRequestBuilder.() -> Parameters = { Parameters.Empty }
): R {
    return tryHttpCall {
        if (!connectionListener.isConnected) error("No internet connection")
        val response = get(url) {
            url {
                parameters.appendAll(request(this@get))
            }
        }
        response.body()
    }
}

@Throws(Throwable::class)
suspend fun <R> tryHttpCall(
    block: suspend CoroutineScope.() -> R
): R {
    return try {
        withContext(context = Dispatchers.IO, block = block)
    } catch (_: JsonConvertException) {
        error("Invalid data found")
    } catch (_: RedirectResponseException) { // 3xx
        error("Unexpected redirect from server")
    } catch (_: ClientRequestException) { // 4xx
        error("Client request error")
    } catch (_: ServerResponseException) { // 5xx
        error("Server internal error")
    } catch (_: ConnectTimeoutException) {
        error("Connection timed out")
    } catch (_: SocketTimeoutException) {
        error("Server response timeout")
    } catch (_: UnresolvedAddressException) {
        error("No internet connection")
    } catch (_: IOException) {
        error("Network I/O error")
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        error(e.message ?: "Unknown error occurred")
    }
}

//suspend inline fun <reified R> HttpResponse.getSuccessBody(): R {
//    return if (status.isSuccess()) {
//        tryGet { body<ResultDto>() }?.let {
//            if (it.code != null && it.code != 200) {
//                error(it.message ?: HttpStatusCode.fromValue(it.code).description)
//            }
//        }
//        body<R>().apply {
//            log("processRequest")
//        }
//    } else {
//        val errorBody = tryGet { body<ErrorDto>() }
//        error(errorBody?.error ?: errorBody?.errorMsg ?: status.description)
//    }
//}