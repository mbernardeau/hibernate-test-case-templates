package org.hibernate.envers.bugs;


import javax.persistence.Embeddable;


/**
 * Created by presta19 on 29/06/2017.
 */
@Embeddable
public class EmbeddedBean {

    private Integer value;

    public EmbeddedBean(){}

    public EmbeddedBean(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
