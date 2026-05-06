package ngo.friendship.mhealth.dc.presentation.screens.case

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseIntent
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components.addDiagnosis
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components.addInvestigation
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components.removeDiagnosis
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components.removeInvestigation
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab
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
        onIntent(CaseIntent.LoadMedicineList())
        loadSetupData()
    }

    private fun loadSetupData() {
        launch {
            mainRepository.getSetupData().collectLatest { setupData ->
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
            is CaseIntent.LoadInterviewDetails -> loadInterviewDetails(intent.interviewId)
            is CaseIntent.LoadQuestionAnswerData -> loadQuestionAnswerData(intent.interviewId)
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
                _state.update { it.copy(medicineComposerState = intent.state) }
            }

            is CaseIntent.AddDiagnosis -> {
                updateFormState(addDiagnosis(_state.value.formState, intent.diagnosis))
            }

            is CaseIntent.RemoveDiagnosis -> {
                updateFormState(removeDiagnosis(_state.value.formState, intent.diagnosis))
            }

            is CaseIntent.AddPrescription -> {
                updateFormState(_state.value.formState.copy(prescriptions = _state.value.formState.prescriptions + intent.item))
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
                _state.update { it.copy(customMessageState = intent.messageState) }
            }

            is CaseIntent.TogglePrescriptionSms -> {
                _state.update { it.copy(isPrescriptionWithSmsChecked = intent.checked) }
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

            loadQuestionAnswerData(interviewId)
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
        launch {
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
            if (_state.value.formState.prescriptions.isNotEmpty()) {
                _state.update { it.copy(isSaving = true) }
                try {
                    // First, save the doctor feedback
                    repository.saveDoctorFeedback(formState = _state.value.formState)
                    showSuccess("Feedback saved successfully")

                    // After successful save, update interview status to "Close"
                    repository.updateInterviewStatus(
                        interviewId = _state.value.formState.interviewId ?: 0,
                        status = CaseTab.Answered.apiParam // "Close"
                    )

                    _state.update { it.copy(isSaving = false) }
                    _uiEvent.send(CaseUiEvent.NavigateBack)
                } catch (e: Exception) {
                    _state.update { it.copy(isSaving = false) }
                    "Error saving feedback: ${e.message}".log("CASE_DEBUG")
                    showError("Failed to save feedback")
                }
            } else {
                showSuccess("Please add the prescriptions")
            }
        }
    }

    private fun updateFormState(state: DoctorFeedbackFormState) {
        _state.update { it.copy(formState = state) }
    }
}