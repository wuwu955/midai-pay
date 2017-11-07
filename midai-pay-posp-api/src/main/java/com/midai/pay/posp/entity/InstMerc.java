package com.midai.pay.posp.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "tbl_inst_merc")
@EqualsAndHashCode(callSuper = false)
public class InstMerc extends BaseEntity {

	@Id
	private String instCode;
	@Id
	private String instMercId;

	private String isstMercName;
	@Id
	private String instDeviceId;

}
