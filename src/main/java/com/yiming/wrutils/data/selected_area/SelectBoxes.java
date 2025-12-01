package com.yiming.wrutils.data.selected_area;

import com.yiming.wrutils.data.Dimension;

import java.util.ArrayList;
import java.util.HashSet;

public class SelectBoxes {
    private ArrayList<SelectBox> selectBoxList = new ArrayList<>();
    private SelectBox currentSelectBox;
    private String selectBoxesName = "Area-0";

    public void addAndSetCurrent(SelectBox selectBox) {
        this.add(selectBox);
        this.currentSelectBox = selectBox;
    }

    public void add(SelectBox selectBox) {
        this.selectBoxList.add(selectBox);
    }

    public SelectBox get(int index) {
        return this.selectBoxList.get(index);
    }

    public void remove(SelectBox selectBox) {
        this.selectBoxList.remove(selectBox);
        if (this.currentSelectBox == selectBox) {
            this.currentSelectBox = null;
        }
    }

    public SelectBox getCurrentSelectBox() {
        return this.currentSelectBox;
    }

    public void setCurrentSelectBox(SelectBox box) {
        this.currentSelectBox = box;
    }

    public ArrayList<SelectBox> getList() {
        return this.selectBoxList;
    }

    public ArrayList<SelectBox> getList(Dimension dimension) {
        // 返回指定维度的SelectBox
        ArrayList<SelectBox> list = new ArrayList<>();
        this.selectBoxList.stream().filter(box -> box.getDimension() == dimension).forEach(list::add);
        return list;
    }


    public String getName() {
        return this.selectBoxesName;
    }

    public HashSet<String> getSelectBoxListNames() {
        HashSet<String> nameSet = new HashSet<>();
        for (SelectBox box : this.selectBoxList) {
            nameSet.add(box.getName());
        }
        return nameSet;
    }

    public void setName(String name) {
        this.selectBoxesName = name;
    }

}
