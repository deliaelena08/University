package Model;

import Helper.Entity;
import Helper.Tuple;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<User,User>> {
        private LocalDateTime date= LocalDateTime.now();

        /*
        * Obtinem data la care s a creat o prietenie
        * */
        public LocalDateTime getDate() {
            return date;
        }

        /*
        * Seteaza o noua data pentru o prietenie
        * */
        public void setDate(LocalDateTime date) {
            this.date = date;
        }
}
