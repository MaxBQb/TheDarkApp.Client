package lab.maxb.dark.DI

import lab.maxb.dark.Presentation.ViewModel.AddRecognitionTaskViewModel
import lab.maxb.dark.Presentation.ViewModel.InputListViewModel
import lab.maxb.dark.Presentation.ViewModel.RecognitionTaskListViewModel
import lab.maxb.dark.Presentation.ViewModel.SolveRecognitionTaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val MODULE_viewModels = module {
    viewModel { InputListViewModel(get()) }
    viewModel { AddRecognitionTaskViewModel(get()) }
    viewModel { RecognitionTaskListViewModel(get()) }
    viewModel { SolveRecognitionTaskViewModel(get()) }
}