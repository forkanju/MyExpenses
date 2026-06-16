package ngo.friendship.mhealth.dc.presentation.screen.case

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.addDiagnosis
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.addInvestigation
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.removeDiagnosis
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.removeInvestigation
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.utils.defJson
import ngo.friendship.mhealth.dc.utils.log
import ngo.friendship.mhealth.dc.utils.minusAt

class CaseViewModel(
    private val repository: CaseRepository,
    private val mainRepository: MainRepository
) : BaseViewModel() {
    private val _uiEvent = Channel<CaseUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(CaseUiState())
    val state = _state.asStateFlow()

    init {
        onIntent(CaseIntent.LoadMedicineList("Tab"))
        loadSetupData()
        loadPrescriptionTemplates()
    }

    private fun loadPrescriptionTemplates() {
        launch(loading = Loading.Gone) {
            val templates = mainRepository.getPrescriptionTemplateDtos()
            _state.update { it.copy(prescriptionTemplates = templates) }
        }
    }

    private fun loadSetupData() {
        launch(loading = Loading.Gone) {
            mainRepository.getSetupData(forceRefresh = false).collectLatest { setupData ->
                _state.update {
                    it.copy(
                        medicineBrandTypeList = setupData.medicineBrandTypes.map { it.type }
                            .distinct()
                    )
                }
            }
        }
    }

    fun onIntent(intent: CaseIntent) {
        when (intent) {
            is CaseIntent.LoadFromTemplate -> {
                launch(loading = Loading.Gone) {
                    val template = intent.template
                    val allMedicines = repository.getAllMedicines()

                    val newPrescriptions = template.medicineList?.map { medDto ->
                        val medicine =
                            allMedicines.find { it.medicineId == medDto.medicineId?.toLongOrNull() }

                        val mealTimeText = when (medDto.takingRule) {
                            "1" -> "Before"
                            "2" -> "After"
                            else -> medDto.takingRule
                        }

                        val medicineName = if (medicine != null) {
                            "${medicine.type}: ${medicine.genericName} (${medicine.brandName})"
                        } else if (!medDto.brandName.isNullOrBlank()) {
                            medDto.brandName
                        } else if (!medDto.genericName.isNullOrBlank()) {
                            medDto.genericName
                        } else {
                            "Unknown"
                        }

                        PrescriptionItem(
                            medicineName = medicineName,
                            dose = medDto.dailyDose.orEmpty(),
                            duration = medDto.durationDay.orEmpty(),
                            medicineId = medDto.medicineId?.toLongOrNull(),
                            mealTime = mealTimeText
                        )
                    } ?: emptyList()

                    _state.update {
                        it.copy(
                            templateName = template.title,
                            isGlobalTemplate = template.isGlobal,
                            prescriptionId = template.prescriptionId,
                            formState = it.formState.copy(
                                prescriptionName = template.title,
                                isGlobalPrescription = if (template.isGlobal) 1 else 0,
                                prescriptionId = template.prescriptionId,
                                doctorAdvice = template.doctorAdvice.orEmpty(),
                                commentsForFcm = template.commentsForFcm.orEmpty(),
                                doctorNotes = template.doctorNotes.orEmpty(),
                                prescriptions = newPrescriptions,
                                isPresTempSave = 1
                            )
                        )
                    }
                }
            }

            is CaseIntent.LoadInterviewDetails -> loadInterviewDetails(intent.interviewId)
            is CaseIntent.LoadQuestionAnswerData -> loadQuestionAnswerData(intent.interviewId)
            is CaseIntent.LoadDoctorFeedback -> loadDoctorFeedback(intent.interviewId)
            is CaseIntent.LoadMedicineList -> loadMedicineList(intent.type)
            CaseIntent.SaveDoctorFeedback -> saveDoctorFeedback()
            is CaseIntent.SetMode -> {
                _state.update { it.copy(mode = intent.mode) }
            }

            is CaseIntent.SetSelectedTab -> {
                _state.update { it.copy(selectedTab = intent.tab) }
            }

            is CaseIntent.UpdateFormState -> updateFormState(intent.state)
            is CaseIntent.UpdateMedicineComposerState -> {
                val oldType = _state.value.medicineComposerState.doseType
                val newType = intent.state.doseType
                _state.update { it.copy(medicineComposerState = intent.state) }
                if (oldType != newType) {
                    onIntent(CaseIntent.LoadMedicineList(newType))
                }
            }

            is CaseIntent.AddDiagnosis -> {
                updateFormState(addDiagnosis(_state.value.formState, intent.diagnosis))
            }

            is CaseIntent.RemoveDiagnosis -> {
                updateFormState(removeDiagnosis(_state.value.formState, intent.diagnosis))
            }

            is CaseIntent.AddPrescription -> {
                val exists = _state.value.formState.prescriptions.any {
                    (it.medicineId != null && it.medicineId == intent.item.medicineId) ||
                            (it.medicineName == intent.item.medicineName)
                }
                if (exists) {
                    showWarning("Duplicate same medicine entry not allowed!")
                } else {
                    updateFormState(_state.value.formState.copy(prescriptions = _state.value.formState.prescriptions + intent.item))
                }
            }

            is CaseIntent.RemovePrescription -> {
                updateFormState(
                    _state.value.formState.copy(
                        prescriptions = _state.value.formState.prescriptions.minusAt(
                            intent.index
                        )
                    )
                )
            }

            is CaseIntent.UpdateDoctorAdvice -> {
                updateFormState(_state.value.formState.copy(doctorAdvice = intent.advice))
            }

            is CaseIntent.AddInvestigation -> {
                updateFormState(addInvestigation(_state.value.formState, intent.investigation))
            }

            is CaseIntent.RemoveInvestigation -> {
                updateFormState(removeInvestigation(_state.value.formState, intent.investigation))
            }

            is CaseIntent.UpdateInvestigationResult -> {
                updateFormState(_state.value.formState.copy(investigationResult = intent.result))
            }

            is CaseIntent.UpdateCommentsForFcm -> {
                updateFormState(_state.value.formState.copy(commentsForFcm = intent.comments))
            }

            is CaseIntent.UpdateReferralCenter -> {
                updateFormState(_state.value.formState.copy(selectedReferralCenter = intent.center))
            }

            is CaseIntent.UpdateDoctorNotes -> {
                updateFormState(_state.value.formState.copy(doctorNotes = intent.notes))
            }

            is CaseIntent.UpdateFollowUpDate -> {
                updateFormState(_state.value.formState.copy(nextFollowUpDate = intent.date))
            }

            CaseIntent.ToggleDatePicker -> {
                _state.update { it.copy(isDatePickerVisible = !it.isDatePickerVisible) }
            }

            is CaseIntent.SetSummaryTab -> {
                _state.update { it.copy(selectedSummaryTab = intent.index) }
            }

            CaseIntent.ToggleSendMessageDialog -> {
                _state.update { it.copy(isSendMessageDialogVisible = !it.isSendMessageDialogVisible) }
            }

            is CaseIntent.UpdateCustomMessage -> {
                _state.update {
                    it.copy(
                        customMessageState = intent.messageState,
                        formState = it.formState.copy(
                            isFcmChecked = intent.messageState.isFcmChecked,
                            isBeneficiaryChecked = intent.messageState.isBeneficiaryChecked
                        )
                    )
                }
            }

            is CaseIntent.TogglePrescriptionSms -> {
                _state.update {
                    it.copy(
                        isPrescriptionWithSmsChecked = intent.checked,
                        customMessageState = it.customMessageState.copy(isFcmChecked = intent.checked),
                        formState = it.formState.copy(isFcmChecked = intent.checked)
                    )
                }
            }

            is CaseIntent.ToggleCalledBack -> {
                _state.update {
                    it.copy(
                        isCalledBackChecked = intent.checked,
                        formState = it.formState.copy(isCalledBack = intent.checked)
                    )
                }
            }

            is CaseIntent.UpdatePatientName -> {
                _state.update { it.copy(patientName = intent.name) }
            }

            is CaseIntent.UpdateGender -> {
                _state.update { it.copy(selectedGender = intent.gender) }
            }

            is CaseIntent.UpdateAge -> {
                _state.update { it.copy(age = intent.age) }
            }

            is CaseIntent.UpdateOfficeId -> {
                _state.update { it.copy(officeId = intent.id) }
            }

            is CaseIntent.UpdateSector -> {
                _state.update { it.copy(sector = intent.sector) }
            }

            is CaseIntent.UpdateInterviewNote -> {
                _state.update { it.copy(interviewNote = intent.note) }
            }

            is CaseIntent.OpenCaseFromTab -> {
                openCaseFromTab(intent.interviewId, intent.sourceTab)
            }

            CaseIntent.ToggleSaveTemplateDialog -> {
                _state.update {
                    val nextVisible = !it.isSaveTemplateDialogVisible
                    it.copy(
                        isSaveTemplateDialogVisible = nextVisible,
                        formState = it.formState.copy(
                            isPresTempSave = if (nextVisible) 1 else 0
                        )
                    )
                }
            }

            is CaseIntent.UpdateTemplateName -> {
                _state.update {
                    it.copy(
                        templateName = intent.name,
                        formState = it.formState.copy(prescriptionName = intent.name)
                    )
                }
            }

            CaseIntent.ToggleGlobalTemplate -> {
                _state.update {
                    val nextGlobal = !it.isGlobalTemplate
                    it.copy(
                        isGlobalTemplate = nextGlobal,
                        formState = it.formState.copy(isGlobalPrescription = if (nextGlobal) 1 else 0)
                    )
                }
            }

            CaseIntent.SaveAsTemplate -> {
                saveAsTemplate()
            }

            is CaseIntent.SelectPrescriptionTemplate -> {
                launch(loading = Loading.Gone) {
                    val template = intent.template
                    val allMedicines = repository.getAllMedicines()
                    val newPrescriptions = template.medicineList?.map { medDto ->
                        val medicine =
                            allMedicines.find { it.medicineId == medDto.medicineId?.toLongOrNull() }

                        val mealTimeText = when (medDto.takingRule) {
                            "1" -> "Before"
                            "2" -> "After"
                            else -> medDto.takingRule
                        }

                        val medicineName = if (medicine != null) {
                            "${medicine.type}: ${medicine.genericName} (${medicine.brandName})"
                        } else if (!medDto.brandName.isNullOrBlank()) {
                            medDto.brandName
                        } else if (!medDto.genericName.isNullOrBlank()) {
                            medDto.genericName
                        } else {
                            "Unknown"
                        }

                        PrescriptionItem(
                            medicineName = medicineName,
                            dose = medDto.dailyDose.orEmpty(),
                            duration = medDto.durationDay.orEmpty(),
                            medicineId = medDto.medicineId?.toLongOrNull(),
                            mealTime = mealTimeText
                        )
                    } ?: emptyList()

                    val templateName = template.prescLabel.orEmpty()
                        .substringBefore(" Prescription").trim()

                    _state.update {
                        it.copy(
                            templateName = templateName,
                            isGlobalTemplate = template.isGlobalPress == "1",
                            formState = it.formState.copy(
                                prescriptionName = templateName,
                                doctorAdvice = template.doctorAdvice.orEmpty(),
                                commentsForFcm = template.messageToFcm.orEmpty(),
                                doctorNotes = template.doctorFindings.orEmpty(),
                                prescriptions = newPrescriptions
                            )
                        )
                    }
                }
            }
        }
    }

    private fun saveAsTemplate() {
        // Implementation for saving as template
        // For now, just close the dialog and show a success message
        launch {
            _state.update {
                it.copy(
                    isSaveTemplateDialogVisible = false,
                    formState = it.formState.copy(
                        isPresTempSave = 1,
                        prescriptionName = it.templateName,
                        isGlobalPrescription = if (it.isGlobalTemplate) 1 else 0
                    )
                )
            }
//            showSuccess("Template saved successfully")
        }
    }

    private fun openCaseFromTab(
        interviewId: Long,
        sourceTab: CaseTab
    ) {
        if (interviewId == -1L) return

        launch {
            _state.update {
                it.copy(
                    selectedTab = sourceTab,
                    interviewDetails = InterviewDetails()
                )
            }

            val statusToSend = when (sourceTab) {
                CaseTab.Pending -> CaseTab.Opened.apiParam
                CaseTab.Answered -> null
                CaseTab.Opened -> null
                CaseTab.Older -> null
            }

            if (statusToSend != null) {
                repository.updateInterviewStatus(
                    interviewId = interviewId,
                    status = statusToSend
                )
            }

            val details = repository.getInterviewDetails(interviewId = interviewId)
            _state.update { it.copy(interviewDetails = details) }

            loadMedicineList(_state.value.medicineComposerState.doseType.ifEmpty { "Tab" })
            loadQuestionAnswerData(interviewId)

            if (sourceTab == CaseTab.Answered) {
                loadDoctorFeedback(interviewId)
            }
        }
    }

    private fun loadInterviewDetails(interviewId: Long) {
        if (interviewId == -1L) return
        launch(onEnd = {
            if (_state.value.interviewDetails.interviewId == -1L)
                launch { _uiEvent.send(CaseUiEvent.NavigateBack) }
        }) {
            _state.update { it.copy(interviewDetails = InterviewDetails()) }
            val details = repository.getInterviewDetails(interviewId = interviewId)
            _state.update { it.copy(interviewDetails = details) }

            val fcmInfo = details.fcmInfo
            "Interview loaded. fcmInfo: $fcmInfo".log("CASE_DEBUG")

            fcmInfo?.takeIf { it.isNotBlank() }?.let { fcmCode ->
                launch {
                    _state.update { it.copy(fcmProfileState = it.fcmProfileState.copy(isLoading = true)) }
                    "Fetching FCM Profile for code: $fcmCode".log("CASE_DEBUG")
                    val profile = repository.getFcmProfile(fcmCode)
                    "Fetched profile: ${profile?.userName}, ${profile?.location}".log("CASE_DEBUG")
                    _state.update {
                        it.copy(
                            fcmProfileState = it.fcmProfileState.copy(
                                fcmProfile = profile,
                                isLoading = false
                            )
                        )
                    }
                }
            } ?: run {
                "fcmInfo is null or blank, skipping FCM fetch".log("CASE_DEBUG")
            }
        }
    }

    private fun loadQuestionAnswerData(interviewId: Long) {
        launch(loading = Loading.Gone) {
            val result = repository.getQuestionAnswerData()
            _state.update {
                it.copy(
                    questionAnswerData = result,
                    formState = it.formState.copy(
                        interviewId = if (interviewId == -1L) null else interviewId,
                        questionAnswers = result.questionAnswerJson,
                        questionAnswers2 = result.questionAnswerJson2
                    )
                )
            }
        }
    }

    private fun loadMedicineList(
        type: String = "Tab"
    ) {
        launch(loading = Loading.Gone) {
            val list = repository.getMedicineList(type = type)
            _state.update { it.copy(medicineList = list) }
        }
    }

    private fun saveDoctorFeedback() {
        launch {
            val currentState = _state.value
            val formState = currentState.formState
            val isFromTemplate = currentState.interviewDetails.interviewId == -1L

            // 1. Prescription Validation (Optional)
            // Check for duplicate medicines
            val duplicates = formState.prescriptions.groupBy { it.medicineId ?: it.medicineName }.any { it.value.size > 1 }
            if (duplicates) {
                showWarning("Duplicate same medicine entry not allowed!")
                return@launch
            }

            // 2. Case-specific field validation (Skip if saving as Template)
            if (!isFromTemplate) {
                // Diagnosis (DX) is optional

                // Investigation Result is optional
                
                if (formState.doctorAdvice.isBlank()) {
                    showWarning("Please enter doctor advice")
                    return@launch
                }

                if (formState.commentsForFcm.isBlank() && !currentState.isPrescriptionWithSmsChecked) {
                    showWarning("Please enter comments for FCM")
                    return@launch
                }

                // Doctor notes is optional

                // Next Follow Up Date is optional as per requirement
            } else {
                // Template specific validation
                if (currentState.templateName.isBlank()) {
                    showWarning("Please enter template name")
                    return@launch
                }
            }

            _state.update { it.copy(isSaving = true) }
            try {
                val isUpdate = isFromTemplate && currentState.prescriptionId != null

                // Update formState with correct template save status and comments for FCM if needed
                val finalFormState = formState.copy(
                    isPresTempSave = if (isFromTemplate) 1 else formState.isPresTempSave,
                    prescriptionId = currentState.prescriptionId,
                    prescriptionName = if (isFromTemplate) currentState.templateName else formState.prescriptionName,
                    isGlobalPrescription = if (isFromTemplate) (if (currentState.isGlobalTemplate) 1 else 0) else formState.isGlobalPrescription,
                    commentsForFcm = if (!isFromTemplate && currentState.isPrescriptionWithSmsChecked && currentState.customMessageState.isFcmChecked) {
                        currentState.customMessageState.messageText
                    } else {
                        formState.commentsForFcm
                    },
                    isFcmChecked = currentState.customMessageState.isFcmChecked,
                    isBeneficiaryChecked = currentState.customMessageState.isBeneficiaryChecked
                )

                // First, save the doctor feedback
                val feedbackResult = repository.saveDoctorFeedback(formState = finalFormState)

                if (!feedbackResult.isSuccess) {
                    _state.update { it.copy(isSaving = false) }
                    showError(feedbackResult.message)
                    return@launch
                }

                // Only send SMS and update status if it's a real case (not a template creation)
                if (!isFromTemplate) {
                    // If SMS is checked and Beneficiary is selected, send the SMS
                    if (currentState.isPrescriptionWithSmsChecked && currentState.customMessageState.isBeneficiaryChecked) {
                        val smsText = currentState.customMessageState.messageText
                        val phoneNumber = currentState.customMessageState.phoneNumber.ifBlank {
                            currentState.fcmProfileState.fcmProfile?.mobileNo
                                ?: formState.mobile
                        }

                        if (phoneNumber.isNotBlank() && smsText.isNotBlank()) {
                            try {
                                repository.sendSms(msisdn = phoneNumber, message = smsText)
                                "SMS sent successfully to $phoneNumber".log("CASE_DEBUG")
                            } catch (e: Exception) {
                                "Failed to send SMS: ${e.message}".log("CASE_DEBUG")
                            }
                        }
                    }

                    // After successful save, update interview status to "Close"
                    val (statusSuccess, statusError) = repository.updateInterviewStatus(
                        interviewId = formState.interviewId ?: 0,
                        status = CaseTab.Answered.apiParam // "Close"
                    )
                    if (!statusSuccess) {
                        _state.update { it.copy(isSaving = false) }
                        showError(statusError ?: "Failed to update interview status")
                        return@launch
                    }
                }

                val successMessage = when {
                    isUpdate -> "Template updated successfully"
                    isFromTemplate -> "Template saved successfully"
                    else -> "Feedback saved successfully"
                }
                showSuccess(successMessage)

                _state.update { it.copy(isSaving = false) }
                _uiEvent.send(CaseUiEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false) }
                "Error saving feedback: ${e.message}".log("CASE_DEBUG")
                showError(e.message ?: "Something went wrong!")
            }
        }
    }

    private fun loadDoctorFeedback(interviewId: Long) {
        launch(loading = Loading.Gone) {
            try {
                val response = mainRepository.getDoctorFeedback(interviewId)
                println("response69: ${response.data}")
                if (response.responseCode == "01") {
                    val feedbackList = response.data?.patientInterviewFeedback
                    if (!feedbackList.isNullOrEmpty()) {
                        val feedback = feedbackList[0]

                        // Parse prescriptions
                        val prescriptions = mutableListOf<PrescriptionItem>()
                        feedback.prescribedMedicine?.let { jsonStr ->
                            try {
                                if (jsonStr.startsWith("[")) {
                                    val list =
                                        defJson.decodeFromString<List<PrescriptionItem>>(jsonStr)
                                    prescriptions.addAll(list)
                                } else if (jsonStr.startsWith("{")) {
                                    val item = defJson.decodeFromString<PrescriptionItem>(jsonStr)
                                    prescriptions.add(item)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        val setupData = mainRepository.getSetupData(forceRefresh = false).firstOrNull()

                        val selectedReferralCenter = setupData?.referralCenters?.find {
                            it.refCenterId == feedback.refCenterId
                        }

                        val selectedDiagnoses = mutableListOf<Diagnosis>()
                        if (!feedback.diagId.isNullOrBlank() && feedback.diagId != "0") {
                            setupData?.diagnoses?.find { it.diagId == feedback.diagId }?.let {
                                selectedDiagnoses.add(it)
                            }
                        } else if (!feedback.diagDesc.isNullOrBlank()) {
                            // Handle comma separated diagnoses
                            val names = feedback.diagDesc.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            names.forEach { name ->
                                setupData?.diagnoses?.find {
                                    it.diagName.equals(name, ignoreCase = true)
                                }?.let {
                                    selectedDiagnoses.add(it)
                                } ?: run {
                                    selectedDiagnoses.add(Diagnosis(diagName = name))
                                }
                            }
                        }

                        val selectedInvestigations = mutableListOf<Investigation>()
                        if (!feedback.investigationAdvice.isNullOrBlank()) {
                            // Handle comma separated investigations
                            val names = feedback.investigationAdvice.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            names.forEach { name ->
                                setupData?.investigations?.find {
                                    it.investigationName.equals(name, ignoreCase = true) ||
                                            it.investigationCode.equals(name, ignoreCase = true)
                                }?.let {
                                    selectedInvestigations.add(it)
                                } ?: run {
                                    selectedInvestigations.add(Investigation(investigationName = name))
                                }
                            }
                        }

                        _state.update {
                            it.copy(
                                formState = it.formState.copy(
                                    doctorNotes = feedback.doctorFindings ?: "",
                                    commentsForFcm = feedback.messageToFcm ?: "",
                                    investigationResult = feedback.investigationResult ?: "",
                                    nextFollowUpDate = feedback.nextFollowupDate?.split(" ")
                                        ?.firstOrNull() ?: "",
                                    prescriptions = prescriptions,
                                    selectedReferralCenter = selectedReferralCenter,
                                    selectedDiagnoses = selectedDiagnoses,
                                    selectedInvestigations = selectedInvestigations
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                "Error loading doctor feedback: ${e.message}".log("CASE_DEBUG")
            }
        }
    }

    private fun updateFormState(state: DoctorFeedbackFormState) {
        _state.update { it.copy(formState = state) }
    }
}