package com.oms.product.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.oms.product.utility.CustomPK;

@Entity
@Table(name = "subscribed_product")
public class SubscribedProduct {
	
	@EmbeddedId
	private CustomPK customId;
	private Integer quantity;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public CustomPK getCustomId() {
		return customId;
	}

	public void setCustomId(CustomPK customId) {
		this.customId = customId;
	}
	
	

}
