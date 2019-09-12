package com.android.demo.bean;

import com.android.demo.FilterTargetInterface;
import com.android.demo.R;
import com.android.demo.adapter.BaseFilterableSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;

public class FilterableSection extends SectionEntity<SectionBean> {

        public FilterableSection(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public FilterableSection(SectionBean o) {
            super(o);
        }


}
