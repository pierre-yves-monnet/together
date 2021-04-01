package com.togh.entity.base;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;

import com.togh.entity.ParticipantEntity.ParticipantRoleEnum;
import com.togh.entity.ToghUserEntity.ContextAccess;

import lombok.Data;

@MappedSuperclass
@Inheritance
public abstract @Data class BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;  

    private String name;
    
    @Column(name="datecreation")    
    private LocalDateTime datecreation =LocalDateTime.now(ZoneOffset.UTC);

    @Column(name="datemodification")    
    private LocalDateTime datemodification = LocalDateTime.now(ZoneOffset.UTC);
         
    public BaseEntity( String name ) {
        this.name= name;
        setDatecreation( LocalDateTime.now(ZoneOffset.UTC));

    }
    public BaseEntity() {
        
    }
    
    /*
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDatecreation() {
		return datecreation;
	}

	public void setDatecreation(LocalDateTime datecreation) {
		this.datecreation = datecreation;
	}

	public LocalDateTime getDatemodification() {
		return datemodification;
	}

	public void setDatemodification(LocalDateTime datemodification) {
		this.datemodification = datemodification;
	}
	*/
	/**
	 * A date may be manipulate by the interface as an Absolute Date. Example, in the InineraryStep, the dateStep is manipulate by the interface as "2021-08_01T00:00:00Z". 
	 * Then, the time offset must not be calulated here to save the UTC value.
	 * Each entity MUST redifine this method
	 * @param attributName
	 * @return
	 */
	public boolean isAbsoluteLocalDate(String attributName ) {
	    return false;
	}
	
	public void touch() {
	   this.datemodification = LocalDateTime.now(ZoneOffset.UTC);
	}
    
	  /**
     * Get the information as the levelInformation in the event. A OWNER see more than a OBSERVER for example
     * @param levelInformation
     * @return
     */
    public Map<String,Object> getMap(ContextAccess contextAccess) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        resultMap.put("name", name);
        return resultMap;
    }
}
