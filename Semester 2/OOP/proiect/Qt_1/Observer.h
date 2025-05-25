#pragma once
class Observer {
public:
    virtual void update() = 0;
};

class Observable {
    vector<Observer*> observers;
public:
    void addObservcer(Observer* obs) {
        observers.push_back(obs);
    }

    void removeObserver(Observer* obs) {
        observers.erase(remove(begin(observers), end(observers), obs), observers.end());
    }
protected:
    void notify() {
        for (auto obs : observers) {
            obs->update();
        }
    }
};