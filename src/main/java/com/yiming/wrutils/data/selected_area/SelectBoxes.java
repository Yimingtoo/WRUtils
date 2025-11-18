package com.yiming.wrutils.data.selected_area;

import javax.print.DocFlavor;
import java.util.ArrayList;

public class SelectBoxes {
    private ArrayList<SelectBox> selectBoxList = new ArrayList<>();
    private SelectBox currentSelectBox;

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

    public SelectBox getCurrentBox() {
        return this.currentSelectBox;
    }

    public void setCurrentSelectBox(SelectBox box) {
        this.currentSelectBox = box;
    }

    public ArrayList<SelectBox> getList() {
        return this.selectBoxList;
    }

}
