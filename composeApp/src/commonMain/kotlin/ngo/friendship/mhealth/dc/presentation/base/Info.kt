package ngo.friendship.mhealth.dc.presentation.base

data class Info(val message: String="", val type: Type = Type.Default) {
    enum class Type() {
        SuccessSignIn,
        SuccessSignUp,
        InvalidEmail,
        InvalidPassword,
        Network,
        ServerError,
        Default
    }
}

