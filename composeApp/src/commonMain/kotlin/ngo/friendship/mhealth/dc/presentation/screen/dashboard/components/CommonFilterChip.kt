package ngo.friendship.mhealth.dc.presentation.screen.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun CommonFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(5.dp),
        color = if (isSelected) PrimaryBlue else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 1.dp),
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}
