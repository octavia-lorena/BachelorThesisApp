package com.example.bachelorthesisapp.data.businesses.remote

import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.businesses.remote.api.BusinessApi
import com.example.bachelorthesisapp.data.businesses.remote.dto.BusinessDto
import com.example.bachelorthesisapp.data.notifications.NotificationApi
import com.example.bachelorthesisapp.data.notifications.PushNotification
import javax.inject.Inject

class BusinessRemoteDataSourceImpl @Inject constructor(
    private val api: BusinessApi,
    private val notificationApi: NotificationApi,
) : BusinessRemoteDataSource {

    override suspend fun getAllBusiness(): List<BusinessDto> = api.getBusinessesData()
    override suspend fun getBusinessByType(type: String): List<BusinessDto> =
        api.getBusinessesDataByType(type)

    override suspend fun getBusinessByCity(city: String): List<BusinessDto> =
        api.getBusinessesDataByCity(city)


    override suspend fun getBusinessById(businessId: String): BusinessDto =
        api.getBusinessDataById(businessId)


    override suspend fun updateBusinessDeviceToken(id: String, token: String): BusinessDto =
        api.updateBusinessDeviceToken(id, token)

    override suspend fun addBusiness(business: BusinessEntity): BusinessDto = api.addBusiness(business)

    suspend fun sendNotification(pushNotification: PushNotification) =
        notificationApi.postNotification(pushNotification)

}