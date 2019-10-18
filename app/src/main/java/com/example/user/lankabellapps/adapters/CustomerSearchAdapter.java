package com.example.user.lankabellapps.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.helper.Constants;
import com.example.user.lankabellapps.models.Customers;
import com.example.user.lankabellapps.models.LocationRegisterWithCustomer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thejanthrimanna on 20/05/16.
 */
public class CustomerSearchAdapter extends RecyclerView.Adapter<CustomerSearchAdapter.Holder> {

    public List<Customers> item = new ArrayList<>();
    //    private Facilities facilities;
//    private FacilityTypes facilityTypes;
    String AfterTime;
    String UntilTime;

    String fromCall = "";


    //CustomFilter filter;
//    List<Facilities> facilityListFiltered;
//
//    List<Facilities> filter1;
//    List<GetFacilityAvailabilityResponse.FacilittAvailability> Filtereditem;


    Context context;

    public CustomerSearchAdapter(List<Customers> items, Context context, String fromCall) {
        this.item = items;
        this.context = context;
        this.fromCall = fromCall;

    }

    public void addItems(Customers getFacilityAvailabilityResponse) {
        item.add(getFacilityAvailabilityResponse);
        notifyDataSetChanged();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row;


        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_customer_search_item, parent, false);

        return new Holder(row);


    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        LocationRegisterWithCustomer locationRegisterWithCustomer = new LocationRegisterWithCustomer();
        List<LocationRegisterWithCustomer> locationRegisterWithCustomerList = new ArrayList<>();
        locationRegisterWithCustomerList = locationRegisterWithCustomer.getLocationRegisterDetailsByCusCode(item.get(position).companyCode);
        if (!locationRegisterWithCustomerList.isEmpty()) {
            holder.register.setChecked(true);
            holder.itemView.setBackgroundResource(R.drawable.not_registered_item);
            holder.name.setTextColor(Color.BLACK);
            //      holder.address.setTextColor(Color.BLACK);
            //          holder.contactNo.setTextColor(Color.BLACK);
            //         holder.os.setTextColor(Color.BLACK);
            //         holder.dueDate.setTextColor(Color.BLACK);
            //          holder.status.setTextColor(Color.BLACK);

        } else {
            holder.register.setChecked(false);
            holder.itemView.setBackgroundResource(R.drawable.registered_item);
            holder.name.setTextColor(Color.BLACK);
//            holder.address.setTextColor(Color.BLACK);
//            holder.contactNo.setTextColor(Color.BLACK);
            //           holder.os.setTextColor(Color.BLACK);
//            holder.dueDate.setTextColor(Color.BLACK);
//            holder.status.setTextColor(Color.BLACK);
        }
//        holder.status.setText(item.get(position).getDistrict());


        holder.name.setText(item.get(position).getNamem());
        holder.nicbr.setText(item.get(position).getCompanyCode());
        //holder.noAccounts.setText(item.get(position).getAc());
        holder.city.setText(item.get(position).getCity());
        //.holder.dueDate.setText(item.get(position).getDueDate());

    }
//        holder.name.setText(item.get(position).getMname());
//        holder.date.setText(item.get(position).getLeavedate());
//        holder.count.setText(item.get(position).getCount())

//        facilities = new Facilities();
//        facilityTypes = new FacilityTypes();
//
//        String fasilitytype = "";
//        String facilityClass = "";
//
//        List<Facilities> facilityList = facilities.getFilteredFacilities(item.get(position).getFacilityKey());
//
//
//        if (facilityList.size() > 0) {
//            fasilitytype = facilityList.get(0).facilityType;
//            holder.name.setText(facilityList.get(0).facilityName);
//        }
//
//        List<FacilityTypes> fasilityTypelist = facilityTypes.getFasilityFilteredClass(fasilitytype);
//
//
//        if (fasilityTypelist.size() > 0) {
//            facilityClass = fasilityTypelist.get(0).facilityClass;
//        }
//
//
//        if (facilityClass.equals("Seat")) {
//            if (item.get(position).getIsAvailable().equals("true")) {
//                holder.im.setImageResource(R.drawable.facility_available_seat);
//                //holder.date.setText();
//
//
//            } else if (item.get(position).getIsAvailable().equals("false")) {
//                holder.im.setImageResource(R.drawable.facility_booked_seat);
//            }
//        } else if (facilityClass.equals("Room")) {
//            if (item.get(position).getIsAvailable().equals("true")) {
//                holder.im.setImageResource(R.drawable.facility_available_room);
//
//
//            } else if (item.get(position).getIsAvailable().equals("false")) {
//                holder.im.setImageResource(R.drawable.facility_booked_room);
//
//            }
//        }
//
//
//        //  Date dateformat=new Date();
////        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
//
//
//        if (!item.get(position).getFreeAfter().equals("")) {
//
//            String dateString = null;
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
//
//            try {
//                Date dt = new Date();
//                dt = dateFormat.parse(item.get(position).getFreeAfter());
//                SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
//                //SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
//                dateString = newFormat.format(dt);
//                holder.date.setText("Free After " + dateString);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//
////            String dat  = sdf.format(item.get(position).getFreeAfter());
////            holder.date.setText("Free After" +dat);
//            //holder.date.setText("After " + AfterTime);
//            // holder.date.setText("After");
//
//        } else if (!item.get(position).getFreeUntil().equals("")) {
////            String dat  = sdf.format(item.get(position).getFreeUntil());
////            holder.date.setText("Free Until" +dat);
//            /// holder.date.setText("Untill");
//
//            try {
//                String dateString = null;
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
//                Date dt = new Date();
//                dt = dateFormat.parse(item.get(position).getFreeUntil());
//                SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");
//                //SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
//                dateString = newFormat.format(dt);
//                holder.date.setText("Free Until " + dateString);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            holder.date.setText("");
//        }
//
//
//        System.out.println("Item size" + item.size() + fasilityTypelist.size() + facilityList.size());


    @Override
    public int getItemCount() {
        return null != item ? item.size() : 0;
    }

//    @Override
//    public Filter getFilter() {
//
//        return new CustomFilter(this, item);
//    }


    public class Holder extends RecyclerView.ViewHolder {
        ImageView im;
        RelativeLayout rel;
        CheckBox register;
        TextView name, address, contactNo, os, dueDate, status, time, city, noAccounts, nicbr, revenu;

        public Holder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_name);
            city = (TextView) itemView.findViewById(R.id.tv_city);
            noAccounts = (TextView) itemView.findViewById(R.id.tv_no_of_accounts);
            //os = (TextView) itemView.findViewById(R.id.tv_os);
            nicbr = (TextView) itemView.findViewById(R.id.tv_nicbr);
            revenu = (TextView) itemView.findViewById(R.id.tv_revenue);
            //time = (TextView) itemView.findViewById(R.id.tv_time);
            register = (CheckBox) itemView.findViewById(R.id.cb_register);

//            name = (CustomTextView) itemView.findViewById(R.id.tv_facilityname);
//            rel = (RelativeLayout) itemView.findViewById(R.id.lay_facility_item);
//            date = (CustomTextView) itemView.findViewById(R.id.tv_availabletime);
//            im = (ImageView) itemView.findViewById(R.id.iv_booked_available);

        }
    }
}
//    public class CustomFilter extends Filter {
//
//        Facilities fasilityOb = new Facilities();
//
//        private final List<GetFacilityAvailabilityResponse.FacilittAvailability> OriginalItems;
//
//        private final List<GetFacilityAvailabilityResponse.FacilittAvailability> FiltteredResults;
//
//        private final List<Facilities> filteredFacilities;
//
//        private final FacilityAvailableAdapter adapter;
//
//    }


//        private CustomFilter(FacilityAvailableAdapter madapter, List<GetFacilityAvailabilityResponse.FacilittAvailability> originalList){
//            // this.OriginalItems =new LinkedList<>(originalList);
//            this.OriginalItems = ((FacilityAvailabilityActivity) context).sortedList;
//            this.adapter = madapter;
//            this.FiltteredResults = new ArrayList<>();
//            this.filteredFacilities = new ArrayList<>();
//        }
//
//
//
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            FiltteredResults.clear();
//
//            FilterResults results = new FilterResults();
//
//
//            List<Facilities> OriginalFacilityList = fasilityOb.getAllFacilities();
//
//
//
//            String cont = constraint.toString().toUpperCase().trim();
//
//
//            if(cont != null && cont.length()>0){
//                //constraint = constraint.toString().toUpperCase();
//
//
//
//
//
//                for(int i=0; i<OriginalFacilityList.size(); i++){
//                    if(OriginalFacilityList.get(i).getFacilityName().toUpperCase().contains(cont)){
//
//                        filteredFacilities.add(OriginalFacilityList.get(i));
//
//                    }
//                }
//
//                for (int j = 0; j < filteredFacilities.size(); j++) {
//                    for (int k = 0; k < OriginalItems.size(); k++) {
//                        // System.out.println(filter1.get(j).facilityKey + " " + item.get(k).getFacilityKey());
//                        if (filteredFacilities.get(j).facilityKey.equals(OriginalItems.get(k).getFacilityKey())){
//                            FiltteredResults.add(OriginalItems.get(k));
//                        }
//                    }
//                }
//
//
//
//                results.values = FiltteredResults;
//                results.count = FiltteredResults.size();
//                System.out.println("Filtered item size  " +results.count);
//
//
//
//
//            }else{
//                FiltteredResults.addAll(OriginalItems);
//
//            }
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            // item.clear();
//
////            adapter..clear();
////            adapter.Filtereditem.addAll((Collection<? extends GetFacilityAvailabilityResponse.FacilittAvailability>) results.values);
////            adapter.notifyDataSetChanged();
//
//
//            ((FacilityAvailabilityActivity) context).NewAdapterLoad(FiltteredResults);
//
//
//
//
//
//
//        }
//    }



