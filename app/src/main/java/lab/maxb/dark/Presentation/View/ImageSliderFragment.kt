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
import lab.maxb.dark.Presentation.Extra.toBitmap
import lab.maxb.dark.Presentation.Extra.toggleVisibility
import lab.maxb.dark.Presentation.View.Adapters.ImageSliderAdapter
import lab.maxb.dark.Presentation.ViewModel.ImageSliderViewModel
import lab.maxb.dark.databinding.ImageSliderFragmentBinding

class ImageSliderFragment : Fragment() {
    private val mViewModel: ImageSliderViewModel by viewModels()
    private var mBinding: ImageSliderFragmentBinding by autoCleaned()
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
        mViewModel.images.observe(viewLifecycleOwner, { uris ->
            mAdapter = ImageSliderAdapter( uris.mapNotNull { it.toBitmap(
                requireContext(),
                mBinding.imageSlider.layoutParams.width,
                mBinding.imageSlider.layoutParams.height,
            )})
            if (mViewModel.isEditable) {
                mBinding.addImageButton.toggleVisibility(uris.isEmpty())
                mBinding.imageSlider.toggleVisibility(uris.isNotEmpty())
            }
            mBinding.imageSlider.adapter = mAdapter
            parentFragmentManager.setFragmentResult(RESULT_URIS,
                bundleOf(URIS to uris.map { it.toString() }))
        })
        if (mViewModel.isEditable)
            mBinding.addImageButton.setOnClickListener{addImages()}
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

    private fun addImages() {
        getContent.launch(arrayOf("image/*"))
    }

    override fun onDestroy() {
        if (mViewModel.isEditable)
            getContent.unregister()
        super.onDestroy()
    }

    companion object {
        const val URIS = "ImageSliderFragment.params.URIS"
        const val RESULT_URIS = "ImageSliderFragment.result.URIS"
        const val ADD_URIS = "ImageSliderFragment.registerForResult.ADD_URIS"
        const val IS_EDITABLE = "ImageSliderFragment.params.IS_EDITABLE"
        const val MAX_AMOUNT = "ImageSliderFragment.params.MAX_AMOUNT"

        fun newInstance(uris: List<String>? = null, isEditable: Boolean = false, maxAmount: Int? = null)
            = ImageSliderFragment().apply { arguments = bundleOf(
                URIS to uris?.toTypedArray(),
                IS_EDITABLE to isEditable,
                MAX_AMOUNT to maxAmount
            )}
    }
}