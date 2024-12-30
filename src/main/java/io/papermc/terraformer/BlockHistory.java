package io.papermc.terraformer;

import java.util.Stack;

public class BlockHistory {
    private final Stack<Stack<BlockState>> undoStack = new Stack<>();
    private final Stack<BrushAction> redoStack = new Stack<>();

    public void pushModification(Stack<BlockState> states) {
        undoStack.push(states);
        redoStack.clear();
    }

    public void pushRedo(Stack<BlockState> states) {
        undoStack.push(states);
    }

    public Stack<BlockState> undo() {
        if (undoStack.isEmpty())
            return null;
        Stack<BlockState> states = undoStack.pop();
        BlockState firstState = states.firstElement();
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