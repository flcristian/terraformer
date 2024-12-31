package io.papermc.terraformer.terraformer_properties.block_history;

import java.util.Stack;

public class BlockHistory {
    private final Stack<Stack<BlockStateHistory>> undoStack = new Stack<>();
    private final Stack<BrushAction> redoStack = new Stack<>();

    public void pushModification(Stack<BlockStateHistory> states) {
        undoStack.push(states);
        redoStack.clear();
    }

    public void pushRedo(Stack<BlockStateHistory> states) {
        undoStack.push(states);
    }

    public Stack<BlockStateHistory> undo() {
        if (undoStack.isEmpty())
            return null;
        Stack<BlockStateHistory> states = undoStack.pop();
        BlockStateHistory firstState = states.firstElement();
        redoStack.push(new BrushAction(
                firstState.targetLocation(),
                firstState.brushType(),
                firstState.brushSize()));
        return states;
    }

    public BrushAction redo() {
        return redoStack.isEmpty() ? null : redoStack.pop();
    }
}