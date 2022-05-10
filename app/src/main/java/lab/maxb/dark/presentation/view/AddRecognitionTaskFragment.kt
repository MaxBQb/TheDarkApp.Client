package lab.maxb.dark.presentation.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AddRecognitionTaskFragmentBinding
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.presentation.extra.delegates.autoCleaned
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.view.adapter.InputListAdapter
import lab.maxb.dark.presentation.viewModel.AddRecognitionTaskViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddRecognitionTaskFragment : Fragment(R.layout.add_recognition_task_fragment) {
    private val mViewModel: AddRecognitionTaskViewModel by sharedViewModel()
    private val mBinding: AddRecognitionTaskFragmentBinding by viewBinding()
    private var mAdapter: InputListAdapter by autoCleaned()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInputsList()
        parentFragmentManager.commit {
            replace(
                R.id.image_slider, ImageSliderFragment.newInstance(
                    mViewModel.imageUris,
                    true,
                    RecognitionTask.MAX_IMAGES_COUNT
                )
            )
        }

        setFragmentResultListener(ImageSliderFragment.RESPONSE_URIS) {
            _: String, result: Bundle ->
            result.getStringArrayList(ImageSliderFragment.URIS)?.let {
                mViewModel.imageUris = it
            }
            createRecognitionTask()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as? MainActivity)?.withToolbar {
            setNavigationIcon(R.drawable.ic_close)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.submit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submit -> startCreateRecognitionTask()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun startCreateRecognitionTask() {
        requestFragmentResult(ImageSliderFragment.REQUEST_URIS)
    }

    private fun createRecognitionTask() = launch {
        if (mViewModel.addRecognitionTask())
            goBack()
        else Toast.makeText(
            context,
            getString(R.string.not_enough_data_provided_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupInputsList() {
        mAdapter = InputListAdapter()
        mBinding.inputListRecycler.adapter = mAdapter
        mAdapter.onItemTextChangedListener = { editText, position, text ->
            mViewModel.setText(position, text ?: "")

            text?.let {
                mViewModel.suggestions observe { values: Set<String> ->
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        values.toList()
                    ).apply { filter.filter(null) }
                     .also { input ->
                        (editText as AutoCompleteTextView).setAdapter(input)
                    }
                }
            }
        }
        mAdapter.onItemFocusedListener = { _, _, focused: Boolean ->
            if (!focused)
                mViewModel.shrinkInputs()
        }
        mViewModel.names observe {
            mAdapter.submitList(it)
        }
    }
}