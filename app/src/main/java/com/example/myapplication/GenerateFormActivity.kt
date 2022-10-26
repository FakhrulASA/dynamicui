package com.example.myapplication

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.NavUtils
import com.example.myapplication.data.GenericDataCollectorModel
import com.example.myapplication.databinding.ActivityGenerateFormBinding
import com.example.myapplication.databinding.FormButtonsLayoutBinding
import com.example.myapplication.model.FormViewComponent
import com.example.myapplication.model.WidgetItems
import com.example.myapplication.model.itemmodel.BillInfoRequiredParameter
import com.example.myapplication.model.itemmodel.FormResult
import com.example.myapplication.utils.Utils
import com.example.myapplication.utils.Utils.Companion.method
import com.example.myapplication.utils.Utils.Companion.setMerginToviews
import com.example.myapplication.utils.dialog.ShowDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class GenerateFormActivity : AppCompatActivity() {

    var dataStored: MutableList<GenericDataCollectorModel> = mutableListOf()
    lateinit var binding: ActivityGenerateFormBinding
    var formViewCollection: ArrayList<FormViewComponent> = arrayListOf()

    var submitRootJsonObj: JsonObject? = null

    companion object {
        var submitPropertyArrayJson: JsonArray? = null
    }

    var formComponent: FormResult? = null
    val textColor = Color.parseColor("#000000")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val json = intent.getStringExtra("value")
        Toast.makeText(this, json.toString(), Toast.LENGTH_LONG).show()

        submitRootJsonObj = JsonObject()
        submitPropertyArrayJson = JsonArray()
        json?.let {
            populateForm(it)
        }
    }

    private fun populateForm(json: String) {
        formComponent = Gson().fromJson(json, FormResult::class.java)
        var viewId = 1
        binding.miniAppFormContainer.visibility = View.VISIBLE

        //TODO:- GENERATE FORM LAYOUT
        formComponent?.let {


            it.bill_info_required_parameter.forEach { component ->
                when (component.type) {
                    "text" -> {
                        createEditableTextWithLabel(component, viewId++)

                    }
                    "spinner" -> {
                        createSpinner(component, viewId++)
                    }
                }
            }
        }
        addSubmitButtonLayout()
    }


    private fun createEditableTextWithLabel(component: BillInfoRequiredParameter, viewId: Int) {
        val editText = EditText(this)
        var rows = 1

        setMerginToviews(
            editText,
            20,
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (component.type.equals(WidgetItems.TEXTAREA.label)) editText.gravity = Gravity.NO_GRAVITY
        editText.hint = component.level
        editText.setPadding(20, 30, 20, 30)
        editText.setBackgroundResource(R.drawable.edit_text_background)
        editText.id = viewId
        component.max?.let {
            editText.filters = arrayOf<InputFilter>(LengthFilter(it.toInt()))
        }


        val finalRow = rows
        editText.setOnKeyListener { v, keyCode, event ->
            (v as EditText).lineCount > finalRow
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (editText.lineCount > finalRow) {
                    editText.setText(method(editText.text.toString()))
                    editText.setSelection(editText.text.toString().length)
                }
            }
        })

        editText.setLines(rows)
        binding.miniAppFormContainer.addView(editText)
        formViewCollection.add(FormViewComponent(editText, component))
        dataStored.add(
            GenericDataCollectorModel(
                component, editText.text.toString(), editText
            )
        )
        Log.i("EditTextInputType", editText.inputType.toString() + "")
    }


    private fun createSpinner(component: BillInfoRequiredParameter, viewId: Int) {
        var selectedIndex = 0
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(40, 30, 40, 40)
        val spinner = Spinner(this)
        spinner.id = viewId
        spinner.setBackgroundColor(Color.WHITE)
        spinner.setBackgroundResource(R.drawable.edit_text_background)
        spinner.layoutParams = layoutParams
        //Spinner data source
        val spinnerDatasource = mutableListOf<Any?>()
        var listArray = listOf("A", "B", "C")

        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            applicationContext, R.layout.form_spinner_row, listArray
        )
        spinner.adapter = spinnerAdapter
        spinner.setSelection(selectedIndex)
        binding.miniAppFormContainer.addView(spinner)
        dataStored.add(
            GenericDataCollectorModel(
                component, spinner.isSelected.toString(), spinner
            )
        )
//        formViewCollection.add(FormViewComponent(spinner, component))
    }

    private fun isLabelNull(viewComponentModel: BillInfoRequiredParameter) {
        if (viewComponentModel.name != null) createLabelForViews(viewComponentModel)
    }

    private fun isSubTypeNull(viewComponentModel: BillInfoRequiredParameter, editText: EditText) {
        if (viewComponentModel.type != null) {
            setInputTypeForEditText(editText, viewComponentModel)
        }
    }

    private fun isPlaceHolderNull(
        viewComponentModel: BillInfoRequiredParameter, editText: EditText
    ) {
        if (viewComponentModel.name != null) editText.hint = viewComponentModel.name
    }

    private fun isValueNull(viewComponentModel: BillInfoRequiredParameter, view: TextView) {
        viewComponentModel.name.let {
            view.text = it
        }
    }

    private fun createLabelForViews(viewComponentModel: BillInfoRequiredParameter) {
        val label = TextView(this)
        label.setTextColor(Color.BLACK)
        label.setTypeface(null, Typeface.BOLD)
        setMerginToviews(
            label,
            40,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        viewComponentModel.name?.let { labelText ->
            viewComponentModel.required?.let {
                if (it == "required") {
                    label.text = "Is Required"
                } else {
                    label.text = createStringForViewLabel(false, labelText)
                }
            }
            binding.miniAppFormContainer.addView(label)
        }
    }

    /**
     * EditText Input type selection
     */
    private fun setInputTypeForEditText(
        editText: EditText, viewComponentModel: BillInfoRequiredParameter
    ) {
        when (viewComponentModel.data_type) {
            "password" -> editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            "email" -> editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            "tel" -> editText.inputType = InputType.TYPE_CLASS_PHONE
            "dateTimeLocal" -> editText.inputType = InputType.TYPE_CLASS_DATETIME
            else -> editText.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun createStringForViewLabel(
        required: Boolean, label: String
    ): SpannableStringBuilder {
        val labelStr = Utils.fromHtml(label)
        return if (required) {
            labelStringForRequiredField(labelStr)
        } else {
            val username = SpannableString(labelStr)
            val description = SpannableStringBuilder()
            username.setSpan(
                RelativeSizeSpan(1.1f), 0, username.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            username.setSpan(
                ForegroundColorSpan(textColor),
                0,
                username.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            description.append(username)
            description
        }
    }

    private fun labelStringForRequiredField(label: String): SpannableStringBuilder {
        val username = SpannableString(label)
        val description = SpannableStringBuilder()
        username.setSpan(
            RelativeSizeSpan(1.1f), 0, username.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        username.setSpan(
            ForegroundColorSpan(textColor), 0, username.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        description.append(username)
        val commentSpannable = SpannableString(" *")
        commentSpannable.setSpan(
            ForegroundColorSpan(Color.RED),
            0,
            commentSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        commentSpannable.setSpan(
            RelativeSizeSpan(1.0f), 0, commentSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        description.append(commentSpannable)
        return description
    }

    private fun addSubmitButtonLayout() {
        val layoutInflater = LayoutInflater.from(this)
        val buttonViewBinding: FormButtonsLayoutBinding =
            FormButtonsLayoutBinding.inflate(layoutInflater)
        binding.miniAppFormContainer.addView(buttonViewBinding.root)
        buttonViewBinding.btnReset.setOnClickListener {
            startActivity(intent)
            finish()
        }

        buttonViewBinding.btnSubmit.setOnClickListener {
            for (formViewComponent in formViewCollection) {
                val view: View = formViewComponent.createdView
                val viewComponentModel: BillInfoRequiredParameter =
                    formViewComponent.getViewComponentModel()
                var str: String = ""
                var errorEditText = false
                var errorSPinner = false
                when (viewComponentModel.type) {
                    "text" -> {
                        dataStored.forEach { generic ->
                            generic.params.let {

                                when (it.type) {

                                    "text" -> {
                                        (generic.view as EditText).apply {
                                            if (this.text.length < generic.params.min.toInt()) {
                                                this.error = "Minimum lenght required is ${generic.params.min}"
                                                errorEditText = true
                                            } else {
                                                str = str + this.text + "\n"
                                                errorEditText = false

                                            }
                                        }
                                    }
                                    "spinner"->{

                                        (generic.view as Spinner).apply {
                                            if (this.selectedItemPosition==0) {
                                                errorSPinner = true
                                                Toast.makeText(this@GenerateFormActivity,"Please select ${generic.params.name} spinner value", Toast.LENGTH_LONG).show()
                                            } else {
                                                str = str + this.isSelected+ "\n"
                                                errorSPinner = false
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(!errorEditText&&!errorSPinner){
                    ShowDialog.customDialog(this, "Collected Data", str, null)
                }
            }
        }
    }
}