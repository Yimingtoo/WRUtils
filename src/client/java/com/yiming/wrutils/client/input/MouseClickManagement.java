package com.yiming.wrutils.client.input;

import com.yiming.wrutils.Wrutils;
import com.yiming.wrutils.client.Notification;
import com.yiming.wrutils.client.WrutilsClientUtils;
import com.yiming.wrutils.client.utils.GeometryUtil;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class MouseClickManagement {
    public static MouseState wasLeftPressedLastTick = new MouseState(GLFW.GLFW_MOUSE_BUTTON_LEFT);
    public static MouseState wasRightPressedLastTick = new MouseState(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    public static MouseState wasMiddlePressedLastTick = new MouseState(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);

    public static void clickEvent(MinecraftClient client) {
        if (client.currentScreen == null) {
            long window = client.getWindow().getHandle();
            checkClick(client, window, wasLeftPressedLastTick, () -> leftClickDown(client), null);
            checkClick(client, window, wasRightPressedLastTick, () -> rightClickDown(client), null);
            checkClick(client, window, wasMiddlePressedLastTick, () -> middleClickDown(client), null);
        }
    }


    public static void checkClick(MinecraftClient client, long window, MouseState mouseState, @Nullable Runnable pressedAction, @Nullable Runnable releasedAction) {
        boolean isPressedNow = GLFW.glfwGetMouseButton(window, mouseState.getButton()) == GLFW.GLFW_PRESS;
        if (isPressedNow && !mouseState.isPressed()) {
            // 按下一次
            if (pressedAction != null) {
                pressedAction.run();
            }
        }
        if (!isPressedNow && mouseState.isPressed()) {
            // 抬起一次
            if (releasedAction != null) {
                releasedAction.run();
            }
        }
        mouseState.setPressed(isPressedNow);
    }


    private static void leftClickDown(MinecraftClient client) {
        if (client.player.getMainHandStack().getItem() == Items.WOODEN_SWORD) {
            // 按下的是木剑
            if (Wrutils.selectedAreaManagement.getCurrentBoxes().getCurrentSelectBox() != null) {
                BlockPos pos = WrutilsClientUtils.getLookingBlockPosClient(client, 128, 1);
                if (pos != null) {
                    Wrutils.selectedAreaManagement.getCurrentBoxes().getCurrentSelectBox().setPos2(pos);
                }
            }

            Notification.addNotification("方块已经添加" + Notification.cnt++, 1000);
        }
    }

    private static void rightClickDown(MinecraftClient client) {
        if (client.player.getMainHandStack().getItem() == Items.WOODEN_SWORD) {
            // 按下的是木剑
            if (Wrutils.selectedAreaManagement.getCurrentBoxes().getCurrentSelectBox() != null) {
                BlockPos pos = WrutilsClientUtils.getLookingBlockPosClient(client, 128, 1);
                if (pos != null) {
                    Wrutils.selectedAreaManagement.getCurrentBoxes().getCurrentSelectBox().setPos1(pos);
                }
            }
        }
    }


    private static void middleClickDown(MinecraftClient client) {
        if (client.player.getMainHandStack().getItem() == Items.WOODEN_SWORD) {
            // 按下的是木剑
            System.out.println("Middle click");
            Vec3d camPos = client.gameRenderer.getCamera().getPos();
            Vec3d camDir = client.player.getRotationVec(1.0F);
            SelectBoxes boxes = Wrutils.selectedAreaManagement.getCurrentBoxes();

            double distanceToBlock = -1;
            BlockPos pos = WrutilsClientUtils.getLookingBlockPosClient(client, 128, 1);
            if (pos != null) {
                ArrayList<GeometryUtil.Plane> planes = GeometryUtil.getPlanes(camPos, new Vec3d(pos), new Vec3d(pos).add(1, 1, 1));
                for (GeometryUtil.Plane plane : planes) {
                    double distance1 = GeometryUtil.intersectLineWithRectangle(camPos, camDir, plane, 128);
                    if (distance1 > 0) {
                        if (distanceToBlock < 0 || distance1 < distanceToBlock) {
                            distanceToBlock = distance1;
                        }
                    }
                }
            }

            double distanceToBox = -1;
            SelectBox beingSelectedBox = null;
            for (SelectBox box : boxes.getList()) {
                ArrayList<GeometryUtil.Plane> planes = GeometryUtil.getPlanes(camPos, box.getMinBorderPos(), box.getMaxBorderPos());
                for (GeometryUtil.Plane plane : planes) {
                    double distance1 = GeometryUtil.intersectLineWithRectangle(camPos, camDir, plane, 128);
                    if (distance1 > 0) {
                        if (distanceToBox < 0 || distance1 < distanceToBox) {
                            distanceToBox = distance1;
                            beingSelectedBox = box;
                        }
                    }
                }
            }

            if ((distanceToBlock >= 0 && distanceToBox >= 0 && distanceToBlock >= distanceToBox) || (distanceToBlock < 0 && distanceToBox >= 0)) {
                boxes.setCurrentSelectBox(beingSelectedBox);
                if (beingSelectedBox != null)
                    Notification.addNotification(beingSelectedBox.getName() + "已经选中", 1000);
                else
                    Notification.addNotification("取消选中", 1000);
            } else {
                boxes.setCurrentSelectBox(null);
                Notification.addNotification("取消选中", 1000);

            }


        }
    }


    public static class MouseState {
        private boolean isPressed = false;
        private int button;

        public MouseState(int button) {
            this.button = button;
        }

        public int getButton() {
            return button;
        }

        public boolean isPressed() {
            return isPressed;
        }

        public void setPressed(boolean pressed) {
            isPressed = pressed;
        }


    }

}
