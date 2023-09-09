package com.github.kevinrob

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.json.jackson.DatabindCodec
//import org.msgpack.jackson.dataformat.MessagePackFactory


//________________________________________________________________________________
// RANDOM GENERATORS
//________________________________________________________________________________
fun randomId(): Int = (1..99999).random()

fun getRandomPortNumber() : Int {
    return (41413..61612).random()
}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('1'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}
//________________________________________________________________________________
// JSON
//________________________________________________________________________________
val mapper = DatabindCodec.mapper()
    .registerModule(Jdk8Module())
    .registerModule(JavaTimeModule()) // new module, NOT JSR310Module
    .registerModule(KotlinModule())
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)


//val mapper = jacksonObjectMapper()
//        .registerModule(Jdk8Module())
//        .registerModule(JavaTimeModule()) // new module, NOT JSR310Module
//        .registerModule(KotlinModule())
//        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
//        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

// NOTE: require default constructor
//val mapperMsgPack = ObjectMapper(MessagePackFactory())

//fun String.toMsgPack() : ByteArray{
//    return mapperMsgPack.writeValueAsBytes(this)
//}

//________________________________________________________________________________
// GENERIC FUNCTION RETURN
//________________________________________________________________________________
sealed class GenericResult<R> {
    data class Success<R>(val result: R): GenericResult<R>()
    data class Failure<R>(val message: String, val cause: Exception? = null) : GenericResult<R>()
}


