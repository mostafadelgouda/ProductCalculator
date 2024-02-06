package com.example.productcalculator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.productcalculator.R


// Set of Material typography styles to start with

val protestriotregular = FontFamily(
    Font(R.font.worksansvariablefontwght)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    displayLarge = TextStyle(
        fontFamily = protestriotregular,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    displayMedium = TextStyle(
        fontFamily = protestriotregular,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    displaySmall = TextStyle(
        fontFamily = protestriotregular,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = protestriotregular,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
//    labelSmall = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    )

)
