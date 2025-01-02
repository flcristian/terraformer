package io.papermc.terraformer.terraformer_properties.block_history;

import java.util.Stack;

public class BrushBlockHistory {
    private final Stack<BlockHistoryStates> undoStack = new Stack<>();
    private final Stack<BrushAction> redoStack = new Stack<>();

    public void pushModification(BlockHistoryStates states) {
        undoStack.push(states);
        redoStack.clear();
    }

    public void pushRedo(BlockHistoryStates states) {
        undoStack.push(states);
    }

    public BlockHistoryStates undo() {
        if (undoStack.isEmpty())
            return null;
        BlockHistoryStates historyStates = undoStack.pop();
        redoStack.push(new BrushAction(
                historyStates.targetLocation(),
                historyStates.brushType(),
                historyStates.brushSize()));
        return historyStates;
    }

    public BrushAction redo() {
        return redoStack.isEmpty() ? null : redoStack.pop();
    }
}