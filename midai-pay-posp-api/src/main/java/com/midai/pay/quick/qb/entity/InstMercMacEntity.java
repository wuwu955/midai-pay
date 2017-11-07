package com.midai.pay.quick.qb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="tbl_inst_merc_mac")
@EqualsAndHashCode(callSuper=false)
@Data
public class InstMercMacEntity extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer Id;
	@Length(max=12)
	private String instCode;
	@Length(max=100)
	private String mackey;
	@Length(max=50)
	private String mercId;
}
