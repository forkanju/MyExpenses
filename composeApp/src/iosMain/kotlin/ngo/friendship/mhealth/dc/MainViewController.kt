package ngo.friendship.mhealth.dc

import androidx.compose.ui.uikit.EndEdgePanGestureBehavior
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController(
    configure = {
        endEdgePanGestureBehavior = EndEdgePanGestureBehavior.Back
    }
) { App() }