package com.hn.library.indexlayout;

public interface IndexableEntity {

    String getFieldIndexBy();

    void setFieldIndexBy(String indexField);

    void setFieldPinyinIndexBy(String pinyin);
}
