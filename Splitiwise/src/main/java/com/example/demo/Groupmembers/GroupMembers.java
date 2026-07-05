package com.example.demo.Groupmembers;

import com.example.demo.Groups.Groups;
import jakarta.persistence.*;

@Entity
@Table(name="group_members")
public class GroupMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Groups group;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}
    
    
}