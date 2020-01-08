package org.neodatis.odb.test.fromusers.wonman.real;

import org.neodatis.odb.*;
import org.neodatis.odb.core.oid.uuid.ObjectOidImpl;
import org.neodatis.odb.core.query.criteria.Criterion;
import org.neodatis.odb.core.query.criteria.W;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NeoDatisDAO {

	public static String ODB_NAME = null;
	private ODB odb = null;
	
	public NeoDatisDAO(String odbname) {
		ODB_NAME = odbname;
	}
	
	public Entity load(long id) {
		return load("object-oid:" + id);
	}
	
	public Entity load(String oidString) {
		Entity e = null;
		//
		try {
			
			ObjectOid oid = ObjectOidImpl.objectOidfromString(oidString);
			
			odb = NeoDatis.open(ODB_NAME);
			e = (Entity)odb.getObjectFromId(oid);
			//e.setId(oid.getObjectId());
		}
		finally {
			if(odb != null && !odb.isClosed()) odb.close();
		}
		//
		return e;
	}
	
	public <T> T load(Class<T> clz) {
		T e = null;
		//
		try {
			odb = NeoDatis.open(ODB_NAME);
			
			Objects<T> objs = odb.getObjects(clz);
			if(objs.hasNext())
				e = objs.first();
		}
		finally {
			if(odb != null && !odb.isClosed()) odb.close();
		}
		//
		return e;
	}
	
	public void delete(long id) {
		delete("object-oid:" + id);
	}
	
	public void delete(String oidString) {
		try {
			ObjectOid oid = ObjectOidImpl.objectOidfromString(oidString);
			
			odb = NeoDatis.open(ODB_NAME);
			odb.deleteObjectWithId(oid);
		}
		finally {
			if(odb != null && !odb.isClosed()) odb.close();
		}
	}
	
	public <T> List<T> list(Class<T> clz, String key, Object value) {
		List<T> list = new ArrayList<T>();
		//
		try {
			odb = NeoDatis.open(ODB_NAME);
			
			Query query = odb.query(clz.getName(), W.equal(key, value));
			Objects<T> objs = odb.getObjects(query);
			while(objs.hasNext()) {
				list.add(objs.next());
			}
		}
		finally {
			if(odb != null && !odb.isClosed()) odb.close();
		}
		//
		return list;
	}
	
	public <T> List<T> list(Class<T> clz) {
		List<T> list = new ArrayList<T>();
		//
		try {
			odb = NeoDatis.open(ODB_NAME);
			
			Objects<T> objs = odb.getObjects(clz);
			while(objs.hasNext()) {
				list.add(objs.next());
			}
		}
		finally {
			if(odb != null && !odb.isClosed()) odb.close();
		}
		//
		return list;
	}
	
	public <T> List<T> list(Class<T> clz, Criterion criterion) {
		List<T> list = new ArrayList<T>();
		//
		try {
			odb = NeoDatis.open(ODB_NAME);
			
			Query query = odb.query(clz.getName(), criterion);
			Objects<T> objs = query.objects();
			while(objs.hasNext()) {
				list.add(objs.next());
			}
		}
		finally {
			if(odb != null && !odb.isClosed()) odb.close();
		}
		//
		return list;
	}

	public <T> T save(String id, String userUpdate, UpdateHelper<T> helper) throws ArgumentException {
		
		T e = null;
		//
		try {
			odb = NeoDatis.open(ODB_NAME);
			e = helper.update(odb, id, userUpdate);
		}
		finally {
			if(odb != null && !odb.isClosed()) 
				odb.close();
		}
		//
		return e;
	}
	
	abstract public static class UpdateHelper<T> {
		private Class<?> aClass = null;
		
		@SuppressWarnings("unchecked")
		public T update(ODB odb, String id, String userUpdated) throws ArgumentException {

			boolean savedObject = id !=null;

			T e = null;
			
			if(savedObject) {

				ObjectOid oid = ObjectOidImpl.objectOidfromString(id);
				e = (T)odb.getObjectFromId(oid);
			}

			if(e == null) {
				
				ParameterizedType superclass = (ParameterizedType) getClass()
					.getGenericSuperclass();
				this.aClass = (Class<?>) ((ParameterizedType) superclass)
					.getActualTypeArguments()[0];
				
				try {
					e = (T)aClass.newInstance();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			e = setVariables(odb, e);
			
			((Entity)e).setUserUpdated(userUpdated);
			((Entity)e).setTimeUpdated(new Date());
			
			((Entity)e).validateState();
			
			ObjectOid oid = odb.store(e);
			
			if(!savedObject) {

				e = (T) odb.getObjectFromId(oid);
				((Entity)e).setId(oid.toString());

				odb.store(e);
				
				System.out.println( "Created. " + e );
			}
			else {
				System.out.println( "Updated. " + e );
			}
			//
			return e;
		};
		
		public Entity load(ODB odb, String id) {
			Entity c = null;
			//
			ObjectOid oid = ObjectOidImpl.objectOidfromString(id);
			c = (Entity) odb.getObjectFromId(oid);
			//
			return c;
		}

		abstract public T setVariables(ODB odb, T e) throws ArgumentException;
	}

}
