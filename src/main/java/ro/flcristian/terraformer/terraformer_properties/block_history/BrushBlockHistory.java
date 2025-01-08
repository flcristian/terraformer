package ro.flcristian.terraformer.terraformer_properties.block_history;

import java.util.Stack;

public class BrushBlockHistory {
    private static final int MAX_STACK_SIZE = 50;
    private final Stack<BlockHistoryStates> undoStack = new Stack<>();
    private final Stack<BrushAction> redoStack = new Stack<>();

    public void pushModification(BlockHistoryStates states) {
        undoStack.push(states);
        trimStackSize(undoStack);
        redoStack.clear();
    }

    public void pushRedo(BlockHistoryStates states) {
        undoStack.push(states);
        trimStackSize(undoStack);
    }

    public BlockHistoryStates undo() {
        if (undoStack.isEmpty())
            return null;
        BlockHistoryStates historyStates = undoStack.pop();
        redoStack.push(new BrushAction(
                historyStates.targetLocation(),
                historyStates.brushProperties()));
        trimStackSize(redoStack);
        return historyStates;
    }

    public BrushAction redo() {
        return redoStack.isEmpty() ? null : redoStack.pop();
    }

    private <T> void trimStackSize(Stack<T> stack) {
        while (stack.size() > MAX_STACK_SIZE) {
            stack.remove(0);
        }
    }
}