package org.unibl.etf.database.mysql;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import java.util.List;
import java.util.function.Supplier;

public class DBUtils {
    public static<T> void loadTableViewFromDatabase(Supplier<List<T>> serviceMethod, TableView<T> tableView) {
        DatabaseLoadService<T> task = new DatabaseLoadService<>(serviceMethod);

        task.setOnSucceeded(event -> {
            ObservableList<T> items = task.getValue();
            tableView.setItems(items);
        });

        task.setOnFailed(event -> {
            Throwable e = task.getException();
            e.printStackTrace();
        });

        task.start();
    }

    public static <T> void loadObservableListFromDatabase(Supplier<List<T>> serviceMethod, ObservableList<T> observableList) {
        DatabaseLoadService<T> service = new DatabaseLoadService<>(serviceMethod);

        service.setOnSucceeded(event -> {
            ObservableList<T> items = service.getValue();
            Platform.runLater(() -> {
                observableList.setAll(items);
                System.out.println("UI updated with " + observableList.size() + " items.");
            });
//            observableList.setAll(service.getValue());
//            System.out.println("Service succeeded. List updated with " + observableList.size() + " items.");
        });

        service.setOnFailed(event -> {
            Throwable e = service.getException();
            e.printStackTrace();
        });

        service.start(); // Start the service
    }

//    public static<T> void loadObservableListFromDatabase(Supplier<List<T>> serviceMethod, ObservableList<T> observableList) {
//        DatabaseLoadTask<T> task = new DatabaseLoadTask<>(serviceMethod);
//
//        task.setOnSucceeded(event -> {
//            ObservableList<T> items = task.getValue();
//            observableList.setAll(items);
//            System.out.println("finished: " + observableList.size());
//        });
//
//        task.setOnFailed(event -> {
//            Throwable e = task.getException();
//            e.printStackTrace();
//        });
//
//        Thread thread = new Thread(task);
//        thread.setDaemon(true);
//        thread.start();
//    }

}
