package closures


import dao.hbase.{HBaseDao, HFileUtils}
import global.{AppParams, Configs}
import model.hfile
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Connection
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2
import org.apache.hadoop.hbase.{KeyValue, TableName}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import spark.RddLogging
import spark.extensions.sql._

import scala.util.Try

trait BaseClosure extends HFileUtils with Serializable with RddLogging{

  val hbaseDao: HBaseDao = HBaseDao


  /**
    * Fields:
    * id,BusinessName, UPRN, PostCode,IndustryCode,LegalStatus,
    * TradingStatus, Turnover, EmploymentBands, PayeRefs, VatRefs, CompanyNo
    * */
  def getIncomingBiData(appconf: AppParams)(implicit spark: SparkSession) = {
    val parquetDF = spark.read.parquet(appconf.PATH_TO_PARQUET)
    parquetDF.castAllToString
  }

  def getAllLUs(joinedLUs: DataFrame,appconf:AppParams)(implicit spark: SparkSession) = {

    val rows = joinedLUs.rdd.map { row => {
      val ern = if (row.isNull("ern")) generateErn(row, appconf) else row.getAs[String]("ern")

      Row(
        ern,
        row.getAs[String]("id"),
        row.getAs[String]("BusinessName"),
        row.getAs[String]("IndustryCode"),
        row.getAs[String]("LegalStatus"),
        getValueOrEmptyStr(row,"PostCode"),
        row.getAs[String]("TradingStatus"),
        row.getAs[String]("Turnover"),
        row.getAs[String]("UPRN"),
        row.getAs[String]("CompanyNo"),
        row.getAs[Seq[String]]("PayeRefs"),
        row.getAs[Seq[String]]("VatRefs")
      )}}
    spark.createDataFrame(rows, biWithErnSchema)
  }



  def createNewRUs(ents: DataFrame, appconf: AppParams)(implicit spark: SparkSession) = {

    spark.createDataFrame(
      ents.rdd.map(row => Row(
        generateRurn(row,appconf),
        row.getAs[String]("ern"),
        row.getAs[String]("name"),
        Try {row.getAs[String]("entref")}.getOrElse(null),
        Try {row.getAs[String]("ruref")}.getOrElse(null),
        Try {row.getAs[String]("trading_style")}.getOrElse(null),
        row.getAs[String]("address1"),
        Try {row.getAs[String]("address2")}.getOrElse(null),
        Try {row.getAs[String]("address3")}.getOrElse (null),
        Try {row.getAs[String]("address4")}.getOrElse (null),
        Try {row.getAs[String]("address5")}.getOrElse (null),
        getValueOrEmptyStr(row,"postcode"),
        getValueOrEmptyStr(row,"sic07"),
        getValueOrEmptyStr(row,"employees"),
        getValueOrEmptyStr(row,"employment"),
        getValueOrEmptyStr(row,"turnover"),
        generatePrn(row,appconf)
      )), ruRowSchema)
  }


  def createNewEnts(newLEUsCalculatedDF:DataFrame, appconf: AppParams)(implicit spark: SparkSession) = spark.createDataFrame(
    newLEUsCalculatedDF.rdd.map(row => Row(
      row.getAs[String]("ern"),
      generatePrn(row,appconf),
      Try {row.getAs[String]("entref")}.getOrElse(null),
      row.getAs[String]("BusinessName"),
      null, //trading_style
      Try {row.getAs[String]("address1")}.getOrElse(""),
      null, null, null, null, //address2,3,4,5
      row.getAs[String]("PostCode"),
      Try {row.getAs[String]("IndustryCode")}.getOrElse(""),
      row.getAs[String]("LegalStatus")
    )
    ), entRowSchema)


  /**
    * Creates new Enterprises from new LEUs with calculations
    * schema: completeNewEntSchema
    * ern, entref, name, address1, postcode, sic07, legal_status,
    * AND calculations:
    * paye_empees, paye_jobs, app_turnover, ent_turnover, cntd_turnover, std_turnover, grp_turnover
    * */
    def createNewEntsWithCalculations(newLEUsCalculatedDF:DataFrame, appconf: AppParams)(implicit spark: SparkSession) = spark.createDataFrame(

      newLEUsCalculatedDF.rdd.map(row => Row(
          row.getAs[String]("ern"),
          generatePrn(row,appconf),
          Try {row.getAs[String]("entref")}.getOrElse(null),
          row.getAs[String]("BusinessName"),
          null, //trading_style
          Try {row.getAs[String]("address1")}.getOrElse(""),
          null, null, null, null, //address2,3,4,5
          row.getAs[String]("PostCode"),
          Try {row.getAs[String]("IndustryCode")}.getOrElse(""),
          row.getAs[String]("LegalStatus"),
          Try {row.getAs[String]("paye_empees")}.getOrElse(null),
          Try {row.getAs[String]("paye_jobs")}.getOrElse(null),
          Try {row.getAs[String]("cntd_turnover")}.getOrElse(null),
          Try {row.getAs[String]("app_turnover")}.getOrElse(null),
          Try {row.getAs[String]("std_turnover")}.getOrElse(null),
          Try {row.getAs[String]("grp_turnover")}.getOrElse(null),
          Try {row.getAs[String]("ent_turnover")}.getOrElse(null)
        )), completeEntSchema)



  def createNewLOUs(ents: DataFrame, appconf: AppParams)(implicit spark: SparkSession) = {

    spark.createDataFrame(
      ents.rdd.map(row => Row(
        generateLurn(row,appconf),
        Try {row.getAs[String]("luref")}.getOrElse(null),
        row.getAs[String]("ern"),
        row.getAs[String]("name"),
        Try {row.getAs[String]("entref")}.getOrElse(null),
        Try {row.getAs[String]("trading_style")}.getOrElse(null),
        row.getAs[String]("address1"),
        Try {row.getAs[String]("address2")}.getOrElse(null),
        Try {row.getAs[String]("address3")}.getOrElse (null),
        Try {row.getAs[String]("address4")}.getOrElse (null),
        Try {row.getAs[String]("address5")}.getOrElse (null),
        getValueOrEmptyStr(row,"postcode"),
        getValueOrEmptyStr(row,"sic07"),
        getValueOrEmptyStr(row,"paye_empees")
      )), louRowSchema)
  }


  def getExistingRusDF(appconf: AppParams, confs: Configuration)(implicit spark: SparkSession) = {
    val ruHFileRowRdd: RDD[Row] = hbaseDao.readTable(appconf, confs, HBaseDao.rusTableName(appconf)).map(_.toRuRow)
    spark.createDataFrame(ruHFileRowRdd, ruRowSchema)
  }

  def getExistingLousDF(appconf: AppParams, confs: Configuration)(implicit spark: SparkSession) = {
    val louHFileRowRdd: RDD[Row] = hbaseDao.readTable(appconf, confs, HBaseDao.lousTableName(appconf)).map(_.toLouRow)
    spark.createDataFrame(louHFileRowRdd, louRowSchema)
  }

  def getExistingEntsDF(appconf: AppParams, confs: Configuration)(implicit spark: SparkSession) = {
    val entHFileRowRdd: RDD[Row] = hbaseDao.readTable(appconf,confs, HBaseDao.entsTableName(appconf)).map(_.toEntRow)
    spark.createDataFrame(entHFileRowRdd, entRowSchema)
  }

  /**
    * returns existing ~LEU~ links DF
    * fields:
    * ubrn, ern, CompanyNo, PayeRefs, VatRefs
    **/
  def getExistingLinksLeusDF(appconf: AppParams, confs: Configuration)(implicit spark: SparkSession) = {
    val leuHFileRowRdd: RDD[Row] = hbaseDao.readTable(appconf,confs, HBaseDao.linksTableName(appconf)).map(_.toLeuLinksRow)
    spark.createDataFrame(leuHFileRowRdd, linksLeuRowSchema)
  }

  def getExistingLeusDF(appconf: AppParams, confs: Configuration)(implicit spark: SparkSession) = {
    val leuHFileRowRdd: RDD[Row] = hbaseDao.readTable(appconf,confs, HBaseDao.leusTableName(appconf)).map(_.toLeuRow)
    spark.createDataFrame(leuHFileRowRdd, leuRowSchema)
  }

  def saveLinks(louDF: DataFrame, ruDF:DataFrame, leuDF: DataFrame, appconf: AppParams)(implicit spark: SparkSession,connection:Connection) = {
    import spark.implicits._

    val tableName = TableName.valueOf(hbaseDao.linksTableName(appconf))
    val regionLocator = connection.getRegionLocator(tableName)
    val partitioner = HFilePartitioner(connection.getConfiguration, regionLocator.getStartKeys, 1)

    val lousLinks: RDD[(String, hfile.HFileCell)] = louDF.map(row => louToLinks(row, appconf)).flatMap(identity(_)).rdd
    val restOfLinks: RDD[(String, hfile.HFileCell)] = leuDF.map(row => leuToLinks(row, appconf)).flatMap(identity(_)).rdd

    val allLinks: RDD[((String,String), hfile.HFileCell)] = lousLinks.union(restOfLinks).filter(_._2.value!=null).map(entry => ((entry._1,entry._2.qualifier),entry._2) ).repartitionAndSortWithinPartitions(partitioner)
    allLinks.map(rec => (new ImmutableBytesWritable(rec._1._1.getBytes()), rec._2.toKeyValue))
        .saveAsNewAPIHadoopFile(appconf.PATH_TO_LINKS_HFILE, classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat2], Configs.conf)
  }

  def saveEnts(entsDF: DataFrame, appconf: AppParams)(implicit spark: SparkSession) = {
    val hfileCells: RDD[(String, hfile.HFileCell)] = getEntHFileCells(entsDF, appconf)
    hfileCells.filter(_._2.value!=null).sortBy(t => s"${t._2.key}${t._2.qualifier}")
      .map(rec => (new ImmutableBytesWritable(rec._1.getBytes()), rec._2.toKeyValue))
      .saveAsNewAPIHadoopFile(appconf.PATH_TO_ENTERPRISE_HFILE, classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat2], Configs.conf)
  }


  def saveLous(lousDF: DataFrame, appconf: AppParams)(implicit spark: SparkSession) = {
    import spark.implicits._
    val hfileCells = lousDF.map(row => rowToLocalUnit(row, appconf)).flatMap(identity(_)).rdd
    hfileCells.filter(_._2.value!=null).sortBy(t => s"${t._2.key}${t._2.qualifier}")
      .map(rec => (new ImmutableBytesWritable(rec._1.getBytes()), rec._2.toKeyValue))
      .saveAsNewAPIHadoopFile(appconf.PATH_TO_LOCALUNITS_HFILE, classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat2], Configs.conf)
  }


  def saveLeus(leusDF: DataFrame, appconf: AppParams)(implicit spark: SparkSession) = {
    import spark.implicits._
    val hfileCells = leusDF.map(row => rowToLegalUnit(row, appconf)).flatMap(identity(_)).rdd
    hfileCells.filter(_._2.value!=null).sortBy(t => s"${t._2.key}${t._2.qualifier}")
      .map(rec => (new ImmutableBytesWritable(rec._1.getBytes()), rec._2.toKeyValue))
      .saveAsNewAPIHadoopFile(appconf.PATH_TO_LEGALUNITS_HFILE, classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat2], Configs.conf)
  }

  def getEntHFileCells(entsDF: DataFrame, appconf: AppParams)(implicit spark: SparkSession): RDD[(String, hfile.HFileCell)] = {
    import spark.implicits._
    val res: RDD[(String, hfile.HFileCell)] = entsDF.map(row => rowToEnt(row, appconf)).flatMap(identity).rdd
    res
  }


}
