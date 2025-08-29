package com.blueventor.network

import com.blueventor.network.request.RequestCheckCompanyDomain
import com.blueventor.network.request.RequestDashBoardDetails
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.network.response.ResponseDashBoardDetails
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("?type=check_companydomain")
    suspend fun checkCompanyDomain(
        @Body requestCompanyDomain: RequestCheckCompanyDomain
    ): ResponseCheckCompanyDomain

    @POST("=?type=vendor_login")
    suspend fun vendorLogin(
        @Body requestloginaccess: Requestloginaccess
    ):RespondeLoginAccess


    @POST("=?type=home_dashboard_count")
    suspend fun getDashBoardDetails(
        @Body requestDashBoardDetails: RequestDashBoardDetails
    ):ResponseDashBoardDetails
}