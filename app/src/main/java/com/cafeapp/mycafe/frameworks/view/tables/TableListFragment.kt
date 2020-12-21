package com.cafeapp.mycafe.frameworks.view.tables

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.room.TableEntity
import com.cafeapp.mycafe.frameworks.view.BaseFragment
import com.cafeapp.mycafe.frameworks.view.utils.RecyclerViewUtil
import com.cafeapp.mycafe.interface_adapters.viewmodels.tables.TableViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.tables.TableViewState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.dialog_add_table.*
import kotlinx.android.synthetic.main.dialog_add_table.view.*
import kotlinx.android.synthetic.main.dialog_add_table.view.cancelBtn
import kotlinx.android.synthetic.main.fragment_categorylist.view.*
import kotlinx.android.synthetic.main.fragment_tablelist.*
import kotlinx.android.synthetic.main.fragment_tablelist.view.*
import org.koin.androidx.scope.currentScope

class TableListFragment() : BaseFragment<TableViewModel, TableViewState>(),
    OnTableListItemClickListener {
    override val viewModel: TableViewModel by currentScope.inject()
    private lateinit var gDialog: AlertDialog
    private lateinit var tableListAdapter: TableListRVAdapter
    private var currentTableEntity: TableEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tablelist, container, false)
        return root
    }

    private fun initRecyclerView() {
        tableListAdapter = TableListRVAdapter(TableListFragment@ this)
        tableListRW.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = tableListAdapter
            RecyclerViewUtil.addDecorator(context, this)}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allTable()
        setFabImageResource(android.R.drawable.ic_input_add)
    }

    private fun showAddTableDialog(defaultText: String): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_add_table, null)
        builder.setView(view)
        view.tableNameTIT.setText(defaultText)
        val dialog = builder.create()
        view.cancelBtn.setOnClickListener {
            gDialog.dismiss()
        }
        view.saveTableBtn.setOnClickListener {
            view.tableNameTIT?.let {
                if (currentTableEntity == null)
                    addNewTable(it.text.toString())
                else
                    editTable(it.text.toString())
                currentTableEntity = null
                gDialog.dismiss()
            }
        }
        return dialog
    }

    private fun editTable(tableName: String) {
        this.currentTableEntity?.tablename = tableName
        viewModel.updateTable(currentTableEntity)
    }

    private fun addNewTable(tableName: String) {
        val tableEntity = TableEntity(tablename = tableName)
        viewModel.addTable(tableEntity)
    }

    override fun onEditTableButtonClick(tableEntity: TableEntity) {
        this.currentTableEntity = tableEntity
        gDialog = showAddTableDialog(tableEntity.tablename)
        gDialog.show()
    }

    override fun onRemoveTableButtonClick(tableEntity: TableEntity) {
        viewModel.removeTable(tableEntity)
    }

    override fun onMainFabClick() {
        gDialog = showAddTableDialog(" ")
        gDialog.show()
    }

    override fun onSharedMsg(msg: SharedMsg) {}

    override fun onViewModelMsg(state: TableViewState) {
        super.onViewModelMsg(state)
        state.tableList?.let { tableList ->
            initRecyclerView()
            tableListAdapter.setTableList(tableList)
        }
    }
}