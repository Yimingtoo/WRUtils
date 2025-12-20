package com.yiming.wrutils.client.hud;

import com.yiming.wrutils.client.utils.WrutilsColor;
import com.yiming.wrutils.data.event.*;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

public class EventInfoHud extends InfoHud {
    protected BaseEvent baseEvent;

    public EventInfoHud(BaseEvent baseEvent) {
        super();
        this.baseEvent = baseEvent;
        this.addMessage();
    }

    public void addMessage() {
        TimeStamp stamp = baseEvent.getTimeStamp();
        int y = 0;
        this.messages.add(new Message("GameTick: %d - %d  %s".formatted(stamp.gameTime(), stamp.eventId(), stamp.sequence().getAbbr()), 0, y, WrutilsColor.GREEN));
        y += 10;

        BlockInfo targetBlockInfo = baseEvent.getTargetBlockInfo();
        y = this.addBlockInfoMessage(targetBlockInfo, "Target", y);
        BlockInfo sourceBlockInfo = baseEvent.getSourceBlockInfo();
        y = this.addBlockInfoMessage(sourceBlockInfo, "Source", y);
        if (this.baseEvent instanceof ScheduledTickInfo info) {
            this.messages.add(new Message("  Delay: %d".formatted(info.getDelay()), 0, y, WrutilsColor.WHITE));
            y += 10;
            this.messages.add(new Message("  Priority: %s".formatted(info.getPriority()), 0, y, WrutilsColor.WHITE));
            y += 10;
            if (baseEvent instanceof ScheduledTickAddEvent event) {
                this.messages.add(new Message("  Is Added: %s".formatted(event.getIsAdded()), 0, y, WrutilsColor.WHITE));
                y += 10;
            }
            this.messages.add(new Message("ScheduledTick:", 0, y, WrutilsColor.WHITE));
            y += 10;
        }
        this.messages.add(new Message("Event Type: %s".formatted(baseEvent.getEventType().getName()), 0, y, WrutilsColor.WHITE));
        y += 10;
    }

    private int addBlockInfoMessage(BlockInfo blockInfo, String type, int y) {
        if (blockInfo != null) {
            BlockState state = blockInfo.state();
            if (state != null) {
                boolean bl1 = state.getEntries().containsKey(Properties.POWERED);
                boolean bl2 = state.getEntries().containsKey(Properties.POWER);
                if (bl1 || bl2) {
                    String text = type + " State: ";
                    if (bl1) {
                        boolean powered = (Boolean) state.getEntries().get(Properties.POWERED);
                        text += powered ? " Powered" : " Not Powered";
                    }
                    if (bl2) {
                        int power = (Integer) state.getEntries().get(Properties.POWER);
                        if (!bl1) {
                            text += power != 0 ? " Powered" : " Not Powered";
                        }
                        text += " (%d)".formatted(power);
                    }
                    this.messages.add(new Message(text, 0, y, WrutilsColor.WHITE));
                    y += 10;
                }
                this.messages.add(new Message(type + " Block: %s".formatted(state.getBlock().getName().getString()), 0, y, WrutilsColor.WHITE));
                y += 10;

            }
        }
        return y;
    }
}
