/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.lang.StringSerializable;

/**
 * 数据库信息
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-8 下午11:57:24
 */
public class Database extends StringSerializable {

    private static final long serialVersionUID = -58860157407942790L;

    /**
     * 数据库产品，如：MYSQL
     */
    private String productName;

    /**
     * 数据库名，如：beidou
     */
    private String name;

    /**
     * 数据库产品版本号
     */
    private String version;

    /**
     * JDBC地址
     */
    private String url;

    /**
     * {@link #name}下需要处理的表
     */
    private List<Table> primaryTables = CollectionUtil.createArrayList();

    /**
     * 无主键表
     */
    private List<Table> noPrimaryTables = CollectionUtil.createArrayList();

    /**
     * 复合主键表
     */
    private List<Table> complexTables = CollectionUtil.createArrayList();

    /**
     * {@link #name}所有表名与表的字典
     */
    private Map<String, Table> tableMap = CollectionUtil.createHashMap();

    public Database() {

    }

    public Database(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Table getTable(String name) {
        return tableMap.get(name);
    }

    /**
     * 设置表信息 @see Table
     * 
     * @param tables 表列表
     */
    public void setTables(List<Table> tables) {
        if (CollectionUtil.isEmpty(tables)) {
            clear();
            return;
        }

        reset(tables);
    }

    /**
     * 重置表信息 @see Table
     * 
     * @param tables 表列表
     */
    private void reset(List<Table> tables) {
        List<Table> tableList = CollectionUtil.createArrayList(tables);
        Map<String, Table> tableMap = CollectionUtil.createHashMap();
        List<Table> complexTables = CollectionUtil.createArrayList();
        List<Table> noPrimaryTables = CollectionUtil.createArrayList();
        for (Table table : tables) {
            tableMap.put(table.getName(), table);
            if (table.isComplexPrimary()) {
                complexTables.add(table);
            } else if (!table.hasPrimary()) {
                noPrimaryTables.add(table);
            }
        }

        this.tableMap = tableMap;
        tableList.removeAll(complexTables);
        tableList.removeAll(noPrimaryTables);
        this.primaryTables = tableList;
        this.complexTables = complexTables;
        this.noPrimaryTables = noPrimaryTables;
    }

    /**
     * 清除
     */
    private void clear() {
        tableMap.clear();
        primaryTables.clear();
        complexTables.clear();
        noPrimaryTables.clear();
    }

    public Collection<Table> getAllTables() {
        return tableMap.values();
    }

    public List<Table> getNoPrimaryTables() {
        return noPrimaryTables;
    }

    public List<Table> getComplexTables() {
        return complexTables;
    }

    public List<Table> getPrimaryTables() {
        return primaryTables;
    }

    /**
     * 是否存在复合主键的表
     * 
     * @return 是否存在复合主键的表
     */
    public boolean hasComplexTables() {
        return CollectionUtil.isNotEmpty(complexTables);
    }

    /**
     * 是否存在无主键的表
     * 
     * @return 是否存在无主键的表
     */
    public boolean hasNoPrimaryTables() {
        return CollectionUtil.isNotEmpty(noPrimaryTables);
    }

    public boolean contains(Table table) {
        return tableMap.containsKey(table.getName());
    }

}
