package lab.maxb.dark.Presentation.View

import android.net.Uri
import lab.maxb.dark.Presentation.ViewModel.SolveRecognitionTaskViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import lab.maxb.dark.Domain.Model.RecognitionTask
import lab.maxb.dark.Presentation.Extra.toBitmap
import lab.maxb.dark.Presentation.Repository.Repository
import lab.maxb.dark.databinding.SolveRecognitionTaskFragmentBinding

class SolveRecognitionTaskView : Fragment() {
    private val mViewModel: SolveRecognitionTaskViewModel by viewModels()
    private var mBinding: SolveRecognitionTaskFragmentBinding? = null
    private val args: SolveRecognitionTaskViewArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SolveRecognitionTaskFragmentBinding.inflate(layoutInflater, container, false)
        mViewModel.id = args.id
        mBinding!!.checkAnswer.setOnClickListener { v ->
            if (mViewModel.solveRecognitionTask(
                    mBinding!!.answer.text.toString()
                ))
                v.findNavController().popBackStack()
            else
                Toast.makeText(context, "Неверно", Toast.LENGTH_SHORT).show()
        }
        mViewModel.recognitionTask.observe(viewLifecycleOwner, { task: RecognitionTask? ->
            mBinding!!.taskImage.setImageBitmap(Uri.parse(task?.image).toBitmap(
                requireContext(),
                mBinding!!.taskImage.measuredWidth,
                mBinding!!.taskImage.measuredHeight
            ))
        })
        return mBinding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}