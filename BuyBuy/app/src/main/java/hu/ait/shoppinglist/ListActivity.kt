package hu.ait.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import hu.ait.shoppinglist.adapter.ShoppingListAdapter
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.ShoppingItem
import hu.ait.shoppinglist.dialogs.DetailsDialog
import hu.ait.shoppinglist.dialogs.ItemDialog
import hu.ait.shoppinglist.touch.ShoppingListRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    companion object {
        const val KEY_ITEM = "KEY_ITEM"
        const val TAG_ITEM_DIALOG = "TAG_ITEM_DIALOG"
        const val TAG_ITEM_EDIT = "TAG_ITEM_EDIT"
        const val TAG_ITEM_DETAILS = "TAG_ITEM_DETAILS"
    }

    lateinit var shoppingListAdapter : ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setSupportActionBar(toolbar)

        initRecyclerView()

        fab.setOnClickListener() {
            showAddItemDialog()
        }

        fabDeleteAll.setOnClickListener{
            shoppingListAdapter.deleteAllItems()
        }



    }

    private fun initRecyclerView() {

        Thread{
            var shoppingList = AppDatabase.getInstance(this@ListActivity).shoppingDao().getAllItems()

            runOnUiThread {
                shoppingListAdapter = ShoppingListAdapter(this, shoppingList)
                recyclerShoppingList.adapter = shoppingListAdapter

                var itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

                recyclerShoppingList.addItemDecoration(itemDecoration)

                //recyclerTodo.layoutManager = GridLayoutManager(this, 2)//

                val callback = ShoppingListRecyclerTouchCallback(shoppingListAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShoppingList)
            }

        }.start()

    }

    fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, TAG_ITEM_DIALOG)
    }

    var editIndex : Int = -1

    fun showEditItemDialog(itemToEdit: ShoppingItem, idx: Int) {
        editIndex = idx
        val editDialog = ItemDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM, itemToEdit)

        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, TAG_ITEM_EDIT)
    }

    fun showItemDetailsDialog(itemToShow: ShoppingItem){
        val detailsDialog = DetailsDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM, itemToShow)

        detailsDialog.arguments = bundle

        detailsDialog.show(supportFragmentManager, TAG_ITEM_DETAILS)
    }

    fun saveItem(shoppingItem: ShoppingItem) {
        Thread {
            var newId =
                AppDatabase.getInstance(this@ListActivity).shoppingDao().insertItem(
                    shoppingItem
                )
            shoppingItem.shoppingItemId = newId

            runOnUiThread {
                shoppingListAdapter.addItem(shoppingItem)
            }
        }.start()
    }

    override fun itemCreated(item: ShoppingItem) {
        saveItem(item)
    }

    override fun itemUpdated(item: ShoppingItem) {
        Thread {
            AppDatabase.getInstance(
                this@ListActivity
            ).shoppingDao().updateItem(item)

            runOnUiThread {
                shoppingListAdapter.updateItemOnPosition(item, editIndex)
            }
        }.start()
    }

}
