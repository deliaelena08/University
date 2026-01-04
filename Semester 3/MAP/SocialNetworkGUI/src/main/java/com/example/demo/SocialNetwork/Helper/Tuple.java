package com.example.demo.SocialNetwork.Helper;

import java.util.Objects;

public class Tuple <E1,E2>{
    /*
    * model generic care reprezintă o pereche de două elemente (de tipurile E1 și E2)
    * */
    private E1 e1;
    private E2 e2;

    //constructorul pentru o pereche
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    //obtinem prima entitate din pereche
    public E1 getE1() {
        return e1;
    }

    //obtinem a doua entitate din pereche
    public E2 getE2() {
        return e2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(e1, tuple.e1) && Objects.equals(e2, tuple.e2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
