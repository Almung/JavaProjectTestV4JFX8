package model.command;

import java.util.Stack;

public class TransactionInvoker {
    private final Stack<TransactionCommand> undoStack;
    private final Stack<TransactionCommand> redoStack;

    public TransactionInvoker() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void executeCommand(TransactionCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            TransactionCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            TransactionCommand command = redoStack.pop();
            command.redo();
            undoStack.push(command);
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}