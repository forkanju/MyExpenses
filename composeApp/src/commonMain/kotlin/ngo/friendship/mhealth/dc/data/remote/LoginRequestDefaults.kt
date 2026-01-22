package ngo.friendship.mhealth.dc.data.remote

import ngo.friendship.mhealth.dc.data.remote.dto.LoginRequestDto

class LoginRequestDefaults(
    private val orgCode: String,
    private val orgId: Int,
    private val moduleName: String,
    private val langProvider: () -> String,
    private val imeiProvider: () -> String?,
    private val timeProvider: () -> String
) {
    fun build(userCode: String, password: String): LoginRequestDto {
        return LoginRequestDto(
            orgCode = orgCode,
            orgId = orgId,
            userCode = userCode,
            password = password,
            imei = imeiProvider(),
            demo = false,
            requestType = "USER_GATE",
            requestName = "LOGIN_WEB",
            moduleName = moduleName,
            requestTime = timeProvider(),
            lang = langProvider()
        )
    }
}