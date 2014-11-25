package com.garethahealy.databaseplayground.database.model.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "beers")
@NamedQueries(@NamedQuery(name = Beer.SELECT_ALL_QUERY, query = "SELECT b FROM Beer b"))
public class Beer implements Serializable {

        private static final long serialVersionUID = -5958885654581897644L;

        public static final String SELECT_ALL_QUERY = "Beer.SelectAll";
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
        @Column(name = "id", unique = true, nullable = false)
        public Integer getId() {
                return this.id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "name")
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Override
        public String toString() {
                return "com.garethahealy.databaseplayground.database.model.entities.Beer{" +
                       "id=" + getId() +
                       ", name='" + getName() + '\'' +
                       '}';
        }
}
