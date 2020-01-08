package org.neodatis.odb.test.fromusers.emerson;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

import java.util.Date;

public class TestRelations extends ODBTest {

	public void test1(){
		
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Date dataInicio = null;
		Date dataFim = null;
		Objects<ReceitaVO> receitas = odb.query(ReceitaVO.class, W.ge("data", dataInicio).and(W.le("data", dataFim))).objects();
		while(receitas.hasNext()){
			System.out.println(receitas.next().getAnimal().getCliente());
		}
	}
}
