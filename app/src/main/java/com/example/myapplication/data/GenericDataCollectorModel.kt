package com.example.myapplication.data

import android.view.View
import com.example.myapplication.model.itemmodel.BillInfoRequiredParameter
import com.example.myapplication.model.itemmodel.FormResult

data class GenericDataCollectorModel(
    var params: BillInfoRequiredParameter, var value: String, var view: View
)