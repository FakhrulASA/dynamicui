package com.example.myapplication.model.itemmodel

data class FormResult(
    val bill_info_required_parameter: List<BillInfoRequiredParameter>,
    val category_name: String,
    val utility_code: String,
    val utility_image: String,
    val utility_value: String
)