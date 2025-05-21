Block 中

neighborUpdate是方块接收到NC更新后执行的操作

scheduledTick是执行计划刻的地方

prepare目前只有RedstoneWire实现了，用于发出PP更新

getStateForNeighborUpdate 用于执行接收到PP更新



World中

setBlockstate:

> if ((flags & Block.FORCE_STATE) == 0 && maxUpdateDepth > 0) :
>
> blockState.prepare和state.prepare用于RedstoneWire发出PP更新，其他Block不起作用
>
> state.updateNeighbors 用于除了RedstoneWire其他方块发出PP更新
>
> 最终调用的是ChainRestrictedNeighborUpdater的replaceWithStateForNeighborUpdate
