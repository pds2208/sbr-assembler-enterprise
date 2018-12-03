package dao.parquet

import global.AppParams
import org.apache.spark.sql.SparkSession

trait ParquetDao extends Serializable {
  def jsonToParquet(jsonFilePath: String)
                   (implicit spark: SparkSession, appconf: AppParams): Unit =
    spark.read.json(jsonFilePath).write.parquet(appconf.PATH_TO_PARQUET)
}

object ParquetDao extends ParquetDao