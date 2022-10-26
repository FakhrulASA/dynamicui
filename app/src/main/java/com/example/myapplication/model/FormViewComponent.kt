package com.example.myapplication.model

import android.view.View
import com.example.myapplication.model.itemmodel.BillInfoRequiredParameter

class FormViewComponent(
    val createdView: View,
    viewComponentModel: BillInfoRequiredParameter
) {
    private val viewComponentModel: BillInfoRequiredParameter = viewComponentModel
    fun getViewComponentModel(): BillInfoRequiredParameter {
        return viewComponentModel
    }

}