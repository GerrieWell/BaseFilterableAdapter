package com.android.demo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.demo.adapter.BaseFilterableSectionQuickAdapter;
import com.android.demo.bean.FilterableSection;
import com.android.demo.bean.SectionBean;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 */
public class FilterableUseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<FilterableSection> mData;
    private EditText etSearch;
    MyAdapter sectionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterable_section);
        mRecyclerView = findViewById(R.id.rv_list);
        etSearch = findViewById(R.id.et_search);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mData = getSampleData();
        sectionAdapter = new MyAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);

        mRecyclerView.setAdapter(sectionAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sectionAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static List<FilterableSection> getSampleData() {
        List<FilterableSection> list = new ArrayList<>();
        list.add(new FilterableSection(true, "A"));
        list.add(new FilterableSection(new SectionBean.Builder().name("A").brand("itemA1").build()));
        list.add(new FilterableSection(new SectionBean.Builder().name("A").brand("itemA2").build()));

        list.add(new FilterableSection(true, "B"));
        list.add(new FilterableSection(new SectionBean.Builder().name("B").brand("itemB1").build()));
        list.add(new FilterableSection(true, "C"));
        list.add(new FilterableSection(new SectionBean.Builder().name("C").brand("item C1").build()));
        return list;
    }

    static class MyAdapter extends BaseFilterableSectionQuickAdapter<FilterableSection, BaseViewHolder> {

        public MyAdapter(int layoutResId, int sectionHeadResId, List data) {
            super(layoutResId, sectionHeadResId, data);
        }

        @Override
        protected String getFilterPattern(String keyWord) {
            return  "(?i).*" + keyWord+ ".*";
        }

        @Override
        protected void convertHead(BaseViewHolder helper, FilterableSection item) {
            helper.setText(R.id.header, item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, FilterableSection item) {
            helper.setText(R.id.tv, item.t.brand);
        }
    }
}
