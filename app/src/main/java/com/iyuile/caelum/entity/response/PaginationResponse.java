package com.iyuile.caelum.entity.response;


import java.io.Serializable;

/**
 * 
 * @Description 响应 页码
 * @ProjectName Apus
 * @ClassName {@link PaginationResponse}
 * @author WangYao
 * @version 1
 * @Date 2016-4-9 下午6:33:26
 */
public class PaginationResponse implements Serializable {
	private int total;
	private int count;
	private int per_page;
	private int current_page;
	private int total_pages;
	private Object links;// 用不到的一个对象

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPer_page() {
		return per_page;
	}

	public void setPer_page(int per_page) {
		this.per_page = per_page;
	}

	public int getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}

	public int getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}

	public Object getLinks() {
		return links;
	}

	public void setLinks(Object links) {
		this.links = links;
	}

}
