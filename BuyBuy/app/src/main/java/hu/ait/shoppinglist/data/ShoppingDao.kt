package hu.ait.shoppinglist.data

import androidx.room.*

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM shoppinglist")
    fun getAllItems() : List<ShoppingItem>

    @Insert
    fun insertItem(item: ShoppingItem) : Long

    @Delete
    fun deleteItem(item: ShoppingItem)

    @Update
    fun updateItem(item : ShoppingItem)

    @Query("DELETE FROM shoppinglist")
    fun deleteAllItems()
}