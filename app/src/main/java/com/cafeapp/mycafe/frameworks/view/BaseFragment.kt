package com.cafeapp.mycafe.frameworks.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.baseviewmodel.BaseViewState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class BaseFragment<T : BaseViewModel, ViewState : BaseViewState>(): Fragment() {
    protected abstract val viewModel: T
    protected val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedViewModelObserver()
        initViewModelObserver()
        initFab()
    }

    private fun initViewModelObserver() {
        viewModel.viewState.observe(viewLifecycleOwner, {
            onViewModelMsg(it as ViewState)
        })
    }

    private fun initSharedViewModelObserver() {
        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            onSharedMsg(msg)
        })
    }

    protected fun setFabImageResource(imageRes:Int) {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        fab?.setImageResource(imageRes)
    }

    private fun initFab() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)
        fab?.setImageResource(android.R.drawable.ic_input_add)
        fab?.setOnClickListener {
            onMainFabClick()
        }
    }

    abstract fun onMainFabClick()
    abstract fun onSharedMsg(msg: SharedMsg)
    open fun onViewModelMsg(state: ViewState) {
        state.error?.let {
            Toast.makeText(context, state.error!!.message, Toast.LENGTH_LONG).show()
        }
    }
}