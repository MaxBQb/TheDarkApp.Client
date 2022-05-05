package lab.maxb.dark.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.viewModel.SolveRecognitionTaskViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.SolveRecognitionTaskFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SolveRecognitionTaskFragment : Fragment(R.layout.solve_recognition_task_fragment) {
    private val mViewModel: SolveRecognitionTaskViewModel by sharedViewModel()
    private val mBinding: SolveRecognitionTaskFragmentBinding by viewBinding()
    private val args: SolveRecognitionTaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.id = args.id
        mBinding.checkAnswer.setOnClickListener { v ->
            if (mViewModel.solveRecognitionTask(
                    mBinding.answer.text.toString()
                ))
                activity?.onBackPressed()
            else
                Toast.makeText(context, "Неверно", Toast.LENGTH_SHORT).show()
        }

        mViewModel.recognitionTask observe {
            it?.let { task: RecognitionTask ->
                parentFragmentManager.commit {
                    replace(
                        R.id.image_slider, ImageSliderFragment.newInstance(
                            task.images?.toList() ?: listOf()
                        )
                    )
                }
                mViewModel.isReviewMode observe {
                    mBinding.markReviewedButton.isVisible = !(it && task.reviewed)
                }
            } ?: activity?.onBackPressed()
        }
        mViewModel.isReviewMode observe {
            mBinding.moderatorTools.isVisible = it
            mBinding.answerLayout.isVisible = !it

            mBinding.markReviewedButton.setOnClickListener { _ ->
                if (!it) return@setOnClickListener
                mViewModel.mark(true).invokeOnCompletion {
                    activity?.onBackPressed()
                }
            }

            mBinding.deleteButton.setOnClickListener { _ ->
                if (!it) return@setOnClickListener
                mViewModel.mark(false).invokeOnCompletion {
                    activity?.onBackPressed()
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