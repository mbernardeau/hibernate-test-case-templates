/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.bugs;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


/**
 * This template demonstrates how to develop a test case for Hibernate Envers, using
 * its built-in unit test framework.
 */
public class EnversUnitTestCase extends AbstractEnversTestCase {
	private static final int BEAN_ID = 1;

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				BaseBean.class,
				EmbeddedBean.class
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
//				"Foo.hbm.xml",
//				"Bar.hbm.xml"
		};
	}
	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( "org.hibernate.envers.audit_strategy", "org.hibernate.envers.strategy.ValidityAuditStrategy");
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		// Create a new bean with a few elements in embedded list
		initEntities();

		// Change content of list by adding an element
		changeList();

		AuditReader reader = getAuditReader();
		List<Number> revisionsIds = reader.getRevisions(BaseBean.class, BEAN_ID);

		// Get last revision instance
		BaseBean currentInstance = (BaseBean) reader.createQuery()
				.forEntitiesAtRevision(BaseBean.class, revisionsIds.get(revisionsIds.size() - 1))
				.add(AuditEntity.property("id").eq(BEAN_ID))
				.getSingleResult();

		// Will throw java.lang.IndexOutOfBoundsException: Index: 2, Size: 1
		System.out.println(currentInstance.getEmbeddedList());
	}

	private void changeList() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		BaseBean b = s.get(BaseBean.class, BEAN_ID);
		b.addEmbeddedBean(new EmbeddedBean(3));

		s.save(b);
		tx.commit();
		s.close();
	}

	private void initEntities(){
		Session s = openSession();
		Transaction tx = s.beginTransaction();

		BaseBean b1 = new BaseBean();
		b1.setId(BEAN_ID);
		EmbeddedBean e1 = new EmbeddedBean(1);
		EmbeddedBean e2 = new EmbeddedBean(2);

		b1.setEmbeddedList(Arrays.asList(e1, e2));

		s.save(b1);
		tx.commit();
		s.close();
	}
}
