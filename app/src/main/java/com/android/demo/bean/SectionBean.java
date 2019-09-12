package com.android.demo.bean;

import com.android.demo.FilterTargetInterface;

import java.util.ArrayList;
import java.util.List;

public class SectionBean implements FilterTargetInterface {
    String name;
    String name_en;
    public String brand;

    private SectionBean(Builder builder) {
        name = builder.name;
        name_en = builder.name_en;
        brand = builder.brand;
    }

    public static final class Builder {
        private String name;
        private String name_en;
        private String brand;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder name_en(String val) {
            name_en = val;
            return this;
        }

        public Builder brand(String val) {
            brand = val;
            return this;
        }

        public SectionBean build() {
            return new SectionBean(this);
        }
    }
    @Override
    public List<String> getFilterString() {
        List<String> arr = new ArrayList<>();
        arr.add(name);
        arr.add(brand);
        return arr;
    }
}