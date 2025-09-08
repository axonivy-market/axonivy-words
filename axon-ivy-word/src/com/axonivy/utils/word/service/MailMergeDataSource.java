package com.axonivy.utils.word.service;

import com.aspose.words.IMailMergeDataSource;
import com.aspose.words.ref.Ref;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Recordset;

public class MailMergeDataSource implements IMailMergeDataSource {

    private final String tableName;
    private final Recordset tableValues;
    private int rowIndex;

    public MailMergeDataSource(Recordset recordSet, String tableName) {
        this.tableValues = recordSet;
        this.tableName = tableName;
        this.rowIndex = -1; 
    }

    @Override
    public IMailMergeDataSource getChildDataSource(String childTableName) {
        return null;
    }

    @Override
    public boolean moveNext() {
        rowIndex++;
        return rowIndex < tableValues.size(); 
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean getValue(String fieldName, Ref<Object> fieldValue) throws Exception {
      try {
        if (tableValues.getKeys().contains(fieldName)) {
          fieldValue.set(tableValues.getColumn(fieldName).get(rowIndex));
          return true;
        } else {
          fieldValue.set(0);
          return false;
        }
      } catch (Exception e) {
        Ivy.log().error(e.getMessage(), e);
        throw e;
      }
    }
}
