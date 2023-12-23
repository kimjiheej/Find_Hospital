package ddwu.com.mobile.naverretrofittest.network

import ddwu.com.mobile.naverretrofittest.data.HospitalRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IBookAPIService {
    @GET("getMdclinst")
    fun getBooksByKeyword (
        @Query("ServiceKey") serviceKey: String,
        @Query("pageNo") page: String,
        @Query("numOfRows") rows :  String,
        @Query("gu") gu: String? //
    )  : Call<HospitalRoot>
}