package com.yiming.wrutils.data.selected_area;

import java.util.ArrayList;

public class SelectBoxes {
    private ArrayList<SelectBox> selectBoxList = new ArrayList<>();
    private SelectBox currentSelectBox;
    private String selectBoxesName;

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


    public String getName() {
        return this.selectBoxesName;
    }

    public void setName(String name) {
        this.selectBoxesName = name;
    }

}
