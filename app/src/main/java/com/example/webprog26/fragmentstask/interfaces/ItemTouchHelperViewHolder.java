package com.example.webprog26.fragmentstask.interfaces;

/**
 * Created by webprog26 on 22.11.2016.
 */

public interface ItemTouchHelperViewHolder {

   /**
    * Changes background of selected list item to selected
    */
   public void onItemSelected();

    /**
     * Changes background of selected list item to unselected
     */
   public void onItemClear();
}
