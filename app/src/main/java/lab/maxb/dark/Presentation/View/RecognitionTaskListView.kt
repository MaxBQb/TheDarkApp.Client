package lab.maxb.dark.Presentation.View

import lab.maxb.dark.Presentation.ViewModel.RecognitionTaskListViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import lab.maxb.dark.R
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.View.Adapters.RecognitionTaskListAdapter
import lab.maxb.dark.databinding.RecognitionTaskListFragmentBinding


class RecognitionTaskListView : Fragment() {
    private var mViewModel: RecognitionTaskListViewModel? = null
    private var mBinding: RecognitionTaskListFragmentBinding? = null
    private var mAdapter: RecognitionTaskListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = RecognitionTaskListFragmentBinding.inflate(layoutInflater, container, false)
        mBinding!!.recognitionTaskListRecycler.layoutManager = LinearLayoutManager(context)
        mBinding!!.fab.setOnClickListener { v ->
            v.findNavController().navigate(
                RecognitionTaskListViewDirections.addRecognitionTask()
            )
        }
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(
            RecognitionTaskListViewModel::class.java
        )

        mViewModel!!.recognitionTaskList.observe(viewLifecycleOwner, {
            recognitionTasks: List<RecognitionTask>? ->
                mAdapter = RecognitionTaskListAdapter(recognitionTasks)
                mBinding!!.recognitionTaskListRecycler.adapter = mAdapter
                mAdapter!!.onElementClickListener = { _: View, task: RecognitionTask ->
                    mViewModel!!.removeRecognitionTask(task)
                }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        mViewModel = null
        mAdapter = null
    }
}