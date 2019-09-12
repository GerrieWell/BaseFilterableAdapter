package com.android.demo.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.demo.FilterTargetInterface;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 一个可搜索的adapter.
 * 使用:
 * 1. 自定义bean实现 FilterTargetInterface
 * 2. 搜索时, getFilter 执行搜索, adapter 会自动执行搜索结果.
 * @author wjl
 * @param <T>
 * @param <K>
 */
public abstract class BaseFilterableSectionQuickAdapter<T extends SectionEntity, K extends BaseViewHolder>
        extends BaseSectionQuickAdapter<T, K> implements Filterable {
    private List<T> mFilterList;
    private List<T> mSourceList;
    public BaseFilterableSectionQuickAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, sectionHeadResId, data);
        mSourceList = data;
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
        /**
         * //to-do 用注解获得也可行. 遍历bean 所有带有Filter Annotation 的field.
         * @param constraint
         * @return
         */
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering() called with: constraint = [" + constraint + "]" + mSourceList.size());
            String charString = constraint.toString();
            if (charString.isEmpty()) {
                mFilterList = mSourceList;
            } else {
                mFilterList = mSourceList.stream().filter(((section)->{
                    String pattern = getFilterPattern((String) constraint);
                    if(section.isHeader){
                        return true;
                    }
                    if(! (section.t instanceof FilterTargetInterface)){
                        Log.d(TAG, "performFiltering: section.t instanceof FilterTargetInterface " );
                        return false;
                    }

                    FilterTargetInterface bean = (FilterTargetInterface) section.t;
                    boolean result = false;
                    for(String str: bean.getFilterString()){
                        if(str!=null) {
                            result |= Pattern.matches(pattern, str);
                        }
                    }
                    return result;
                })).collect(Collectors.toList());
                //以上结果为( IsHeader || section命中), 以下过滤, section列表不为0, 或header命中
                List<Pair<T,T>> zip = IntStream.range(0, mFilterList.size() -1).mapToObj(i -> new Pair<>(mFilterList.get(i), mFilterList.get(i+1))).collect(Collectors.toList());

                zip.removeIf(ttPair -> {
                    boolean keepHeader = (ttPair.first == null ||(ttPair.first.isHeader&&isHit(charString, ttPair.first.header)));
                    if (keepHeader){
                        return false;
                    }
                    return (ttPair.first.isHeader && ttPair.second.isHeader);
                });

                mFilterList =zip.stream().map(ttPair -> ttPair.first).collect(Collectors.toList());
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = mFilterList;
            return filterResults;
        }

        private boolean isHit(String string, String target){
            String pattern = getFilterPattern(string);
            return Pattern.matches(pattern, target);
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilterList = (List<T>) results.values;
            setNewData(mFilterList);
        }

    };


}
