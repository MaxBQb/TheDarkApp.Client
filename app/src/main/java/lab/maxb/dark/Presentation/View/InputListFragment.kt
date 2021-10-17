package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.FragmentKeys
import lab.maxb.dark.Presentation.Extra.setFragmentResponse
import lab.maxb.dark.Presentation.View.Adapters.InputListAdapter
import lab.maxb.dark.Presentation.ViewModel.InputListViewModel
import lab.maxb.dark.databinding.InputListFragmentBinding

class InputListFragment : Fragment() {
    private val mViewModel: InputListViewModel by viewModels()
    private var mBinding: InputListFragmentBinding by autoCleaned()
    private var mAdapter: InputListAdapter by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = InputListFragmentBinding.inflate(layoutInflater, container, false)
        mBinding.inputListRecycler.layoutManager = LinearLayoutManager(context)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getStringArray(TEXTS)?.toMutableList()?.let { mViewModel.texts = it }
        mAdapter = InputListAdapter(mViewModel.texts)
        mBinding.inputListRecycler.adapter = mAdapter
        mAdapter.onItemTextChangedListener = { editText, text, position -> text?.let {
            mViewModel.texts[position] = it
            mViewModel.getSuggestions(mViewModel.texts).observe(viewLifecycleOwner) { values: Set<String> ->
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
        private val keys = FragmentKeys(this::class)
        val TEXTS = keys.param("TEXTS")
        val REQUEST_TEXTS = keys.request(TEXTS)
        val RESPONSE_TEXTS = keys.response(TEXTS)

        init { keys.clear() }

        fun newInstance(texts: List<String>? = null)
            = InputListFragment().apply { arguments = bundleOf(
                TEXTS to texts?.toTypedArray(),
            )}
    }
}