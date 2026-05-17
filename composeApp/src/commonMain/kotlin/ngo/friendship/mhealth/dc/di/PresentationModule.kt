package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.screen.login.LoginViewModel
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screen.case.case_list.CaseListViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DashboardViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.PrescriptionTemplateListViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DxListViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.MedicineListViewModel
import ngo.friendship.mhealth.dc.presentation.screen.home.HomeViewModel
import ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary.BeneficiaryProfileViewModel
import ngo.friendship.mhealth.dc.presentation.screens.profile.fcm.FcmProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(constructor = ::LoginViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::CaseViewModel)
    viewModelOf(::CaseListViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::PrescriptionTemplateListViewModel)
    viewModelOf(::DxListViewModel)
    viewModelOf(::MedicineListViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::FcmProfileViewModel)
    viewModelOf(::BeneficiaryProfileViewModel)
}
