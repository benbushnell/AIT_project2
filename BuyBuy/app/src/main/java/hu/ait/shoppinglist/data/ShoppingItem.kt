package hu.ait.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shoppinglist")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) var shoppingItemId: Long?,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "bought") var bought: Boolean,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "type") var type: Int

) : Serializable