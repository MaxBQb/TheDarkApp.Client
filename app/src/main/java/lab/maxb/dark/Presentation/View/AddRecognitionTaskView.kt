package lab.maxb.dark.Presentation.View

import android.net.Uri
import lab.maxb.dark.Presentation.ViewModel.AddRecognitionTaskViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import lab.maxb.dark.databinding.AddRecognitionTaskFragmentBinding

class AddRecognitionTaskView : Fragment() {
    private var mViewModel: AddRecognitionTaskViewModel? = null
    private var mBinding: AddRecognitionTaskFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddRecognitionTaskFragmentBinding.inflate(layoutInflater, container, false)

        mBinding!!.loadTaskImage.setOnClickListener { v ->
            getContent.launch("image/*")
        }

        mBinding!!.fab.setOnClickListener { v ->
            if (mViewModel!!.addRecognitionTask(listOf(
                    mBinding!!.taskName1.text.toString(),
                    mBinding!!.taskName2.text.toString(),
                    mBinding!!.taskName3.text.toString(),
                )))
                Navigation.findNavController(v).popBackStack()
            else
                Toast.makeText(context, "Вы ввели не все данные", Toast.LENGTH_SHORT).show()
        }
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this).get(
            AddRecognitionTaskViewModel::class.java
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        mViewModel = null
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        mViewModel?.imageUri = uri
        mBinding?.taskImage?.setImageURI(uri)
    }
}