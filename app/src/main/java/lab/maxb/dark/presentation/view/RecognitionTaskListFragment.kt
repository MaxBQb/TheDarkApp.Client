package lab.maxb.dark.presentation.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import lab.maxb.dark.R
import lab.maxb.dark.databinding.RecognitionTaskListFragmentBinding
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.delegates.autoCleaned
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.extra.navigate
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.view.adapter.RecognitionTaskListAdapter
import lab.maxb.dark.presentation.viewModel.RecognitionTaskListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RecognitionTaskListFragment : Fragment(R.layout.recognition_task_list_fragment) {
    private val mViewModel: RecognitionTaskListViewModel by sharedViewModel()
    private val mBinding: RecognitionTaskListFragmentBinding by viewBinding()
    private var mAdapter: RecognitionTaskListAdapter by autoCleaned()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.isTaskCreationAllowed observe {
            mBinding.fab.isVisible = it
        }
        mBinding.fab.setOnClickListener {
            RecognitionTaskListFragmentDirections.navToAddTaskFragment().navigate()
        }
        mAdapter = RecognitionTaskListAdapter(Glide.with(this)) {
            load(mViewModel.getImage(it)).transition(withCrossFade())
        }
        mBinding.recognitionTaskListRecycler.adapter = mAdapter
        mBinding.recognitionTaskListRecycler.addOnScrollListener(mAdapter.preloader.raw)
        mAdapter.onElementClickListener = { _: View, task: RecognitionTask ->
            RecognitionTaskListFragmentDirections.navToSolveTaskFragment(
                task.id
            ).navigate()
        }

        mViewModel.recognitionTaskList observe {
            mAdapter.submitData(it)
        }
    }
}