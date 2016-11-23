package com.example.webprog26.fragmentstask.interfaces;

/**
 * Created by webprog26 on 22.11.2016.
 */

public interface ItemTouchHelperViewHolder {

   /**
    * Implemented by adapter's ViewHolder. Changes background of selected list item to selected
    */
   public void onItemSelected();

    /**
     * Implemented by adapter's ViewHolder. Changes background of selected list item to unselected
     */
   public void onItemClear();
}
