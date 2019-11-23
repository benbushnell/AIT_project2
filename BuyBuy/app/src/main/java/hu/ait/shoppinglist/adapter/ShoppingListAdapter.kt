package hu.ait.shoppinglist.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ait.shoppinglist.ListActivity
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.touch.ShoppingListTouchHelperCallback
import kotlinx.android.synthetic.main.new_item_dialog.view.*
import kotlinx.android.synthetic.main.shopping_list_row.view.*
import java.util.*

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>, ShoppingListTouchHelperCallback {

    var shoppingList = mutableListOf<ShoppingItem>()

    val context : Context
    constructor(context: Context, listItems : List<ShoppingItem>){
        this.context = context

        shoppingList.addAll(listItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val shoppingListRow = LayoutInflater.from(context).inflate(
            R.layout.shopping_list_row, parent, false
        )
        return ViewHolder(shoppingListRow)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var shoppingItem = shoppingList.get(holder.adapterPosition)

        holder.cbItem.text = shoppingItem.itemName
        holder.cbItem.isChecked = shoppingItem.bought
        holder.tvPrice.text = shoppingItem.price.toString()
        when (shoppingItem.type){
            0 -> holder.layoutCard.setBackgroundResource(R.drawable.food2)
            1 -> holder.layoutCard.setBackgroundResource(R.drawable.clothing2)
            else -> holder.layoutCard.setBackgroundResource(R.drawable.furniture2)
        }

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.cbItem.setOnClickListener {
            shoppingItem.bought = holder.cbItem.isChecked
            updateItem(shoppingItem)
        }

        holder.btnEdit.setOnClickListener {
            (context as ListActivity).showEditItemDialog(
                shoppingItem, holder.adapterPosition
            )
        }
        holder.btnDetails.setOnClickListener {
            (context as ListActivity).showItemDetailsDialog(
                shoppingItem
            )
        }

    }

    fun updateItem(shoppingItem: ShoppingItem) {
        Thread{
            AppDatabase.getInstance(context).shoppingDao().updateItem(shoppingItem)
        }.start()
    }

    fun updateItemOnPosition(shoppingItem: ShoppingItem, index : Int) {
        shoppingList.set(index, shoppingItem)
        notifyItemChanged(index)
    }

    fun deleteItem(index : Int){
        Thread{
            AppDatabase.getInstance(context).shoppingDao().deleteItem(shoppingList[index])
            (context as ListActivity).runOnUiThread {
                shoppingList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAllItems() {
        Thread {
            AppDatabase.getInstance(context).shoppingDao().deleteAllItems()

            (context as ListActivity).runOnUiThread{
                shoppingList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addItem(shoppingItem: ShoppingItem) {
        shoppingList.add(shoppingItem)
        notifyItemInserted(shoppingList.lastIndex)
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val layoutCard = itemView.layoutCard
        val cbItem = itemView.cbItem
        val tvPrice = itemView.tvPrice
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val btnDetails = itemView.btnDetails
    }
}