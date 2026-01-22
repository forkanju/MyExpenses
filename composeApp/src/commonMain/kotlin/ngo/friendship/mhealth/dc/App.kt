package ngo.friendship.mhealth.dc

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ngo.friendship.mhealth.dc.presentation.navigation.SetupNavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        SetupNavGraph()
    }
}