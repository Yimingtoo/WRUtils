package com.yiming.wrutils.data.selected_area;

import java.util.ArrayList;

public class SelectedAreaManagement {

    private ArrayList<SelectBoxes> selectBoxesList = new ArrayList<>();
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

    public SelectBoxes get(int index) {
        return selectBoxesList.get(index);
    }

    public void remove(int index) {
        selectBoxesList.remove(index);
    }

}
