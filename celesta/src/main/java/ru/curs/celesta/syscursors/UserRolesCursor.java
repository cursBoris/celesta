package ru.curs.celesta.syscursors;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.python.core.PyFunction;
import ru.curs.celesta.CallContext;
import ru.curs.celesta.Celesta;
import ru.curs.celesta.CelestaException;
import ru.curs.celesta.dbutils.BasicCursor;
import ru.curs.celesta.dbutils.Cursor;
import ru.curs.celesta.event.TriggerType;

/**
 * Курсор на таблице userroles.
 */
public final class UserRolesCursor extends SysCursor {

	public static final String TABLE_NAME = "userroles";

	private String userid;
	private String roleid;

	public UserRolesCursor(CallContext context) throws CelestaException {
		super(context);
	}

	@Override
	// CHECKSTYLE:OFF
	protected String _tableName() {
		// CHECKSTYLE:ON
		return TABLE_NAME;
	}

	@Override
	// CHECKSTYLE:OFF
	protected void _parseResult(ResultSet rs) throws SQLException {
		// CHECKSTYLE:ON
		userid = rs.getString("userid");
		roleid = rs.getString("roleid");
		setRecversion(rs.getInt("recversion"));
	}

	@Override
	// CHECKSTYLE:OFF
	protected void _clearBuffer(boolean withKeys) {
		// CHECKSTYLE:ON
		if (withKeys) {
			userid = null;
			roleid = null;
		}
	}

	@Override
	// CHECKSTYLE:OFF
	protected Object[] _currentKeyValues() {
		// CHECKSTYLE:ON
		Object[] result = { userid, roleid };
		return result;
	}

	@Override
	// CHECKSTYLE:OFF
	public Object[] _currentValues() {
		// CHECKSTYLE:ON
		Object[] result = { userid, roleid };
		return result;
	}

	/**
	 * Идентификатор пользователя.
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * Устанавливает идентификатор пользователя.
	 * 
	 * @param userId
	 *            идентификатор
	 */
	public void setUserid(String userId) {
		this.userid = userId;
	}

	/**
	 * Роль пользователя.
	 */
	public String getRoleid() {
		return roleid;
	}

	/**
	 * Устанавливает роль пользователя.
	 * 
	 * @param roleId
	 *            имя роли
	 */
	public void setRoleid(String roleId) {
		this.roleid = roleId;
	}

	@Override
	public void copyFieldsFrom(BasicCursor c) {
		UserRolesCursor from = (UserRolesCursor) c;
		userid = from.userid;
		roleid = from.roleid;
		setRecversion(from.getRecversion());
	}

	@Override
	// CHECKSTYLE:OFF
	public Cursor _getBufferCopy(CallContext context) throws CelestaException {
		// CHECKSTYLE:ON
		UserRolesCursor result = new UserRolesCursor(context);
		result.copyFieldsFrom(this);
		return result;
	}

	public static void onPreDelete(PyFunction pyFunction) {
		try {
			Celesta.getInstance().getTriggerDispatcher()
					.registerTrigger(TriggerType.PRE_DELETE, TABLE_NAME, pyFunction);
		} catch (CelestaException e) {
			throw new RuntimeException(e);
		}
	}

	public static void onPostDelete(PyFunction pyFunction) {
		try {
			Celesta.getInstance().getTriggerDispatcher()
					.registerTrigger(TriggerType.POST_DELETE, TABLE_NAME, pyFunction);
		} catch (CelestaException e) {
			throw new RuntimeException(e);
		}
	}

	public static void onPreUpdate(PyFunction pyFunction) {
		try {
			Celesta.getInstance().getTriggerDispatcher()
					.registerTrigger(TriggerType.PRE_UPDATE, TABLE_NAME, pyFunction);
		} catch (CelestaException e) {
			throw new RuntimeException(e);
		}
	}

	public static void onPostUpdate(PyFunction pyFunction) {
		try {
			Celesta.getInstance().getTriggerDispatcher()
					.registerTrigger(TriggerType.POST_UPDATE, TABLE_NAME, pyFunction);
		} catch (CelestaException e) {
			throw new RuntimeException(e);
		}
	}

	public static void onPreInsert(PyFunction pyFunction) {
		try {
			Celesta.getInstance().getTriggerDispatcher()
					.registerTrigger(TriggerType.PRE_INSERT, TABLE_NAME, pyFunction);
		} catch (CelestaException e) {
			throw new RuntimeException(e);
		}
	}

	public static void onPostInsert(PyFunction pyFunction) {
		try {
			Celesta.getInstance().getTriggerDispatcher()
					.registerTrigger(TriggerType.POST_INSERT, TABLE_NAME, pyFunction);
		} catch (CelestaException e) {
			throw new RuntimeException(e);
		}
	}
}
