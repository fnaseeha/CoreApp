package com.example.user.lankabellapps.popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.user.lankabellapps.R;
import com.example.user.lankabellapps.adapters.SingleItemSelectAndSearchAdapter;
import com.example.user.lankabellapps.helper.DisplayPixelCalculator;
import com.example.user.lankabellapps.helper.FilterSingleItem;
import com.example.user.lankabellapps.helper.StringEmptyCheck;
import com.example.user.lankabellapps.models.SingleItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thejan on 6/28/2016.
 */
public class SingleItemSearchAndSelectPopup implements SingleItemSelectAndSearchAdapter.SingleItemClickListener {

    private LayoutInflater mLayoutInflater;
    private View mDialogView;
    private Dialog mListDialog;
    private Context mContext;
    private RecyclerView mRvSingleList;
    private EditText mSearchView;
    private TextView mHeaderTitle;
    private ProgressBar mProgressBar;
    private TextView mTvMessage;
    private RelativeLayout mRelSearchContainer;
    private SingleItemSelectAndSearchAdapter mAdapter;
    private SingleItemPopupItemClickListener mListener;
    private List<SingleItemModel> mSingleList = new ArrayList<>();
    private String mBtnType;
    private ImageButton btnOk;
    private int checkStatus = 0;
    private String callActivity, selectedItemID = "";

    public SingleItemSearchAndSelectPopup(Context context, SingleItemPopupItemClickListener listener, LayoutInflater inflater, String callActivity) {
        mLayoutInflater = inflater;
        mContext = context;
        mListener = listener;
        this.callActivity = callActivity;
        configView();
    }

    public void makeDialog(String title, boolean hasSearch, List<SingleItemModel> list, String btnType) {
        mHeaderTitle.setText(title);
        mBtnType = btnType;

        //tempList = list;

        if (hasSearch) {
            mRelSearchContainer.setVisibility(View.VISIBLE);
        }
        //mSingleList.clear();

        for(SingleItemModel x : list){
            mSingleList.add(x);
        }

        // mSingleList = list;

//        for(int i = 0; i<mSingleList.size(); i++){
//            //checkStatusList.add(s.isChecked);
//            TempClass tempClass = new TempClass();
//            tempClass.index = i;
//            tempClass.checked = mSingleList.get(i).isChecked;
//        }


        setToAdapter(list);

    }

    private void configView() {
        mListDialog = new Dialog(mContext, R.style.CustomDialog);
        mDialogView = mLayoutInflater.inflate(R.layout.dialog_list, null);
        mRvSingleList = (RecyclerView) mDialogView.findViewById(R.id.rv_single_list);
        ImageButton btnClose = (ImageButton) mDialogView.findViewById(R.id.img_btn_close);
        btnOk = (ImageButton) mDialogView.findViewById(R.id.img_btn_ok);
        mSearchView = (EditText) mDialogView.findViewById(R.id.et_searchView);
        mHeaderTitle = (TextView) mDialogView.findViewById(R.id.tv_dialog_header_title);
        mProgressBar = (ProgressBar) mDialogView.findViewById(R.id.progressBar);
        mTvMessage = (TextView) mDialogView.findViewById(R.id.tv_message);
        mRelSearchContainer = (RelativeLayout) mDialogView.findViewById(R.id.rel_lay_search_container);

        mSearchView.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);

        mSearchView.addTextChangedListener(mTextWatcher);
        mRvSingleList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvSingleList.setHasFixedSize(true);


        if(callActivity.equals("1")){
            btnOk.setVisibility(View.VISIBLE);
        }else if(callActivity.equals("2")){
            btnOk.setVisibility(View.GONE);
        }


        btnClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListDialog.dismiss();
                    }
                });

//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        btnOk.setVisibility(View.GONE);

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = ((displayMetrics.heightPixels / displayMetrics.density) / 100) * 57;
        float dpWidth = ((displayMetrics.widthPixels / displayMetrics.density) / 100) * 90;


        final float x, y;
        DisplayPixelCalculator converter = new DisplayPixelCalculator();
        x = converter.dipToPixels(mContext, dpWidth);
        y = converter.dipToPixels(mContext, dpHeight);

        mListDialog.setContentView(mDialogView);
        mListDialog.show();
        mListDialog.setCanceledOnTouchOutside(false);
        mListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mListDialog.getWindow().setLayout((int) x, (int) y);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterItem(s.toString().trim());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void filterItem(String search) {
        boolean isEmpty = new StringEmptyCheck().isNotNullNotEmptyNotWhiteSpaceOnly(search);
        mTvMessage.setVisibility(View.GONE);
        mTvMessage.setText("");
        if (!isEmpty) {
            if (mSingleList != null) {
                setToAdapter(mSingleList);
            }
        } else {
            if (mSingleList != null) {
                List<SingleItemModel> SingleList = new FilterSingleItem().filterSR(mSingleList, search);
                if (SingleList.size() > 0) {
                    setToAdapter(SingleList);
                } else {
                    mAdapter.clearDataSet();
                    mTvMessage.setText("No Result for \"" + search + "\"");
                    mTvMessage.setVisibility(View.VISIBLE);
                }
            } else {
                mTvMessage.setText("Poor Internet Connection.");
                mTvMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setToAdapter(List<SingleItemModel> list) {
        List<SingleItemModel> currentList = new ArrayList<>();
        if (list.size() == 0) {
            mTvMessage.setText("No Items");
        }
        for (SingleItemModel singleItemModel : list) {
            currentList.add(singleItemModel);

        }

        mAdapter = new SingleItemSelectAndSearchAdapter(mContext, currentList, this, mBtnType);
        mRvSingleList.setAdapter(mAdapter);
        mSearchView.setEnabled(true);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickSingleItem(SingleItemModel selectedItem, int position) {
        mListener.onClickSingleItem(selectedItem, mBtnType, position);

        mListDialog.dismiss();
    }

    @Override
    public void onCheckChange(SingleItemModel checkedChanged, int position, String itemKey) {

    }


    public interface SingleItemPopupItemClickListener {
        void onClickSingleItem(SingleItemModel selectedItem, String btnType, int position);

        void onClickSelectOk(String selectedItemID);
    }


    public class TempClass{
        public int index;
        public boolean checked;
    }
}
