package lab.maxb.dark.presentation.View

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import lab.maxb.dark.Domain.Operations.unicname
import lab.maxb.dark.presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.presentation.Extra.Delegates.viewBinding
import lab.maxb.dark.presentation.Extra.observe
import lab.maxb.dark.presentation.Extra.setFragmentResponse
import lab.maxb.dark.presentation.Extra.withArgs
import lab.maxb.dark.presentation.View.Adapters.InputListAdapter
import lab.maxb.dark.presentation.view_model.InputListViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.InputListFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class InputListFragment : Fragment(R.layout.input_list_fragment) {
    private val mViewModel: InputListViewModel by viewModel()
    private val mBinding: InputListFragmentBinding by viewBinding()
    private var mAdapter: InputListAdapter by autoCleaned()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getStringArray(TEXTS)?.toMutableList()?.let { mViewModel.texts = it }
        mAdapter = InputListAdapter(mViewModel.texts)
        mBinding.inputListRecycler.layoutManager = LinearLayoutManager(context)
        mBinding.inputListRecycler.adapter = mAdapter
        mAdapter.onItemTextChangedListener = { editText, text, position -> text?.let {
            mViewModel.texts[position] = it
            mViewModel.getSuggestions(mViewModel.texts) observe { values: Set<String> ->
                val input = editText as AutoCompleteTextView
                input.setAdapter(ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    values.toList()
                ).apply { filter.filter(null) })
            }
        }}
        mAdapter.onItemFocusedListener = { _, position, hasFocus ->
            if (hasFocus)
                if (position + 1 == mViewModel.texts.size
                    && (position == 0 || mViewModel.texts[position-1].isNotBlank())) {
                    mViewModel.texts.add("")
                    mAdapter.notifyItemInserted(position+1)
                }
        }

        setFragmentResponse(REQUEST_TEXTS, RESPONSE_TEXTS) {bundleOf(
            TEXTS to mViewModel.texts
                    .filter { it.isNotBlank() }
                    .toTypedArray()
        )}
    }

    companion object {
        val TEXTS = unicname
        val REQUEST_TEXTS = unicname
        val RESPONSE_TEXTS = unicname

        fun newInstance(texts: List<String>? = null)
            = InputListFragment().withArgs(
                TEXTS to texts?.toTypedArray(),
            )
    }
}