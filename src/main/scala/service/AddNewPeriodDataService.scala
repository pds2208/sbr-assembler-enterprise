package service

import closures.AssembleUnitsClosure$
import dao.hbase.{HBaseConnectionManager, HBaseDao}
import dao.parquet.ParquetDao
import global.AppParams
import global.Configs.PATH_TO_JSON
import org.apache.hadoop.hbase.client.Connection
import org.apache.spark.sql.SparkSession
import spark.SparkSessionManager

trait AddNewPeriodDataService extends HBaseConnectionManager with SparkSessionManager {

  def createNewPeriodParquet(implicit appconf: AppParams): Unit =
    withSpark {
      implicit ss: SparkSession => ParquetDao.jsonToParquet(PATH_TO_JSON)(ss, appconf)
    }

  def loadNewPeriodWithCalculationsData(implicit appconf: AppParams): Unit =
    withSpark {
      implicit ss: SparkSession =>
        withHbaseConnection {
          implicit con: Connection =>

            //ParquetDao.jsonToParquet(PATH_TO_JSON)(ss, appconf)
            AssembleUnitsClosure$.createUnitsHfiles
            HBaseDao.truncateTables
            HBaseDao.loadLinksHFile
            HBaseDao.loadEnterprisesHFile
            HBaseDao.loadLousHFile
            HBaseDao.loadLeusHFile
            HBaseDao.loadRusHFile

        }
    }


}
