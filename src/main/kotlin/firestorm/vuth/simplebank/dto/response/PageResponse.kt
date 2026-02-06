package firestorm.vuth.simplebank.dto.response

import javax.xml.crypto.Data

data class PageResponse(
    val data: List<Data>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)
