package com.blueventor.network

import ResponseAllDriverList
import com.blueventor.network.request.RequestCheckCompanyDomain
import com.blueventor.network.request.RequestDashBoardCarPerformanceDetails
import com.blueventor.network.request.RequestDashBoardDetails
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestDriverRecharge
import com.blueventor.network.request.RequestGetAllDriverList
import com.blueventor.network.request.RequestGetDriverLocation
import com.blueventor.network.request.RequestTripDetails
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseCarList
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.network.response.ResponseDashBoardDetails
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.response.ResponseDriverWalletRecharge
import com.blueventor.network.response.ResponseGetDriverLocation
import com.blueventor.network.response.ResponseTripDetails
import com.blueventor.network.response.ResponseTripList
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

    @POST("=?type=taxiwise_trip_count")
    suspend fun getCarPerformanceDetails(
        @Body requestDashBoardCarPerformanceDetails: RequestDashBoardCarPerformanceDetails
    ):ResponseCarPerformance

    @POST("=?type=single_driver_details")
    suspend fun getDriverDetails(
        @Body requestDriverDetails: RequestDriverDetails
    ):ResponseDriverDetails

    @POST("=?type=get_driver_trips")
    suspend fun getTripList(
        @Body requestTripList: RequestTripList
    ):ResponseTripList
    @POST("=?type=get_driver_trips_viewdetail")
    suspend fun getTripDetails(
        @Body requestTripDetails: RequestTripDetails
    ):ResponseTripDetails


    @POST("=?type=company_driver_details")
    suspend fun getAllDriverList(
        @Body requestGetAllDriverList: RequestGetAllDriverList
    ):ResponseAllDriverList

    @POST("=?type=add_driver_wallet")
    suspend fun addAmountDriverWallet(
        @Body requestDriverRecharge: RequestDriverRecharge
    ): ResponseDriverWalletRecharge


    @POST("=?type=dispatcher_tracking")
    suspend fun getDriverLocation(
        @Body requestGetDriverLocation: RequestGetDriverLocation
    ): ResponseGetDriverLocation

    @POST("=?type=company_taxi_details")
    suspend fun getCompanyWiseCarList(
        @Body requestGetDriverLocation: RequestGetDriverLocation
    ): ResponseCarList
}