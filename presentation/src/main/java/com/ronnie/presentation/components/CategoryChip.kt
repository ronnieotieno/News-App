package com.ronnie.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ronnie.presentation.R

@Composable
fun CategoryChip(
    modifier: Modifier, category: String, isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.light_blue),
        ),
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) colorResource(id = R.color.blue) else colorResource(id = R.color.lightest_blue),
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(category)
                }
            )
        ) {
            Text(
                text = category,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else colorResource(id = R.color.primaryTextColor),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ChipGroup(
    categories: List<String>,
    selectedCategory: String? = null,
    onSelectedChanged: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .width(intrinsicSize = IntrinsicSize.Max)
        ) {
            categories.forEach { category ->
                CategoryChip(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    category = category,
                    isSelected = selectedCategory == category,
                    onSelectionChanged = {
                        onSelectedChanged(it)
                    },
                )
            }
        }
    }
}