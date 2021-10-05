package tankrotationexample.game;

public interface Observable {
    void notifyObservers();

    void attachObserver(Observer obv);

    void detachObserver(Observer obv);
}
