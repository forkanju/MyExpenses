package ngo.friendship.mhealth.dc.presentation.screen.case

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItemDto
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
import ngo.friendship.mhealth.dc.utils.JsonSorter
import ngo.friendship.mhealth.dc.utils.defJson
import ngo.friendship.mhealth.dc.utils.isNotNull
import ngo.friendship.mhealth.dc.utils.log
import ngo.friendship.mhealth.dc.utils.minusAt
import ngo.friendship.mhealth.dc.utils.toJson

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
        loadAdviceList()
        loadDoseHistory()
        launch(loading = Loading.Gone) {
            repository.getQuestionAnswerData(forceRefresh = true)
        }
    }

    private fun loadAdviceList() {
        launch(loading = Loading.Gone) {
            val list = mainRepository.getAdviceList()
            _state.update { it.copy(adviceList = list.map { it.title }) }
        }
    }

    private fun loadDoseHistory() {
        _state.update { it.copy(doseHistory = repository.getDoseHistory()) }
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

                        PrescriptionItemDto(
                            medId = medDto.medicineId.orEmpty(),
                            genName = medDto.genericName.orEmpty()
                                .ifBlank { medicine?.genericName.orEmpty() },
                            medType = medicine?.type.orEmpty(),
                            medName = medDto.brandName.orEmpty()
                                .ifBlank { medicine?.brandName.orEmpty() },
                            medQty = medDto.qty.orEmpty(),
                            saleQty = medDto.qty.orEmpty(),
                            medDuration = medDto.durationDay.orEmpty(),
                            mtr = medDto.dailyDose.orEmpty(),
                            mtrLbl = medDto.dailyDose.orEmpty(),
                            mtrSf = medDto.dailyDose.orEmpty(),
                            afm = medDto.dailyDose.orEmpty(),
                            afmSf = medDto.dailyDose.orEmpty(),
                            sf = medDto.sf.orEmpty(),
                            smsSf = medDto.smsSf.orEmpty()
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
                repository.saveDoseToHistory(intent.item.mtr)
                loadDoseHistory()
                val exists = _state.value.formState.prescriptions.any {
                    (it.medId.isNotBlank() && it.medId != "-1" && it.medId == intent.item.medId) ||
                            (it.genName == intent.item.genName && it.medName == intent.item.medName)
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

                        PrescriptionItemDto(
                            medId = medDto.medicineId.orEmpty(),
                            genName = medDto.genericName.orEmpty()
                                .ifBlank { medicine?.genericName.orEmpty() },
                            medType = medicine?.type.orEmpty(),
                            medName = medDto.brandName.orEmpty()
                                .ifBlank { medicine?.brandName.orEmpty() },
                            medQty = medDto.qty.orEmpty(),
                            saleQty = medDto.qty.orEmpty(),
                            medDuration = medDto.durationDay.orEmpty(),
                            mtr = medDto.dailyDose.orEmpty(),
                            mtrLbl = medDto.dailyDose.orEmpty(),
                            mtrSf = medDto.dailyDose.orEmpty(),
                            afm = medDto.dailyDose.orEmpty(),
                            afmSf = medDto.dailyDose.orEmpty(),
                            sf = medDto.sf.orEmpty(),
                            smsSf = medDto.smsSf.orEmpty()
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
            try {
                _state.update {
                    it.copy(
                        selectedTab = sourceTab,
                        interviewDetails = InterviewDetails(),
                        error = null
                    )
                }

                val statusToSend = when (sourceTab) {
                    CaseTab.Pending -> CaseTab.Opened.apiParam
                    CaseTab.Answered -> null
                    CaseTab.Opened -> null
                    CaseTab.Older -> null
                }

                // Launch independent tasks in parallel
                statusToSend?.let { status ->
                    launch(loading = Loading.Gone) {
                        repository.updateInterviewStatus(
                            interviewId = interviewId,
                            status = status
                        )
                    }
                }

                launch(loading = Loading.Gone) {
                    loadMedicineList(_state.value.medicineComposerState.doseType.ifEmpty { "Tab" })
                }

                launch(loading = Loading.Gone) {
                    loadQuestionAnswerData(interviewId)
                }

                if (sourceTab == CaseTab.Answered) {
                    launch(loading = Loading.Gone) {
                        loadDoctorFeedback(interviewId)
                    }
                }

                // Main data fetch
                val details = repository.getInterviewDetails(interviewId = interviewId)
                _state.update { it.copy(interviewDetails = details) }

                // Wait for other critical background jobs if needed, 
                // but usually they can finish on their own as they update the state.
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to open case") }
            }
        }
    }

    private fun loadInterviewDetails(interviewId: Long) {
        if (interviewId == -1L) return
        launch(onEnd = {
            if (_state.value.interviewDetails.interviewId == -1L && _state.value.error == null)
                launch { _uiEvent.send(CaseUiEvent.NavigateBack) }
        }) {
            try {
                _state.update { it.copy(interviewDetails = InterviewDetails(), error = null) }
                val details = repository.getInterviewDetails(interviewId = interviewId)
                _state.update { it.copy(interviewDetails = details) }

                "Beneficiary Code for mobile fetch: ${details.beneficiaryCode}".log("CASE_DEBUG")
                
                // Parallelize profile fetches
                details.beneficiaryCode.takeIf { it.isNotBlank() }?.let { benefCode ->
                    launch {
                        val profile = repository.getBeneficiaryProfile(benefCode)
                        "Fetched Beneficiary Profile: ${profile?.benefName}, Mobile: ${profile?.mobileNumber}".log(
                            "CASE_DEBUG"
                        )
                        val lastVisitedDate = profile?.serviceList?.firstOrNull()?.interviewTime ?: ""
                        _state.update {
                            it.copy(
                                lastVisitedDate = lastVisitedDate,
                                formState = it.formState.copy(
                                    mobile = profile?.mobileNumber ?: ""
                                )
                            )
                        }
                    }
                }

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
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to load interview details") }
            }
        }
    }

    private fun loadQuestionAnswerData(interviewId: Long) {
        launch(loading = Loading.Gone) {
            "loadQuestionAnswerData triggered. interviewId: $interviewId".log("CAPTION_DEBUG")

            val result = try {
                repository.getQuestionAnswerData()
            } catch (e: Exception) {
                "Error fetching QuestionAnswerData: ${e.message}".log("CAPTION_DEBUG")
                _state.value.questionAnswerData
            }

            _state.update {
                it.copy(
                    questionAnswerData = result,
                    formState = it.formState.copy(
                        interviewId = if (interviewId == -1L) null else interviewId,
                        questionAnswers = JsonArray(emptyList()),
                        questionAnswers2 = JsonArray(emptyList())
                    )
                )
            }
        }
    }

    fun modifyQuestionAnswerJson(
        obj: JsonObject?,
        key: String,
        field: String,
        value: String
    ): JsonObject? {
        if (obj == null) return null
        return buildJsonObject {
            for ((k, v) in obj) {
                if (k == key && v is JsonObject) {
                    put(k, buildJsonObject {
                        for ((innerK, innerV) in v) put(innerK, innerV)
                        put(field, JsonPrimitive(value))
                    })
                } else {
                    put(k, v)
                }
            }
        }
    }

    fun modifyQuestionAnswerCaptionJson(
        obj: JsonObject?,
        key: String,
        targetField: String,
        newValue: String
    ): JsonObject? {
        if (obj == null) return null
        return buildJsonObject {
            for ((k, v) in obj) {
                if (k == key && v is JsonObject) {
                    put(k, buildJsonObject {
                        for ((innerK, innerV) in v) {
                            if (innerK == "options" && innerV is JsonArray) {
                                put(innerK, buildJsonArray {
                                    innerV.forEachIndexed { index, element ->
                                        if (index == 0 && element is JsonObject) {
                                            add(buildJsonObject {
                                                for ((ek, ev) in element) put(ek, ev)
                                                put(targetField, JsonPrimitive(newValue))
                                            })
                                        } else {
                                            add(element)
                                        }
                                    }
                                })
                            } else {
                                put(innerK, innerV)
                            }
                        }
                    })
                } else {
                    put(k, v)
                }
            }
        }
    }

    fun modifyQuestionAnswerJson2(
        array: JsonArray,
        index: Int,
        value: String?
    ): JsonArray {
        if (index !in 0 until array.size) return array

        return try {
            buildJsonArray {
                for (i in 0 until array.size) {
                    if (i == index) {
                        val oldObj = array[i].jsonObject
                        add(buildJsonObject {
                            for ((k, v) in oldObj) {
                                put(k, v)
                            }
                            put("answer", JsonPrimitive(value))
                        })
                    } else {
                        add(array[i])
                    }
                }
            }
        } catch (_: Exception) {
            array
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
            var modifiedJsonArray1: JsonArray = _state.value.formState.questionAnswers
            var modifiedJsonArray2: JsonArray = _state.value.formState.questionAnswers2

            var result = _state.value.questionAnswerData
            if (result.questionAnswerJson.isEmpty() && result.questionAnswerJson2.isEmpty()) {
                "QuestionAnswerData is empty in state, fetching from repository...".log("CAPTION_DEBUG")
                try {
                    result = repository.getQuestionAnswerData()
                    _state.update { it.copy(questionAnswerData = result) }
                } catch (e: Exception) {
                    "Error fetching QuestionAnswerData in saveDoctorFeedback: ${e.message}".log("CAPTION_DEBUG")
                }
            }

            var finalTemplateCaption = ""
            var finalTemplateArray: JsonArray = JsonArray(emptyList())

            // Logging and parsing captions for debugging
            val firstCaption = result.questionAnswerJson.firstOrNull()?.caption
            if (!firstCaption.isNullOrBlank()) {
                try {
                    val jsonElement = defJson.parseToJsonElement(firstCaption)
                    val questionnaire = jsonElement.jsonArray.getOrNull(0)
                        ?.jsonObject?.get("QUESTIONNAIRE_DATA")
                        ?.jsonObject?.get("questionnaire")
                        ?.jsonObject

                    var updatedQ = questionnaire
                    updatedQ = modifyQuestionAnswerJson(
                        updatedQ,
                        "question10001",
                        "hidden",
                        if (_state.value.formState.commentsForFcm.isNotBlank()) "false" else "true"
                    )

                    updatedQ = modifyQuestionAnswerCaptionJson(
                        updatedQ,
                        "question10001",
                        "caption",
                        _state.value.formState.commentsForFcm
                    )

                    updatedQ = modifyQuestionAnswerJson(
                        updatedQ,
                        "question10002",
                        "hidden",
                        if (_state.value.formState.prescriptions.isNotEmpty()) "false" else "true"
                    )

                    val jsonArray =
                        Json.parseToJsonElement(_state.value.formState.prescriptions.toJson()).jsonArray
                    val pipeSeparatedPrescriptions: String =
                        JsonSorter.convertJsonArrayToPipeSeparated(jsonArray)
                    updatedQ = modifyQuestionAnswerCaptionJson(
                        updatedQ,
                        "question10002",
                        "caption",
                        pipeSeparatedPrescriptions
                    )

                    updatedQ = modifyQuestionAnswerCaptionJson(
                        updatedQ,
                        "question10002",
                        "value",
                        pipeSeparatedPrescriptions
                    )

                    updatedQ = modifyQuestionAnswerJson(
                        updatedQ,
                        "question10003",
                        "hidden",
                        if (_state.value.formState.selectedReferralCenter?.refCenterId.isNotNull()) "false" else "true"
                    )
                    updatedQ = modifyQuestionAnswerJson(
                        updatedQ,
                        "question10003",
                        "defaultval",
                        _state.value.formState.selectedReferralCenter?.refCenterId.toString()
                    )

                    updatedQ = modifyQuestionAnswerJson(
                        updatedQ,
                        "question10004",
                        "hidden",
                        if (_state.value.formState.nextFollowUpDate.isNotNull()) "false" else "true"
                    )

                    updatedQ = modifyQuestionAnswerJson(
                        updatedQ,
                        "question10004",
                        "defaultval",
                        _state.value.formState.nextFollowUpDate
                    )

                    updatedQ = modifyQuestionAnswerJson(
                        updatedQ,
                        "question10005",
                        "hidden",
                        if (_state.value.formState.investigationResult.isNotBlank()) "false" else "true"
                    )

                    if (updatedQ != null) {
                        val updatedJsonArray = buildJsonArray {
                            jsonElement.jsonArray.forEach { element ->
                                add(buildJsonObject {
                                    element.jsonObject.forEach { (key, value) ->
                                        if (key == "QUESTIONNAIRE_DATA") {
                                            put("QUESTIONNAIRE_DATA", buildJsonObject {
                                                value.jsonObject.forEach { (innerKey, innerValue) ->
                                                    if (innerKey == "questionnaire") {
                                                        put("questionnaire", updatedQ)
                                                    } else {
                                                        put(innerKey, innerValue)
                                                    }
                                                }
                                            })
                                        } else {
                                            put(key, value)
                                        }
                                    }
                                })
                            }
                        }

                        finalTemplateArray = updatedJsonArray
                        "Modified First Caption JSON: $updatedJsonArray".log("CAPTION_DEBUG")
                    }

                    val secondCaption = result.questionAnswerJson2.firstOrNull()?.caption
                    if (!secondCaption.isNullOrBlank()) {
                        var jsonArray = defJson.parseToJsonElement(secondCaption).jsonArray
                        "Modified Second Caption Array:modifyQuestionAnswerJson2 $jsonArray".log("CAPTION_DEBUG")
                        val comments =
                            _state.value.formState.commentsForFcm.replace(Regex("[\\r\\n]"), " ")
                        jsonArray = modifyQuestionAnswerJson2(jsonArray, 1, comments)
                        jsonArray = modifyQuestionAnswerJson2(
                            jsonArray,
                            2,
                            _state.value.formState.prescriptions.toJson()
                        )
                        jsonArray = modifyQuestionAnswerJson2(
                            jsonArray,
                            3,
                            _state.value.formState.selectedReferralCenter?.refCenterId.toString()
                        )
                        jsonArray = modifyQuestionAnswerJson2(
                            jsonArray,
                            4,
                            _state.value.formState.nextFollowUpDate
                        )
                        jsonArray = modifyQuestionAnswerJson2(
                            jsonArray,
                            5,
                            _state.value.formState.investigationResult
                        )

                        modifiedJsonArray2 = buildQuestionAnswerJson2(jsonArray)

                        "Modified Second Caption Array:modifyQuestionAnswerJson2 $modifiedJsonArray2".log(
                            "CAPTION_DEBUG"
                        )
                    }

                } catch (e: Exception) {
                    "Error parsing caption JSON: ${e.message}".log("CAPTION_DEBUG")
                }
            }
            val currentState = _state.value
            val formState = currentState.formState
            val isFromTemplate = currentState.interviewDetails.interviewId == -1L

            // 1. Prescription Validation (Optional)
            val duplicates =
                formState.prescriptions.groupBy { it.medId.ifBlank { "${it.genName} ${it.medName}" } }
                    .any { it.value.size > 1 }
            if (duplicates) {
                showWarning("Duplicate same medicine entry not allowed!")
                return@launch
            }

            // 2. Case-specific field validation
            if (!isFromTemplate) {
                val hasMedicine = formState.prescriptions.isNotEmpty()
                val hasAdvice = formState.doctorAdvice.isNotBlank()
                val hasReferral = formState.selectedReferralCenter != null
                val hasFollowUp = formState.nextFollowUpDate.isNotBlank()

                if (!hasMedicine && !hasAdvice && !hasReferral && !hasFollowUp) {
                    showWarning("Please provide at least one: Medicine, Advice, Referral or Follow-up date")
                    return@launch
                }
            } else {
                if (currentState.templateName.isBlank()) {
                    showWarning("Please enter template name")
                    return@launch
                }
            }

            _state.update { it.copy(isSaving = true) }
            try {
                val isUpdate = isFromTemplate && currentState.prescriptionId != null

                // Update formState with correct values and SWAP fields for server requirement
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
                    isBeneficiaryChecked = currentState.customMessageState.isBeneficiaryChecked,
                    // SWAP: QUESTION_ANSWER_JSON gets modifiedJsonArray2 (answers)
                    questionAnswers = finalTemplateArray ?: JsonArray(emptyList()),
                    // SWAP: QUESTION_ANSWER_JSON2 gets finalTemplateArray (template string wrapped in JsonArray)
                    questionAnswers2 = modifiedJsonArray2 ?: JsonArray(emptyList()),
                )

                // Log final state for debugging
                "Final Data to Server - QA1: ${finalFormState.questionAnswers}".log("SAVE_DEBUG")
                "Final Data to Server - QA2: ${finalFormState.questionAnswers2}".log("SAVE_DEBUG")

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

    fun buildQuestionAnswerJson2(array: JsonArray): JsonArray {
        return try {
            JsonArray(
                array.filter { element ->
                    val answer = element.jsonObject["answer"]?.jsonPrimitive?.contentOrNull
                    answer != null && answer != "0"
                }
            )
        } catch (e: Exception) {
            JsonArray(emptyList())
        }
    }

    private fun loadDoctorFeedback(interviewId: Long) {
        if (interviewId == -1L) return
        launch(loading = Loading.Gone) {
            try {
                _state.update { it.copy(error = null) }
                val response = mainRepository.getDoctorFeedback(interviewId)

                val code = response.responseCode
                val feedbackList = response.data?.patientInterviewFeedback

                if (code == "0" || code == "00") {
                    _state.update { it.copy(error = "No data found") }
                    return@launch
                }

                if (code == "01" || code == "1") {
                    if (feedbackList.isNullOrEmpty()) {
                        _state.update { it.copy(error = "No data found") }
                    } else {
                        val feedback = feedbackList[0]

                        // Parse prescriptions
                        val prescriptions = mutableListOf<PrescriptionItemDto>()
                        feedback.prescribedMedicine?.let { jsonStr ->
                            try {
                                if (jsonStr.startsWith("[")) {
                                    val list =
                                        defJson.decodeFromString<List<PrescriptionItemDto>>(jsonStr)
                                    prescriptions.addAll(list)
                                } else if (jsonStr.startsWith("{")) {
                                    val item =
                                        defJson.decodeFromString<PrescriptionItemDto>(jsonStr)
                                    prescriptions.add(item)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        val setupData =
                            mainRepository.getSetupData(forceRefresh = false).firstOrNull()

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
                            val names = feedback.diagDesc.split(",").map { it.trim() }
                                .filter { it.isNotBlank() }
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
                            val names = feedback.investigationAdvice.split(",").map { it.trim() }
                                .filter { it.isNotBlank() }
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
                } else {
                    _state.update {
                        it.copy(
                            error = response.errorDesc ?: "Failed to load feedback"
                        )
                    }
                }
            } catch (e: Exception) {
                "Error loading doctor feedback: ${e.message}".log("CASE_DEBUG")
                _state.update { it.copy(error = e.message ?: "An unknown error occurred") }
            }
        }
    }

    private fun updateFormState(state: DoctorFeedbackFormState) {
        _state.update { it.copy(formState = state) }
    }
}
