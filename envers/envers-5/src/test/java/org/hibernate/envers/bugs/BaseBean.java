package org.hibernate.envers.bugs;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

import org.hibernate.envers.Audited;


/**
 * Created by presta19 on 29/06/2017.
 */
@Entity
@Audited
public class BaseBean {
    @Id
    private int id;

    @ElementCollection(targetClass = EmbeddedBean.class)
    @OrderColumn(name = "SETORDINAL")
    private final List<EmbeddedBean> embeddedList = new ArrayList<>();

    public List<EmbeddedBean> getEmbeddedList() {
        return Collections.unmodifiableList(embeddedList);
    }

    public void setEmbeddedList(List<EmbeddedBean> embeddedList) {
        this.embeddedList.clear();
        Optional.ofNullable(embeddedList) //
            .ifPresent(this.embeddedList::addAll);
    }

    public void addEmbeddedBean(EmbeddedBean bean){
        this.embeddedList.add(bean);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
