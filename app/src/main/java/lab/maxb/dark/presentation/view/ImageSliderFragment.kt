package lab.maxb.dark.presentation.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import lab.maxb.dark.domain.operations.unicname
import lab.maxb.dark.presentation.extra.*
import lab.maxb.dark.presentation.extra.delegates.autoCleaned
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.view.adapter.ImageSliderAdapter
import lab.maxb.dark.presentation.viewModel.ImageSliderViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.ImageSliderFragmentBinding

class ImageSliderFragment : Fragment(R.layout.image_slider_fragment) {
    private val mViewModel: ImageSliderViewModel by viewModels()
    private val mBinding: ImageSliderFragmentBinding by viewBinding()
    private var mUris: List<Uri> by autoCleaned()
    private var mAdapter: ImageSliderAdapter by autoCleaned()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getStringArray(URIS)?.toMutableList()?.let {
                uris -> mViewModel.setImages(uris)
            }
            mViewModel.maxAmount = it.getInt(MAX_AMOUNT, -1)
            mViewModel.isEditable = it.getBoolean(IS_EDITABLE, false)
        }
        mViewModel.images observe { uris ->
            mAdapter = ImageSliderAdapter( uris.mapNotNull { it.toBitmap(
                requireContext(),
                mBinding.imageSlider.layoutParams.width,
                mBinding.imageSlider.layoutParams.height,
            )})
            if (mViewModel.isEditable) {
                mBinding.addImageButtonAlternative.toggleVisibility(uris.isEmpty())
                mBinding.imageSlider.toggleVisibility(uris.isNotEmpty())
                mBinding.editImageButton.toggleVisibility(uris.isNotEmpty())
                mBinding.deleteImageButton.toggleVisibility(uris.isNotEmpty())
                mBinding.addImageButton.toggleVisibility(uris.isNotEmpty()
                        && mViewModel.maxAmount !in 1..uris.size)
            }
            mBinding.imageSlider.adapter = mAdapter
            mUris = uris
        }

        if (mViewModel.isEditable) {
            mBinding.addImageButton.setOnClickListener{addImages()}
            mBinding.addImageButtonAlternative.setOnClickListener{addImages()}
            mBinding.deleteImageButton.setOnClickListener {
                mViewModel.deleteImage(mBinding.imageSlider.currentItem)
            }
            mBinding.editImageButton.setOnClickListener {
                updateContent.launch(arrayOf("image/*"))
            }
        }

        setFragmentResponse(REQUEST_URIS, RESPONSE_URIS) {bundleOf(
            URIS to mUris.map { it.toString() }
        )}
    }

    private val getContent by lazy {
        requireActivity().activityResultRegistry.register(ADD_URIS, ActivityResultContracts.OpenMultipleDocuments()) {
                uris: List<Uri?>? ->
            uris?.filterNotNull()?.let {
                mViewModel.addImages(it)
            }
        }
    }

    private val updateContent by lazy {
        requireActivity().activityResultRegistry.register(UPDATE_URI, ActivityResultContracts.OpenDocument()) {
            it?.let {
                mViewModel.updateImage(mBinding.imageSlider.currentItem, it)
            }
        }
    }

    private fun addImages() = getContent.launch(arrayOf("image/*"))

    override fun onDestroy() {
        if (mViewModel.isEditable) {
            getContent.unregister()
            updateContent.unregister()
        }
        super.onDestroy()
    }

    companion object {
        val URIS = unicname
        val IS_EDITABLE = unicname
        val MAX_AMOUNT = unicname
        val REQUEST_URIS = unicname
        val RESPONSE_URIS = unicname
        val ADD_URIS = unicname
        val UPDATE_URI = unicname

        fun newInstance(uris: List<String>? = null,
                        isEditable: Boolean = false,
                        maxAmount: Int? = null)
            = ImageSliderFragment().withArgs(
                URIS to uris?.toTypedArray(),
                IS_EDITABLE to isEditable,
                MAX_AMOUNT to maxAmount,
            )
    }
}