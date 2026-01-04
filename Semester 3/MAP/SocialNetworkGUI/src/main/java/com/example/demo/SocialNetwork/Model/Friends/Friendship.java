package com.example.demo.SocialNetwork.Model.Friends;

import com.example.demo.SocialNetwork.Helper.Entity;
import com.example.demo.SocialNetwork.Helper.Tuple;
import com.example.demo.SocialNetwork.Model.User;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<User, User>> {
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

        public String getFirstFriend()
        {
            return this.getId().getE1().getFirstName();
        }

        public String getSecondFriend()
        {
            return this.getId().getE2().getFirstName();
        }
}
