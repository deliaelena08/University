package org.example.zboruri.models;
import java.util.Objects;

public class Entity<ID>  {
    /*
     * model generic folosit pentru a reprezenta o entitate abstractă cu un identificator de tip ID
     * */

    private ID id;

    /*
     * returneaza id-ul unei entitati
     * */
    public ID getId() {
        return id;
    }

    /*
    id- de tip id diferit de null
    seteaza id ul unei entitati
     */
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
