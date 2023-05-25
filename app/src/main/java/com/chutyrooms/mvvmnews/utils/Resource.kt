package com.chutyrooms.mvvmnews.utils

/*** A generic class to wrap around our network responses
 * it is very useful to differentiate between successful and error responses
 * and also helps us to handle the loading state */

/*** Sealed class is a abstract class but we can define which
 * classes are allowed to inherit from that Resource class*/
sealed class Resource<T>(val data:T?=null, val message:String?=null

) {

    class Success<T>(data: T) :Resource<T>(data)
    class Error<T>(message: String,data: T?=null) :Resource<T>(data,message)
    class Loading<T> :Resource<T>( )


}