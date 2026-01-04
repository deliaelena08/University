package com.example.demo.SocialNetwork.Model.Pages;

import com.example.demo.SocialNetwork.Model.User;

public class Page<E> {
    private Iterable<E> elementsOnPage;
    private int totalNrOfElements;

    public Page(Iterable<E> elementsOnPage, int totalNrOfElements) {
        this.elementsOnPage = elementsOnPage;
        this.totalNrOfElements = totalNrOfElements;
    }

    public int getTotalNrOfElements() {
        return totalNrOfElements;
    }
    public void setTotalNrOfElements(int totalNrOfElements) {
        this.totalNrOfElements = totalNrOfElements;
    }
    public Iterable<E> getElementsOnPage() {
        return elementsOnPage;
    }
    public void setElementsOnPage(Iterable<E> elementsOnPage) {
        this.elementsOnPage = elementsOnPage;
    }

}
