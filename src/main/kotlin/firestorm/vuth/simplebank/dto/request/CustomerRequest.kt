package firestorm.vuth.simplebank.dto.request

data class CustomerRequest(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
)
