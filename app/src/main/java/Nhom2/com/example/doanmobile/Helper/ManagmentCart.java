package Nhom2.com.example.doanmobile.Helper;

import android.content.Context;
import android.widget.Toast;

import Nhom2.com.example.doanmobile.Domain.ItemsDomain;
import Nhom2.com.example.doanmobile.Models.CartItem;

import java.util.ArrayList;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertItem(CartItem item) {
        ArrayList<CartItem> listItem = getListCart();
        boolean existAlready = false;
        int n = 0;

        for (int y = 0; y < listItem.size(); y++) {
            // Check if both the title, size, and color are the same
            if (listItem.get(y).getTitle().equals(item.getTitle()) &&
                    listItem.get(y).getSize().equals(item.getSize()) &&
                    listItem.get(y).getColor().equals(item.getColor())) {
                existAlready = true;
                n = y;
                break;
            }
        }

        if (existAlready) {
            // If the item already exists with the same title, size, and color, increase the quantity
            listItem.get(n).setQuantity(listItem.get(n).getQuantity() + item.getQuantity());
        } else {
            // Otherwise, add a new item to the cart
            listItem.add(item);
        }

        // Save the updated list to TinyDB
        tinyDB.putListObject("CartList", listItem);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<CartItem> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<CartItem> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getQuantity() == 1) {
            listItem.remove(position);
        } else {
            listItem.get(position).setQuantity(listItem.get(position).getQuantity() - 1);
        }
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<CartItem> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setQuantity(listItem.get(position).getQuantity() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.changed();
    }
    public void clearCart() {
        // Clear the list of cart items
        ArrayList<CartItem> emptyCart = new ArrayList<>();

        // Save the empty list back to TinyDB
        tinyDB.putListObject("CartList", emptyCart);

        // Optionally, show a toast message to notify the user
        Toast.makeText(context, "Cart is now empty", Toast.LENGTH_SHORT).show();
    }
    public Double getTotalFee() {
        ArrayList<CartItem> listItem2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listItem2.size(); i++) {
            fee = fee + (listItem2.get(i).getPrice() * listItem2.get(i).getQuantity());
        }
        return fee;
    }
}
