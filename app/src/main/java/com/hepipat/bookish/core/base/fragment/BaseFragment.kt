package com.hepipat.bookish.core.base.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.hepipat.bookish.core.base.dialog.BaseDialog
import com.hepipat.bookish.core.base.dialog.BaseDialogInterface

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    private var baseDialog: BaseDialog? = null
    private var dismissDialog: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    protected fun goToFragment(
        parentFragment: Fragment,
        actionId: Int? = null,
        direction: NavDirections? = null,
    ) {
        actionId?.let { NavHostFragment.findNavController(parentFragment).navigate(it) }
        direction?.let { NavHostFragment.findNavController(parentFragment).navigate(it) }
    }

    protected fun showDialogLoading(dismiss: Boolean, message: String?) {
        dismissDialog = dismiss
        baseDialog = BaseDialog.BuildBaseDialog()
            .onBackPressedDismiss(dismiss)
            .setContent(message)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    protected fun showDialogAlert(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        drawableImage: Int?,
    ) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildAlertDialog()
            .onBackPressedDismiss(false)
            .setTitle(title)
            .setContent(message)
            .setSubmitButtonText("OK")
            .setImageContent(drawableImage)
            .setListener(object : BaseDialogInterface {
                override fun onSubmitClick() {
                    confirmCallback()
                }

                override fun onDismissClick() {

                }
            })
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()

    }

    protected fun showDialogConfirmation(
        title: String?,
        message: String?,
        confirmCallback: () -> Unit?,
        cancelCallback: () -> Unit?,
        drawableImage: Int?,
        singleButton: Boolean?,
    ) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildConfirmationDialog()
            .onBackPressedDismiss(dismissDialog)
            .setTitle(title)
            .setContent(message)
            .setImageContent(drawableImage)
            .setSubmitButtonText("OK")
            .setCancelButtonText("Cancel")
            .setSingleButton(singleButton!!)
            .setListener(object : BaseDialogInterface {
                override fun onSubmitClick() {
                    confirmCallback()
                }

                override fun onDismissClick() {
                    cancelCallback()
                }
            })
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    protected fun showDialogPopImage(drawableImage: Int?) {
        dismissDialog = false
        baseDialog = BaseDialog.BuildAlertDialog()
            .onBackPressedDismiss(dismissDialog)
            .hideAllButton(true)
            .showPanelButton(true)
            .setImageContent(drawableImage)
            .build(requireActivity())
        hideSoftKeyboard()
        baseDialog?.show()
    }

    private fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(requireActivity(), InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(savedInstanceState)

        initUI()
        initRecyclerView()
        initObserver()
        initClickListener()
        initFetchData()
        initLocalData()
        initProperties()
        initView()
    }

    abstract fun onViewReady(savedInstanceState: Bundle?)

    open fun initUI() {}
    open fun initFetchData() {}
    open fun initLocalData() {}
    open fun initRecyclerView() {}
    open fun initObserver() {}
    open fun initClickListener() {}
    open fun initProperties() {}
    open fun initView() {}
}