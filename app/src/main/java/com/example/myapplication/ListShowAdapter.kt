package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemFormBinding
import com.example.myapplication.model.itemmodel.FormResult

class ListShowAdapter(val context: Context) :
    RecyclerView.Adapter<ListShowAdapter.ListShowViewHolder>() {

    private val diffUtilsCallBack = object : DiffUtil.ItemCallback<FormResult>() {
        override fun areItemsTheSame(
            oldItem: FormResult,
            newItem: FormResult,
        ): Boolean {
            return oldItem.utility_code == newItem.utility_code
        }

        override fun areContentsTheSame(
            oldItem: FormResult,
            newItem: FormResult,
        ): Boolean {
            return oldItem.utility_code == newItem.utility_code
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearDataSet() {
        differ.submitList(null)
        this.notifyDataSetChanged()
    }


    fun getCurrentDataSet(): List<FormResult> =
        ArrayList(differ.currentList.toMutableList()).toMutableList()

    private var differ: AsyncListDiffer<FormResult> = AsyncListDiffer(this, diffUtilsCallBack)

    fun submitListData(dataList: List<FormResult>) {
        differ.submitList(dataList)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ListShowViewHolder =
        ListShowViewHolder(
            ItemFormBinding.inflate(
                LayoutInflater.from(context), parent, false
            ), this
        )

    internal var itemActionListener: (model: FormResult) -> Unit = {}

    override fun onBindViewHolder(
        holder: ListShowViewHolder, position: Int
    ) = holder.bindView(position)


    override fun getItemCount(): Int = differ.currentList.size

    @SuppressLint("SetTextI18n")
    inner class ListShowViewHolder(
        private val binding: ItemFormBinding, private val adapter: ListShowAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(position: Int) {
            val model = adapter.getCurrentDataSet()[position]
            binding.headerName.text = model.category_name
            binding.name.text = model.utility_value
            binding.headerName.setOnClickListener {
                itemActionListener.invoke(model)
            }
        }
    }
}
