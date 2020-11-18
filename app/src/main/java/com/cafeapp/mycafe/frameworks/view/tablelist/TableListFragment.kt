package com.cafeapp.mycafe.frameworks.view.tablelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.interface_adapters.viewmodels.TableListViewModel

class TableListFragment : Fragment() {

    private lateinit var tableListViewModel: TableListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tableListViewModel =
            ViewModelProvider(this).get(TableListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tablelist, container, false)
        val textView: TextView = root.findViewById(R.id.text_table)
        tableListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}