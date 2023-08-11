package com.example.bachelorthesisapp.data.model.converters

import androidx.room.TypeConverter
import com.example.bachelorthesisapp.domain.model.BusinessType

class BusinessTypeConverter {

    @TypeConverter
    fun businessTypeToString(businessType: BusinessType): String = businessType.name


}