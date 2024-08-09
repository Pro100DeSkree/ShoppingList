package com.deskree.shoppinglist.utils

import android.text.Html
import android.text.Spanned
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object HtmlManager {
    fun getFromHtml(text: String): Spanned{
        return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
    }

    fun toHtml(text: Spanned): String{
        return Html.toHtml(text, Html.FROM_HTML_MODE_COMPACT)
    }
}

fun isHtmlString(input: String): Boolean {
    val htmlPattern = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>".toRegex()
    return htmlPattern.containsMatchIn(input)
}

fun convertHtmlToAnnotatedString(htmlText: String): AnnotatedString {
    val spanned = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
    val annotatedString = buildAnnotatedString {
        append(spanned.toString())

        val spans = spanned.getSpans(0, spanned.length, Any::class.java)
        for (span in spans) {
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            val flags = spanned.getSpanFlags(span)

            when (span) {
                is android.text.style.StyleSpan -> {
                    when (span.style) {
                        android.graphics.Typeface.BOLD -> {
                            addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = start, end = end)
                        }
                        android.graphics.Typeface.ITALIC -> {
                            addStyle(style = SpanStyle(fontStyle = FontStyle.Italic), start = start, end = end)
                        }
                    }
                }
                is android.text.style.ForegroundColorSpan -> {
                    addStyle(style = SpanStyle(color = Color(span.foregroundColor)), start = start, end = end)
                }
                is android.text.style.AbsoluteSizeSpan -> {
                    addStyle(style = SpanStyle(fontSize = span.size.toFloat().sp), start = start, end = end)
                }
                // Add more styles here if needed
            }
        }
    }
    return annotatedString
}