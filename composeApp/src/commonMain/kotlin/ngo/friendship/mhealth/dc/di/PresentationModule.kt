package ngo.friendship.mhealth.dc.di

import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.screen.login.LoginViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.CaseListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DashboardViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DxListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.MedicineListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.MoreViewModel
import ngo.friendship.mhealth.dc.presentation.screens.home.HomeViewModel
import ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary.BeneficiaryProfileViewModel
import ngo.friendship.mhealth.dc.presentation.screens.profile.fcm.FcmProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::CaseViewModel)
    viewModelOf(::CaseListViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::DxListViewModel)
    viewModelOf(::MedicineListViewModel)
    viewModelOf(::MoreViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::FcmProfileViewModel)
    viewModelOf(::BeneficiaryProfileViewModel)
}
