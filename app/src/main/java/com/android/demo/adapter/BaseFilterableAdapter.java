package com.android.demo.adapter;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.RequiresApi;

import com.android.demo.FilterTargetInterface;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class BaseFilterableAdapter<T extends FilterTargetInterface, K extends BaseViewHolder> extends BaseQuickAdapter<T,K> implements Filterable {
    private List<T> mFilterList;
    private List<T> mSourceList;
    public BaseFilterableAdapter(int layoutResId,  List<T> data) {
        super(layoutResId, data);
        mSourceList = data;
    }

    public BaseFilterableAdapter(List<T> data) {
        super(data);
        mSourceList = data;
    }

    public BaseFilterableAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    /**设置筛选匹配的正则表达式.
     *一个例子:  "(?i).*" + keyWord+ ".*";
     * @param keyWord
     * @return
     */
    protected abstract String getFilterPattern(String keyWord);

    private final Filter filter = new Filter() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering() called with: constraint = [" + constraint + "]" + mSourceList.size());
            String charString = constraint.toString();
            if (TextUtils.isEmpty(charString)) {
                mFilterList = mSourceList;
            } else {
                mFilterList = mSourceList.stream().filter(((bean)->{
//                    String pattern = "(?i).*" + charString+ ".*";
                    String pattern = getFilterPattern(charString);
                    if(bean == null){
                        Log.d(TAG, "performFiltering: section.t instanceof FilterTargetInterface " );
                        return false;
                    }
                    boolean result = false;
                    for(String str: bean.getFilterString()){
                        if(str!=null) {
                            result |= Pattern.matches(pattern, str);
                        }
                    }
                    return result;
                })).collect(Collectors.toList());
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = mFilterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilterList = (List<T>) results.values;
            setNewData(mFilterList);
            notifyDataSetChanged();
        }
    };
}
