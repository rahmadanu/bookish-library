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

abstract class BaseFragment<VB : ViewBinding>: Fragment(){

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

    fun goToFragment(parentFragment: Fragment, actionId: Int?, direction: NavDirections?) {
        actionId?.let { NavHostFragment.findNavController(parentFragment).navigate(it) }
        direction?.let { NavHostFragment.findNavController(parentFragment).navigate(it) }
    }

    private fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager =
                ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady(savedInstanceState)

        initRecyclerView()
        initObserver()
        initClickListener()
        initFetchData()
        initLocalData()
    }

    abstract fun onViewReady(savedInstanceState: Bundle?)

    open fun initFetchData() {}
    open fun initLocalData() {}
    open fun initRecyclerView() {}
    open fun initObserver() {}
    open fun initClickListener() {}
}