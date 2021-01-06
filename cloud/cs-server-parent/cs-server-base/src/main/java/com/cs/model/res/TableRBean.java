package com.cs.model.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TableRBean {
    private int total;
    private List<Object> rows;
    public TableRBean setTotal(int total){
        this.total = total;
        return this;
    }
    public TableRBean setRows(List<Object> rows){
        this.rows = rows;
        return this;
    }
}
