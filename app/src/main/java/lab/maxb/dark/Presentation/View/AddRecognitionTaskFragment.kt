package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.MainActivity
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.requestFragmentResult
import lab.maxb.dark.Presentation.ViewModel.AddRecognitionTaskViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AddRecognitionTaskFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddRecognitionTaskFragment : Fragment() {
    private val mViewModel: AddRecognitionTaskViewModel by viewModel()
    private var mBinding: AddRecognitionTaskFragmentBinding by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddRecognitionTaskFragmentBinding.inflate(layoutInflater, container, false)
        parentFragmentManager.commit {
            replace(
                R.id.image_slider, ImageSliderFragment.newInstance(
                    mViewModel.imageUris, true, RecognitionTask.MAX_IMAGES_COUNT
                )
            )
            replace(R.id.task_names, InputListFragment.newInstance())
        }

        setFragmentResultListener(ImageSliderFragment.RESPONSE_URIS) {
            _: String, result: Bundle ->
            result.getStringArrayList(ImageSliderFragment.URIS)?.let {
                mViewModel.imageUris = it
            }
            createRecognitionTask()
        }

        setFragmentResultListener(InputListFragment.RESPONSE_TEXTS) {
                _, result: Bundle ->
            result.getStringArray(InputListFragment.TEXTS)?.let {
                mViewModel.names = it.toList()
            }
            requestFragmentResult(ImageSliderFragment.REQUEST_URIS)
        }

        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as? MainActivity)?.binding?.toolbar?.setNavigationIcon(R.drawable.ic_close)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.submit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
        = when (item.itemId) {
            R.id.submit -> {
                startCreateRecognitionTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun startCreateRecognitionTask() {
        requestFragmentResult(InputListFragment.REQUEST_TEXTS)
    }

    private fun createRecognitionTask() {
        mViewModel.addRecognitionTask().observe(viewLifecycleOwner) {
            if (it) activity?.onBackPressed()
            else Toast.makeText(context,
                getString(R.string.not_enough_data_provided_message),
                Toast.LENGTH_SHORT).show()
        }
    }
}