package hu.ait.shoppinglist.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import hu.ait.shoppinglist.ListActivity
import hu.ait.shoppinglist.R
import hu.ait.shoppinglist.data.ShoppingItem
import kotlinx.android.synthetic.main.details_dialog.view.*
class DetailsDialog : DialogFragment() {

    private lateinit var tvDtName : TextView
    private lateinit var tvDtPrice : TextView
    private lateinit var tvDtDescription: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.item_details_title))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.details_dialog, null
        )
        tvDtName = rootView.tvDtName
        tvDtPrice = rootView.tvDtPrice
        tvDtDescription = rootView.tvDtDescription
        builder.setView(rootView)

        val item = (arguments?.getSerializable(ListActivity.KEY_ITEM) as ShoppingItem)

        tvDtName.setText(item.itemName)
        tvDtPrice.setText(item.price.toString())
        tvDtDescription.setText(item.description)

        builder.setPositiveButton(getString(R.string.ok_button)) {
                dialog, which -> // empty
        }

        return builder.create()
    }
}