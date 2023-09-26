package database;

import java.sql.ResultSet;

public class TableReceiver extends DatabaseConnector<Table> {

	@Override
	protected Table processResult(ResultSet result) {
		return new Table(result);
	}

}
