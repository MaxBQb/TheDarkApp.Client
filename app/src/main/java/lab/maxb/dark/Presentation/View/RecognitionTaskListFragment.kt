package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.Delegates.viewBinding
import lab.maxb.dark.Presentation.Extra.observe
import lab.maxb.dark.Presentation.View.Adapters.RecognitionTaskListAdapter
import lab.maxb.dark.Presentation.ViewModel.RecognitionTaskListViewModel
import lab.maxb.dark.Presentation.ViewModel.UserViewModel
import lab.maxb.dark.Presentation.ViewModel.ifHasProfile
import lab.maxb.dark.R
import lab.maxb.dark.databinding.RecognitionTaskListFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class RecognitionTaskListFragment : Fragment(R.layout.recognition_task_list_fragment) {
    private val mViewModel: RecognitionTaskListViewModel by sharedViewModel()
    private val mBinding: RecognitionTaskListFragmentBinding by viewBinding()
    private var mAdapter: RecognitionTaskListAdapter by autoCleaned()
    private val mUserViewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(mUserViewModel.profile) {
            it.ifHasProfile { profile ->
                mBinding.fab.isVisible = mViewModel.isTaskCreationAllowed(profile)
            }
        }
        mBinding.recognitionTaskListRecycler.layoutManager = LinearLayoutManager(context)
        mBinding.fab.setOnClickListener { v ->
            v.findNavController().navigate(
                RecognitionTaskListFragmentDirections.addRecognitionTask()
            )
        }
        observe(mUserViewModel.profile) {
            it.ifHasProfile { profile ->
                observe(mViewModel.getRecognitionTaskList(profile)) { recognitionTasks: List<RecognitionTask>? ->
                    mAdapter = RecognitionTaskListAdapter(recognitionTasks)
                    mBinding.recognitionTaskListRecycler.adapter = mAdapter
                    mAdapter.onElementClickListener = { v: View, task: RecognitionTask ->
                        v.findNavController().navigate(
                            RecognitionTaskListFragmentDirections.solveRecognitionTask(
                                task.id
                            )
                        )
                    }
                }
            }
        }
    }
}