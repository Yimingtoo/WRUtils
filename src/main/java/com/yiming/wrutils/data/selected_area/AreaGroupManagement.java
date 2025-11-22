package com.yiming.wrutils.data.selected_area;

import java.util.ArrayList;
import java.util.HashSet;

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

    public HashSet<String> getSelectBoxesListNames() {
        HashSet<String> nameSet = new HashSet<>();
        for (SelectBoxes boxes : this.selectBoxesList) {
            nameSet.add(boxes.getName());
        }
        return nameSet;
    }

    public ArrayList<SelectBoxes> getList() {
        return this.selectBoxesList;
    }


}
