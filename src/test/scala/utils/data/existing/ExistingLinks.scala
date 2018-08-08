package utils.data.existing

import model.domain.{HFileRow, KVCell}
import utils.data.TestIds


trait ExistingLinks {this:TestIds =>

//     HFileRow("00032261~CH",List(KVCell("p_LEU","100002826247"))),
//{"id":100000601835, "BusinessName": "MBI LTD", "UPRN": 789789, "PostCode": "CF23 9EU", "IndustryCode": "44044", "LegalStatus": "5", "TradingStatus": "X", "Turnover": "Y", "EmploymentBands": "Z", "VatRefs": [555666777003],"PayeRefs": ["9876L"], "CompanyNo": "01012444","ern":"5000000011"}
  val existingLinksForAddNewPeriodScenarion = List(
    HFileRow("00012345~CH",List(KVCell("p_LEU","100000508724"))),
    HFileRow("00032262~CH",List(KVCell("p_LEU","100000246017"))),
    HFileRow("04223165~CH",List(KVCell("p_LEU","100000508723"))),
    HFileRow("00032263~CH",List(KVCell("p_LEU","100000827984"))),
    HFileRow("04223164~CH",List(KVCell("p_LEU","100000459235"))),

    HFileRow("01012444~CH",List(KVCell("p_LEU","100000601835"))),

    HFileRow("100000246017~LEU",List(KVCell("c_00032262","CH"), KVCell("c_111222333","VAT"), KVCell("c_1152L","PAYE"), KVCell("c_1153L","PAYE"), KVCell("p_ENT","3000000011"))),
    HFileRow("100000459235~LEU",List(KVCell("c_04223164","CH"), KVCell("c_1166L","PAYE"), KVCell("c_222666000","VAT"), KVCell("c_555666777","VAT"), KVCell("p_ENT","4000000011"))),
    HFileRow("100000508723~LEU",List(KVCell("c_04223165","CH"), KVCell("c_111000111","VAT"), KVCell("c_1188L","PAYE"), KVCell("c_1199L","PAYE"), KVCell("p_ENT","4000000011"))),
    HFileRow("100000508724~LEU",List(KVCell("c_00012345","CH"), KVCell("c_5555L","PAYE"), KVCell("c_999888777","VAT"), KVCell("p_ENT","4000000011"))),
    HFileRow("100000827984~LEU",List(KVCell("c_00032263","CH"), KVCell("c_1154L","PAYE"), KVCell("c_1155L","PAYE"), KVCell("c_222333444","VAT"), KVCell("p_ENT","3000000011"))),

    HFileRow("100000601835~LEU",List(KVCell("c_01012444","CH"), KVCell("c_9876L","PAYE"), KVCell("c_555666777003","VAT"), KVCell("p_ENT","5000000011"))),

    HFileRow("100002826247~LEU",List(KVCell("c_00032261","CH"), KVCell("c_1151L","PAYE"), KVCell("c_123123123","VAT"), KVCell("p_ENT",entWithMissingLouId))),
    HFileRow("111000111~VAT",List(KVCell("p_LEU","100000508723"))),
    HFileRow("111222333~VAT",List(KVCell("p_LEU","100000246017"))),

    HFileRow("555666777003~VAT",List(KVCell("p_LEU","100000601835"))),

    HFileRow("1151L~PAYE",List(KVCell("p_LEU","100002826247"))),
    HFileRow("1152L~PAYE",List(KVCell("p_LEU","100000246017"))),
    HFileRow("1153L~PAYE",List(KVCell("p_LEU","100000246017"))),
    HFileRow("1154L~PAYE",List(KVCell("p_LEU","100000827984"))),
    HFileRow("1155L~PAYE",List(KVCell("p_LEU","100000827984"))),
    HFileRow("1166L~PAYE",List(KVCell("p_LEU","100000459235"))),
    HFileRow("1177L~PAYE",List(KVCell("p_LEU","100000459235"))),
    HFileRow("1188L~PAYE",List(KVCell("p_LEU","100000508723"))),
    HFileRow("1199L~PAYE",List(KVCell("p_LEU","100000508723"))),

    HFileRow("9876L~PAYE",List(KVCell("p_LEU","100000601835"))),

    HFileRow("123123123~VAT",List(KVCell("p_LEU","100002826247"))),
    HFileRow(s"$entWithMissingLouId~ENT",List(KVCell("c_100002826247","LEU"), KVCell(s"c_$missingLouLurn","LOU"))),
    HFileRow(s"$missingLouLurn~LOU",List(KVCell("p_ENT",entWithMissingLouId))),
    HFileRow("222333444~VAT",List(KVCell("p_LEU","100000827984"))),
    HFileRow("222666000~VAT",List(KVCell("p_LEU","100000459235"))),
    HFileRow("3000000011~ENT",List(KVCell("c_100000246017","LEU"), KVCell("c_100000827984","LEU"), KVCell("c_300000088","LOU"), KVCell("c_300000099","LOU"))),
    HFileRow("300000055~LOU",List(KVCell("p_ENT","4000000011"))),
    HFileRow("300000066~LOU",List(KVCell("p_ENT","4000000011"))),
    HFileRow("300000077~LOU",List(KVCell("p_ENT","4000000011"))),
    HFileRow("300000088~LOU",List(KVCell("p_ENT","3000000011"))),
    HFileRow("300000099~LOU",List(KVCell("p_ENT","3000000011"))),

    HFileRow("550000088~LOU",List(KVCell("p_ENT","5000000011"))),

    HFileRow("4000000011~ENT",List(KVCell("c_100000459235","LEU"), KVCell("c_100000508723","LEU"), KVCell("c_100000508724","LEU"), KVCell("c_300000055","LOU"), KVCell("c_300000066","LOU"), KVCell("c_300000077","LOU"))),
    HFileRow("5555L~PAYE",List(KVCell("p_LEU","100000508724"))),
    HFileRow("555666777~VAT",List(KVCell("p_LEU","100000459235"))),
    HFileRow("999888777~VAT",List(KVCell("p_LEU","100000508724"))),
    HFileRow("00032261~CH",List(KVCell("p_LEU","100002826247")))  //this will be deleted with update
  )

  val existingLinksForMissingLousScenario = List(
    HFileRow("00012345~CH",List(KVCell("p_LEU","100000508724"))),
    HFileRow("00032262~CH",List(KVCell("p_LEU","100000246017"))),
    HFileRow("04223165~CH",List(KVCell("p_LEU","100000508723"))),
    HFileRow("00032263~CH",List(KVCell("p_LEU","100000827984"))),
    HFileRow("04223164~CH",List(KVCell("p_LEU","100000459235"))),
    HFileRow("100000246017~LEU",List(KVCell("c_00032262","CH"), KVCell("c_111222333","VAT"), KVCell("c_1152L","PAYE"), KVCell("c_1153L","PAYE"), KVCell("p_ENT","3000000011"))),
    HFileRow("100000459235~LEU",List(KVCell("c_04223164","CH"), KVCell("c_1166L","PAYE"), KVCell("c_222666000","VAT"), KVCell("c_555666777","VAT"), KVCell("p_ENT","4000000011"))),
    HFileRow("100000508723~LEU",List(KVCell("c_04223165","CH"), KVCell("c_111000111","VAT"), KVCell("c_1188L","PAYE"), KVCell("c_1199L","PAYE"), KVCell("p_ENT","4000000011"))),
    HFileRow("100000508724~LEU",List(KVCell("c_00012345","CH"), KVCell("c_5555L","PAYE"), KVCell("c_999888777","VAT"), KVCell("p_ENT","4000000011"))),
    HFileRow("100000827984~LEU",List(KVCell("c_00032263","CH"), KVCell("c_1154L","PAYE"), KVCell("c_1155L","PAYE"), KVCell("c_222333444","VAT"), KVCell("p_ENT","3000000011"))),
    HFileRow("100002826247~LEU",List(KVCell("c_00032261","CH"), KVCell("c_1151L","PAYE"), KVCell("c_123123123","VAT"), KVCell("p_ENT",entWithMissingLouId))),
    HFileRow("111000111~VAT",List(KVCell("p_LEU","100000508723"))),
    HFileRow("111222333~VAT",List(KVCell("p_LEU","100000246017"))),
    HFileRow("1151L~PAYE",List(KVCell("p_LEU","100002826247"))),
    HFileRow("1152L~PAYE",List(KVCell("p_LEU","100000246017"))),
    HFileRow("1153L~PAYE",List(KVCell("p_LEU","100000246017"))),
    HFileRow("1154L~PAYE",List(KVCell("p_LEU","100000827984"))),
    HFileRow("1155L~PAYE",List(KVCell("p_LEU","100000827984"))),
    HFileRow("1166L~PAYE",List(KVCell("p_LEU","100000459235"))),
    HFileRow("1177L~PAYE",List(KVCell("p_LEU","100000459235"))),
    HFileRow("1188L~PAYE",List(KVCell("p_LEU","100000508723"))),
    HFileRow("1199L~PAYE",List(KVCell("p_LEU","100000508723"))),
    HFileRow("123123123~VAT",List(KVCell("p_LEU","100002826247"))),
    HFileRow(s"$entWithMissingLouId~ENT",List(KVCell("c_100002826247","LEU"))),
    HFileRow("222333444~VAT",List(KVCell("p_LEU","100000827984"))),
    HFileRow("222666000~VAT",List(KVCell("p_LEU","100000459235"))),
    HFileRow("3000000011~ENT",List(KVCell("c_100000246017","LEU"), KVCell("c_100000827984","LEU"), KVCell("c_300000088","LOU"), KVCell("c_300000099","LOU"))),
    HFileRow("300000055~LOU",List(KVCell("p_ENT","4000000011"))),
    HFileRow("300000066~LOU",List(KVCell("p_ENT","4000000011"))),
    HFileRow("300000077~LOU",List(KVCell("p_ENT","4000000011"))),
    HFileRow("300000088~LOU",List(KVCell("p_ENT","3000000011"))),
    HFileRow("300000099~LOU",List(KVCell("p_ENT","3000000011"))),
    HFileRow("4000000011~ENT",List(KVCell("c_100000459235","LEU"), KVCell("c_100000508723","LEU"), KVCell("c_100000508724","LEU"), KVCell("c_300000055","LOU"), KVCell("c_300000066","LOU"), KVCell("c_300000077","LOU"))),
    HFileRow("5555L~PAYE",List(KVCell("p_LEU","100000508724"))),
    HFileRow("555666777~VAT",List(KVCell("p_LEU","100000459235"))),
    HFileRow("999888777~VAT",List(KVCell("p_LEU","100000508724"))),
    HFileRow("00032261~CH",List(KVCell("p_LEU","100002826247")))  //this will be deleted with update
  )

}
