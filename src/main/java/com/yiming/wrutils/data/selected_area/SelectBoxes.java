package com.yiming.wrutils.data.selected_area;

import javax.print.DocFlavor;
import java.util.ArrayList;

public class SelectBoxes {
    private ArrayList<SelectBox> selectBoxList = new ArrayList<>();
    private SelectBox currentSelectBox;

    public void addAndSetCurrent(SelectBox selectBox) {
        this.add(selectBox);
        currentSelectBox = selectBox;
    }

    public void add(SelectBox selectBox) {
        selectBoxList.add(selectBox);
    }

    public SelectBox get(int index) {
        return selectBoxList.get(index);
    }

    public SelectBox getCurrentBox() {
        return currentSelectBox;
    }

    public ArrayList<SelectBox> getList() {
        return selectBoxList;
    }

}
