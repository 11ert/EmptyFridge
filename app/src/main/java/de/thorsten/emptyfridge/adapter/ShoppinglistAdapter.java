package de.thorsten.emptyfridge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.thorsten.emptyfridge.R;
import de.thorsten.emptyfridge.model.ShoppingItem;

/**
 * Created by Thorsten on 29.11.2015.
 */
public class ShoppinglistAdapter extends ArrayAdapter<ShoppingItem>{

    private List<ShoppingItem> itemList;
    private Context context;

    public ShoppinglistAdapter(List<ShoppingItem> itemList, Context ctx) {

        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList; this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public ShoppingItem getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null; }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Keine Ahnung ob das stimmt, ursprünglich:
            //v = inflater.inflate(R.layout.list_item, null);
            v = inflater.inflate(android.R.layout.simple_list_item_1, null); }
        ShoppingItem shoppingItem = itemList.get(position);
        //TextView text = (TextView) v.findViewById(R.id.name  );
        //stext.setText(shoppingItem.getName());

        return v;
    }

    public List<ShoppingItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ShoppingItem> itemList) {
        this.itemList = itemList;
    }

}
