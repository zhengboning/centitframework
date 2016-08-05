package com.centit.product.ip.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.centit.framework.core.po.EntityWithTimestamp;

@Entity
@Table(name = "F_OS_INFO")

public class OsInfo implements EntityWithTimestamp, java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OS_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private String osId;
    
    @Column(name = "OS_NAME")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String osName;
    
    @Column(name = "OS_URL")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String osUrl;

    @Column(name = "DDE_SYNC_URL")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String ddeSyncUrl;
    
    @Column(name = "SYS_DATA_PUSH_OPTION")
    @Length(max = 32, message = "字段长度不能大于{max}")
    private String sysDataPushOption;
 
    
    @Column(name = "CREATED")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String created;
 
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;
    @Column(name = "CREATE_TIME")
    private Date createTime;


    // Constructors

    /**
     * default constructor
     */
    public OsInfo() {
    }

    /**
     * minimal constructor
     */
    public OsInfo(
            String osId
            , String osName) {
        this.osId = osId;
        this.osName = osName;
    }

    /**
     * full constructor
     */
    public OsInfo(
            String osId
            , String osName, 
            String sysDataPushOption, String osUrl,String ddeSyncUrl,
            String created, Date lastUpdateTime, Date createTime) {
        this.osId = osId;
        this.osName = osName;
        this.ddeSyncUrl=ddeSyncUrl;
        this.sysDataPushOption=sysDataPushOption;
        this.osUrl = osUrl;
        this.created = created;
        this.lastModifyDate= lastUpdateTime;
        this.createTime = createTime;
    }


   

    public String getOsId() {
        return this.osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }
    // Property accessors

    public String getOsName() {
        return this.osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

   
    public String getOsUrl() {
		return osUrl;
	}

	public void setOsUrl(String osUrl) {
		this.osUrl = osUrl;
	}

	public String getDdeSyncUrl() {
		return ddeSyncUrl;
	}

	public void setDdeSyncUrl(String ddeSyncUrl) {
		this.ddeSyncUrl = ddeSyncUrl;
	}

	public String getSysDataPushOption() {
		return sysDataPushOption;
	}

	public void setSysDataPushOption(String sysDataPushOption) {
		this.sysDataPushOption = sysDataPushOption;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public void copy(OsInfo other) {

        this.setOsId(other.getOsId());
        this.osName = other.getOsName();
        this.ddeSyncUrl=other.getDdeSyncUrl();
        this.sysDataPushOption=other.getSysDataPushOption();
        this.osUrl = other.getOsUrl();
        this.lastModifyDate= other.getLastModifyDate();
        this.createTime = other.getCreateTime();
        this.created = other.getCreated();
    }

    public void copyNotNullProperty(OsInfo other) {

        if (other.getOsId() != null)
            this.setOsId(other.getOsId());
        if (other.getOsName() != null)
            this.osName = other.getOsName();       
        if (other.getDdeSyncUrl() != null)
        	this.ddeSyncUrl=other.getDdeSyncUrl();
        if (other.getSysDataPushOption() != null)
        	this.sysDataPushOption=other.getSysDataPushOption();
        if (other.getOsUrl() != null)
        	this.osUrl = other.getOsUrl();
        if (other.getLastModifyDate() != null)
        	this.lastModifyDate= other.getLastModifyDate();
        if (other.getCreated() != null)
        	this.created = other.getCreated();        
        if (other.getCreateTime() != null)
            this.createTime = other.getCreateTime();
    }

    public void clearProperties() {

    	this.osId = null;
        this.osName = null;
        this.ddeSyncUrl=null;
        this.sysDataPushOption=null;
        this.osUrl = null;
        this.created = null;
        this.lastModifyDate= null;
        this.createTime = null;

    }
}
