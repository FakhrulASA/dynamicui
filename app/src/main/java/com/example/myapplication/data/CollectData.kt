package com.example.myapplication.data

import android.view.View
import android.widget.*
import com.example.myapplication.GenerateFormActivity.Companion.submitPropertyArrayJson
import com.example.myapplication.model.Value
import com.example.myapplication.model.itemmodel.BillInfoRequiredParameter
import com.example.myapplication.model.itemmodel.FormResult
import com.example.myapplication.utils.Utils.Companion.isValidEmailAddress
import com.example.myapplication.utils.Utils.Companion.isValidTelephoneNumber
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.*

class CollectData {
    companion object {
        /**
         * Get selected Date from date TextView.
         *
         * @param view
         * @param BillInfoRequiredParameter
         */
        fun getDataFromDateTextView(
            view: View,
            viewComponentModel: BillInfoRequiredParameter
        ) {
            val dateView = view as TextView
            val json = populateSubmitPropertyJson(viewComponentModel, null)
            json.addProperty("value", dateView.text.toString())
            submitPropertyArrayJson?.add(json)
        }

        fun getDataFromEditText(
            view: View,
            viewComponentModel: BillInfoRequiredParameter
        ): Boolean {
            val editText = view as EditText
            if (editText.text.toString() != "") {
                viewComponentModel.required.let {
                    //Not null
                    if (it == "required") {
                        if (editText.text.toString() == "") {
                            editText.error = "Please enter input"
                        } else {
                             if(editText.text.length<8){
                                 editText.error = "Length is short"

                            }
                        }
                    }
                }
                val json = populateSubmitPropertyJson(viewComponentModel, null)
                json.addProperty("value", editText.text.toString())
                submitPropertyArrayJson?.add(json)
            }
            return true
        }

        fun populateSubmitPropertyJson(
            componentItem: BillInfoRequiredParameter,
            valueModel: Value?
        ): JsonObject {
            val submitPropertiesValueObj = JsonObject()
            submitPropertiesValueObj.addProperty("label", componentItem.name)
            valueModel?.let {
                if (it.label == "Other") {
                    submitPropertiesValueObj.addProperty("value", it.value)
                } else submitPropertiesValueObj.addProperty("value", it.label)
            }
            submitPropertiesValueObj.addProperty("type", componentItem.type)
            submitPropertiesValueObj.addProperty("subtype", componentItem.data_type)
            return submitPropertiesValueObj
        }

        fun printProperties(array: JsonArray): String {
            val builder: StringBuilder = StringBuilder()
            for (i in 0 until array.size()) {
                val propObj = array[i].asJsonObject
                val label = propObj.get("label")
                val value = propObj.get("value")
                builder.append(label).append(" : ").append(value)
                builder.append("\n")
            }
            return builder.toString()
        }
    }
}