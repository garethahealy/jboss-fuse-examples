package com.garethahealy.databaseplayground.database.cameljpa.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BEERS")
public class Beer implements Serializable {

        private static final long serialVersionUID = -5958885654581897644L;

        private Integer id;
        private String name;

        public Beer() {

        }

        public Beer(Integer id, String name) {
                this.id = id;
                this.name = name;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "ID", unique = true, nullable = false)
        public Integer getId() {
                return this.id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "NAME")
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }
}
