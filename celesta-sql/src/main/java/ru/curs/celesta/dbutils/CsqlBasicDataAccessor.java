package ru.curs.celesta.dbutils;

import ru.curs.celesta.CelestaException;
import ru.curs.celesta.ICallContext;
import ru.curs.celesta.dbutils.adaptors.DBAdaptor;
import ru.curs.celesta.score.GrainElement;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class CsqlBasicDataAccessor<T extends ICallContext> {
    static final String DATA_ACCESSOR_IS_CLOSED = "DataAccessor is closed.";

    private final T context;
    private final Connection conn;
    private final DBAdaptor db;

    private boolean closed = false;


    public CsqlBasicDataAccessor(T context) throws CelestaException {
        validateInitContext(context);

        this.context = context;
        this.conn = context.getConn();
        try {
            if (conn.isClosed())
                throw new CelestaException("Trying to create a cursor on closed connection.");
        } catch (SQLException e) {
            throw new CelestaException(e.getMessage());
        }
        this.db = callContext().getDbAdaptor();
    }

    protected void validateInitContext(T context) throws CelestaException {
        if (context == null)
            throw new CelestaException(
                    "Invalid context passed to %s constructor: context should not be null.",
                    this.getClass().getName());
        if (context.getConn() == null)
            throw new CelestaException(
                    "Invalid context passed to %s constructor: connection is null.",
                    this.getClass().getName());
        if (context.isClosed())
            throw new CelestaException("Cannot create %s on a closed CallContext.",
                    this.getClass().getName());
    }

    /**
     * Возвращает контекст вызова, в котором создан данный курсор.
     */
    public final T callContext() {
        return context;
    }

    final DBAdaptor db() {
        return db;
    }

    final Connection conn() {
        return conn;
    }

    /**
     * Является ли объект доступа закрытым.
     */
    public boolean isClosed() {
        return closed;
    }

    public final void close() {
        if (!isClosed()) {
            closed = true;
            closeInternal();
        }
    }

    abstract void closeInternal();

    public abstract void clear() throws CelestaException;

    /**
     * Объект метаданных (таблица, представление или последовательность), на основе которого создан
     * данный объект доступа.
     *
     * @throws CelestaException
     *             в случае ошибки извлечения метаинформации (в норме не должна
     *             происходить).
     */
    public abstract GrainElement meta() throws CelestaException;
}