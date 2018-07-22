package com.example.quocb14005xx.apipython.db

class DBConstants {
    companion object {
        const val DB_NAME="HistoryVocabulary.db"
        const val TABLE_NAME="tb_HistoryVocabulary"
        const val VERSION=1
        const val COL_ID="id"
        const val COL_BASE64STR="base64str"
        const val COL_CONTENT="content"
        const val COL_DATE="date"
        const val SQL_CREATE_TABLE="create table $TABLE_NAME ( $COL_ID integer primary key autoincrement, $COL_BASE64STR text, $COL_CONTENT text, $COL_DATE text) "
        const val SQL_DELETE_TABLE="drop table if exists $TABLE_NAME"

    }
}