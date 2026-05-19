# API Error Message Handling - Summary of Changes

## Overview
Updated the codebase to ensure that API error messages (`errorDesc`) are properly displayed in the UI when the API returns `responseCode != "01"`. 

## Problem
Previously, when API calls failed (responseCode != "01"), generic error messages were shown to the user instead of the actual error description from the API response.

## Solution
Updated repository methods to return `Pair<Boolean, String?>` instead of just `Boolean`, extracting the `errorDesc` field from the API response when the call is not successful.

## Files Modified

### 1. Domain Layer - Repository Interfaces

#### `MainRepository.kt`
**Changes:**
- `saveAdvice(title: String, content: String): Boolean` → `Pair<Boolean, String?>`
- `saveDiagnosis(title: String): Boolean` → `Pair<Boolean, String?>`
- `saveInvestigation(title: String): Boolean` → `Pair<Boolean, String?>`
- `saveMedicine(...): Boolean` → `Pair<Boolean, String?>`

#### `CaseRepository.kt`
**Changes:**
- `updateInterviewStatus(interviewId: Long, status: String): Boolean` → `Pair<Boolean, String?>`

### 2. Data Layer - Repository Implementations

#### `MainRepositoryImpl.kt`
**Changes:**
- `saveAdvice()`: Returns `success to (if (success) null else response.errorDesc)`
- `saveDiagnosis()`: Returns `success to (if (success) null else response.errorDesc)`
- `saveInvestigation()`: Returns `success to (if (success) null else response.errorDesc)`
- `saveMedicine()`: Returns `success to (if (success) null else response.errorDesc)`

#### `CaseRepositoryImpl.kt`
**Changes:**
- `updateInterviewStatus()`: Returns `success to (if (success) null else response.errorDesc)`

### 3. Presentation Layer - ViewModels

#### `MainViewModel.kt`
**Changes:**
- `saveAdvice()`: Changed to destructure the Pair and display error message
  ```kotlin
  val (isSuccess, errorMessage) = mainRepository.saveAdvice(title, content)
  if (isSuccess) { ... } else { showError(errorMessage ?: "Failed to save advice") }
  ```
- `saveInvestigation()`: Same pattern applied

#### `MedicineListViewModel.kt`
**Changes:**
- `saveMedicine()`: Updated to destructure the Pair and display error message

#### `DxListViewModel.kt`
**Changes:**
- `createDx()`: Updated to destructure the Pair for saveDiagnosis call

#### `CaseViewModel.kt`
**Changes:**
- `saveDoctorFeedback()`: Updated to:
  1. Check the result of `saveDoctorFeedback()`
  2. Destructure the Pair from `updateInterviewStatus()`
  3. Display specific error messages from the API

## Response DTOs Affected

All response DTOs with `errorDesc` and `responseCode` fields:
- SaveAdviceResDto
- SaveDiagnosisResDto
- SaveInvestigationResDto
- SaveMedicineResDto
- UpdateInterviewStatusResDto
- SaveDoctorFeedbackResDto (already handled via toDomain() mapper)

## Fixed Pattern

**Before:**
```kotlin
val isSuccess = mainRepository.saveAdvice(title, content)
if (isSuccess) {
    showSuccess("Advice saved successfully")
} else {
    showError("Failed to save advice")  // Generic error!
}
```

**After:**
```kotlin
val (isSuccess, errorMessage) = mainRepository.saveAdvice(title, content)
if (isSuccess) {
    showSuccess("Advice saved successfully")
} else {
    showError(errorMessage ?: "Failed to save advice")  // Actual API error message!
}
```

## Error Message Flow

1. API returns response with `errorDesc` and `responseCode`
2. Repository checks `responseCode == "01"` for success
3. If not successful, extracts `errorDesc` and returns it in Pair
4. ViewModel receives the Pair, destructures it
5. If not successful, displays the actual `errorDesc` to user via Snackbar/Dialog

## Testing Recommendations

Test with these scenarios:
- Save operations when API returns `responseCode: "00"` with `errorDesc`
- Verify error messages are displayed in UI instead of generic messages
- Check that successful operations still show success message
- Verify that exception cases still show fallback error message

## Notes

- `changePassword()` method in MainRepository already followed this pattern
- `saveDoctorFeedback()` in CaseRepository uses SaveDoctorFeedbackResult which includes message
- All changes maintain backward compatibility with existing error handling

