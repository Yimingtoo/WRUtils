package com.yiming.wrutils.data.selected_area;

import java.util.ArrayList;

public class AreaGroupManagement {

    private final ArrayList<SelectBoxes> selectBoxesList = new ArrayList<>();
    public SelectBoxes currentSelectBoxes;


    public void addAndSetCurrent(SelectBoxes selectBoxes) {
        this.add(selectBoxes);
        currentSelectBoxes = selectBoxes;
    }

    public void add(SelectBoxes selectBoxes) {
        selectBoxesList.add(selectBoxes);
    }

    public SelectBoxes getCurrentBoxes() {
        return currentSelectBoxes;
    }

    public void setCurrentSelectBox(SelectBoxes boxes) {
        currentSelectBoxes = boxes;
    }

    public SelectBoxes get(int index) {
        return selectBoxesList.get(index);
    }

    public void remove(SelectBoxes selectBoxes) {
        selectBoxesList.remove(selectBoxes);
        if (this.currentSelectBoxes == selectBoxes) {
            this.currentSelectBoxes = null;
        }
    }

    public ArrayList<SelectBoxes> getList() {
        return this.selectBoxesList;
    }


}
