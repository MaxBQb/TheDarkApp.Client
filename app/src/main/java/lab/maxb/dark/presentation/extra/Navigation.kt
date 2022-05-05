package lab.maxb.dark.presentation.extra

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

context(Fragment)
fun NavDirections.navigate()
    = findNavController().navigate(this)

fun Fragment.goBack()
    = findNavController().popBackStack()