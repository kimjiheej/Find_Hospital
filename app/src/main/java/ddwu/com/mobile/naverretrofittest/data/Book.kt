package ddwu.com.mobile.naverretrofittest.data

data class HospitalRoot(
    val resultCode: Long,
    val resultMsg: String,
    val numOfRows: Long,
    val pageNo: Long,
    val totalCount: Long,
    val items: List<Item>
)

data class Item(
    val no: Long,
    val type: String,
    val inst_nm: String,
    val lctn: String,
    val telno: String
)
