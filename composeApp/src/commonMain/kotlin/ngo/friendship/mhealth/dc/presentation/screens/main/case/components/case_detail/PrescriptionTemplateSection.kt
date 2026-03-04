import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.theme.DarkerGray
import ngo.friendship.mhealth.dc.theme.FocusedBorderColor
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.TextSecondary
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

@Composable
fun PrescriptionTemplateSection(
    title: String = "You can select from template",
    linkText: String = "See all prescription template",
    chips: List<String>,
    onLinkClick: () -> Unit,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = linkText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = PrimaryColor,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { onLinkClick() }
            )
        }

        Spacer(Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(chips) { label ->
                OutlineFilterChip(
                    text = label,
                    selected = false, // চাইলে state দিয়ে control করবে
                    onClick = { onChipClick(label) }
                )
            }
        }
    }
}

@Composable
private fun OutlineFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = DarkerGray
            )
        },
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(6.dp),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = UnfocusedBorderColor,
            selectedBorderColor = FocusedBorderColor,
            borderWidth = 1.dp
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            selectedContainerColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PrescriptionHeaderPreview() {
    MaterialTheme {
        OutlineFilterChip(
            text = "label",
            selected = false, // চাইলে state দিয়ে control করবে
            onClick = { })
    }
}
