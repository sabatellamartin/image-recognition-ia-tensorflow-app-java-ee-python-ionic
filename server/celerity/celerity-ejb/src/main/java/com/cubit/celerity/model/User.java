package com.cubit.celerity.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorColumn(name="user_type")
@Table(name = "users")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class User implements Serializable {
	private static final long serialVersionUID = -8236018059619729784L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "email", unique=true)
	private String email;

	@Column(name = "password")
	private String password;
	
	@Column(name = "creation")
	private Date creation;
	
	@Column(name = "lock")
	private Date lock;
	
    @OneToOne(cascade = CascadeType.PERSIST, optional=false)
    @JoinColumn(name="setting_id", unique=true, nullable=false, updatable=false)
    private Setting setting;
    
	public User() {
		this.setting = new Setting();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getLock() {
		return lock;
	}

	public void setLock(Date lock) {
		this.lock = lock;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}
	
}
