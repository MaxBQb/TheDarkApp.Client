package lab.maxb.dark.presentation.view

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AddRecognitionTaskFragmentBinding
import lab.maxb.dark.domain.operations.unicname
import lab.maxb.dark.presentation.extra.delegates.autoCleaned
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.extra.goBack
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.view.adapter.ImageSliderAdapter
import lab.maxb.dark.presentation.view.adapter.InputListAdapter
import lab.maxb.dark.presentation.viewModel.AddRecognitionTaskViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddRecognitionTaskFragment : Fragment(R.layout.add_recognition_task_fragment) {
    private val mViewModel: AddRecognitionTaskViewModel by sharedViewModel()
    private val mBinding: AddRecognitionTaskFragmentBinding by viewBinding()
    private var mInputsAdapter: InputListAdapter by autoCleaned()
    private var mGlide: RequestManager by autoCleaned()
    private var mImagesAdapter: ImageSliderAdapter by autoCleaned()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mViewModel.clear()
            goBack()
        }
        setupInputsList()
        setupImageUploadPanel()
    }

    private fun setupImageUploadPanel() = with (mBinding) {
        mGlide = Glide.with(this@AddRecognitionTaskFragment)
        mImagesAdapter = ImageSliderAdapter {
            mGlide.load(it.toUri())
                .error(R.drawable.ic_error)
        }
        imageSlider.adapter = mImagesAdapter

        mViewModel.images observe { uris ->
            mImagesAdapter.submitList(uris)
            val hasUris = uris.isNotEmpty()
            addImageButtonAlternative.isVisible = !hasUris
            imageSlider.isVisible = hasUris
            editImageButton.isVisible = hasUris
            deleteImageButton.isVisible = hasUris
            addImageButton.isVisible = hasUris && mViewModel.allowImageAddition
        }

        addImageButton.setOnClickListener{
            getContent.launch(ALLOWED_CONTENT)
        }
        addImageButtonAlternative.setOnClickListener{
            getContent.launch(ALLOWED_CONTENT)
        }
        deleteImageButton.setOnClickListener {
            mViewModel.deleteImage(imageSlider.currentItem)
        }
        editImageButton.setOnClickListener {
            updateContent.launch(ALLOWED_CONTENT)
        }
    }

    private val getContent by lazy {
        requireActivity().activityResultRegistry.register(ADD_URIS,
            ActivityResultContracts.OpenMultipleDocuments()) {
                uris: List<Uri?>? ->
            uris?.filterNotNull()?.let {
                mViewModel.addImages(it)
            }
        }
    }

    private val updateContent by lazy {
        requireActivity().activityResultRegistry.register(UPDATE_URI,
            ActivityResultContracts.OpenDocument()) {
            it?.let {
                mViewModel.updateImage(mBinding.imageSlider.currentItem, it)
            }
        }
    }

    override fun onDestroy() {
        getContent.unregister()
        updateContent.unregister()
        super.onDestroy()
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
            R.id.menu_submit -> createRecognitionTask()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun createRecognitionTask() = launch {
        if (mViewModel.addRecognitionTask()) {
            mViewModel.clear()
            goBack()
        } else Toast.makeText(
            context,
            getString(R.string.addTask_message_notEnoughDataProvided),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupInputsList() {
        mInputsAdapter = InputListAdapter()
        mBinding.inputListRecycler.adapter = mInputsAdapter
        mInputsAdapter.onItemTextChangedListener = { editText, position, text ->
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
        mInputsAdapter.onItemFocusedListener = { _, _, focused: Boolean ->
            if (!focused)
                mViewModel.shrinkInputs()
        }
        mViewModel.names observe {
            mInputsAdapter.submitList(it)
        }
    }

    companion object {
        val ALLOWED_CONTENT = arrayOf("image/*")
        val ADD_URIS = unicname
        val UPDATE_URI = unicname
    }
}