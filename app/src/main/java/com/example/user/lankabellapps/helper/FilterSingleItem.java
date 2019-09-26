package com.example.user.lankabellapps.helper;


import com.example.user.lankabellapps.models.DownloadItemModel;
import com.example.user.lankabellapps.models.Merchants;
import com.example.user.lankabellapps.models.SingleItemModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thejan on 6/2/2016.
 */

/**
 * Filter String item from a list
 */
public class FilterSingleItem {

    public FilterSingleItem() {
    }

    public List<SingleItemModel> filterSR(List<SingleItemModel> singleItemModelList , String query) {
        List<SingleItemModel> filteredList = new ArrayList<>();

        for (SingleItemModel model : singleItemModelList) {
            boolean result = StringLikes.like(model.itemText.toString(), query);
            if (!result)
                result = StringLikes.like(model.itemText.toString(), query);

            if (result) {
                filteredList.add(model);
            }
        }

        return filteredList;
    }

    public List<DownloadItemModel> filterSRDownload(List<DownloadItemModel> singleItemModelList , String query) {
        List<DownloadItemModel> filteredList = new ArrayList<>();

        for (DownloadItemModel model : singleItemModelList) {
            boolean result = StringLikes.like(model.itemText.toString(), query);
            if (!result)
                result = StringLikes.like(model.itemText.toString(), query);

            if (result) {
                filteredList.add(model);
            }
        }

        return filteredList;
    }


    public List<Merchants> filterMerchants(List<Merchants> merchantsList, String query) {
        List<Merchants> filteredList = new ArrayList<>();

        for (Merchants model : merchantsList) {
            boolean result = StringLikes.like(model.getMerchantName().toString(), query);
            boolean result1 = StringLikes.like(model.getMcity().toString(), query);
            boolean result2 = StringLikes.like(model.getTelephone().toString(), query);
            if (!result || !result1 || !result2)
                result = StringLikes.like(model.getMerchantName().toString(), query);
            result1 = StringLikes.like(model.getMcity().toString(), query);
            result2 = StringLikes.like(model.getTelephone().toString(), query);

            if (result || result1 || result2) {
                filteredList.add(model);
            }
        }

        return filteredList;
    }

}
