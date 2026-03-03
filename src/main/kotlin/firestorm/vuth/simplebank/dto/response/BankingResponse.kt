package firestorm.vuth.simplebank.dto.response

data class BankingResponse(
    val success: Boolean,
    val referenceId: String,
    val message: String
)
