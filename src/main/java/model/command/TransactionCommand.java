package model.command;

public interface TransactionCommand {
    void execute();
    void undo();
    void redo();
}