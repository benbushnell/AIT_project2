package hu.ait.shoppinglist.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import hu.ait.shoppinglist.ListActivity
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.data.ShoppingItem
import kotlinx.android.synthetic.main.new_item_dialog.view.*
import java.lang.RuntimeException

class ItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(shoppingItem: ShoppingItem)
        fun itemUpdated(shoppingItem: ShoppingItem)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        }
        else {
            throw RuntimeException(
                "The activity does not implement to ItemHandler Interface"
            )
        }
    }

    private lateinit var etItemName: EditText
    private lateinit var etItemPrice: EditText
    private lateinit var etItemDescription: EditText
    private lateinit var spinnerType: Spinner

    var isEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_item_title))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_item_dialog, null
        )
        etItemName = rootView.etName
        etItemDescription = rootView.etDescription
        etItemPrice = rootView.etPrice
        spinnerType = rootView.spinnerType
        builder.setView(rootView)

        isEditMode = ((arguments != null) && arguments!!.containsKey(ListActivity.KEY_ITEM))

        if (isEditMode) {
            builder.setTitle(getString(R.string.edit_item_title))
            var item = (arguments?.getSerializable(ListActivity.KEY_ITEM) as ShoppingItem)

            etItemName.setText(item.itemName)
            etItemDescription.setText(item.description)
            etItemPrice.setText(item.price.toString())
            spinnerType.setSelection(item.type)


        }

        builder.setPositiveButton(R.string.ok_button) {
                dialog, which -> // empty
        }

        return builder.create()

    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener{
            if (etItemName.text.isNotEmpty() && etItemPrice.text.isNotEmpty()) {
                if (isEditMode){
                    handleItemEdit()
                }
                else{
                    handleItemCreate()
                }
                dialog!!.dismiss()
            } else {
                etItemName.error = getString(R.string.error_empty)
                etItemPrice.error = getString(R.string.error_empty)
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
            ShoppingItem(
                null,
                etItemName.text.toString(),
                false, etItemDescription.text.toString(),
                etItemPrice.text.toString().toInt(), spinnerType.selectedItemPosition
            )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            ListActivity.KEY_ITEM
        ) as ShoppingItem
        itemToEdit.itemName = etItemName.text.toString()
        itemToEdit.description = etItemDescription.text.toString()
        itemToEdit.price = etItemPrice.text.toString().toInt()
        itemToEdit.type = spinnerType.selectedItemPosition

        itemHandler.itemUpdated(itemToEdit)
    }

}