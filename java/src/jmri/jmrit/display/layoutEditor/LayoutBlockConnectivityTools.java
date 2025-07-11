package jmri.jmrit.display.layoutEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import jmri.*;
import jmri.jmrit.display.EditorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * These are a series of layout block connectivity tools that can be used when
 * the advanced layout block routing has been enabled. These tools can determine
 * if a path from a source to destination bean is valid. If a route between two
 * layout blocks is usable and free.
 *
 * @author Kevin Dickerson Copyright (C) 2011
 * @author George Warner Copyright (c) 2017-2018
 */
final public class LayoutBlockConnectivityTools {

    public LayoutBlockConnectivityTools() {
    }

    public enum Routing {
        /**
         * Constant used in the getLayoutBlocks to represent a path from one Signal
         * Mast to another and that no mast should be in the path.
         */
        MASTTOMAST,

        /**
         * Constant used in the getLayoutBlocks to represent a path from one Signal
         * Head to another and that no head should be in the path.
         */
        HEADTOHEAD,

        /**
         * Constant used in the getLayoutBlocks to represent a path from one Sensor
         * to another and that no sensor should be in the path.
         */
        SENSORTOSENSOR,

        /**
         * Constant used in the getLayoutBlocks to represent a path from either a
         * Signal Mast or Head to another Signal Mast or Head and that no mast of
         * head should be in the path.
         */
        ANY,

        /**
         * Constant used in the getLayoutBlocks to indicate that the system
         * should not check for signal masts or heads on the path.
         */
        NONE
    }

    public enum Metric {
        HOPCOUNT,
        METRIC,
        DISTANCE
    }
    
    private static final int ttlSize = 50;
    

    /**
     * Determines if a pair of NamedBeans (Signalhead, Signalmast or Sensor)
     * assigned to a block boundary are reachable.<br>
     * Called by {@link jmri.jmrit.signalling.SignallingPanel} using MASTTOMAST.
     * <p>
     * Search all of the layout editor panels to find the facing and protecting
     * layout blocks for each bean.  Call the 3 block+list version of checkValidDest() to finish the checks.
     *
     * @param sourceBean The source bean.
     * @param destBean   The destination bean.
     * @param pathMethod Indicates the type of path:  Signal head, signal mast or sensor.
     * @return true if source and destination beans are reachable.
     * @throws jmri.JmriException if no blocks can be found that related to the
     *                            named beans.
     */
    public boolean checkValidDest(NamedBean sourceBean, NamedBean destBean, Routing pathMethod) throws jmri.JmriException {
        if (log.isDebugEnabled()) {
            log.debug("checkValidDest with source/dest bean {} {}", sourceBean.getDisplayName(), destBean.getDisplayName());
        }
        LayoutBlock facingBlock = null;
        LayoutBlock protectingBlock = null;
        LayoutBlock destFacingBlock = null;
        List<LayoutBlock> destProtectBlock = null;
        Set<LayoutEditor> layout = InstanceManager.getDefault(EditorManager.class).getAll(LayoutEditor.class);
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        for (LayoutEditor layoutEditor : layout) {
            if (log.isDebugEnabled()) {
                log.debug("Layout name {}", layoutEditor.getLayoutName());
            }
            if (facingBlock == null) {
                facingBlock = lbm.getFacingBlockByNamedBean(sourceBean, layoutEditor);
            }
            if (protectingBlock == null) {
                protectingBlock = lbm.getProtectedBlockByNamedBean(sourceBean, layoutEditor);
            }
            if (destFacingBlock == null) {
                destFacingBlock = lbm.getFacingBlockByNamedBean(destBean, layoutEditor);
            }
            if (destProtectBlock == null) {
                destProtectBlock = lbm.getProtectingBlocksByNamedBean(destBean, layoutEditor);
            }
            if ((destFacingBlock != null) && (facingBlock != null) && (protectingBlock != null)) {
                /*Destination protecting block list is allowed to be empty, as the destination signalmast
                 could be assigned to an end bumper */
                // A simple to check to see if the remote signal/sensor is in the correct direction to ours.
                try {
                    return checkValidDest(facingBlock, protectingBlock, destFacingBlock, destProtectBlock, pathMethod);
                } catch (JmriException e) {
                    log.debug("Rethrowing exception from checkValidDest(..)");
                    throw e;
                }
            } else {
                log.debug("blocks not found");
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("No valid route from {} to {}", sourceBean.getDisplayName(), destBean.getDisplayName());
        }
        throw new jmri.JmriException("Blocks Not Found");
    }

    /**
     * The is used in conjunction with the layout block routing protocol, to
     * discover a clear path from a source layout block through to a destination
     * layout block. By specifying the sourceLayoutBlock and
     * protectingLayoutBlock or sourceLayoutBlock+1, a direction of travel can
     * then be determined, eg east to west, south to north etc.
     * @param sourceBean    The source bean (SignalHead, SignalMast or Sensor)
     *                     assigned to a block boundary that we are starting
     *                     from.
     * @param destBean      The destination bean.
     * @param validateOnly  When set false, the system will not use layout
     *                     blocks that are set as either reserved(useExtraColor
     *                     set) or occupied, if it finds any then it will try to
     *                     find an alternative path When set false, no block
     *                     state checking is performed.
     * @param pathMethod    Performs a check to see if any signal heads/masts
     *                     are in the path, if there are then the system will
     *                     try to find an alternative path. If set to NONE, then
     *                     no checking is performed.
     * @return an List of all the layoutblocks in the path.
     * @throws jmri.JmriException if it can not find a valid path or the routing
     *                            has not been enabled.
     */
    public List<LayoutBlock> getLayoutBlocks(NamedBean sourceBean, NamedBean destBean, boolean validateOnly, Routing pathMethod) throws jmri.JmriException {
        Set<LayoutEditor> layout = InstanceManager.getDefault(EditorManager.class).getAll(LayoutEditor.class);
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        LayoutBlock facingBlock = null;
        LayoutBlock protectingBlock = null;
        LayoutBlock destFacingBlock = null;
        for (LayoutEditor layoutEditor : layout) {
            if (log.isDebugEnabled()) {
                log.debug("Layout name {}", layoutEditor.getLayoutName());
            }
            if (facingBlock == null) {
                facingBlock = lbm.getFacingBlockByNamedBean(sourceBean, layoutEditor);
            }
            if (protectingBlock == null) {
                protectingBlock = lbm.getProtectedBlockByNamedBean(sourceBean, layoutEditor);
            }
            if (destFacingBlock == null) {
                destFacingBlock = lbm.getFacingBlockByNamedBean(destBean, layoutEditor);
            }
            if ((destFacingBlock != null) && (facingBlock != null) && (protectingBlock != null)) {
                try {
                    return getLayoutBlocks(facingBlock, destFacingBlock, protectingBlock, validateOnly, pathMethod);
                } catch (JmriException e) {
                    log.debug("Rethrowing exception from getLayoutBlocks()");
                    throw e;
                }
            } else {
                log.debug("blocks not found");
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("No valid route from {} to {}", sourceBean.getDisplayName(), destBean.getDisplayName());
        }
        throw new jmri.JmriException("Blocks Not Found");
    }

    /**
     * Returns a list of NamedBeans (Signalhead, Signalmast or Sensor) that are
     * assigned to block boundaries in a given list.
     *
     * @param blocklist The list of block in order that need to be checked.
     * @param panel     (Optional) panel that the blocks need to be checked
     *                  against
     * @param T         (Optional) the class that we want to check against,
     *                  either Sensor, SignalMast or SignalHead, set null will
     *                  return any.
     * @return the list of NamedBeans
     */
    public List<NamedBean> getBeansInPath(List<LayoutBlock> blocklist, LayoutEditor panel, Class<?> T) {
        List<NamedBean> beansInPath = new ArrayList<>();
        if (blocklist.size() >= 2) {
            LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
            for (int x = 1; x < blocklist.size(); x++) {
                LayoutBlock facingBlock = blocklist.get(x - 1);
                LayoutBlock protectingBlock = blocklist.get(x);
                NamedBean nb = null;
                if (T == null) {
                    nb = lbm.getFacingNamedBean(facingBlock.getBlock(), protectingBlock.getBlock(), panel);
                } else if (T.equals(jmri.SignalMast.class)) {
                    nb = lbm.getFacingSignalMast(facingBlock.getBlock(), protectingBlock.getBlock(), panel);
                } else if (T.equals(jmri.Sensor.class)) {
                    nb = lbm.getFacingSensor(facingBlock.getBlock(), protectingBlock.getBlock(), panel);
                } else if (T.equals(jmri.SignalHead.class)) {
                    nb = lbm.getFacingSignalHead(facingBlock.getBlock(), protectingBlock.getBlock());
                }
                if (nb != null) {
                    beansInPath.add(nb);
                }
            }
        }
        return beansInPath;
    }

    /**
     * Determines if one set of blocks is reachable from another set of blocks
     * based upon the directions of the set of blocks.
     * <ul>
     * <li>Called by {@link jmri.implementation.DefaultSignalMastLogic} using MASTTOMAST.</li>
     * <li>Called by {@link jmri.jmrit.entryexit.DestinationPoints} using SENSORTOSENSOR.</li>
     * <li>Called by {@link jmri.jmrit.entryexit.EntryExitPairs} using SENSORTOSENSOR.</li>
     * </ul>
     * Convert the destination protected block to an array list.
     * Call the 3 block+list version of checkValidDest() to finish the checks.
     * @param currentBlock The facing layout block for the source signal or sensor.
     * @param nextBlock    The protected layout block for the source signal or sensor.
     * @param destBlock    The facing layout block for the destination signal mast or sensor.
     * @param destProBlock The protected destination block.
     * @param pathMethod   Indicates the type of path:  Signal head, signal mast or sensor.
     * @return true if a path to the destination is valid.
     * @throws jmri.JmriException if any Block is null;
     */
    public boolean checkValidDest(LayoutBlock currentBlock, LayoutBlock nextBlock, LayoutBlock destBlock, LayoutBlock destProBlock, Routing pathMethod) throws jmri.JmriException {

        List<LayoutBlock> destList = new ArrayList<>();
        if (destProBlock != null) {
            destList.add(destProBlock);
        }
        try {
            return checkValidDest(currentBlock, nextBlock, destBlock, destList, pathMethod);
        } catch (jmri.JmriException e) {
            throw e;
        }

    }

    /**
     * Determines if one set of blocks is reachable from another set of blocks
     * based upon the directions of the set of blocks.
     * <p>
     * This is used to help with identifying items such as signalmasts located
     * at positionable points or turnouts are facing in the same direction as
     * other given signalmasts.
     * <p>
     * Given the current block and the next block we can work out the direction
     * of travel. Given the destBlock and the next block on, we can determine
     * the whether the destBlock comes before the destBlock+1.
     * <p>
     * Note: This version is internally called by other versions that
     * pre-process external calls.
     * @param currentBlock The facing layout block for the source signal or
     *                     sensor.
     * @param nextBlock    The protected layout block for the source signal or
     *                     sensor.
     * @param destBlock    The facing layout block for the destination signal
     *                     mast or sensor.
     * @param destBlockn1  A list of protected destination blocks. Can be empty
     *                     if the destination is at an end bumper.
     * @param pathMethod   Indicates the type of path: Signal head, signal mast
     *                     or sensor.
     * @return true if a path to the destination is valid.
     * @throws jmri.JmriException if any layout block is null or advanced
     *                            routing is not enabled.
     */
    public boolean checkValidDest(LayoutBlock currentBlock, LayoutBlock nextBlock, LayoutBlock destBlock, List<LayoutBlock> destBlockn1, Routing pathMethod) throws jmri.JmriException {
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        if (!lbm.isAdvancedRoutingEnabled()) {
            log.debug("Advanced routing has not been enabled therefore we cannot use this function");
            throw new jmri.JmriException("Advanced routing has not been enabled therefore we cannot use this function");
        }

        if (log.isDebugEnabled()) {
            try {
                log.debug("faci {}", currentBlock.getDisplayName());
                log.debug("next {}", nextBlock.getDisplayName());
                log.debug("dest {}", destBlock.getDisplayName());
                for (LayoutBlock dp : destBlockn1) {
                    log.debug("dest + 1 {}", dp.getDisplayName());
                }
            } catch (java.lang.NullPointerException e) {

            }
        }
        if ((destBlock != null) && (currentBlock != null) && (nextBlock != null)) {
            if (!currentBlock.isRouteToDestValid(nextBlock.getBlock(), destBlock.getBlock())) {
                log.debug("Route to dest not valid");
                return false;
            }
            if (log.isDebugEnabled()) {
                log.debug("dest {}", destBlock.getDisplayName());
                /*if(destBlockn1!=null)
                 log.debug("remote prot " + destBlockn1.getDisplayName());*/
            }
            if (!destBlockn1.isEmpty() && currentBlock == destBlockn1.get(0) && nextBlock == destBlock) {
                log.debug("Our dest protecting block is our current block and our protecting block is the same as our destination block");
                return false;
            }
            // Do a simple test to see if one is reachable from the other.
            int proCount = 0;
            int desCount = 0;
            if (!destBlockn1.isEmpty()) {
                desCount = currentBlock.getBlockHopCount(destBlock.getBlock(), nextBlock.getBlock());
                proCount = currentBlock.getBlockHopCount(destBlockn1.get(0).getBlock(), nextBlock.getBlock());
                if (log.isDebugEnabled()) {
                    log.debug("dest {} protecting {}", desCount, proCount);
                }
            }

            if ((proCount == -1) && (desCount == -1)) {
                // The destination block and destBlock+1 are both directly connected
                log.debug("Dest and dest+1 are directly connected");
                return false;
            }

            if (proCount > desCount && (proCount - 1) == desCount) {
                // The block that we are protecting should be one hop greater than the destination count.
                log.debug("Protecting is one hop away from destination and therefore valid.");
                return true;
            }

            /*Need to do a more advanced check in this case as the destBlockn1
             could be reached via a different route and therefore have a smaller
             hop count we need to therefore step through each block until we reach
             the end.
             The advanced check also covers cases where the route table is inconsistent.
             We also need to perform a more advanced check if the destBlockn1
             is null as this indicates that the destination signal mast is assigned
             on an end bumper*/

            if (pathMethod == Routing.SENSORTOSENSOR && destBlockn1.size() == 0) {
                // Change the pathMethod to accept the NX sensor at the end bumper.
                pathMethod = Routing.NONE;
            }

            List<LayoutBlock> blockList = getLayoutBlocks(currentBlock, destBlock, nextBlock, true, pathMethod); // Was MASTTOMAST
            if (log.isDebugEnabled()) {
                log.debug("checkValidDest blockList for {}", destBlock.getDisplayName());
                blockList.forEach(blk -> log.debug("  block = {}", blk.getDisplayName()));
            }
            for (LayoutBlock dp : destBlockn1) {
                log.debug("dp = {}", dp.getDisplayName());
                if (blockList.contains(dp) && currentBlock != dp) {
                    log.debug("Signal mast in the wrong direction");
                    return false;
                }
            }
                /*Work on the basis that if you get the blocks from source to dest
                 then the dest+1 block should not be included*/
            log.debug("Signal mast in the correct direction");
            return true;

        } else if (destBlock == null) {
            throw new jmri.JmriException("Block in Destination Field returns as invalid");
        } else if (currentBlock == null) {
            throw new jmri.JmriException("Block in Facing Field returns as invalid");
        } else if (nextBlock == null) {
            throw new jmri.JmriException("Block in Protecting Field returns as invalid");
        }
        throw new jmri.JmriException("BlockIsNull");
    }

    /**
     * This uses the layout editor to check if the destination location is
     * reachable from the source location.<br>
     * Only used internally to the class.
     * <p>
     * @param facing     Layout Block that is considered our first block
     * @param protecting Layout Block that is considered first block +1
     * @param dest       Layout Block that we want to get to
     * @param pathMethod the path method
     * @return true if valid
     * @throws JmriException during nested getProtectingBlocks operation
     */
    private boolean checkValidDest(LayoutBlock facing, LayoutBlock protecting, FacingProtecting dest, Routing pathMethod) throws JmriException {
        if (facing == null || protecting == null || dest == null) {
            return false;
        }
        if (log.isDebugEnabled()) {
            log.debug("facing : {} protecting : {} dest {}", protecting.getDisplayName(), dest.getBean().getDisplayName(), facing.getDisplayName());
        }

        // In this instance it doesn't matter what the destination protecting block is so we get the first
        /*LayoutBlock destProt = null;
         if(!dest.getProtectingBlocks().isEmpty()){
         destProt = InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(dest.getProtectingBlocks().get(0));
         // log.info(dest.getProtectingBlocks());
         }*/
         
        List<LayoutBlock> destList = new ArrayList<>();

         // may throw JmriException here
        dest.getProtectingBlocks().forEach((b) -> {
            destList.add(InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(b));
        });
        return checkValidDest(facing, protecting, InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(dest.getFacing()), destList, pathMethod);
    }

    /**
     * This used in conjunction with the layout block routing protocol, to
     * discover a clear path from a source layout block through to a destination
     * layout block. By specifying the sourceLayoutBlock and
     * protectingLayoutBlock or sourceLayoutBlock+1, a direction of travel can
     * then be determined, eg east to west, south to north etc.
     *
     * @param sourceLayoutBlock      The layout block that we are starting from,
     *                               can also be considered as the block facing
     *                               a signal.
     * @param destinationLayoutBlock The layout block that we want to get to
     * @param protectingLayoutBlock  The next layout block connected to the
     *                               source block, this can also be considered
     *                               as the block being protected by a signal
     * @param validateOnly           When set false, the system will not use
     *                               layout blocks that are set as either
     *                               reserved(useExtraColor set) or occupied, if
     *                               it finds any then it will try to find an
     *                               alternative path When set true, no block
     *                               state checking is performed.
     * @param pathMethod             Performs a check to see if any signal
     *                               heads/masts are in the path, if there are
     *                               then the system will try to find an
     *                               alternative path. If set to NONE, then no
     *                               checking is performed.
     * @return an List of all the layoutblocks in the path.
     * @throws jmri.JmriException if it can not find a valid path or the routing
     *                            has not been enabled.
     */
    public List<LayoutBlock> getLayoutBlocks(LayoutBlock sourceLayoutBlock, LayoutBlock destinationLayoutBlock, LayoutBlock protectingLayoutBlock, boolean validateOnly, Routing pathMethod) throws jmri.JmriException {
        lastErrorMessage = "Unknown Error Occured";
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        
        if (!lbm.isAdvancedRoutingEnabled()) {
            log.debug("Advanced routing has not been enabled therefore we cannot use this function");
            throw new jmri.JmriException("Advanced routing has not been enabled therefore we cannot use this function");
        }

        int directionOfTravel = sourceLayoutBlock.getNeighbourDirection(protectingLayoutBlock);
        Block currentBlock = sourceLayoutBlock.getBlock();

        Block destBlock = destinationLayoutBlock.getBlock();
        log.debug("Destination Block {} {}", destinationLayoutBlock.getDisplayName(), destBlock);

        Block nextBlock = protectingLayoutBlock.getBlock();
        if (log.isDebugEnabled()) {
            log.debug("s:{} p:{} d:{}", sourceLayoutBlock.getDisplayName(), protectingLayoutBlock.getDisplayName(), destinationLayoutBlock.getDisplayName());
        }
        List<BlocksTested> blocksInRoute = new ArrayList<>();
        blocksInRoute.add(new BlocksTested(sourceLayoutBlock));

        if (!validateOnly) {
            if (canLBlockBeUsed(protectingLayoutBlock)) {
                blocksInRoute.add(new BlocksTested(protectingLayoutBlock));
            } else {
                lastErrorMessage = "Block we are protecting is already occupied or reserved";
                log.debug("will throw {}", lastErrorMessage);
                throw new jmri.JmriException(lastErrorMessage);
            }
            if (!canLBlockBeUsed(destinationLayoutBlock)) {
                lastErrorMessage = "Destination Block is already occupied or reserved";
                log.debug("will throw {}", lastErrorMessage);
                throw new jmri.JmriException(lastErrorMessage);
            }
        } else {
            blocksInRoute.add(new BlocksTested(protectingLayoutBlock));
        }
        if (destinationLayoutBlock == protectingLayoutBlock) {
            List<LayoutBlock> returnBlocks = new ArrayList<>();
            blocksInRoute.forEach((blocksTested) -> {
                returnBlocks.add(blocksTested.getBlock());
            });
            return returnBlocks;
        }

        BlocksTested bt = blocksInRoute.get(blocksInRoute.size() - 1);

        int ttl = 1;
        List<Integer> offSet = new ArrayList<>();
        while (ttl < ttlSize) { // value should be higher but low for test!
            log.debug("===== Ttl value = {} ======", ttl);
            log.debug("Looking for next block");
            int nextBlockIndex = findBestHop(currentBlock, nextBlock, destBlock, directionOfTravel, offSet, validateOnly, pathMethod);
            if (nextBlockIndex != -1) {
                bt.addIndex(nextBlockIndex);
                if (log.isDebugEnabled()) {
                    log.debug("block index returned {} Blocks in route size {}", nextBlockIndex, blocksInRoute.size());
                }
                // Sets the old next block to be our current block.
                LayoutBlock currentLBlock = InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(nextBlock);
                if (currentLBlock == null) {
                    log.error("Unable to get block :{}: from instancemanager", nextBlock);
                    continue;
                }
                offSet.clear();

                directionOfTravel = currentLBlock.getRouteDirectionAtIndex(nextBlockIndex);

                //allow for routes that contain more than one occurrence of a block in a route to allow change of direction.
                Block prevBlock = currentBlock;
                LayoutBlock prevLBlock = InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(prevBlock);
                currentBlock = nextBlock;
                nextBlock = currentLBlock.getRouteNextBlockAtIndex(nextBlockIndex);
                LayoutBlock nextLBlock = InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(nextBlock);
                if (log.isDebugEnabled()) {
                    log.debug("Blocks in route size {}", blocksInRoute.size());
                    log.debug("next: {} dest: {}", nextBlock.getDisplayName(), destBlock.getDisplayName());
                }
                if (nextBlock == currentBlock) {
                    nextBlock = currentLBlock.getRouteDestBlockAtIndex(nextBlockIndex);
                    log.debug("the next block to our destination we are looking for is directly connected to this one");
                } else if (!((protectingLayoutBlock == prevLBlock)&&(protectingLayoutBlock == nextLBlock))) {
                    if (nextLBlock != null) {
                        log.debug("Add block {}", nextLBlock.getDisplayName());
                    }
                    bt = new BlocksTested(nextLBlock);
                    blocksInRoute.add(bt);
                }
                if (nextBlock == destBlock) {
                    if (!validateOnly && !checkForLevelCrossing(destinationLayoutBlock)) {
                        throw new jmri.JmriException("Destination block is in conflict on a crossover");
                    }
                    List<LayoutBlock> returnBlocks = new ArrayList<>();
                    blocksInRoute.forEach((blocksTested) -> {
                        returnBlocks.add(blocksTested.getBlock());
                    });
                    returnBlocks.add(destinationLayoutBlock);
                    if (log.isDebugEnabled()) {
                        log.debug("Adding destination Block {}", destinationLayoutBlock.getDisplayName());
                        log.debug("arrived at destination block");
                        log.debug("{} Return as Long", sourceLayoutBlock.getDisplayName());
                        returnBlocks.forEach((returnBlock) -> {
                            log.debug("  return block {}", returnBlock.getDisplayName());
                        });
                        log.debug("Finished List");
                    }
                    return returnBlocks;
                }
            } else {
                //-1 is returned when there are no more valid besthop valids found
                // Block index is -1, so we need to go back a block and find another way.

                // So we have gone back as far as our starting block so we better return.
                int birSize = blocksInRoute.size();
                log.debug("block in route size {}", birSize);
                if (birSize <= 2) {
                    log.debug("drop out here with ttl");
                    ttl = ttlSize + 1;
                } else {
                    if (log.isDebugEnabled()) {
                        for (int t = 0; t < blocksInRoute.size(); t++) {
                            log.debug("index {} block {}", t, blocksInRoute.get(t).getBlock().getDisplayName());
                        }
                        log.debug("To remove last block {}", blocksInRoute.get(birSize - 1).getBlock().getDisplayName());
                    }

                    currentBlock = blocksInRoute.get(birSize - 3).getBlock().getBlock();
                    nextBlock = blocksInRoute.get(birSize - 2).getBlock().getBlock();
                    offSet = blocksInRoute.get(birSize - 2).getTestedIndexes();
                    bt = blocksInRoute.get(birSize - 2);
                    blocksInRoute.remove(birSize - 1);
                    ttl--;
                }
            }
            ttl++;
        }
        if (ttl == ttlSize) {
            lastErrorMessage = "ttlExpired";
        }
        // we exited the loop without either finding the destination or we had error.
        throw new jmri.JmriException(lastErrorMessage);
    }

    static class BlocksTested {

        LayoutBlock block;
        List<Integer> indexNumber = new ArrayList<>();

        BlocksTested(LayoutBlock block) {
            this.block = block;
        }

        void addIndex(int x) {
            indexNumber.add(x);
        }

        int getLastIndex() {
            return indexNumber.get(indexNumber.size() - 1); // get the last one in the list
        }

        List<Integer> getTestedIndexes() {
            return indexNumber;
        }

        LayoutBlock getBlock() {
            return block;
        }
    }

    private boolean canLBlockBeUsed(LayoutBlock lBlock) {
        if (lBlock == null) {
            return true;
        }
        if (lBlock.getUseExtraColor()) {
            return false;
        }
        if (lBlock.getBlock().getPermissiveWorking()) {
            return true;
        }
        return (lBlock.getState() != Block.OCCUPIED);
    }

    String lastErrorMessage = "Unknown Error Occured";

    // We need to take into account if the returned block has a signalmast attached.
    int findBestHop(final Block preBlock, final Block currentBlock, Block destBlock, int direction, List<Integer> offSet, boolean validateOnly, Routing pathMethod) {
        int result = 0;

        LayoutBlock currentLBlock = InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(currentBlock);
        if (currentLBlock == null) {
            return -1;
        }
        List<Integer> blkIndexTested = new ArrayList<>(5);
        if (log.isDebugEnabled()) {
            log.debug("In find best hop current {} previous {}", currentLBlock.getDisplayName(), preBlock.getDisplayName());
        }
        Block block;
        while (result != -1) {
            if (currentBlock == preBlock) {
                // Basically looking for the connected block, which there should only be one of!
                log.debug("At get ConnectedBlockRoute");
                result = currentLBlock.getConnectedBlockRouteIndex(destBlock, direction);
                log.trace("getConnectedBlockRouteIndex returns result {} with destBlock {}, direction {}", result, destBlock, direction);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Off Set {}", offSet);
                }
                result = currentLBlock.getNextBestBlock(preBlock, destBlock, offSet, Metric.METRIC);
                log.trace("getNextBestBlock returns result {} with preBlock {}, destBlock {}", result, preBlock, destBlock);
            }
            if (result != -1) {
                block = currentLBlock.getRouteNextBlockAtIndex(result);
                LayoutBlock lBlock = InstanceManager.getDefault(LayoutBlockManager.class).getLayoutBlock(block);

                Block blocktoCheck = block;
                if (block == currentBlock) {
                    log.debug("current block matches returned block therefore the next block is directly connected");
                    blocktoCheck = destBlock;
                }

                if ((block == currentBlock) && (currentLBlock.getThroughPathIndex(preBlock, destBlock) == -1)) {
                    lastErrorMessage = "block " + block.getDisplayName() + " is directly attached, however the route to the destination block " + destBlock.getDisplayName() + " can not be directly used";
                    log.debug("continue after {}", lastErrorMessage);
                } else if ((validateOnly) || ((checkForDoubleCrossover(preBlock, currentLBlock, blocktoCheck) && checkForLevelCrossing(currentLBlock)) && canLBlockBeUsed(lBlock))) {
                    if (log.isDebugEnabled()) {
                        log.debug("{} not occupied & not reserved but we need to check if the anchor point between the two contains a signal or not", block.getDisplayName());
                        log.debug("  current {} {}", currentBlock.getDisplayName(), block.getDisplayName());
                    }

                    jmri.NamedBean foundBean = null;
                    /* We change the logging level to fatal in the layout block manager as we are testing to make sure that no signalhead/mast exists
                     this would generate an error message that is expected.*/
                    MDC.put("loggingDisabled", LayoutBlockManager.class.getName());
                    log.trace(" current pathMethod is {}", pathMethod, new Exception("traceback"));
                    switch (pathMethod) {
                        case MASTTOMAST:
                            foundBean = InstanceManager.getDefault(LayoutBlockManager.class).getFacingSignalMast(currentBlock, blocktoCheck);
                            break;
                        case HEADTOHEAD:
                            foundBean = InstanceManager.getDefault(LayoutBlockManager.class).getFacingSignalHead(currentBlock, blocktoCheck);
                            break;
                        case SENSORTOSENSOR:
                            foundBean = InstanceManager.getDefault(LayoutBlockManager.class).getFacingSensor(currentBlock, blocktoCheck, null);
                            break;
                        case NONE:
                            break;
                        default:
                            foundBean = InstanceManager.getDefault(LayoutBlockManager.class).getFacingNamedBean(currentBlock, blocktoCheck, null);
                            break;
                    }
                    MDC.remove("loggingDisabled");
                    if (foundBean == null) {
                        log.debug("No object found so okay to return");
                        return result;
                    } else {
                        lastErrorMessage = "Signal " + foundBean.getDisplayName() + " already exists between blocks " + currentBlock.getDisplayName() + " and " + blocktoCheck.getDisplayName() + " in the same direction on this path";
                        log.debug("continue after {}", lastErrorMessage);
                    }
                } else {
                    lastErrorMessage = "block " + block.getDisplayName() + " found not to be not usable";
                    log.debug("continue after {}", lastErrorMessage);
                }
                if (blkIndexTested.contains(result)) {
                    lastErrorMessage = ("No valid free path found");
                    return -1;
                }
                blkIndexTested.add(result);
                offSet.add(result);
            } else {
                log.debug("At this point the getNextBextBlock() has returned a -1");
            }
        }
        return -1;
    }

    private boolean checkForDoubleCrossover(Block prevBlock, LayoutBlock curBlock, Block nextBlock) {
        LayoutEditor le = curBlock.getMaxConnectedPanel();
        ConnectivityUtil ct = le.getConnectivityUtil();
        List<LayoutTrackExpectedState<LayoutTurnout>> turnoutList = ct.getTurnoutList(curBlock.getBlock(), prevBlock, nextBlock);
        for (LayoutTrackExpectedState<LayoutTurnout> layoutTurnoutLayoutTrackExpectedState : turnoutList) {
            LayoutTurnout lt = layoutTurnoutLayoutTrackExpectedState.getObject();
            if (lt.getTurnoutType() == LayoutTurnout.TurnoutType.DOUBLE_XOVER) {
                if (layoutTurnoutLayoutTrackExpectedState.getExpectedState() == jmri.Turnout.THROWN) {
                    jmri.Turnout t = lt.getTurnout();
                    if (t.getKnownState() == jmri.Turnout.THROWN) {
                        if (lt.getLayoutBlock() == curBlock || lt.getLayoutBlockC() == curBlock) {
                            if (!canLBlockBeUsed(lt.getLayoutBlockB()) && !canLBlockBeUsed(lt.getLayoutBlockD())) {
                                return false;
                            }
                        } else if (lt.getLayoutBlockB() == curBlock || lt.getLayoutBlockD() == curBlock) {
                            if (!canLBlockBeUsed(lt.getLayoutBlock()) && !canLBlockBeUsed(lt.getLayoutBlockC())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkForLevelCrossing(LayoutBlock curBlock) {
        LayoutEditor lay = curBlock.getMaxConnectedPanel();
        for (LevelXing lx : lay.getLevelXings()) {
            if (lx.getLayoutBlockAC() == curBlock
                    || lx.getLayoutBlockBD() == curBlock) {
                if ((lx.getLayoutBlockAC() != null)
                        && (lx.getLayoutBlockBD() != null)
                        && (lx.getLayoutBlockAC() != lx.getLayoutBlockBD())) {
                    if (lx.getLayoutBlockAC() == curBlock) {
                        if (!canLBlockBeUsed(lx.getLayoutBlockBD())) {
                            return false;
                        }
                    } else if (lx.getLayoutBlockBD() == curBlock) {
                        if (!canLBlockBeUsed(lx.getLayoutBlockAC())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Discovers valid pairs of beans type T assigned to a layout editor. If no
     * bean type is provided, then either SignalMasts or Sensors are discovered
     * If no editor is provided, then all editors are considered
     *
     * @param editor     the layout editor panel
     * @param T          the type
     * @param pathMethod Determine whether or not we should reject pairs if
     *                   there are other beans in the way. Constant values of
     *                   NONE, ANY, MASTTOMAST, HEADTOHEAD
     * @return the valid pairs
     */
    public HashMap<NamedBean, List<NamedBean>> discoverValidBeanPairs(LayoutEditor editor, Class<?> T, Routing pathMethod) {
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        HashMap<NamedBean, List<NamedBean>> retPairs = new HashMap<>();
        List<FacingProtecting> beanList = generateBlocksWithBeans(editor, T);
        beanList.forEach((fp) -> {
            fp.getProtectingBlocks().stream().map((block) -> {
                if (log.isDebugEnabled()) {
                    try {
                        log.debug("\nSource {}", fp.getBean().getDisplayName());
                        log.debug("facing {}", fp.getFacing().getDisplayName());
                        log.debug("protecting {}", block.getDisplayName());
                    } catch (java.lang.NullPointerException e) {
                        // Can be considered normal if the signalmast is assigned to an end bumper.
                    }
                }
                return block;
            }).forEachOrdered((block) -> {
                LayoutBlock lFacing = lbm.getLayoutBlock(fp.getFacing());
                LayoutBlock lProtecting = lbm.getLayoutBlock(block);
                NamedBean source = fp.getBean();
                try {
                    retPairs.put(source, discoverPairDest(source, lProtecting, lFacing, beanList, pathMethod));
                } catch (JmriException ex) {
                    log.error("exception in retPairs.put", ex);
                }
            });
        });
        return retPairs;
    }

    /**
     * Returns a list of valid destination beans reachable from a given source
     * bean.
     *
     * @param source     Either a SignalMast or Sensor
     * @param editor     The layout editor that the source is located on, if
     *                   null, then all editors are considered
     * @param T          The class of the remote destination, if null, then both
     *                   SignalMasts and Sensors are considered
     * @param pathMethod Determine whether or not we should reject pairs if
     *                   there are other beans in the way. Constant values of
     *                   NONE, ANY, MASTTOMAST, HEADTOHEAD
     * @return A list of all reachable NamedBeans
     * @throws jmri.JmriException occurring during nested readAll operation
     */
    public List<NamedBean> discoverPairDest(NamedBean source, LayoutEditor editor, Class<?> T, Routing pathMethod) throws JmriException {
        if (log.isDebugEnabled()) {
            log.debug("discover pairs from source {}", source.getDisplayName());
        }
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        LayoutBlock lFacing = lbm.getFacingBlockByNamedBean(source, editor);
        List<LayoutBlock> lProtecting = lbm.getProtectingBlocksByNamedBean(source, editor);
        List<NamedBean> ret = new ArrayList<>();
        List<FacingProtecting> beanList = generateBlocksWithBeans(editor, T);
        
        // may throw JmriException here
        for (LayoutBlock lb : lProtecting) {
            ret.addAll(discoverPairDest(source, lb, lFacing, beanList, pathMethod));
        }
        return ret;
    }

    List<NamedBean> discoverPairDest(NamedBean source, LayoutBlock lProtecting, LayoutBlock lFacing, List<FacingProtecting> blockList, Routing pathMethod) throws JmriException {
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        if (!lbm.isAdvancedRoutingEnabled()) {
            throw new JmriException("advanced routing not enabled");
        }
        if (!lbm.routingStablised()) {
            throw new JmriException("routing not stabilised");
        }
        List<NamedBean> validDestBean = new ArrayList<>();
        for (FacingProtecting facingProtecting : blockList) {
            if (facingProtecting.getBean() != source) {
                NamedBean destObj = facingProtecting.getBean();
                if (log.isDebugEnabled()) {
                    log.debug("looking for pair {} {}", source.getDisplayName(), destObj.getDisplayName());
                }
                try {
                    if (checkValidDest(lFacing, lProtecting, facingProtecting, pathMethod)) {
                        if (log.isDebugEnabled()) {
                            log.debug("Valid pair {} {}", source.getDisplayName(), destObj.getDisplayName());
                        }
                        LayoutBlock ldstBlock = lbm.getLayoutBlock(facingProtecting.getFacing());
                        try {
                            List<LayoutBlock> lblks = getLayoutBlocks(lFacing, ldstBlock, lProtecting, true, pathMethod);
                            if (log.isDebugEnabled()) {
                                log.debug("Adding block {} to paths, current size {}", destObj.getDisplayName(), lblks.size());
                            }
                            validDestBean.add(destObj);
                        } catch (JmriException e) {  // Considered normal if route not found.
                            log.debug("not a valid route through {} - {}", source.getDisplayName(), destObj.getDisplayName());
                        }
                    }
                } catch (JmriException ex) {
                    log.debug("caught exception in discoverPairsDest", ex);
                }
            }
        }
        return validDestBean;
    }

    List<FacingProtecting> generateBlocksWithBeans(LayoutEditor editor, Class<?> T) {
        LayoutBlockManager lbm = InstanceManager.getDefault(LayoutBlockManager.class);
        List<FacingProtecting> beanList = new ArrayList<>();

        for (LayoutBlock curLblk : lbm.getNamedBeanSet()) {
            Block curBlk = curLblk.getBlock();
            LayoutEditor useEdit = editor;
            if (editor == null) {
                useEdit = curLblk.getMaxConnectedPanel();
            }
            if (curBlk != null) {
                int noNeigh = curLblk.getNumberOfNeighbours();
                for (int x = 0; x < noNeigh; x++) {
                    Block blk = curLblk.getNeighbourAtIndex(x);
                    List<Block> proBlk = new ArrayList<>();
                    NamedBean bean = null;
                    if (T == null) {
                        proBlk.add(blk);
                        bean = lbm.getFacingNamedBean(curBlk, blk, useEdit);
                    } else if (T.equals(SignalMast.class)) {
                        bean = lbm.getFacingSignalMast(curBlk, blk, useEdit);
                        if (bean != null) {
                            if (log.isDebugEnabled()) {
                                log.debug("Get list of protecting blocks for {} facing {}", bean.getDisplayName(), curBlk.getDisplayName());
                            }
                            List<LayoutBlock> lProBlk = lbm.getProtectingBlocksByNamedBean(bean, useEdit);
                            for (LayoutBlock lb : lProBlk) {
                                if (lb != null) {
                                    proBlk.add(lb.getBlock());
                                }
                            }
                        }
                    } else if (T.equals(Sensor.class)) {
                        bean = lbm.getFacingSensor(curBlk, blk, useEdit);
                        if (bean != null) {
                            if (log.isDebugEnabled()) {
                                log.debug("Get list of protecting blocks for {}", bean.getDisplayName());
                            }
                            List<LayoutBlock> lProBlk = lbm.getProtectingBlocksByNamedBean(bean, useEdit);
                            for (LayoutBlock lb : lProBlk) {
                                if (lb != null) {
                                    proBlk.add(lb.getBlock());
                                }
                            }
                        }
                    } else {
                        log.error("Past bean type is unknown {}", T);
                    }
                    if (bean != null) {
                        FacingProtecting toadd = new FacingProtecting(curBlk, proBlk, bean);
                        boolean found = false;
                        for (FacingProtecting fp : beanList) {
                            if (fp.equals(toadd)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            beanList.add(toadd);
                        }
                    }
                }
                if (noNeigh == 1) {
                    NamedBean bean = null;
                    if (log.isDebugEnabled()) {
                        log.debug("We have a dead end {}", curBlk.getDisplayName());
                    }
                    if (T == null) {
                        bean = lbm.getNamedBeanAtEndBumper(curBlk, useEdit);
                    } else if (T.equals(SignalMast.class)) {
                        bean = lbm.getSignalMastAtEndBumper(curBlk, useEdit);
                    } else if (T.equals(Sensor.class)) {
                        bean = lbm.getSensorAtEndBumper(curBlk, useEdit);
                    } else {
                        log.error("Past bean type is unknown {}", T);
                    }
                    if (bean != null) {
                        FacingProtecting toadd = new FacingProtecting(curBlk, null, bean);
                        boolean found = false;
                        for (FacingProtecting fp : beanList) {
                            if (fp.equals(toadd)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            beanList.add(toadd);
                        }
                    }
                }
            }
        }
        return beanList;
    }

    static class FacingProtecting {

        Block facing;
        List<Block> protectingBlocks;
        NamedBean bean;

        FacingProtecting(Block facing, List<Block> protecting, NamedBean bean) {
            this.facing = facing;
            if (protecting == null) {
                this.protectingBlocks = new ArrayList<>(0);
            } else {
                this.protectingBlocks = protecting;
            }
            this.bean = bean;
        }

        Block getFacing() {
            return facing;
        }

        List<Block> getProtectingBlocks() {
            return protectingBlocks;
        }

        NamedBean getBean() {
            return bean;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }

            if (!(getClass() == obj.getClass())) {
                return false;
            } else {
                FacingProtecting tmp = (FacingProtecting) obj;
                if (tmp.getBean() != this.bean) {
                    return false;
                }
                if (tmp.getFacing() != this.facing) {
                    return false;
                }
                if (!tmp.getProtectingBlocks().equals(this.protectingBlocks)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (this.bean != null ? this.bean.hashCode() : 0);
            hash = 37 * hash + (this.facing != null ? this.facing.hashCode() : 0);
            hash = 37 * hash + (this.protectingBlocks != null ? this.protectingBlocks.hashCode() : 0);
            return hash;
        }
    }

    private final static Logger log
            = LoggerFactory.getLogger(LayoutBlockConnectivityTools.class);
}
