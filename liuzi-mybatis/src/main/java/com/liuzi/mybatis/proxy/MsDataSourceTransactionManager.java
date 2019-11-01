package com.liuzi.mybatis.proxy;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

public class MsDataSourceTransactionManager extends DataSourceTransactionManager {
 
	private static final long serialVersionUID = 1L;

	/**
     * 只读事务到读库，读写事务到写库
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        //设置数据源
        boolean readOnly = definition.isReadOnly();
        if(readOnly) {
        	MsDataSourceHolder.putDataSource(MsDataSourceHolder.SLAVE);
        } else {
            MsDataSourceHolder.putDataSource(MsDataSourceHolder.MASTER);
        }
        super.doBegin(transaction, definition);
    }
 
    /**
     * 清理本地线程的数据源
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        MsDataSourceHolder.clearDataSource();
    }

}
