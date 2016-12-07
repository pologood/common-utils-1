package com.baidu.unbiz.common.bean.sample;

import com.baidu.unbiz.common.bean.CopyField;
import com.baidu.unbiz.common.lang.StringableSupport;

public class Overload extends StringableSupport {

    @CopyField
    String company;

    // not a property setter
    public void setCompany(StringBuilder sb) {
        this.company = sb.toString();
    }

    public String getCompany() {
        return company;
    }
}