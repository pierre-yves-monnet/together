/* ******************************************************************************** */
/*                                                                                  */
/*  Togh Project                                                                    */
/*                                                                                  */
/*  This component is part of the Togh Project, developed by Pierre-Yves Monnet     */
/*                                                                                  */
/*                                                                                  */
/* ******************************************************************************** */
package com.togh.entity.base;

import com.togh.entity.ToghUserEntity;
import com.togh.entity.ToghUserEntity.ContextAccess;

import javax.persistence.*;
import java.util.Map;



/* ******************************************************************************** */
/*                                                                                  */
/*  UserEntity,                                                                     */
/*                                                                                  */
/*  Entity is created / modified by an user.                                        */
/*                                                                                  */
/*                                                                                  */
/* ******************************************************************************** */

@MappedSuperclass
@Inheritance
public abstract class UserEntity extends BaseEntity {

    
    
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "authorid")
    private ToghUserEntity author;
    
    @Column(name="accessdata", length=20)
    private String accessdata = "local";

    public UserEntity(ToghUserEntity author, String name) {
        super(name);
        this.author = author;
    }
    public UserEntity() {
        super();
    }
    
	public Long getAuthorId() {
	    
		return (this.author !=null ? this.author.getId() : null);
	}
	public ToghUserEntity getAuthor() {
        return this.author;
    }

	public void setAuthor(ToghUserEntity author) {
		this.author = author;
	}

	public String getAccessdata() {
		return accessdata;
	}

	public void setAccessdata(String accessdata) {
		this.accessdata = accessdata;
	}
	
	
	/**
	 * 
	 * @param levelInformation
	 * @return
	 */
    @Override
    public Map<String,Object> getMap(ContextAccess contextAccess, Long timezoneOffset) {
        Map<String,Object> resultMap = super.getMap( contextAccess, timezoneOffset );
        if (contextAccess== ContextAccess.ADMIN)
            resultMap.put("authorid", this.getAuthorId());
        return resultMap;
    }

}
