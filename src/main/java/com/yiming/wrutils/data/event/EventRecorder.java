package com.yiming.wrutils.data.event;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class EventRecorder {

    ArrayList<BaseEvent> eventList = new ArrayList<>();

    public void addEvent(BaseEvent event) {
        eventList.add(event);
    }

    public void printEvents() {
        for (BaseEvent event : eventList) {

            System.out.println(event.toString());
        }
        for (BlockPos pos : BaseEvent.BLOCK_POS_STACK) {
            System.out.println(pos);
        }

    }


//    public void processEvents() {
//        eventList.add(new SimpleEvent(gameTime, microTimingSequence, sourcePos));
//
//        // 通过类型转换访问SimpleEvent特有功能
//        for (
//                BaseEvent event : eventList) {
//            if (event instanceof SimpleEvent) {
//                SimpleEvent simpleEvent = (SimpleEvent) event;
//                // 现在可以访问SimpleEvent的特有数据和方法
//            }
//        }
//    }


}
