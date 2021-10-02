/* ******************************************************************************** */
/*                                                                                  */
/* Togh Project */
/*                                                                                  */
/* This component is part of the Togh Project, developed by Pierre-Yves Monnet */
/*                                                                                  */
/*                                                                                  */
/* ******************************************************************************** */
package com.togh.entity;

import com.togh.engine.tool.EngineTool;
import com.togh.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

/* ******************************************************************************** */
/*                                                                                  */
/* For each physical user, an endUser is registered */
/*                                                                                  */
/*                                                                                  */
/* ******************************************************************************** */

@Entity
@Table(name = "TOGHUSER")
@EqualsAndHashCode(callSuper = false)
public @Data class ToghUserEntity extends BaseEntity {

    @Column(name = "googleid", length = 100)
    private String googleId;

    @Column(name = "firstname", length = 100)
    private String firstName;

    @Column(name = "lastname", length = 100)
    private String lastName;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "language", length = 5 )
    @org.hibernate.annotations.ColumnDefault("'en'")
    private String language;

    /**
     * Save the user time zone. Then, each communication (email...) will be translated to this time zone 
     */
    @Column(name = "usertimezone", length=10)
    private String userTimeZone;
    
    public enum VisibilityEnum {
        ALWAYS, ALWAYBUTSEARCH, LIMITEDEVENT, NEVER
    }

    @Column(name = "emailvisibility", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private VisibilityEnum emailVisibility = VisibilityEnum.ALWAYS;

    @Column(name = "phonenumber", length = 100)
    private String phoneNumber;

    @Column(name = "phonevisibility", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private VisibilityEnum phoneNumberVisibility = VisibilityEnum.ALWAYS;

    @Column(name = "connectstamp", length = 100)
    private String connectionStamp;

    @Column(name = "connectiontime")
    private LocalDateTime connectionTime;

    @Column(name = "connectionlastactivity")
    public LocalDateTime connectionLastActivity;

    /**
     * Invited: email was sent, waiting to be confirmed
     * @author Firstname Lastname
     *
     */
    public enum StatusUserEnum {
        ACTIF, DISABLED, BLOCKED, INVITED
    }

    @Column(name = "statususer", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.ColumnDefault("'ACTIF'")
    StatusUserEnum statusUser;

    /**
     * The user accept to be part of a search result, to be invited directly in an event
     */
    @Column(name = "searchable")
    Boolean searchable = true;

    public static ToghUserEntity createNewUser(String firstName, String lastName, String email, String password, SourceUserEnum sourceUser) {
        ToghUserEntity endUser = new ToghUserEntity();
        endUser.setEmail(email);
        endUser.setFirstName(firstName);
        endUser.setLastName(lastName);
        endUser.calculateName();
        endUser.setPassword(password);
        endUser.setSource(sourceUser);
        LocalDateTime dateNow = LocalDateTime.now(ZoneOffset.UTC);
        endUser.setDateCreation(dateNow);
        endUser.setDateModification(dateNow);
        endUser.setPrivilegeUser(PrivilegeUserEnum.USER);
        endUser.setStatusUser(StatusUserEnum.ACTIF);
        endUser.setSubscriptionUser(SubscriptionUserEnum.FREE);
        endUser.setTypePicture(TypePictureEnum.TOGH);

        return endUser;
    }
    
    public static ToghUserEntity createInvitedUser(String email) 
    {
        ToghUserEntity toghUserEntity = createNewUser(null, null, email, null, SourceUserEnum.INVITED);
        toghUserEntity.setStatusUser( StatusUserEnum.INVITED );
        toghUserEntity.setStatusUser( StatusUserEnum.ACTIF );

        return toghUserEntity;
    }
    
    /**
     * Calculate the name according the first and last name
     */
    public void calculateName() {
        setName( (firstName==null? "": firstName+" ") + (lastName==null? "": lastName));
    }
    
    public boolean checkPassword(String passwordToCompare) {
        if (passwordToCompare == null)
            return false;
        return passwordToCompare.equals(password);
    }

    /**
     * INVITED : an invitation is sent, the user did not confirm yet
     */

    public enum SourceUserEnum {
        PORTAL, GOOGLE, INVITED, SYSTEM
    }

   

    @Column(name = "source", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)

    SourceUserEnum source;
    
    public enum TypePictureEnum {
        TOGH, CYPRIS, URL, IMAGE
    }
    @Column(name = "typepicture", length = 10, nullable = false)
    @org.hibernate.annotations.ColumnDefault("'TOGH'")
    @Enumerated(EnumType.STRING)

    TypePictureEnum typePicture;

    @Column(name = "picture", length = 300)
    String picture;

    /**
     * Level of privilege
     * an ADMIN can administrate the complete application
     * a TRANSlator access all translation function
     * a USER use the application
     */
    public enum PrivilegeUserEnum {
        ADMIN, TRANS, USER
    }

    @Column(name = "privilegeuser", length = 10)
    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.ColumnDefault("'USER'")
    PrivilegeUserEnum privilegeUser;

    /**
     * attention, this value is used in different entity
     */
    public enum SubscriptionUserEnum {
        FREE, PREMIUM, EXCELLENCE
    }

    @Column(name = "subscriptionuser", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.ColumnDefault("'FREE'")
    SubscriptionUserEnum subscriptionUser;

    @Column(name = "showtipsuser")
    @org.hibernate.annotations.ColumnDefault("'1'")
    Boolean showTipsUser;

    public String toString() {
        return getId() + ":" + getName()
                + (getGoogleId() != null ? " Gid@" + getGoogleId() : "")
                + (getFirstName() != null ? " " + getFirstName() + " " + getLastName() : "")
                + (getEmail() != null ? " email:" + getEmail() : "")
                + ")";
    }

    /**
     * Get the user Label, to add in an email, or explanation
     * 
     * @return
     */
    public String getLabel() {
        if ( isExist(firstName) && isExist(lastName))
            return (firstName != null ? firstName + " " : "") + (lastName != null ? lastName : "");
        return email;
    }

    private boolean isExist(String value) {
        if (value!=null && value.trim().length()>0)
            return true;
        return false;
    }
    // define the user access :
    // SEARCH : the user show up in a public search
    // PUBLICACCESS : access is from a public event : event is public or limited, but the user who want to access is only an observer, or not yet confirmed. So, show only what user want to show to the public
    // FRIENDACCESS : access is from an LimitedEvent. The user who want to access is registered in this LimitedEvent, so show what the user want to shopw to hist friend
    // SECRETEVENT : access is from a SECRET event, then show only a first name, nothing else
    // ADMIN : administrator access, give back everything
    // MYPROFILE : I want to access my profile
    public enum ContextAccess {
        SEARCH, PUBLICACCESS, FRIENDACCESS, SECRETACCESS, ADMIN, MYPROFILE
    }

    /**
     * Get the information as the levelInformation in the event. A OWNER see more than a OBSERVER for example
     * 
     * @param contextAccess Context of the access
     * @param timeZoneOffset time Zone offset of the browser
     * @return
     */
    @Override
    public Map<String, Object> getMap(ContextAccess contextAccess, Long timeZoneOffset) {
        Map<String, Object> resultMap = super.getMap(contextAccess, timeZoneOffset);

        StringBuilder label = new StringBuilder();
        StringBuilder longlabel = new StringBuilder();

        if (getName() != null) {
            label.append(getName());
            longlabel.append(getName());
        }

        resultMap.put("tagid",System.currentTimeMillis() % 10000);
        resultMap.put("name", getName());
        resultMap.put("firstName", firstName);
        resultMap.put("typePicture", typePicture);
        resultMap.put("picture", picture);
        resultMap.put("source", getSource().toString().toLowerCase());

        
        resultMap.put("statusUser", statusUser==null ? "" : statusUser.toString());

        // if the context is SECRET, the last name is not visible
        if (contextAccess != ContextAccess.SECRETACCESS)
            resultMap.put("lastName", lastName);

        if (isVisible(emailVisibility, contextAccess)) {
            resultMap.put("email", email);
            if (email != null && email.trim().length() > 0) {
                // the label is the email only if there is no label at this moment
                if (label.toString().trim().length() == 0)
                    label.append(" (" + email + ")");
                longlabel.append(" (" + email + ")");
            }
        } else
            resultMap.put("email", "*********");

        if (isVisible(phoneNumberVisibility, contextAccess)) {
            resultMap.put("phoneNumber", phoneNumber);
            if (phoneNumber != null) {
                if (label.length() == 0)
                    label.append(" " + phoneNumber);
                longlabel.append(" " + phoneNumber);
            }
        } else
            resultMap.put("phoneNumber", "*********");

        resultMap.put("label", label.toString());
        resultMap.put("longlabel", longlabel.toString());

        if (contextAccess == ContextAccess.MYPROFILE || contextAccess == ContextAccess.ADMIN) {
            resultMap.put("subscriptionuser", subscriptionUser.toString());
            resultMap.put("showTipsUser", showTipsUser);

        }
        if (contextAccess == ContextAccess.ADMIN) {
            resultMap.put("privilegeuser", privilegeUser.toString());
            resultMap.put("connectiontime", EngineTool.dateToString(connectionTime));
            if (connectionTime!=null) {
                LocalDateTime connectionTimeLocal = connectionTime.minusMinutes(timeZoneOffset);
                resultMap.put("connectiontimest", EngineTool.dateToHumanString(connectionTimeLocal));
            }
            resultMap.put("connectionlastactivity", EngineTool.dateToString(connectionLastActivity));
            if (connectionLastActivity!=null) {
                LocalDateTime connectionLastActivityLocal = connectionLastActivity.minusMinutes(timeZoneOffset);
                resultMap.put("connectionlastactivityst", EngineTool.dateToHumanString(connectionLastActivityLocal));
            }
            resultMap.put("connected", connectionStamp == null ? "OFFLINE" : "ONLINE");
        }
        return resultMap;
    }

    private boolean isVisible(VisibilityEnum visibility, ContextAccess userAccess) {
        // first rule : admin, return true
        if (userAccess == ContextAccess.ADMIN)
            return true;
        // second rule : secret : never.
        if (userAccess == ContextAccess.SECRETACCESS)
            return false;
        // then depends of the visibily and the policy
        if (emailVisibility == VisibilityEnum.ALWAYS)
            return true;
        // it's visible only for accepted user in the event
        if (userAccess == ContextAccess.FRIENDACCESS && (visibility == VisibilityEnum.LIMITEDEVENT || visibility == VisibilityEnum.ALWAYBUTSEARCH))
            return true;
        // in all other case, refuse
        return false;
    }
}
