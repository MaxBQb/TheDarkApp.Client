package lab.maxb.dark.DI

import lab.maxb.dark.Presentation.ViewModel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val MODULE_viewModels = module {
    viewModel { InputListViewModel(get()) }
    viewModel { AddRecognitionTaskViewModel(get(), get(), get(), get()) }
    viewModel { RecognitionTaskListViewModel(get(), get(), get(), get()) }
    viewModel { SolveRecognitionTaskViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
}