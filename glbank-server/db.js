const mysql = require('mysql');

const con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "root",
    database: "glbankmart"
    });
  
con.connect(function(err) {
    if (err) throw err;
    console.log("Database connected!");
    });

module.exports = {

    getLoginData(username, password, queryResult)
    {
      const query = "select * from loginclient where login =\'" + username + "\' and password = MD5(\'" + password + "'\);";
      con.query(query, (err, result) =>{
           if (err)
           {
             console.log(err);
             queryResult("Error");
           }
           else
           {
             queryResult(JSON.stringify(result));
           }
      });
    },

    getLastThreeRecords(idc, queryResult)
    {
      const query = "select * from loginhistory where idl = (select id from loginclient where idc = " + idc + ")order by UNIX_TIMESTAMP(logDate) desc limit 3;";
      con.query(query, (err, result) =>{
        if (err)
        {
          console.log(err);
          queryResult("Error");
        }
        else
        {
          queryResult(JSON.stringify(result));
        }
    });
    }

};