package smarthome.library.datalibrary.store.listeners;

public interface HomeExistenceListener {
    void onHomeAlreadyExists();

    void onHomeDoesNotExist();
}
