package lab.maxb.dark.Presentation.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.toBitmap
import lab.maxb.dark.Presentation.ViewModel.SolveRecognitionTaskViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.SolveRecognitionTaskFragmentBinding

class SolveRecognitionTaskView : Fragment() {
    private val mViewModel: SolveRecognitionTaskViewModel by viewModels()
    private var mBinding: SolveRecognitionTaskFragmentBinding by autoCleaned()
    private val args: SolveRecognitionTaskViewArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SolveRecognitionTaskFragmentBinding.inflate(layoutInflater, container, false)
        mViewModel.id = args.id
        mBinding.checkAnswer.setOnClickListener { v ->
            if (mViewModel.solveRecognitionTask(
                    mBinding.answer.text.toString()
                ))
                activity?.onBackPressed()
            else
                Toast.makeText(context, "Неверно", Toast.LENGTH_SHORT).show()
        }
        mViewModel.recognitionTask.observe(viewLifecycleOwner, {
            it?.let { task: RecognitionTask ->
                mBinding.taskImage.setImageBitmap(Uri.parse(task.image).toBitmap(
                    requireContext(),
                    mBinding.taskImage.layoutParams.width,
                    mBinding.taskImage.layoutParams.height
                ))
            } ?: activity?.onBackPressed()
        })
        return mBinding.root
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