package org.unibl.etf.database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;
import java.util.function.Supplier;

public class DatabaseLoadService<T> extends Service<ObservableList<T>> {
    private final Supplier<List<T>> databaseOperation;

    public DatabaseLoadService(Supplier<List<T>> databaseOperation) {
        this.databaseOperation = databaseOperation;
    }

    @Override
    protected Task<ObservableList<T>> createTask() {
        return new Task<>() {
            @Override
            protected ObservableList<T> call() throws Exception {
                List<T> results = databaseOperation.get();
                return FXCollections.observableArrayList(results);
            }
        };
    }
}
