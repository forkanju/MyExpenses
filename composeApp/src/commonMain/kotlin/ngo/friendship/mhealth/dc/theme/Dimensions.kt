package ngo.friendship.mhealth.dc.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Dimen {

    // Basic spacing
    val None = 0.dp
    val One = 1.dp
    val Tiny = 2.dp
    val Small = 4.dp
    val SmallPlus = 6.dp
    val Medium = 8.dp
    val MediumPlus = 12.dp
    val Standard = 16.dp
    val StandardPlus = 20.dp
    val Large = 24.dp
    val LargePlus = 32.dp
    val ExtraLarge = 40.dp
    val ExtraLargePlus = 48.dp
    val Huge = 56.dp
    val HugePlus = 64.dp
    val Massive = 80.dp
    val MassivePlus = 96.dp

    // Component dimensions
    val ButtonHeight = 56.dp
    val TextFieldHeight = 56.dp
    val IconSizeSmall = 16.dp
    val IconSizeMedium = 24.dp
    val IconSizeLarge = 32.dp
    val CardElevation = 8.dp
    val CardBorderWidth = 2.dp
    val CardBorderWidthThick = 4.dp

    private var _screenSize :DpSize? = null
    // Screen size
    @Composable
    fun screenSize() = LocalWindowInfo.current.containerDpSize.also {
        _screenSize = it
    }

    // Screen width
    @Composable
    fun screenWidth() = (_screenSize ?: screenSize()).width

    // Screen height
    @Composable
    fun screenHeight() = (_screenSize ?: screenSize()).height

    // Responsive dimensions
    @Composable
    fun responsiveHorizontalPadding()= PaddingValues(horizontal = responsivePadding())
    @Composable
    fun responsiveSecondaryHorizontalPadding()= PaddingValues(horizontal = responsiveSecondaryPadding())

    @Composable
    fun responsiveVerticalPadding()= PaddingValues(vertical = responsivePadding())

    @Composable
    fun responsivePadding(): Dp = when {
        screenWidth() <= 360.dp -> Standard
        screenWidth() in 361.dp..480.dp -> Large
        else -> ExtraLarge
    }

    @Composable
    fun responsiveSecondaryPadding(): Dp = when {
        screenWidth() <= 360.dp -> 0.dp
        screenWidth() in 361.dp..480.dp -> Medium
        else -> MediumPlus
    }

    @Composable
    fun responsiveSmallPadding(): Dp = when {
        screenWidth() <= 360.dp -> 0.dp
        screenWidth() in 361.dp..480.dp -> Small
        else -> SmallPlus
    }

    @Composable
    fun responsiveLogoSize(): Dp = when {
        screenWidth() <= 360.dp -> HugePlus
        screenWidth() in 361.dp..480.dp -> Massive
        else -> MassivePlus
    }

    @Composable
    fun responsiveButtonHeight(): Dp = if (isSmallPhone()) ExtraLargePlus else ButtonHeight

    @Composable
    fun responsiveTextFieldHeight(): Dp = if (isSmallPhone()) Huge else TextFieldHeight

    @Composable
    fun responsiveIconSize(): Dp = when {
        isSmallPhone() -> LargePlus
        isLargePhone() -> ExtraLarge
        else -> ExtraLargePlus
    }

    // Screen type detection
    @Composable
    fun isTablet() = screenWidth() >= 600.dp

    @Composable
    fun isSmallPhone() = screenWidth() <= 360.dp

    @Composable
    fun isLargePhone() = screenWidth() in 481.dp..< 600.dp

    // Card width fraction
    @Composable
    fun cardWidthFraction() = when {
        isTablet() -> 0.8f
        isLargePhone() -> 0.9f
        else -> 1f
    }
}

// Extension for DP to SP conversion
val Dp.sp get() = value.sp
   