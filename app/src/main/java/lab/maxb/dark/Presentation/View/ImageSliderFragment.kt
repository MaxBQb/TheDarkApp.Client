package lab.maxb.dark.Presentation.View

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.FragmentKeys
import lab.maxb.dark.Presentation.Extra.setFragmentResponse
import lab.maxb.dark.Presentation.Extra.toBitmap
import lab.maxb.dark.Presentation.Extra.toggleVisibility
import lab.maxb.dark.Presentation.View.Adapters.ImageSliderAdapter
import lab.maxb.dark.Presentation.ViewModel.ImageSliderViewModel
import lab.maxb.dark.databinding.ImageSliderFragmentBinding

class ImageSliderFragment : Fragment() {
    private val mViewModel: ImageSliderViewModel by viewModels()
    private var mBinding: ImageSliderFragmentBinding by autoCleaned()
    private var mUris: List<Uri> by autoCleaned()
    private var mAdapter: ImageSliderAdapter by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = ImageSliderFragmentBinding.inflate(layoutInflater, container, false)
        arguments?.let {
            it.getStringArray(URIS)?.toMutableList()?.let {
                uris -> mViewModel.setImages(uris)
            }
            mViewModel.maxAmount = it.getInt(MAX_AMOUNT, -1)
            mViewModel.isEditable = it.getBoolean(IS_EDITABLE, false)
        }
        mViewModel.images.observe(viewLifecycleOwner) { uris ->
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

        return mBinding.root
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
        private val keys = FragmentKeys(this::class)
        private val REGISTER_FOR_RESULT = "${keys.path}.registerForResult"
        val URIS = keys.param("URIS")
        val IS_EDITABLE = keys.param("IS_EDITABLE")
        val MAX_AMOUNT = keys.param("MAX_AMOUNT")
        val REQUEST_URIS = keys.request(URIS)
        val RESPONSE_URIS = keys.response(URIS)
        val ADD_URIS = "$REGISTER_FOR_RESULT.ADD_URIS"
        val UPDATE_URI = "$REGISTER_FOR_RESULT.UPDATE_URI"

        init { keys.clear() }

        fun newInstance(uris: List<String>? = null, isEditable: Boolean = false, maxAmount: Int? = null)
            = ImageSliderFragment().apply { arguments = bundleOf(
                URIS to uris?.toTypedArray(),
                IS_EDITABLE to isEditable,
                MAX_AMOUNT to maxAmount
            )}
    }
}