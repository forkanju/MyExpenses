package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate

data class PrescriptionTemplateListState(
    val templates: List<PrescriptionTemplate> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

sealed interface PrescriptionTemplateListIntent {
    data object LoadTemplates : PrescriptionTemplateListIntent
    data class Search(val query: String) : PrescriptionTemplateListIntent
    data class DeleteTemplate(val template: PrescriptionTemplate) : PrescriptionTemplateListIntent
    data object Refresh : PrescriptionTemplateListIntent
}
