package com.mtvn.persistence.entities.template;

import java.io.Serializable;
import java.util.Date;

public interface GenericEntity<PK extends Serializable> extends Serializable {

    PK getId();

    void setId(PK id);

    Boolean isDeleted();

    void setDeleted(Boolean deleted);

    Boolean isArchived();

    void setArchived(Boolean archived);

    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    Date getModifiedAt();

    void setModifiedAt(Date modifiedAt);
}
