package lab.maxb.dark.presentation.view

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.wada811.databinding.dataBinding
import lab.maxb.dark.R
import lab.maxb.dark.databinding.SolveRecognitionTaskFragmentBinding
import lab.maxb.dark.presentation.extra.GlideApp
import lab.maxb.dark.presentation.extra.delegates.autoCleaned
import lab.maxb.dark.presentation.extra.goBack
import lab.maxb.dark.presentation.extra.launchRepeatingOnLifecycle
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.view.adapter.ImageSliderAdapter
import lab.maxb.dark.presentation.viewModel.SolveRecognitionTaskViewModel
import lab.maxb.dark.presentation.viewModel.utils.ItemHolder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class SolveRecognitionTaskFragment : Fragment(R.layout.solve_recognition_task_fragment) {
    private val mViewModel: SolveRecognitionTaskViewModel by sharedViewModel()
    private val mBinding: SolveRecognitionTaskFragmentBinding by dataBinding()
    private var mAdapter: ImageSliderAdapter by autoCleaned()
    private var mGlide: RequestManager by autoCleaned()
    private var mPlaceholder: AnimatedVectorDrawable by autoCleaned()
    private val args: SolveRecognitionTaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(mBinding) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.init(args.id)
        data = mViewModel
        mGlide = GlideApp.with(this@SolveRecognitionTaskFragment)
        mPlaceholder = getDrawable(requireContext(), R.drawable.loading_vector) as AnimatedVectorDrawable
        mPlaceholder.start()
        mAdapter = ImageSliderAdapter {
            mGlide.load(mViewModel.getImage(it))
                .placeholder(mPlaceholder)
                .error(R.drawable.ic_error)
        }
        imageSlider.adapter = mAdapter
        checkAnswer.setOnClickListener {
            launchRepeatingOnLifecycle {
                if (mViewModel.solveRecognitionTask())
                    goBack()
                else
                    Toast.makeText(requireContext(), "Неверно", Toast.LENGTH_SHORT).show()
            }
        }
        mViewModel.recognitionTask observe {
            it.ifLoaded { task ->
                task ?: run {
                    goBack()
                    return@ifLoaded
                }

                (task.images ?: listOf()).map { image ->
                    ItemHolder(image, image)
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
        if (mViewModel.mark(isAllowed))
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