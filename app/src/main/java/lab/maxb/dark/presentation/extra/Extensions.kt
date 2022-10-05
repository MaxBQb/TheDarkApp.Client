package lab.maxb.dark.presentation.extra

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment


fun UiText.show(context: Context) = Toast.makeText(
    context, this.asString(context), Toast.LENGTH_LONG
).show()

context (Fragment)
fun UiText.show() = this.show(requireContext())