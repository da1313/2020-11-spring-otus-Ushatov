package org.course.domain.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GenericGenerator(name = "user_id_seq", strategy = "enhanced-sequence",
            parameters = {@org.hibernate.annotations.Parameter(name = "start", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private boolean isActive;

}
