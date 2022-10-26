package com.example.myapplication.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.myapplication.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Utils {
    companion object {
        fun fromHtml(str: String): String {
            return if (Build.VERSION.SDK_INT >= 24) Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
                .toString() else Html.fromHtml(str).toString()
        }

        fun setMerginToviews(view: View, topMergin: Int, width: Int, height: Int) {
            val layoutParams = LinearLayout.LayoutParams(width, height)
            layoutParams.setMargins(40, topMergin, 40, 0)
            view.layoutParams = layoutParams
        }

        fun setMerginToviews(view: View) {
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(40, 20, 40, 0)
            view.layoutParams = layoutParams
        }

        fun method(str: String?): String? {
            var str = str
            if (str != null && str.isNotEmpty()) {
                str = str.substring(0, str.length - 1)
            }
            return str
        }

        fun getCustomColorStateList(context: Context): ColorStateList {
            return ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    ContextCompat.getColor(context, R.color.purple_200),//disabled
                    ContextCompat.getColor(context, R.color.purple_500) //enabled
                )
            )
        }

        fun getCurrentDate(): Date {
            val calendar = Calendar.getInstance()
            return calendar.time
        }

        fun getDateStringToShow(date: Date, format: String): String {
            try {
                val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH);
                return simpleDateFormat.format(date)
            } catch (e: Exception) {
                Log.d("Error", e.toString())
                return ""
            }
        }

        fun getDateFromString(date: String, format: String): Date? {
            Log.i("StringDate", date)
            val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            try {
                val formatedDate = simpleDateFormat.parse(date)
                Log.i("ParsedDate", formatedDate.toString())
                return formatedDate
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun setSwitchColor(v: SwitchCompat, context: Context) {
            // thumb color of your choice
            val thumbColor = ContextCompat.getColor(context, R.color.purple_500)

            // trackColor is the thumbColor with 30% transparency (77)
            val trackColor = Color.argb(
                77, Color.red(thumbColor),
                Color.green(thumbColor),
                Color.blue(thumbColor)
            )

            // setting the thumb color
            DrawableCompat.setTintList(
                v.thumbDrawable, ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(
                        thumbColor,
                        Color.WHITE
                    )
                )
            )

            // setting the track color
            DrawableCompat.setTintList(
                v.trackDrawable, ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(
                        trackColor,
                        Color.parseColor("#4D000000") // full black with 30% transparency (4D)
                    )
                )
            )
        }

        fun isValidEmailAddress(emailAddress: String): Boolean {
            val emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
            val p = Pattern.compile(emailPattern)
            val m = p.matcher(emailAddress)
            return m.matches()
        }

        fun isValidTelephoneNumber(telephoneNumber: String): Boolean {
            return telephoneNumber.length >= 10
        }
    }
}