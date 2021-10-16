package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.MainActivity
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.ViewModel.AddRecognitionTaskViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AddRecognitionTaskFragmentBinding

class AddRecognitionTaskFragment : Fragment() {
    private val mViewModel: AddRecognitionTaskViewModel by viewModels()
    private var mBinding: AddRecognitionTaskFragmentBinding by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddRecognitionTaskFragmentBinding.inflate(layoutInflater, container, false)
        parentFragmentManager.beginTransaction()
            .replace(R.id.image_slider, ImageSliderFragment.newInstance(
                mViewModel.imageUris, true, RecognitionTask.MAX_IMAGES_COUNT
            ))
            .replace(R.id.task_names, InputListFragment.newInstance())
            .commit()

        parentFragmentManager.setFragmentResultListener(ImageSliderFragment.RESULT_URIS,
            viewLifecycleOwner) {
            _: String, result: Bundle ->
            result.getStringArrayList(ImageSliderFragment.URIS)?.let {
                mViewModel.imageUris = it
            }
            createRecognitionTask()
        }

        parentFragmentManager.setFragmentResultListener(InputListFragment.RESULT_TEXTS,
            viewLifecycleOwner) {
                _: String, result: Bundle ->
            result.getStringArray(InputListFragment.TEXTS)?.let {
                mViewModel.names = it.toList()
            }
            parentFragmentManager.setFragmentResult(
                ImageSliderFragment.REQUEST,
                Bundle()
            )
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
        parentFragmentManager.setFragmentResult(
            InputListFragment.REQUEST,
            Bundle()
        )
    }

    private fun createRecognitionTask() {
        if (mViewModel.addRecognitionTask())
            activity?.onBackPressed()
        else
            Toast.makeText(context, "Вы ввели не все данные", Toast.LENGTH_SHORT).show()
    }
}