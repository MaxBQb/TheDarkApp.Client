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
import androidx.navigation.fragment.navArgs
import com.wada811.databinding.dataBinding
import lab.maxb.dark.R
import lab.maxb.dark.databinding.SolveRecognitionTaskFragmentBinding
import lab.maxb.dark.domain.model.RecognitionTask
import lab.maxb.dark.presentation.extra.delegates.autoCleaned
import lab.maxb.dark.presentation.extra.goBack
import lab.maxb.dark.presentation.extra.launchRepeatingOnLifecycle
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.extra.toBitmap
import lab.maxb.dark.presentation.view.adapter.ImageSliderAdapter
import lab.maxb.dark.presentation.viewModel.SolveRecognitionTaskViewModel
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SolveRecognitionTaskFragment : Fragment(R.layout.solve_recognition_task_fragment) {
    private val mViewModel: SolveRecognitionTaskViewModel by sharedViewModel()
    private val mBinding: SolveRecognitionTaskFragmentBinding by dataBinding()
    private var mAdapter: ImageSliderAdapter by autoCleaned()
    private val args: SolveRecognitionTaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(mBinding) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.init(args.id)
        mAdapter = ImageSliderAdapter()
        imageSlider.adapter = mAdapter
        checkAnswer.setOnClickListener {
            launchRepeatingOnLifecycle {
                if (mViewModel.solveRecognitionTask())
                    goBack()
                else
                    Toast.makeText(context, "Неверно", Toast.LENGTH_SHORT).show()
            }
        }
        mViewModel.recognitionTask observe {
            it.ifLoaded { task ->
                task ?: run {
                    goBack()
                    return@ifLoaded
                }
                (task.images ?: listOf()).mapNotNull { image ->
                    image.path.toUri().toBitmap(
                        requireContext(),
                        imageSlider.layoutParams.width,
                        imageSlider.layoutParams.height,
                    )?.let { content ->
                        ItemHolder(image.path to content)
                    }
                }.run { mAdapter.submitList(this) }

                mViewModel.isReviewMode observe {
                    mBinding.markReviewedButton.isVisible = !(it && task.reviewed)
                }
            }
        }
        mViewModel.isReviewMode observe {
            mBinding.moderatorTools.isVisible = it
            mBinding.answerLayout.isVisible = !it

            mBinding.markReviewedButton.setOnClickListener { _ ->
                if (!it) return@setOnClickListener
                mark(true)
            }

            mBinding.deleteButton.setOnClickListener { _ ->
                if (!it) return@setOnClickListener
                mark(false)
            }
        }
    }

    private fun mark(isAllowed: Boolean) = launchRepeatingOnLifecycle {
        mViewModel.mark(isAllowed)
        goBack()
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
            R.id.menu_share -> {
                shareTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun shareTask() = launchRepeatingOnLifecycle {
        startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
            "https://dark/task/" + (
                     mViewModel.getCurrentTask()?.id
                     ?: return@launchRepeatingOnLifecycle
                 )
            )
            type = "text/plain"
        }, null))
    }
}