package lab.maxb.dark.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import lab.maxb.dark.R
import lab.maxb.dark.databinding.SolveRecognitionTaskFragmentBinding
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.delegates.autoCleaned
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.extra.goBack
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.extra.toBitmap
import lab.maxb.dark.presentation.view.adapter.ImageSliderAdapter
import lab.maxb.dark.presentation.viewModel.SolveRecognitionTaskViewModel
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder
import lab.maxb.dark.presentation.viewModel.utils.map
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SolveRecognitionTaskFragment : Fragment(R.layout.solve_recognition_task_fragment) {
    private val mViewModel: SolveRecognitionTaskViewModel by sharedViewModel()
    private val mBinding: SolveRecognitionTaskFragmentBinding by viewBinding()
    private var mAdapter: ImageSliderAdapter by autoCleaned()
    private val args: SolveRecognitionTaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(mBinding) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.id = args.id
        mAdapter = ImageSliderAdapter()
        imageSlider.adapter = mAdapter
        mBinding.checkAnswer.setOnClickListener {
            if (mViewModel.solveRecognitionTask(
                    mBinding.answer.text.toString()
                ))
                goBack()
            else
                Toast.makeText(context, "Неверно", Toast.LENGTH_SHORT).show()
        }
        mViewModel.recognitionTask observe {
            it?.let { task: RecognitionTask ->
                (task.images ?: listOf()).mapNotNull { path ->
                    path.toUri().toBitmap(
                        requireContext(),
                        imageSlider.layoutParams.width,
                        imageSlider.layoutParams.height,
                    )?.let { image ->
                        ItemHolder(path to image)
                    }
                }.run { mAdapter.submitList(this) }

                mViewModel.isReviewMode observe {
                    mBinding.markReviewedButton.isVisible = !(it && task.reviewed)
                }
            } ?: goBack()
        }
        mViewModel.isReviewMode observe {
            mBinding.moderatorTools.isVisible = it
            mBinding.answerLayout.isVisible = !it

            mBinding.markReviewedButton.setOnClickListener { _ ->
                if (!it) return@setOnClickListener
                mViewModel.mark(true).invokeOnCompletion {
                    goBack()
                }
            }

            mBinding.deleteButton.setOnClickListener { _ ->
                if (!it) return@setOnClickListener
                mViewModel.mark(false).invokeOnCompletion {
                    goBack()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
        = when (item.itemId) {
            R.id.share -> {
                shareTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun shareTask() {
        startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
            "https://dark/task/" + (
                     mViewModel.recognitionTask.value?.id
                     ?: return
                 )
            )
            type = "text/plain"
        }, null))
    }
}