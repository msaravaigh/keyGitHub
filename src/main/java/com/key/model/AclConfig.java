package com.key.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "acl_config")
	public class AclConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="acl_id")
	private int aclId;
	@Column(name = "audit_insert_date")
	private Timestamp auditInsertDate;

	@Column(name = "audit_update_date")
	private Timestamp auditUpdatetDate;
	@Column(name="page")
	private String page;
	@Column(name="path")
	private String path;
	@Column(name="type")
	private String type;
	@ManyToOne
	@JoinColumn(name="authority_id")
	private Authority authority;
	public int getAclId() {
		return aclId;
	}
	public void setAclId(int aclId) {
		this.aclId = aclId;
	}
	public Timestamp getAuditInsertDate() {
		return auditInsertDate;
	}
	public void setAuditInsertDate(Timestamp auditInsertDate) {
		this.auditInsertDate = auditInsertDate;
	}
	public Timestamp getAuditUpdatetDate() {
		return auditUpdatetDate;
	}
	public void setAuditUpdatetDate(Timestamp auditUpdatetDate) {
		this.auditUpdatetDate = auditUpdatetDate;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Authority getAuthority() {
		return authority;
	}
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

}