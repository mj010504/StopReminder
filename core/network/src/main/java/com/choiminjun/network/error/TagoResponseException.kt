package com.choiminjun.network.error

class TagoResponseException(
    val code: String,
    override val message: String,
) : Exception("[$code] $message")

fun parseTagoError(errorBody: String, httpCode: Int): TagoResponseException {
    return if (errorBody.trim().startsWith("<")) {
        val code = Regex("<returnReasonCode>(.*?)</returnReasonCode>").find(errorBody)?.groupValues?.get(1) ?: "XML_ERR"
        val msg = Regex("<returnAuthMsg>(.*?)</returnAuthMsg>").find(errorBody)?.groupValues?.get(1) ?: errorBody
        TagoResponseException(code, msg)
    } else {
        // XML이 아닌 경우(JSON 에러거나 알 수 없는 형식) 기본 형식으로 반환
        TagoResponseException(httpCode.toString(), errorBody.ifEmpty { "Unknown Error" })
    }
}
