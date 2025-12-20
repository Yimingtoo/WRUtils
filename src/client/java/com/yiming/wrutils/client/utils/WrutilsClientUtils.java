package com.yiming.wrutils.client.utils;

import com.yiming.wrutils.client.data.DataManagerClient;
import com.yiming.wrutils.client.hud.Notification;
import com.yiming.wrutils.data.DataManager;
import com.yiming.wrutils.data.Dimension;
import com.yiming.wrutils.data.selected_area.SelectBox;
import com.yiming.wrutils.data.selected_area.SelectBoxes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;

public class WrutilsClientUtils {
    public static BlockPos getLookingBlockPosClient(MinecraftClient client, double maxDistance, float tickDelta) {

        if (client.player == null) return null;

        HitResult hit = client.player.raycast(maxDistance, tickDelta, false); // false = 不命中流体
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) hit;
            return bhr.getBlockPos(); // 最近的非空气方块位置
        }
        return null; // 没有命中方块
    }

    public static void setSelectBoxCorner(MinecraftClient client, SelectBox.SelectedCorner corner) {
        if (!DataManager.isSelectionEnabled && DataManager.isRecording) {
            return;
        }
        SelectBox selectBox = DataManager.getCurrentSelectBox();
        if (selectBox == null) {
            return;
        }
        if (selectBox.getDimension() != getPlayerDimension()) {
//            System.out.println("当前维度与玩家维度不一致");
            return;
        }

        BlockPos pos = WrutilsClientUtils.getLookingBlockPosClient(client, 128, 1);
        if (pos != null) {
            switch (corner) {
                case SelectBox.SelectedCorner.CORNER_1:
                    selectBox.setPos1(pos);
                    break;
                case SelectBox.SelectedCorner.CORNER_2:
                    selectBox.setPos2(pos);
                    break;
            }
            selectBox.setMoveCtrlPos(pos);
        }
    }

    public static void selectElement(MinecraftClient client) {
        if (!DataManager.isSelectionEnabled && DataManager.isRecording) {
            return;
        }
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
        for (SelectBox box : boxes.getList(getPlayerDimension())) {
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
                    boolean bl1 = targetPos != null;
                    boolean bl2 = beingSelectedBox.getMoveCtrlPos() != null;
                    if (bl1 && bl2) {
                        boolean isCurrent = beingSelectedBox.getMoveCtrlPos().equals(targetPos);
                        beingSelectedBox.setMoveCtrlPos(isCurrent ? null : targetPos);
                    } else {
                        beingSelectedBox.setMoveCtrlPos(targetPos);
                    }

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
            for (SelectBox box : boxes.getList(getPlayerDimension())) {
                if (box.containsVec3dPos(camPos)) {
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
//                boolean bl1 = targetPos != null;
                boolean bl2 = beingSelectedBox.getMoveCtrlPos() != null;
                if (bl2) {
                    boolean isCurrent = beingSelectedBox.getMoveCtrlPos().equals(targetPos);
                    beingSelectedBox.setMoveCtrlPos(isCurrent ? null : targetPos);
                } else {
                    beingSelectedBox.setMoveCtrlPos(targetPos);
                }
            } else {
                boxes.setCurrentSelectBox(null);
                Notification.addNotification("取消选中", 1000);
            }

        }

    }

    public static Dimension getPlayerDimension() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) {
            return Dimension.NONE;
        }

        RegistryKey<World> dimensionKey = client.player.getWorld().getRegistryKey();
        Identifier dimensionId = dimensionKey.getValue();

        return switch (dimensionId.toString()) {
            case "minecraft:the_nether" -> Dimension.NETHER;
            case "minecraft:overworld" -> Dimension.OVERWORLD;
            case "minecraft:the_end" -> Dimension.END;
            default -> Dimension.NONE;
        };
    }

    public static boolean isPlayerHoldingRequiredItem() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && client.player.getMainHandStack().getItem() == DataManagerClient.debugItem;
    }
}
