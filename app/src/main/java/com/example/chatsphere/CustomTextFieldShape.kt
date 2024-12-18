package com.example.chatsphere

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp


class CustomTextFieldShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // Define your custom path here for unique corner shapes
            moveTo(30f, 0f) // Start point at top-left corner
            lineTo(size.width - 30f, 0f) // Top edge to top-right corner
            lineTo(size.width, 30f) // Curve down to bottom-right corner
            lineTo(size.width, size.height - 30f) // Bottom edge to bottom-right corner
            lineTo(size.width - 30f, size.height) // Curve left to bottom-left corner
            lineTo(0f, size.height) // Bottom edge to top-left corner
            close()
        }
        return Outline.Generic(path)
    }
    fun DrawScope.drawSpeechBubble() {
        val bubbleWidth = size.width
        val bubbleHeight = size.height
        val tailHeight = 20.dp.toPx()
        val tailWidth = 20.dp.toPx()

        // Draw the main rounded rectangle
        drawRoundRect(
            color = Color.Black,
            size = Size(bubbleWidth, bubbleHeight - tailHeight),
            cornerRadius = CornerRadius(16.dp.toPx())
        )

        // Draw the tail
        val path = Path().apply {
            moveTo(bubbleWidth / 2 - tailWidth / 2, bubbleHeight - tailHeight)
            lineTo(bubbleWidth / 2, bubbleHeight)
            lineTo(bubbleWidth / 2 + tailWidth / 2, bubbleHeight - tailHeight)
        }
        drawPath(
            path = path,
            color = Color.Black,
            style = Stroke(width = 2.dp.toPx())
        )
    }

//
//fun DrawScope.drawSpeechBubble(message: String, bubbleColor: Color) {
//    val bubbleWidth = size.width - 16.dp.toPx() // Adjusting for padding
//    val bubbleHeightBaseLineOffsetY = 20f // Offset to accommodate tail height
//
//    // Draw the main rounded rectangle for the speech bubble body.
//    drawRoundRect(
//        color = bubbleColor,
//        size = Size(bubbleWidth, size.height - bubbleHeightBaseLineOffsetY),
//        cornerRadius = CornerRadius(16.dp.toPx())
//    )
//
//    // Draw the tail of the speech bubble.
//    val path = Path().apply {
//        moveTo(bubbleWidth / 2 - 10f, size.height - bubbleHeightBaseLineOffsetY)
//        lineTo(bubbleWidth / 2, size.height)
//        lineTo(bubbleWidth / 2 + 10f, size.height - bubbleHeightBaseLineOffsetY)
//    }
//
//    drawPath(path = path, color = bubbleColor)
//
//    // Draw the text inside the speech bubble.
//    drawContext.canvas.nativeCanvas.apply {
//        drawText(
//            message,
//            24f, // X position for text padding from left.
//            (size.height - bubbleHeightBaseLineOffsetY) / 2 + 8.dp.toPx(), // Y position for vertical centering.
//            android.graphics.Paint().apply {
//                color = android.graphics.Color.WHITE // Text color.
//                textSize = 16.sp.toPx() // Text size.
//                isAntiAlias = true // Smooth text rendering.
//            }
//        )
//    }
//}
}
