package test.dao;

import java.lang.reflect.Field;

import org.junit.Test;
import org.rm.bean.MetaDevice;
import org.rm.core.dbquery;

public class TestBaseDAO {
	@Test
	public void test() throws Exception{
		dbquery db = new dbquery();
		System.out.println(db);
		MetaDevice device = new MetaDevice();
		device.setId(1);
		device.AddReserveFild("id");
//		Field[] fields = device.getClass().getDeclaredFields();
//		fields[0].setAccessible(true);
//		System.out.println(fields[0].get(device).toString());
		System.out.println(db.GetUpdateSQL(device, "id = '1'"));
	}
}
