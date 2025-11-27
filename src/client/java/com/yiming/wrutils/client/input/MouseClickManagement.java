package com.yiming.wrutils.client.input;

import com.yiming.wrutils.client.Notification;
import com.yiming.wrutils.client.WrutilsClient;
import com.yiming.wrutils.client.WrutilsClientUtils;
import com.yiming.wrutils.client.utils.GeometryUtil;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

@Deprecated
public class MouseClickManagement {
    public static MouseState wasLeftPressedLastTick = new MouseState(GLFW.GLFW_MOUSE_BUTTON_LEFT);
    public static MouseState wasRightPressedLastTick = new MouseState(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
    public static MouseState wasMiddlePressedLastTick = new MouseState(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);

    public static void clickEvent(MinecraftClient client) {
        if (client.currentScreen == null && client.isWindowFocused()) {
            long window = client.getWindow().getHandle();
            checkClick(client, window, wasLeftPressedLastTick, () -> leftClickDown1(client), null);
            checkClick(client, window, wasRightPressedLastTick, () -> rightClickDown1(client), null);
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


    private static void leftClickDown1(MinecraftClient client) {
        if (client.player.getMainHandStack().getItem() == WrutilsClient.debugItem) {
            // 按下的是木剑
            if (DataManager.getCurrentSelectBox() != null) {
                BlockPos pos = WrutilsClientUtils.getLookingBlockPosClient(client, 128, 1);
                if (pos != null) {
                    DataManager.getCurrentSelectBox().setPos2(pos);
                    DataManager.getCurrentSelectBox().setMoveCtrlPos(pos);

                }
            }

            Notification.addNotification("方块已经添加" + Notification.cnt++, 1000);
        }
    }


    private static void rightClickDown1(MinecraftClient client) {
        if (client.player.getMainHandStack().getItem() == WrutilsClient.debugItem) {
            // 按下的是木剑
            if (DataManager.getCurrentSelectBox() != null) {
                BlockPos pos = WrutilsClientUtils.getLookingBlockPosClient(client, 128, 1);
                if (pos != null) {
                    DataManager.getCurrentSelectBox().setPos1(pos);
                    DataManager.getCurrentSelectBox().setMoveCtrlPos(pos);
                }
            }
        }
    }


    private static void middleClickDown(MinecraftClient client) {
        if (client.player.getMainHandStack().getItem() == WrutilsClient.debugItem) {
            // 按下的是木剑
            SelectBoxes boxes = DataManager.getCurrentBoxes();
            if (boxes == null) {
                return;
            }

            Vec3d camPos = client.gameRenderer.getCamera().getPos();
            Vec3d camDir = client.player.getRotationVec(1.0F);

            double distanceToBlock = -1;
            BlockPos pos = WrutilsClientUtils.getLookingBlockPosClient(client, 128, 1);
            if (pos != null) {
                double distance1 = GeometryUtil.intersectLineWithBox(camPos, camDir, new Vec3d(pos), new Vec3d(pos).add(1, 1, 1));
                if (distance1 >= 0) {
                    distanceToBlock = distance1;
                }
            }

            double distanceToBox = -1;
            SelectBox beingSelectedBox = null;
            for (SelectBox box : boxes.getList()) {
                double distance1 = GeometryUtil.intersectLineWithBox(camPos, camDir, box.getMinBorderPos(), box.getMaxBorderPos());
                if (distance1 >= 0) {
                    if (distanceToBox < 0 || distance1 < distanceToBox) {
                        distanceToBox = distance1;
                        beingSelectedBox = box;
                    }
                }
            }

            if (beingSelectedBox != null) {
                if ((distanceToBlock >= 0 && distanceToBlock >= distanceToBox) || (distanceToBlock < 0)) {
                    boxes.setCurrentSelectBox(beingSelectedBox);
                    Notification.addNotification(beingSelectedBox.getName() + "已经选中", 1000);

                    ArrayList<Vec3i> cubePoses = SelectBox.CornerDirection.getCornerCubePoses(beingSelectedBox);
                    Vec3i targetPos = null;
                    double distanceToCube = -1;
                    for (Vec3i cubePos : cubePoses) {
                        double distance1 = GeometryUtil.intersectLineWithBox(camPos, camDir, new Vec3d(cubePos), new Vec3d(cubePos).add(1, 1, 1));
                        if (distance1 >= 0) {
                            if (distanceToCube < 0 || distance1 < distanceToCube) {
                                distanceToCube = distance1;
                                targetPos = cubePos;
                            }
                        }
                    }

                    if (distanceToBlock >= 0 && distanceToCube >= 0 && distanceToBlock >= distanceToCube || distanceToBlock < 0) {
                        beingSelectedBox.setMoveCtrlPos(targetPos);
                    } else {
                        beingSelectedBox.setMoveCtrlPos(null);
                    }


                } else {
                    boxes.setCurrentSelectBox(null);
                    Notification.addNotification("取消选中", 1000);
                }


            } else {
                Vec3i targetPos = null;
                double distanceToCube = -1;
                for (SelectBox box : boxes.getList()) {
                    if (box.isContainVec3dPos(camPos)) {
                        ArrayList<Vec3i> cubePoses = SelectBox.CornerDirection.getCornerCubePoses(box);
                        for (Vec3i cubePos : cubePoses) {
                            double distance1 = GeometryUtil.intersectLineWithBox(camPos, camDir, new Vec3d(cubePos), new Vec3d(cubePos).add(1, 1, 1));
                            if (distance1 >= 0) {
                                if (distanceToCube < 0 || distance1 < distanceToCube) {
                                    distanceToCube = distance1;
                                    targetPos = cubePos;
                                    beingSelectedBox = box;
                                }
                            }
                        }
                    }

                }
                if (distanceToBlock >= 0 && distanceToCube >= 0 && distanceToBlock >= distanceToCube || distanceToBlock < 0 && distanceToCube > 0) {
                    boxes.setCurrentSelectBox(beingSelectedBox);
                    beingSelectedBox.setMoveCtrlPos(targetPos);
                } else {
                    boxes.setCurrentSelectBox(null);
                    Notification.addNotification("取消选中", 1000);
                }

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
