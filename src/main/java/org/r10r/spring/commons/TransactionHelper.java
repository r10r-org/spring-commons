package org.r10r.spring.commons;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * The @Transaction annotation in Spring has two big shortcomings.
 *
 * 1. They only rollback if they are configured via (rollbackFor = Throwable.class). Otherwise checked exceptions
 *    won't roll back the exception. This might be really unexpected.
 * 2. Transactions might be inactive if placed on "private" methods, or if methods are not executed from another bean.
 *    This is really error prone as transactions might not be active even though annotations are there. Spring won't
 *    warn about that. Nothing. Ouch.
 *
 * The TransactionHelper might help you in that case as it makes it explicit that there's a transaction. And it is less
 * magic as it always rolls back on any exception.
 *
 * Your mileage may vary.
 * Especially the forced definition of return types is no really nice (transactionHelper.<MyException>runI...).
 *
 * This TransactionHelper can be injected into your class and the simply used via:
 *
 * <code>
 *
 * transactionHelper.runInTransaction(() ->
 *   // ... your code
 *  )};
 * </code>
 *
 * If you want to throw checked exception it gets a bit ugly as you have to specify them like that:
 *
 * <code>
 * transactionHelper.<MyException>runInTransactionAndThrow(() ->
 *   // ... your code
 *  )};
 * </code>
 *
 * Less error-prone and it is more clear what happens. There's also TransactionTemplate, but it does not have support
 * for checked exceptions..
 */
@Service
public class TransactionHelper {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Support for exceptions and lambdas that do not return a result
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // No exception
    public interface TransactionWithoutResult {
        void execute();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void runInTransaction(TransactionWithoutResult transactionWithoutResult) {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        transactionWithoutResult.execute();
    }
    // 1 exception
    public interface TransactionWithoutResult1<E extends Throwable> {
        void execute() throws E;
    }

    @Transactional(rollbackFor = Throwable.class)
    public <E extends Throwable> void runInTransactionAndThrow(TransactionWithoutResult1<E> transactionWithoutResult) throws E {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        transactionWithoutResult.execute();
    }

    // 2 exceptions
    public interface TransactionWithoutResult2<E1 extends Exception, E2 extends Exception> {
        void execute() throws E1, E2;
    }

    @Transactional(rollbackFor = Throwable.class)
    public <E1 extends Exception, E2 extends Exception> void runInTransactionAndThrow(TransactionWithoutResult2<E1, E2> transactionWithoutResult) throws E1, E2 {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        transactionWithoutResult.execute();
    }

    // 3 exceptions
    public interface TransactionWithoutResult3<E1 extends Exception, E2 extends Exception, E3 extends Exception> {
        void execute() throws E1, E2, E3;
    }

    @Transactional(rollbackFor = Throwable.class)
    public <E1 extends Exception, E2 extends Exception, E3 extends Exception> void runInTransactionAndThrow(TransactionWithoutResult3<E1, E2, E3> transactionWithoutResult) throws E1, E2, E3 {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        transactionWithoutResult.execute();
    }

    // ... add more implementations if you want to handle more exceptions...


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Support transactions and lambdas that DO return a result
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public interface TransactionWithResult<T>{
        T execute();
    }

    @Transactional(rollbackFor = Throwable.class)
    public <T> T runInTransaction(TransactionWithResult<T> transactionWithResult) {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        return transactionWithResult.execute();
    }

    public interface TransactionWithResult1<T, E extends Throwable> {
        T execute() throws E;
    }

    @Transactional(rollbackFor = Throwable.class)
    public <T, E extends Throwable> T runInTransactionAndThrow(TransactionWithResult1<T, E> transactionWithResult) throws E {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        return transactionWithResult.execute();
    }

    public interface TransactionWithResult2<T, E1 extends Throwable, E2 extends Throwable> {
        T execute() throws E1, E2;
    }

    @Transactional(rollbackFor = Throwable.class)
    public <T, E1 extends Throwable, E2 extends Throwable> T runInTransactionAndThrow(TransactionWithResult2<T, E1, E2> transactionWithResult) throws E1, E2 {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        return transactionWithResult.execute();
    }

    public interface TransactionWithResult3<T, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable> {
        T execute() throws E1, E2, E3;
    }

    @Transactional(rollbackFor = Throwable.class)
    public <T, E1 extends Throwable, E2 extends Throwable, E3 extends Throwable> T runInTransactionAndThrow(TransactionWithResult3<T, E1, E2, E3> transactionWithResult) throws E1, E2, E3 {
        Preconditions.checkState(TransactionSynchronizationManager.isActualTransactionActive());

        return transactionWithResult.execute();
    }



}
