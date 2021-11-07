package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.observeOnce
import lab.maxb.dark.Presentation.View.Adapters.RecognitionTaskListAdapter
import lab.maxb.dark.Presentation.ViewModel.RecognitionTaskListViewModel
import lab.maxb.dark.Presentation.ViewModel.UserViewModel
import lab.maxb.dark.databinding.RecognitionTaskListFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RecognitionTaskListFragment : Fragment() {
    private val mViewModel: RecognitionTaskListViewModel by viewModel()
    private var mBinding: RecognitionTaskListFragmentBinding by autoCleaned()
    private var mAdapter: RecognitionTaskListAdapter by autoCleaned()
    private val mUserViewModel: UserViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = RecognitionTaskListFragmentBinding.inflate(layoutInflater, container, false)
        mUserViewModel.user.observeOnce(viewLifecycleOwner) {
            it?.let { profile ->
                if (!mViewModel.isTaskCreationAllowed(profile))
                    mBinding.fab.hide()
                return@observeOnce
            }
            requireActivity().onBackPressed()
        }
        mBinding.recognitionTaskListRecycler.layoutManager = LinearLayoutManager(context)
        mBinding.fab.setOnClickListener { v ->
            v.findNavController().navigate(
                RecognitionTaskListFragmentDirections.addRecognitionTask()
            )
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserViewModel.user.observe(viewLifecycleOwner) { profile -> profile?.let {
            mViewModel.getRecognitionTaskList(it).observe(viewLifecycleOwner) {
                    recognitionTasks: List<RecognitionTask>? ->
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
    }}
}