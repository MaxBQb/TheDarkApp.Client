package lab.maxb.dark.Presentation.View

import lab.maxb.dark.Presentation.ViewModel.RecognitionTaskListViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.findNavController
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.View.Adapters.RecognitionTaskListAdapter
import lab.maxb.dark.databinding.RecognitionTaskListFragmentBinding


class RecognitionTaskListView : Fragment() {
    private val mViewModel: RecognitionTaskListViewModel by viewModels()
    private var mBinding: RecognitionTaskListFragmentBinding by autoCleaned()
    private var mAdapter: RecognitionTaskListAdapter by autoCleaned()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = RecognitionTaskListFragmentBinding.inflate(layoutInflater, container, false)
        mBinding.recognitionTaskListRecycler.layoutManager = LinearLayoutManager(context)
        mBinding.fab.setOnClickListener { v ->
            v.findNavController().navigate(
                RecognitionTaskListViewDirections.addRecognitionTask()
            )
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.recognitionTaskList.observe(viewLifecycleOwner, {
                recognitionTasks: List<RecognitionTask>? ->
            mAdapter = RecognitionTaskListAdapter(recognitionTasks)
            mBinding.recognitionTaskListRecycler.adapter = mAdapter
            mAdapter.onElementClickListener = { v: View, task: RecognitionTask ->
                v.findNavController().navigate(
                    RecognitionTaskListViewDirections.solveRecognitionTask(
                        task.id
                    )
                )
            }
        })
    }
}